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
import com.innovatech.inventory.entity.ServiceS;  // Cambié ServiceS a Service
import com.innovatech.inventory.repository.ServiceRepository; // Cambié ServiceRepositoryy a ServiceRepository

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import org.springframework.web.multipart.MultipartFile;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;  // Ahora usa el nombre correcto

    @Autowired
    private MinioService minioService;

    public ServiceS findService(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public List<ServiceS> listServices(Integer page, Integer limit) {  // Cambié nombres para ser consistentes con 'Service'
        PageRequest pageable = PageRequest.of(page - 1, limit);
        return serviceRepository.findAll(pageable).getContent();
    }

    public ServiceS createService(ServiceDTO newServiceDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException, ParseException {
        ServiceS service = new ServiceS(newServiceDto.getName(),newServiceDto.getPrice(),new SimpleDateFormat("yyyy-MM-dd").parse(newServiceDto.getInitialDate()),new SimpleDateFormat("yyyy-MM-dd").parse(newServiceDto.getFinalDate()),newServiceDto.getDescription(),"s-" + newServiceDto.getId().toString());
        
        ServiceS createdService = serviceRepository.save(service);


        // Guardar el servicio nuevamente con el campo de imagen actualizado
        serviceRepository.save(createdService);

        // Subir la imagen a MinIO
        uploadServiceImage(createdService.getId(), newServiceDto.getPicture());

        return createdService;
    }

    public ServiceS editService(Long id, ServiceDTO editedServiceDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException, ParseException {
        // Encontrar el servicio que se va a editar
        ServiceS service = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));

        // Actualizar los campos
        service.setName(editedServiceDto.getName());
        service.setPrice(editedServiceDto.getPrice());
        service.setInitialDate(new SimpleDateFormat("yyyy-MM-dd").parse(editedServiceDto.getInitialDate()));
        service.setFinalDate(new SimpleDateFormat("yyyy-MM-dd").parse(editedServiceDto.getFinalDate()));
        service.setDescription(editedServiceDto.getDescription());
        service.setMultimedia("s-" + id.toString()); // Actualizar el nombre del archivo multimedia

        // Guardar los cambios
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

        // Eliminar el servicio de la base de datos
        serviceRepository.delete(service);
    }

    // Helper method para subir la imagen del servicio a MinIO
    private void uploadServiceImage(Long serviceId, MultipartFile picture) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "s-" + serviceId;  // Usar el ID del servicio como parte del nombre del archivo
        minioService.uploadFile(fileName, picture);
    }
}
