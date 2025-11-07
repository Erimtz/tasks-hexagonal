package com.hexagonal.tasks.infraestructure.repositories;

import com.hexagonal.tasks.domain.models.Task;
import com.hexagonal.tasks.domain.ports.out.TaskRepositoryPort;
import com.hexagonal.tasks.infraestructure.entities.TaskEntity;
import com.hexagonal.tasks.infraestructure.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaTaskRepositoryAdapter implements TaskRepositoryPort {

    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;


    @Override
    public Task save(Task task) {
        TaskEntity taskEntity = taskMapper.toEntity(task);
        return taskMapper.toDomain(jpaTaskRepository.save(taskEntity));
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jpaTaskRepository.findById(id).map(taskMapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return jpaTaskRepository.findAll().stream()
                .map(taskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Task> update(Task task) {
        if (jpaTaskRepository.existsById(task.getId())) {
            TaskEntity taskEntity = taskMapper.toEntity(task);
            return Optional.of(taskMapper.toDomain(jpaTaskRepository.save(taskEntity)));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        if (jpaTaskRepository.existsById(id)) {
            jpaTaskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
