package com.hexagonal.tasks.application.usercases;

import com.hexagonal.tasks.domain.ports.in.DeleteTaskUseCase;
import com.hexagonal.tasks.domain.ports.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public boolean deleteTask(Long id) {
        return taskRepositoryPort.deleteById(id);
    }
}
