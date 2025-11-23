package com.growup.repository;

import com.growup.model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    List<UserStory> findByProjetoId(Long projetoId);
    List<UserStory> findBySprintId(Long sprintId);
    List<UserStory> findByProjetoIdAndSprintIsNull(Long projetoId);
}
