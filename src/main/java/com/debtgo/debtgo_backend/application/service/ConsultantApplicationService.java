package com.debtgo.debtgo_backend.application.service;

import com.debtgo.debtgo_backend.domain.ConsultantServiceEntity;
import com.debtgo.debtgo_backend.repository.ConsultantServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultantApplicationService {

    private final ConsultantServiceRepository repository;

    public ConsultantApplicationService(ConsultantServiceRepository repository) {
        this.repository = repository;
    }

    public List<ConsultantServiceEntity> listByConsultant(Long consultantId) {
        return repository.findByConsultantId(consultantId);
    }

    public ConsultantServiceEntity create(ConsultantServiceEntity service) {
        return repository.save(service);
    }

    public ConsultantServiceEntity update(Long id, ConsultantServiceEntity data) {
        ConsultantServiceEntity s = repository.findById(id).orElseThrow();
        s.setTitle(data.getTitle());
        s.setDescription(data.getDescription());
        s.setPrice(data.getPrice());
        return repository.save(s);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}