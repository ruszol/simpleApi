package com.ruszol.petoneapi.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @JsonProperty("created_at")
    private Instant createdAt;

    @NonNull
    private String description;
}
