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

import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private EntrepreneurshipRepository entrepreneurshipRepository;

    @Autowired
    private UserRepository UserRepository;

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

        // Crear el nuevo servicio sin el campo multimedia (se asigna después)
        //logger.info("------------------Crea el objetooooo Servicio-------------------");
        ServiceS service = new ServiceS(newServiceDto.getName(), newServiceDto.getPrice(), 
                new SimpleDateFormat("yyyy-MM-dd").parse(newServiceDto.getInitialDate()), 
                new SimpleDateFormat("yyyy-MM-dd").parse(newServiceDto.getFinalDate()), 
                newServiceDto.getDescription());
               // logger.info("All emprendimientos: {}", entrepreneurshipRepository.findAll());
              // logger.info("------------------Mira si existe un emprendimiento con el id del usuario {}-------------------",newServiceDto.getIdUser_Entity());
               //logger.info("-------------------Asi se ven todos los emprendimientos-------------------");
               //logger.info("{}",UserRepository.findAll());
               Entrepreneurship entrepreneurship = entrepreneurshipRepository.findByUserEntity_Id(newServiceDto.getIdUser_Entity())
        .orElseThrow(() -> new RuntimeException("enterepreneurship not found with ID: " + newServiceDto.getIdUser_Entity()));
        //logger.info("------------------Lo mete en servicio -------------------");
        //logger.info("-----------------Como se ve el objeto de emprendimiento-----------------------------------");
        //logger.info("{}",entrepreneurship);
            service.setEntrepreneurship(entrepreneurship);
            service.setMultimedia("temporary");
        
        logger.info("Se crea el temporar");
        //logger.info("before All products {}", serviceRepository.findAll());

        ServiceS createdService = serviceRepository.save(service);
        //logger.info("Service created successfully with ID: {}", createdService.getId());

       createdService.setMultimedia("s-" + createdService.getId().toString());
        serviceRepository.save(createdService);
       // logger.info("Service updated with multimedia: {}", createdService.getMultimedia());

        // Subir la imagen a MinIO
        uploadServiceImage(createdService.getId(), newServiceDto.getPicture());
        //logger.info("Image uploaded successfully for service with ID: {}", createdService.getId());

        return createdService;
    }

    public ServiceS editService(Long id, ServiceDTO editedServiceDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException, ParseException {
        ServiceS service = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));

        // Actualizar los campos
        service.setName(editedServiceDto.getName());
        service.setPrice(editedServiceDto.getPrice());
        service.setInitialDate(new SimpleDateFormat("yyyy-MM-dd").parse(editedServiceDto.getInitialDate()));
        service.setFinalDate(new SimpleDateFormat("yyyy-MM-dd").parse(editedServiceDto.getFinalDate()));
        service.setDescription(editedServiceDto.getDescription());
        service.setMultimedia("s-" + id.toString());

        Entrepreneurship entrepreneurship = entrepreneurshipRepository.findByUserEntity_Id(editedServiceDto.getIdUser_Entity())
            .orElseThrow(() -> new RuntimeException("Entrepreneurship not found with ID: " + editedServiceDto.getIdUser_Entity()));
            service.setEntrepreneurship(entrepreneurship);	

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
}
