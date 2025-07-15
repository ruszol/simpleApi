package com.ruszol.petoneapi.api.controllers;

import com.ruszol.petoneapi.api.dto.AskDto;
import com.ruszol.petoneapi.api.dto.ProjectDto;
import com.ruszol.petoneapi.api.exceptions.BadRequestExceptions;
import com.ruszol.petoneapi.api.exceptions.NotFoundException;
import com.ruszol.petoneapi.api.factories.ProjectDtoFactory;
import com.ruszol.petoneapi.store.entities.ProjectEntity;
import com.ruszol.petoneapi.store.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ProjectController {

   ProjectDtoFactory projectDtoFactory;
   ProjectRepository projectRepository;
   ControllerHelper controllerHelper;

   private static final String CREATE_PROJECT = "/api/projects";
   private static final String EDIT_PROJECT = "/api/projects/{project_id}";
   private static final String FETCH_PROJECTS = "/api/projects";
   private static final String DELETE_PROJECTS = "/api/projects/{project_id}";


   private static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";


   @GetMapping(value = FETCH_PROJECTS)
   public List<ProjectDto> fetchProjects(@RequestParam(value = "prefix_name",
                                          required = false) Optional<String> optionalPrefixName){

      optionalPrefixName = optionalPrefixName.filter(prefixName->!prefixName.trim().isEmpty());

      Stream<ProjectEntity> projectStream = optionalPrefixName
              .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
              .orElseGet(projectRepository::streamAllBy);

      return projectStream
              .map(projectDtoFactory::makeProjectDto)
              .collect(Collectors.toList());
   }
   @DeleteMapping(value = DELETE_PROJECTS)
   public AskDto deleteProject(@PathVariable("project_id") Long projectId){
      controllerHelper.getProjectOrThrowException(projectId);
      projectRepository.deleteById(projectId);
      return AskDto.makeDefault(true);

   }

//   //Экстракт метода выше
//   private ProjectEntity getProjectOrThrowException(Long projectId) {
//      return projectRepository.findById(projectId)
//              .orElseThrow(() -> new NotFoundException("Project not found"));
//   }


//   @PostMapping(CREATE_PROJECT)
//   public ProjectDto createProject(@RequestParam("project_name") String projectName){
//
//      if(projectName.trim().isEmpty()){
//         throw new BadRequestExceptions("projectName is empty!");
//      }
//
//      projectRepository
//              .findByName(projectName)
//              .ifPresent(project ->{ throw new BadRequestExceptions(String.format("Project \"%s\" already exists", projectName));
//   });
//
//      ProjectEntity entity = projectRepository.saveAndFlush(
//              ProjectEntity.builder()
//                      .name(projectName)
//                      .build()
//      );
//      return projectDtoFactory.makeProjectDto(entity);
//   }
//   @PatchMapping(EDIT_PROJECT)
//   public ProjectDto editProject(@PathVariable("project_id") Long projectId,
//                                 @RequestParam("project_name") String projectName){
//
//      if(projectName.trim().isEmpty()){
//         throw new BadRequestExceptions("projectName is empty!");
//      }
//
//      ProjectEntity project =  projectRepository
//                              .findById(projectId)
//                              .orElseThrow(()-> new NotFoundException(
//                               String.format("Project with id \"%s\" does not exist", projectId)));
//
//
//      projectRepository.findByName(projectName)
//                              .filter(anotherProject -> !Objects.equals(anotherProject.getId(),projectId))
//                              .ifPresent(anotherProject ->{
//                                 throw new BadRequestExceptions(String.format("Project \"%s\" already exists", projectName));
//                              });
//
//      project.setName(projectName);
//      projectRepository.saveAndFlush(project);
//      return projectDtoFactory.makeProjectDto(project);
//   }


   @PutMapping(CREATE_OR_UPDATE_PROJECT)
   public ProjectDto createOrUpdateProject(
           @RequestParam(value = "project_id",required = false)Optional<Long> optionalProjectId,
           @RequestParam (value = "project_name",required = false)Optional<String> optionalProjectName){

      optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());

      boolean isCreate = !optionalProjectId.isPresent();

      if(isCreate && !optionalProjectName.isPresent()){
         throw new BadRequestExceptions("projectName is empty!");
      }

      final ProjectEntity project = optionalProjectId
              .map(controllerHelper::getProjectOrThrowException)
              .orElseGet(()->ProjectEntity.builder().build());

      optionalProjectName.ifPresent(projectName->{
         projectRepository.findByName(projectName)
                 .filter(anotherProject->!Objects.equals(anotherProject.getId(),project.getId()))
                 .ifPresent(anotherProject ->{
                    throw new BadRequestExceptions(String.format("Project \"%s\" already exists", projectName));
                 });
         project.setName(projectName);
      });
      final ProjectEntity savedProject = projectRepository.saveAndFlush(project);
      return projectDtoFactory.makeProjectDto(savedProject);


   }



}
