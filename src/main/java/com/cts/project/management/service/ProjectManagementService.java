package com.cts.project.management.service;

import java.util.List;

import javax.validation.Valid;

import com.cts.project.management.vo.ParentTaskVO;
import com.cts.project.management.vo.ProjectVO;
import com.cts.project.management.vo.TaskVO;
import com.cts.project.management.vo.UserVO;

public interface ProjectManagementService {

	void addUser(UserVO user);

	boolean deleteUser(int userId) throws Exception;

	List<UserVO> getAllUsers();

	void updateUser(UserVO userVO);

	void updateProject(ProjectVO projectVO);

	List<ProjectVO> getAllProjects();

	void saveOrUpdateProject(ProjectVO projectMgmt);

	List<UserVO> getDistinctUser();

	void suspendProject(@Valid ProjectVO vo);

	List<ParentTaskVO> getAllParentTasks();

	void saveTask(TaskVO task);

	List<TaskVO> getAllTasks();

	TaskVO getTask(String taskId);

	void updateTask(TaskVO task);

	List<TaskVO> getTasksByProject(int projectId);

}
