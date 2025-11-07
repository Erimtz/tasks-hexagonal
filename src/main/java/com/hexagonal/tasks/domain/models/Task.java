package com.hexagonal.tasks.domain.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private boolean completed;

}
