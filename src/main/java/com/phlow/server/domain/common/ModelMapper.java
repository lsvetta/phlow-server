package com.phlow.server.domain.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModelMapper<ModelType, DtoType> {

    DtoType modelToDto(ModelType Model);

    ModelType dtoToModel(DtoType dto);

    List<ModelType> dtosToModels(List<DtoType> dtos);

    default Page<ModelType> dtosToModels(Page<DtoType> dtos, Pageable pageable) {
        return new PageImpl<>(dtosToModels(dtos.getContent()), pageable, dtos.getTotalElements());
    }

    List<DtoType> modelsToDtos(List<ModelType> models);

    default Page<DtoType> modelsToDtos(Page<ModelType> models, Pageable pageable) {
        return new PageImpl<>(modelsToDtos(models.getContent()), pageable, models.getTotalElements());
    }
}

