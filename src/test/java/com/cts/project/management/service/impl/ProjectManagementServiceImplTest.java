package com.cts.project.management.service.impl;

import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cts.project.management.model.ParentTask;
import com.cts.project.management.model.Project;
import com.cts.project.management.model.Task;
import com.cts.project.management.model.Users;
import com.cts.project.management.repository.ParentTaskRepository;
import com.cts.project.management.repository.ProjectRepository;
import com.cts.project.management.repository.TaskRepository;
import com.cts.project.management.repository.UsersRepository;
import com.cts.project.management.service.impl.ProjectManagementServiceImpl;
import com.cts.project.management.vo.ManagerVO;
import com.cts.project.management.vo.ParentTaskVO;
import com.cts.project.management.vo.ProjectVO;
import com.cts.project.management.vo.TaskVO;
import com.cts.project.management.vo.UserVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectManagementServiceImplTest {

	@InjectMocks
	ProjectManagementServiceImpl target;
	
	@Mock
	private TaskRepository repo;
	
	@Mock
	private ParentTaskRepository parentRepo;
	
	@Mock
	private ProjectRepository projectRepo;
	
	@Mock
	private UsersRepository userRepo;
	
	private MockMvc mockMvc;
	
	@Before
	public void init() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(target).build();
		 
	}
	
	@Test
	public void testAddUser() {
		Users user = mockUsers(); 
		List<Users> usersList = mockUsersList();
		when(userRepo.findUsersById(1)).thenReturn(user);
		when(userRepo.findUsersByNameandEmpId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(usersList);
		userRepo.save(user);
		userRepo.saveAll(usersList);
		UserVO userVo = mockUserVO();
		target.addUser(userVo);
	}
	
	@Test
	public void testAddUsers_Invalid_userId() {
		Users user = mockUsers(); 
		List<Users> usersList = mockUsersList();
		when(userRepo.findUsersById(1)).thenReturn(user);
		when(userRepo.findUsersByNameandEmpId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(usersList);
		userRepo.save(user);
		userRepo.saveAll(usersList);
		UserVO userVo = mockUserVO();
		userVo.setUserId(0);
		target.addUser(userVo);
	}
	
	@Test
	public void testGetUserById() {
		Users optUser = mockUsers(); 
		when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(optUser));
		UserVO userVO = new UserVO();
		userVO.setFirstName(optUser.getFirstName());
		userVO.setLastName(optUser.getLastName());
		userVO.setEmployeeId(optUser.getEmployeeId());
		userVO.setStatus(optUser.getStatus());
		target.getUserById(Mockito.anyInt());
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		Users user = mockUsers();
		List<Users> usersList = mockUsersList();
		when(userRepo.findUsersById(Mockito.anyInt())).thenReturn(user);
		when(userRepo.findUsersByNameandEmpId(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(usersList);
		userRepo.saveAll(usersList);
		target.deleteUser(Mockito.anyInt());
	}
	@Test
	public void testGetAllUsers() throws Exception {
		List<Users> users  = mockUsersList();
		when(userRepo.findUsers()).thenReturn(users);
		target.getAllUsers();
	}

	
	@Test
	public void getDistinctUser() {
		List<ManagerVO> managerVOs = mockManagersVO(); 
		when(userRepo.findAllManagers()).thenReturn(managerVOs);
		target.getDistinctUser();
	}

	public List<ManagerVO> mockManagersVO() {
		List<ManagerVO> managerVOs = new ArrayList<ManagerVO>();
		ManagerVO managerVO = new ManagerVO();
		managerVO.setEmployeeId("1234");managerVO.setFirstName("Manager");managerVO.setLastName("Manager");
		return managerVOs;
	}

	@Test
	public void testUpdateUser() {
		Users user = mockUsers();
		UserVO userVO = mockUserVO();
		userRepo.save(user);
		target.updateUser(userVO);
	}

	@Test
	public void testUpdateProject() {
		ProjectVO projectVO = mockProjectVO();
		Project project = mockProject();
		when(projectRepo.save(project)).thenReturn(project);
		target.updateProject(projectVO);
	}
	
	@Test
	public void getAllProjects() {
		Users assignedUser = mockUsers();
		List<Task> tasksList = new ArrayList<Task>();
		tasksList.add(mockTask());
		List<Project> projects = new ArrayList<Project>();
		projects.add(mockProject());
		when(projectRepo.findAll()).thenReturn(projects);
		
		when(repo.getTasksByProjectId(projects.get(0).getProjectId())).thenReturn(tasksList);
		when(userRepo.findUserByProjectId(projects.get(0).getProjectId())).thenReturn(assignedUser);
		
		target.getAllProjects();
	}
	
	
	@Test
	public void testSaveOrUpdateProject() {
		ProjectVO vo = mockProjectVO();
		vo.setHasSetDefaultDate(Boolean.TRUE);
		Project project = mockProject();
		
		when(projectRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(project));
		when(projectRepo.save(project)).thenReturn(project);
		
		Users existingAllocatedUser = mockUsers();
		when(userRepo.findUserByProjectId(Mockito.anyInt())).thenReturn(existingAllocatedUser);
		userRepo.deleteById(Mockito.anyInt());
		userRepo.save(existingAllocatedUser);
		
		target.saveOrUpdateProject(vo); 
	}
	
	
	@Test
	public void testSaveOrUpdateProject_Invalid_ProjectId() {
		ProjectVO vo = mockProjectVO();
		vo.setProjectId(1);
		vo.setHasSetDefaultDate(Boolean.FALSE);
		Project project = mockProject();
		
		when(projectRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(project));
		when(projectRepo.save(project)).thenReturn(project);
		
		Users existingAllocatedUser = mockUsers();
		when(userRepo.findUserByProjectId(Mockito.anyInt())).thenReturn(existingAllocatedUser);
		userRepo.deleteById(Mockito.anyInt());
		userRepo.save(existingAllocatedUser);
		
		target.saveOrUpdateProject(vo); 
	}

	@Test
	public void testSuspendProject() {
		ProjectVO vo = mockProjectVO();
		Project project = mockProject(); 
		when(projectRepo.findById(vo.getProjectId())).thenReturn(Optional.of(project));
		project.setStatus("Completed");
		projectRepo.save(project);
		target.suspendProject(vo);
	}
	
	@Test
	public void testGetAllParentTasks() {
		List<ParentTaskVO> parentTaskVOList = new ArrayList<ParentTaskVO>();
		ParentTask parenTask =  mockParentTask();
		List<ParentTask> parentTasks = new ArrayList<ParentTask>();
		parentTasks.add(parenTask);
		when(parentRepo.findAll()).thenReturn(parentTasks);
		target.getAllParentTasks();
	}
	@Test
	public void getParentTasks() {
		ParentTask parenTask = mockParentTask();
		when(parentRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(parenTask));
		target.getParentTasks(Mockito.anyInt()) ;
	}

	@Test
	public void testGetAllTasks() {

		List<Task> tasksList = new ArrayList<Task>();
		tasksList.add(mockTask());
		when(repo.findAll()).thenReturn(tasksList);
		target.getAllTasks();
	}
	
	@Test
	public void testGetTasksByProject() {
		List<Task> tasksList = new ArrayList<Task>();
		tasksList.add(mockTask());
		when(repo.getTasksByProjectId(Mockito.anyInt())).thenReturn(tasksList);
		
		List<Users> usersList = new ArrayList<Users>();
		usersList.add(mockUsers());
		when(userRepo.getUsersByTask(tasksList.stream().map(task -> task.getTaskId()).collect(Collectors.toList()))).thenReturn(usersList);
		target.getTasksByProject(1);
	}

	@Test
	public void saveTask() {
		Project project = mockProject();
		
		TaskVO taskVO = mockTaskVO();
		taskVO.setParentTaskInd(Boolean.FALSE);
		
		Task task = mockTask();
		task.setStatus("A");
		task.setTask(taskVO.getTaskName());
		when(projectRepo.findById(taskVO.getProjectId())).thenReturn(Optional.of(project));
		task.setProject(project);
		when(repo.save(task)).thenReturn(task);
		
		ParentTask parentTask = mockParentTask();
		when(parentRepo.save(mockParentTask())).thenReturn(parentTask);
		
		
		when(parentRepo.findById(taskVO.getParentTaskId())).thenReturn(Optional.of(parentTask));

		Users projectUser = mockUsers();
		when(userRepo.findById(taskVO.getUserId())).thenReturn(Optional.ofNullable(projectUser));
		
		userRepo.save(projectUser);

		//target.saveTask(taskVO);

	}

	
	@Test
	public void testGetTask() {
		Task task = mockTask();
		when(repo.findById(Mockito.anyInt())).thenReturn(Optional.of(task));
		target.getTask("1");
	}

	@Test
	public void testUpdateTask() {
		TaskVO taskVO = mockTaskVO();
		Task task = mockTask(); 
		when(repo.findById(taskVO.getTaskId())).thenReturn(Optional.of(task));
		 
		Project project = mockProject();
		task.setProject(project);
		when(projectRepo.findById(taskVO.getProjectId())).thenReturn(Optional.of(project));
		
		
		Users user = mockUsers();
		when(userRepo.findUserByEmployeeId(taskVO.getEmployeeId())).thenReturn(user);

		when(userRepo.getUserByTaskId(taskVO.getTaskId())).thenReturn(user);
		
		userRepo.deleteById(user.getUserId());
		 
		userRepo.save(user);
		
		ParentTask parentTask = mockParentTask();
		when(parentRepo.findById(taskVO.getParentTaskId())).thenReturn(Optional.of(parentTask));
		
		target.getParentTasks(taskVO.getParentTaskId());
		task.setParentTask(parentTask);
		
		repo.save(task);
		target.updateTask(taskVO);
	}
	
	private UserVO mockUserVO() {
		UserVO userVO = new UserVO();
		userVO.setEmployeeId("1234");
		userVO.setFirstName("Rabhindher");
		userVO.setLastName("Nath");
		userVO.setTask(mockTaskVO());
		userVO.setUserId(1);
		userVO.setStatus("Active");
		userVO.setProject(mockProjectVO());
		return userVO;
	}
	
	private List<Users> mockUsersList() {
		List<Users> usersList = new ArrayList<Users>();
		usersList.add(mockUsers());
		return usersList;
	}
	private Users mockUsers() {
		Users users = new Users();
		users.setEmployeeId("1234");
		users.setFirstName("Rabhindher");
		users.setLastName("Nath");
		users.setTask(mockTaskSet());
		users.setUserId(1);
		users.setTaskId(1);
		users.setStatus("Active");
		users.setProject(mockProjectSet());
		return users;
	}
	
	private Set<Task> mockTaskSet() {
		Set<Task> taskSet = new HashSet<Task>();
		taskSet.add(mockTask());
		return taskSet;
	}

	private Set<Project> mockProjectSet() {
		Set<Project> prjSet = new HashSet<Project>();
		prjSet.add(mockProject());
		return  prjSet;
	}
	private  Project mockProject() {
		Project project = new Project();
		project.setProjectId(1);
		project.setProject("Project Management");
		project.setStatus("Open");
		project.setPriority("10");
		project.setEndDate(new Date());
		project.setStartDate(new Date());
		return project ;
	}
	private List<UserVO> mockUserVOList() {
		List<UserVO> userVOList = new ArrayList<UserVO>();
		userVOList.add(mockUserVO());
		return userVOList;
	}

	private List<ProjectVO> mockProjectVOList() {
		List<ProjectVO> projectVOList = new ArrayList<ProjectVO>();
		projectVOList.add(mockProjectVO());
		return projectVOList;
	}
	
	private ProjectVO mockProjectVO() {
		ProjectVO projectVO = new ProjectVO();
		projectVO.setEmployeeId("1234");
		projectVO.setEmployeeName("Alen Drone");
		projectVO.setFirstName("Alen");
		projectVO.setLastName("Drane");
		projectVO.setProject("Historical Theory");
		projectVO.setProjectId(1);
		projectVO.setPriority("1");
		projectVO.setStatus("Open");
		return projectVO;
	}

	private TaskVO mockTaskVO() {
		TaskVO taskVO = new TaskVO();
		taskVO.setTaskId(1);
		taskVO.setFirstName("Osho");
		taskVO.setLastName("The Philosopher");
		taskVO.setParentTaskId(1);
		taskVO.setParentTaskName("Parent Task");
		taskVO.setParentTaskInd(Boolean.FALSE);
		return taskVO;
	}
	
	private Task mockTask() {
		Task task = new Task();
		task.setTaskId(1);
		task.setTask("Coding");
		task.setParentTask(mockParentTask());
		task.setPriority("10");
		task.setProject(mockProject());
		task.setStatus("A");
		
		return task;
	}
	
	private ParentTask mockParentTask() {
		ParentTask parenTask = new ParentTask();
		parenTask.setParentId(1);
		parenTask.setParentTask("Designing");
		return parenTask;
	}

	private List<ParentTaskVO> mockParentTaskVOList() {
		List<ParentTaskVO> parentTaskList = new ArrayList<ParentTaskVO>();
		ParentTaskVO parentTaskVO = new ParentTaskVO();
		parentTaskVO.setParentId(1);
		parentTaskVO.setParentTask("Project Validation");
		parentTaskList.add(parentTaskVO);
		return parentTaskList;
	}
	private List<TaskVO> mockTaskVOList() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		List<TaskVO> taskVOList= new ArrayList<TaskVO>();
		TaskVO taskVO = new TaskVO();
		taskVO.setTaskId(1);
		taskVO.setFirstName("Osho");
		taskVO.setLastName("The Philosopher");
		taskVO.setStartDate(dateFormat.format(new Date()));
		taskVO.setEndDate(dateFormat.format(new Date()));
		taskVOList.add(taskVO);
		return taskVOList;
	}
	
	
}
