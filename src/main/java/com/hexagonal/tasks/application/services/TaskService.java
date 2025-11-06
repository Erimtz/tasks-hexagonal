package com.hexagonal.tasks.application.services;

import com.hexagonal.tasks.domain.models.AdditionalTaskInfo;
import com.hexagonal.tasks.domain.models.Task;
import com.hexagonal.tasks.domain.ports.in.*;

import java.util.List;
import java.util.Optional;

public class TaskService implements CreateTaskUseCase, DeleteTaskUseCase, GetAdditionalTaskInfoUseCase, RetrieveTaskUseCase, UpdateTaskUseCase {


    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final RetrieveTaskUseCase retrieveTaskUseCase;
    private final GetAdditionalTaskInfoUseCase getAdditionalTaskInfoUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    public TaskService(CreateTaskUseCase createTaskUseCase, UpdateTaskUseCase updateTaskUseCase, RetrieveTaskUseCase retrieveTaskUseCase, GetAdditionalTaskInfoUseCase getAdditionalTaskInfoUseCase, DeleteTaskUseCase deleteTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.retrieveTaskUseCase = retrieveTaskUseCase;
        this.getAdditionalTaskInfoUseCase = getAdditionalTaskInfoUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
    }


    @Override
    public Task createTask(Task task) {
        return createTaskUseCase.createTask(task);
    }

    @Override
    public boolean deleteTask(Long id) {
        return deleteTaskUseCase.deleteTask(id);
    }

    @Override
    public AdditionalTaskInfo getAdditionalTaskInfo(Long id) {
        return getAdditionalTaskInfoUseCase.getAdditionalTaskInfo(id);
    }

    @Override
    public Optional<Task> getTask(Long id) {
        return retrieveTaskUseCase.getTask(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return retrieveTaskUseCase.getAllTasks();
    }

    @Override
    public Optional<Task> updateTask(Long id, Task updateTask) {
        return updateTaskUseCase.updateTask(id, updateTask);
    }
}
