package com.innovatech.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.innovatech.inventory.entity.State;
import com.innovatech.inventory.repository.StateRepository;

@Service
public class StateService implements CrudService<State, Long> {

    @Autowired
    private StateRepository stateRepository;

    @Override
    public State findById(Long id) {
        return stateRepository.findById(id).orElse(null);
    }

    public Page<State> findAll(Pageable pageable) {
        return stateRepository.findAll(pageable);
    }

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        stateRepository.deleteById(id);
    }

    @Override
    public State save(State state) {
        return stateRepository.save(state);
    }
}
