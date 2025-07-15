package com.ruszol.petoneapi.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @Builder.Default
    @NonNull
    @JsonProperty("updated_at")
    private Instant updatedAt =  Instant.now();

    @NonNull
    @JsonProperty("created_at")
    private Instant createdAt;

}
