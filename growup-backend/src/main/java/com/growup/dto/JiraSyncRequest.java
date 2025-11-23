package com.growup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JiraSyncRequest {
    private List<Long> historiaIds;
    private String jiraProjectKey;
}
