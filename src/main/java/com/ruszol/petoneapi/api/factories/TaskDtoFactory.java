package com.ruszol.petoneapi.api.factories;

import com.ruszol.petoneapi.api.dto.TaskDto;
import com.ruszol.petoneapi.store.entities.TaskEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .description(entity.getDescription())
                .build();
    }
}
