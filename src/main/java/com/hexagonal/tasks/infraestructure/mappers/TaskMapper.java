package com.hexagonal.tasks.infraestructure.mappers;

import com.hexagonal.tasks.domain.models.Task;
import com.hexagonal.tasks.infraestructure.entities.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskEntity toEntity(Task task);

    Task toDomain(TaskEntity taskEntity);
}
