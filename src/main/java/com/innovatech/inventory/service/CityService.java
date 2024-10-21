package com.innovatech.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

import com.innovatech.inventory.entity.City;
import com.innovatech.inventory.repository.CityRepository;

@Service
public class CityService implements CrudService<City, Long> {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public City findById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }
}
