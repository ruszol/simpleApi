package com.ruszol.petoneapi.api.factories;


import com.ruszol.petoneapi.api.dto.ProjectDto;
import com.ruszol.petoneapi.store.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto(ProjectEntity entity){
            return ProjectDto.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();

    }

}
