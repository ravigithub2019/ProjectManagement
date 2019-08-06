package com.cts.project.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.project.management.model.ParentTask;
@Repository
public interface ParentTaskRepository  extends JpaRepository<ParentTask,Integer>{

}
