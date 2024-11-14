package com.innovatech.inventory.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.innovatech.inventory.dto.ServiceDTO;
import com.innovatech.inventory.entity.Entrepreneurship;
import com.innovatech.inventory.entity.Product;
import com.innovatech.inventory.entity.ServiceS;  // Cambié ServiceS a Service
import com.innovatech.inventory.entity.UserEntity;
import com.innovatech.inventory.repository.ServiceRepository; // Cambié ServiceRepositoryy a ServiceRepository
import com.innovatech.inventory.repository.EntrepreneurshipRepository; // Added import for EntrepreneurshipRepository
import com.innovatech.inventory.repository.UserRepository;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServiceService implements CrudService<ServiceS, Long> { // Cambié ServiceS a Service

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Autowired
    private UserRepository UserRepository;

    @Override
    public ServiceS findById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        serviceRepository.deleteById(id);
    }

    @Override
    public ServiceS save(ServiceS service) {
        return serviceRepository.save(service);
    }

    private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);

    public ServiceS findService(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public List<ServiceS> listServices(Integer page, Integer limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        return serviceRepository.findAll(pageable).getContent();
    }

    public ServiceS createService(ServiceDTO newServiceDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException, ParseException {
        logger.info("------------------in Service-------------------");
        logger.info("Creating service with name: {}", newServiceDto.getName());
    
        // Verificar si ya existe un servicio con el mismo nombre
        logger.info("------------------Comprobar si asocia el Servicio-------------------");
        serviceRepository.findByName(newServiceDto.getName()).ifPresent(service -> {
            logger.error("Service with name '{}' already exists", newServiceDto.getName());
            throw new RuntimeException("There is already a service with the same name");
        });
    
        // Validar que la fecha final no sea anterior a la fecha inicial
        if (new SimpleDateFormat("yyyy-MM-dd").parse(newServiceDto.getFinalDate())
                .before(new SimpleDateFormat("yyyy-MM-dd").parse(newServiceDto.getInitialDate()))) {
            logger.error("End date {} is before start date {} for service '{}'", newServiceDto.getFinalDate(), newServiceDto.getInitialDate(), newServiceDto.getName());
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha de inicio");
        }
    
        // Crear el nuevo servicio sin el campo multimedia (se asigna después)
        ServiceS service = new ServiceS(newServiceDto.getName(), newServiceDto.getPrice(), 
                new SimpleDateFormat("yyyy-MM-dd").parse(newServiceDto.getInitialDate()), 
                new SimpleDateFormat("yyyy-MM-dd").parse(newServiceDto.getFinalDate()), 
                newServiceDto.getDescription());
    
        // Asociar el emprendimiento
        Entrepreneurship entrepreneurship = entrepreneurshipRepository.findByUserEntity_Id(newServiceDto.getIdUser_Entity())
            .orElseThrow(() -> new RuntimeException("Entrepreneurship not found with ID: " + newServiceDto.getIdUser_Entity()));
        service.setEntrepreneurship(entrepreneurship);
        service.setMultimedia("temporary");
    
        // Guardar el servicio
        ServiceS createdService = serviceRepository.save(service);
    
        // Actualizar el valor multimedia después de guardar
        createdService.setMultimedia("s-" + createdService.getId().toString());
        serviceRepository.save(createdService);
    
        // Subir la imagen a MinIO
        uploadServiceImage(createdService.getId(), newServiceDto.getPicture());
    
        return createdService;
    }
    

    public ServiceS editService(Long id, ServiceDTO editedServiceDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException, ParseException {
        ServiceS service = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
    
        // Validación de fechas: la fecha final no puede ser anterior a la fecha inicial
        if (new SimpleDateFormat("yyyy-MM-dd").parse(editedServiceDto.getFinalDate())
                .before(new SimpleDateFormat("yyyy-MM-dd").parse(editedServiceDto.getInitialDate()))) {
            logger.error("End date {} is before start date {} for service '{}'", editedServiceDto.getFinalDate(), editedServiceDto.getInitialDate(), editedServiceDto.getName());
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha de inicio");
        }
    
        // Actualizar los campos
        service.setName(editedServiceDto.getName());
        service.setPrice(editedServiceDto.getPrice());
        service.setInitialDate(new SimpleDateFormat("yyyy-MM-dd").parse(editedServiceDto.getInitialDate()));
        service.setFinalDate(new SimpleDateFormat("yyyy-MM-dd").parse(editedServiceDto.getFinalDate()));
        service.setDescription(editedServiceDto.getDescription());
        service.setMultimedia("s-" + id.toString());
    
        // Asociar el emprendimiento
        Entrepreneurship entrepreneurship = entrepreneurshipRepository.findByUserEntity_Id(editedServiceDto.getIdUser_Entity())
            .orElseThrow(() -> new RuntimeException("Entrepreneurship not found with ID: " + editedServiceDto.getIdUser_Entity()));
        service.setEntrepreneurship(entrepreneurship);
    
        // Guardar el servicio actualizado
        ServiceS updatedService = serviceRepository.save(service);
    
        // Subir la imagen actualizada a MinIO
        uploadServiceImage(updatedService.getId(), editedServiceDto.getPicture());
    
        return updatedService;
    }
    

    public void deleteService(Long id) {
        ServiceS service = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));

        // Borrar la imagen de MinIO antes de eliminar el servicio
        try {
            minioService.deleteFile("s-" + id);
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException | NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
            throw new RuntimeException("Error deleting image", e);
        }

        serviceRepository.delete(service);
    }

    // Método auxiliar para subir la imagen del servicio a MinIO
    private void uploadServiceImage(Long serviceId, MultipartFile picture) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "s-" + serviceId;  // Usar el ID del servicio como parte del nombre del archivo
        logger.info("Uploading image for service with ID: {}", serviceId);
        minioService.uploadFile(fileName, picture);
    }
    
    public Page<ServiceS> getServicesByEntrepreneurshipId(Long entrepreneurshipId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Entrepreneurship entrepreneurship = entrepreneurshipRepository.findByUserEntity_Id(entrepreneurshipId)
            .orElseThrow(() -> new RuntimeException("Entrepreneurship not found with ID: " + entrepreneurshipId));
        Page<ServiceS> services = serviceRepository.findByEntrepreneurship_Id(entrepreneurship.getId(), pageable);
        
        if (services.isEmpty()) {
            throw new RuntimeException("No services found for Entrepreneurship ID: " + entrepreneurshipId);
        }
    
        return services;
    }

}
