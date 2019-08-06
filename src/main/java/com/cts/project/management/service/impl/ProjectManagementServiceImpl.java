package com.cts.project.management.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.project.management.model.ParentTask;
import com.cts.project.management.model.Project;
import com.cts.project.management.model.Task;
import com.cts.project.management.model.Users;
import com.cts.project.management.repository.ParentTaskRepository;
import com.cts.project.management.repository.ProjectRepository;
import com.cts.project.management.repository.TaskRepository;
import com.cts.project.management.repository.UsersRepository;
import com.cts.project.management.service.ProjectManagementService;
import com.cts.project.management.vo.ManagerVO;
import com.cts.project.management.vo.ParentTaskVO;
import com.cts.project.management.vo.ProjectVO;
import com.cts.project.management.vo.TaskVO;
import com.cts.project.management.vo.UserVO;

@Service
public class ProjectManagementServiceImpl implements ProjectManagementService {

	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ParentTaskRepository parentTaskRepository;

	public void addUser(UserVO vo) {

		if (vo.getUserId() > 0) {
			Users user = userRepository.findUsersById(vo.getUserId());
			List<Users> users = userRepository.findUsersByNameandEmpId(user.getFirstName(), user.getLastName(),
					user.getEmployeeId());
			users.forEach(currentUser -> {
				currentUser.setEmployeeId(vo.getEmployeeId());
				currentUser.setFirstName(vo.getFirstName());
				currentUser.setLastName(vo.getLastName());
			});
			userRepository.saveAll(users);
		} else {
			Users user = new Users();
			user.setFirstName(vo.getFirstName());
			user.setLastName(vo.getLastName());
			user.setEmployeeId(vo.getEmployeeId());
			user.setStatus(vo.getStatus());
			userRepository.save(user);
		}
	}

	public UserVO getUserById(int userId) {
		Optional<Users> optUser = userRepository.findById(userId);
		UserVO userVO = new UserVO();
		userVO.setFirstName(optUser.get().getFirstName());
		userVO.setLastName(optUser.get().getLastName());
		userVO.setEmployeeId(optUser.get().getEmployeeId());
		userVO.setStatus(optUser.get().getStatus());
		return userVO;
	}

	@Override
	public boolean deleteUser(int userId) throws Exception {
		Users user = userRepository.findUsersById(userId);
		List<Users> users = userRepository.findUsersByNameandEmpId(user.getFirstName(), user.getLastName(),
				user.getEmployeeId());
		users.forEach(currentUser -> {
			currentUser.setStatus("Deleted");
		});
		userRepository.saveAll(users);
		return true;
	}

	@Override
	public List<UserVO> getAllUsers() {
		List<UserVO> userVOList = new ArrayList<UserVO>();
		List<Users> users = userRepository.findUsers();

		userVOList.addAll(users.stream().map(user -> new UserVO(user.getUserId(), user.getEmployeeId(),
				user.getFirstName(), user.getLastName(), user.getStatus())).distinct().collect(Collectors.toList()));
		/*
		 * for (Users user : users) { UserVO userVO = new UserVO();
		 * userVO.setFirstName(user.getFirstName());user.getEmployeeId()
		 * userVO.setLastName(user.getLastName());
		 * userVO.setEmployeeId(user.getEmployeeId());
		 * userVO.setStatus(user.getStatus()); userVO.setUserId(user.getUserId());
		 * userVOList.add(userVO);
		 * 
		 * } return userVOList;
		 */
		return userVOList;

	}

	@Override
	public List<UserVO> getDistinctUser() {
		List<UserVO> userVOList = new ArrayList<UserVO>();
		List<ManagerVO> managerVOs = userRepository.findAllManagers();

		userVOList.addAll(managerVOs.stream().map(
				managerVO -> new UserVO(managerVO.getEmployeeId(), managerVO.getFirstName(), managerVO.getLastName()))
				.distinct().collect(Collectors.toList()));
		return userVOList;

	}

	@Override
	public void updateUser(UserVO userVO) {
		Users user = new Users();
		user.setEmployeeId(userVO.getEmployeeId());
		user.setFirstName(userVO.getFirstName());
		user.setLastName(userVO.getLastName());
		user.setStatus(userVO.getStatus());
		userRepository.save(user);

	}

	@Override
	public void updateProject(ProjectVO projectVO) {

		Project project = new Project();
		project.setProjectId(projectVO.getProjectId());
		project.setProject(projectVO.getProject());
		// project.setStartDate(projectVO.getStartDate());
		// project.setEndDate(projectVO.getEndDate());
		project.setPriority(projectVO.getPriority());
		// project.setManagerId(Integer.parseInt(projectVO.getEmployeeId()));
		projectRepository.save(project);

	}

	@Override
	public List<ProjectVO> getAllProjects() {

		List<ProjectVO> projectVOList = new ArrayList<ProjectVO>();
		List<Project> projects = projectRepository.findAll();

		for (Project project : projects) {
			ProjectVO prjVO = new ProjectVO();
			prjVO.setProject(project.getProject());
			prjVO.setProjectId(project.getProjectId());
			prjVO.setStartDate(project.getStartDate());
			prjVO.setEndDate(project.getEndDate());
			prjVO.setStatus(project.getStatus());
			prjVO.setPriority(project.getPriority());
			List<Task> tasksList = taskRepository.getTasksByProjectId(project.getProjectId());
			Users assignedUser = userRepository.findUserByProjectId(project.getProjectId());
			prjVO.setEmployeeId(assignedUser.getEmployeeId());
			prjVO.setFirstName(assignedUser.getFirstName());
			prjVO.setLastName(assignedUser.getLastName());
			prjVO.setEmployeeName(assignedUser.getFirstName() + " " + assignedUser.getLastName());
			if (null != tasksList)
				prjVO.setNoOfTask(tasksList.size());
			projectVOList.add(prjVO);

		}
		return projectVOList;
	}

	@Override
	public void saveOrUpdateProject(ProjectVO vo) {
		Project project = null;

		if (vo.getProjectId() > 0) {
			project = projectRepository.findById(vo.getProjectId()).get();
		} else {
			project = new Project();
		}
		// setting proj name
		project.setProject(vo.getProject());

		if (!vo.isHasSetDefaultDate()) {
			project.setStartDate(vo.getStartDate());
			project.setEndDate(vo.getEndDate());
		} else {
			project.setStartDate(new Date());
			project.setEndDate(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
		}

		project.setPriority(vo.getPriority());
		project.setStatus(vo.getStatus());
		project = projectRepository.save(project);

		ManagerVO managerVO = vo.getManagerVO();
		Users newUser = new Users();
		newUser.setProjectId(project.getProjectId());
		/*
		 * newUser.setEmployeeId(managerVO.getEmployeeId());
		 * newUser.setFirstName(managerVO.getFirstName());
		 * newUser.setLastName(managerVO.getLastName());
		 */

		newUser.setEmployeeId(vo.getEmployeeId());
		newUser.setFirstName(vo.getFirstName());
		newUser.setLastName(vo.getLastName());
		newUser.setStatus("Active");

		// newUser.setProject(projectSet);
		// Need to detach existing user to project mapping
		Users existingAllocatedUser = userRepository.findUserByProjectId(project.getProjectId());

		if (null != existingAllocatedUser) {
			userRepository.deleteById(existingAllocatedUser.getUserId());
		}
		userRepository.save(newUser);

	}

	@Override
	public void suspendProject(@Valid ProjectVO vo) {
		Project project = projectRepository.findById(vo.getProjectId()).get();
		project.setStatus("Completed");
		projectRepository.save(project);
	}

	public List<ParentTaskVO> getAllParentTasks() {
		List<ParentTaskVO> parentTaskVOList = new ArrayList<ParentTaskVO>();
		List<ParentTask> parentTasks = parentTaskRepository.findAll();
		for (ParentTask parentTask : parentTasks) {
			ParentTaskVO parentTaskVO = new ParentTaskVO();
			parentTaskVO.setParentId(parentTask.getParentId());
			parentTaskVO.setParentTask(parentTask.getParentTask());
			// parentTaskVO.setProjectId(parentTask.getProjectId());
			parentTaskVOList.add(parentTaskVO);

		}
		return parentTaskVOList;
	}

	public ParentTask getParentTasks(Integer parentId) {
		 ParentTask optParent = parentTaskRepository.findById(parentId).get();
		return optParent;
	}

	public List<TaskVO> getAllTasks() {

		List<TaskVO> taskVOList = new ArrayList<TaskVO>();
		List<Task> tasks = taskRepository.findAll();
		for (Task task : tasks) {
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskId(task.getTaskId());
			taskVO.setTaskName(task.getTask());
			if (null != task.getParentTask()) {
				taskVO.setParentTaskId(task.getParentTask().getParentId());
				taskVO.setParentTaskName(task.getParentTask().getParentTask());
			}
			taskVO.setProjectId(task.getProject().getProjectId());
			taskVO.setPriority(task.getPriority());
			taskVO.setStartDate(task.getStartDate());
			taskVO.setEndDate(task.getEndDate());
			taskVO.setStatus(task.getStatus());
			// taskVO.setEmployeeId(task.getUserDetails().getEmployeeId());
			taskVOList.add(taskVO);

		}
		return taskVOList;
	}

	public List<TaskVO> getTasksByProject(int projectId) {
		List<Task> tasks = taskRepository.getTasksByProjectId(projectId);
		List<Users> users = userRepository
				.getUsersByTask(tasks.stream().map(task -> task.getTaskId()).collect(Collectors.toList()));
		List<TaskVO> taskVOList = new ArrayList<TaskVO>();
		for (Task task : tasks) {
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskId(task.getTaskId());
			taskVO.setTaskName(task.getTask());
			if (null != task.getParentTask()) {
				taskVO.setParentTaskId(task.getParentTask().getParentId());
				taskVO.setParentTaskName(task.getParentTask().getParentTask());
			}
			taskVO.setProjectId(task.getProject().getProjectId());
			taskVO.setPriority(task.getPriority());
			taskVO.setStartDate(task.getStartDate());
			taskVO.setEndDate(task.getEndDate());
			taskVO.setStatus(task.getStatus());
			taskVO.setProjectName(task.getProject().getProject());
			Users assignedUser = users.stream().filter(user -> (user.getTaskId() == task.getTaskId())).findFirst()
					.get();
			taskVO.setEmployeeId(assignedUser.getEmployeeId());
			taskVO.setFirstName(assignedUser.getFirstName());
			taskVO.setLastName(assignedUser.getLastName());
			taskVOList.add(taskVO);
		}

		return taskVOList;
	}

	public void saveTask(TaskVO taskVO) {

		Task task = new Task();
		task.setStatus("A");
		task.setTask(taskVO.getTaskName());
		task.setProject(projectRepository.findById(taskVO.getProjectId()).get());
		task = taskRepository.save(task);

		if (taskVO.isParentTaskInd()) {
			ParentTask parent = new ParentTask();
			parent.setParentId(task.getTaskId());
			parent.setParentTask(taskVO.getTaskName());
			parentTaskRepository.save(parent);

		} else {
			task.setEndDate(taskVO.getEndDate());
			task.setPriority(taskVO.getPriority());
			task.setStartDate(taskVO.getStartDate());
		}

		if (taskVO.getParentTaskId() > 0) {
			ParentTask parentTask = parentTaskRepository.findById(taskVO.getParentTaskId()).get();
			task.setParentTask(parentTask);
		}
		Users usr = new Users();

		Users projectUser = userRepository.findById(taskVO.getUserId()).get();

		usr.setProjectId(taskVO.getProjectId());
		usr.setTaskId(task.getTaskId());
		usr.setEmployeeId(projectUser.getEmployeeId());
		usr.setFirstName(projectUser.getFirstName());
		usr.setLastName(projectUser.getLastName());
		usr.setStatus(projectUser.getStatus());
		userRepository.save(usr);

	}

	public TaskVO getTask(String taskId) {
		Task optTask = taskRepository.findById(Integer.parseInt(taskId)).get();

		TaskVO taskVO = new TaskVO();
		taskVO.setTaskId(optTask.getTaskId());
		taskVO.setTaskName(optTask.getTask());
		taskVO.setParentTaskId(optTask.getParentTask().getParentId());
		taskVO.setParentTaskName(optTask.getParentTask().getParentTask());
		taskVO.setProjectId(optTask.getProject().getProjectId());
		taskVO.setPriority(optTask.getPriority());
		taskVO.setStartDate(optTask.getStartDate());
		taskVO.setEndDate(optTask.getEndDate());
		taskVO.setStatus(optTask.getStatus());
		// taskVO.setEmployeeId(optTask.getUserDetails().getEmployeeId());
		return taskVO;
	}

	public void updateTask(TaskVO taskVO) {
		Task task = taskRepository.findById(taskVO.getTaskId()).get();
		task.setTaskId(taskVO.getTaskId());
		task.setEndDate(taskVO.getEndDate());
		task.setPriority(taskVO.getPriority());
		task.setStartDate(taskVO.getStartDate());
		task.setStatus(taskVO.getStatus());
		task.setTask(taskVO.getTaskName());

		task.setProject(projectRepository.findById(taskVO.getProjectId()).get());

		Users user = userRepository.findUserByEmployeeId(taskVO.getEmployeeId());

		Users existingAssignee = userRepository.getUserByTaskId(taskVO.getTaskId());
		if (existingAssignee != null) {
			userRepository.deleteById(existingAssignee.getUserId());
		}

		Users newAssignee = new Users();
		newAssignee.setEmployeeId(taskVO.getEmployeeId());
		newAssignee.setFirstName(taskVO.getFirstName());
		newAssignee.setLastName(taskVO.getLastName());
		newAssignee.setStatus(user.getStatus());
		newAssignee.setProjectId(taskVO.getProjectId());
		newAssignee.setTaskId(taskVO.getTaskId());
		userRepository.save(newAssignee);

		ParentTask p = getParentTasks(taskVO.getParentTaskId());
		// p.setParentTaskId(taskVO.getParentTaskId());
		// p.setParentTaskName("tsk screens");
		task.setParentTask(p);

		taskRepository.save(task);
	}
}
