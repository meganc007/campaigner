package com.mcommings.campaigner.config;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public abstract class BaseService<
        Entity,
        IdType,
        ViewDto,
        CreateDto,
        UpdateDto> {

    protected abstract JpaRepository<Entity, IdType> getRepository();

    protected abstract ViewDto toViewDto(Entity entity);

    protected abstract Entity toEntity(CreateDto dto);

    protected abstract void updateEntity(UpdateDto dto, Entity entity);

    protected abstract IdType getId(UpdateDto dto);

    public List<ViewDto> getAll() {
        return getRepository()
                .findAll()
                .stream()
                .map(this::toViewDto)
                .toList();
    }

    public List<ViewDto> getByCampaign(
            UUID campaignUuid,
            Function<UUID, List<Entity>> queryFunction) {

        return queryFunction.apply(campaignUuid)
                .stream()
                .map(this::toViewDto)
                .toList();
    }

    public ViewDto getById(IdType idType) {
        Entity entity = getRepository()
                .findById(idType)
                .orElseThrow(() ->
                        new IllegalArgumentException("Record not found"));

        return toViewDto(entity);
    }

    public ViewDto create(CreateDto dto) {

        Entity entity = toEntity(dto);

        Entity saved = getRepository().save(entity);

        return toViewDto(saved);
    }

    public ViewDto update(UpdateDto dto) {

        IdType idType = getId(dto);

        Entity entity = getRepository()
                .findById(idType)
                .orElseThrow(() ->
                        new IllegalArgumentException("Record not found"));

        updateEntity(dto, entity);

        Entity saved = getRepository().save(entity);

        return toViewDto(saved);
    }

    public void delete(IdType idType) {
        getRepository().deleteById(idType);
    }

}
