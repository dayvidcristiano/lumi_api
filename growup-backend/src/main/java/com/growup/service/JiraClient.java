package com.growup.service;

import com.growup.model.UserStory;

public interface JiraClient {
    String criarIssue(UserStory historia, String projectKey);
}
