package com.hexagonal.tasks.infraestructure.repositories;

import com.hexagonal.tasks.infraestructure.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> {
}
