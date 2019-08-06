package com.cts.project.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.project.management.model.Task;


/**
 * @author Admin
 *
 */
@Repository
public interface TaskRepository  extends JpaRepository<Task,Integer>{
	/*@Query("SELECT count(t) FROM Task t where t.project.projectId = ?1")
	public int getNoOfTask(int projectId); */
	
	@Query("SELECT task FROM Task task where task.project.projectId = :projectId")
	public List<Task> getTasksByProjectId(@Param("projectId") int projectId); 
}
