package org.example.catalogservice.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.catalogservice.entity.CatalogEntity;
import org.example.catalogservice.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService {
    CatalogRepository catalogRepository;

    @Autowired
    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Iterable<CatalogEntity> getAlCatalogs() {
        return catalogRepository.findAll();
    }
}
