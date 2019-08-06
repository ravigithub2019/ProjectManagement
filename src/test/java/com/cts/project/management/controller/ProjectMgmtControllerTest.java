package com.cts.project.management.controller;

import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.project.management.controller.ProjectMgmtController;
import com.cts.project.management.repository.TaskRepository;
import com.cts.project.management.service.ProjectManagementService;
import com.cts.project.management.validator.ProjectValidator;
import com.cts.project.management.validator.UserValidator;
import com.cts.project.management.vo.ParentTaskVO;
import com.cts.project.management.vo.ProjectVO;
import com.cts.project.management.vo.TaskVO;
import com.cts.project.management.vo.UserVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectMgmtControllerTest {

	@InjectMocks
	ProjectMgmtController target;
	
	@Mock
	private TaskRepository taskRepository;
	
	@Mock
	private ProjectValidator projectValidator;
	
	@Autowired
	private UserValidator userValidator; 
	
	@Mock
	WebDataBinder binder;
	
	@Mock
	BindingResult result;
	
	@Mock
	private ProjectManagementService service;
	
	private MockMvc mockMvc;

	@Before
	public void init() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(target).build();
	}
	
	@Test
	public void testInitBinder() {
		binder.setValidator(projectValidator);
		target.InitProjectBinder(binder);
	}
	
	@Test
	public void testUserInitBinder() {
		binder.setValidator(userValidator);
		target.InitUserBinder(binder);
	}
	
	@Test
	public void testInfo() {
		String testhome = target.info();
		Assert.assertEquals(testhome, "Welcome to Project Management");
		
	}
	
	@Test
	public void getParentTasks() {
		List<ParentTaskVO> parentTaskList = this.mockParentTaskVOList();
		when(service.getAllParentTasks()).thenReturn(parentTaskList);
		target.getParentTasks();
		Assert.assertEquals(1, parentTaskList.get(0).getParentId());
		Assert.assertEquals("Project Validation", parentTaskList.get(0).getParentTask());
	}
	
	@Test
	public void testGetTasks() {
		List<TaskVO> taskVOList = this.mockTaskVOList();
		when(service.getAllTasks()).thenReturn(taskVOList);
		target.getTasks();
		Assert.assertEquals("Osho", taskVOList.get(0).getFirstName());
		Assert.assertEquals("The Philosopher", taskVOList.get(0).getLastName());
	}
	

	@Test
	public void testGetTaskByProjectId() {
		List<TaskVO> taskVOList = this.mockTaskVOList();
		when(service.getTasksByProject(1)).thenReturn(taskVOList);
		target.getTasksByProject(1);
		Assert.assertEquals("Osho", taskVOList.get(0).getFirstName());
		Assert.assertEquals("The Philosopher", taskVOList.get(0).getLastName());
	}
	
	@Test
	public void testGetTaskById() {
		TaskVO taskVO = this.mockTaskVO();
		when(service.getTask("1")).thenReturn(taskVO);
		target.getTasksById("1");
		Assert.assertEquals("Osho", taskVO.getFirstName());
		Assert.assertEquals("The Philosopher", taskVO.getLastName());
	}
	
	@Test
	public void testSaveTask() {
		TaskVO taskVO = this.mockTaskVO();
		service.saveTask(taskVO);
		target.saveTask(taskVO);
		Assert.assertEquals("Osho", taskVO.getFirstName());
		Assert.assertEquals("The Philosopher", taskVO.getLastName());
	}
	
	@Test
	public void testSaveTask_Exception() {
		TaskVO taskVO = this.mockTaskVO();
		service.saveTask(taskVO);
		target.saveTask(taskVO);
		Assert.assertEquals("Osho", taskVO.getFirstName());
		Assert.assertEquals("The Philosopher", taskVO.getLastName());
	}
	
	
	@Test
	public void testUpdateTask() {
		TaskVO taskVO = this.mockTaskVO();
		when(service.getTask("1")).thenReturn(taskVO);
		service.updateTask(taskVO);
		target.updateTask(taskVO);
		Assert.assertEquals("Osho", taskVO.getFirstName());
		Assert.assertEquals("The Philosopher", taskVO.getLastName());
	}
	
	@Test
	public void testUpdateTask_Exception() {
		TaskVO taskVO = this.mockTaskVO();
		when(service.getTask("1")).thenReturn(null);
		service.updateTask(taskVO);
		target.updateTask(taskVO);
		Assert.assertEquals("Osho", taskVO.getFirstName());
		Assert.assertEquals("The Philosopher", taskVO.getLastName());
	}
	
	@Test
	public void testDeleteTask() {
		TaskVO taskVO = this.mockTaskVO();
		when(service.getTask("1")).thenReturn(taskVO);
		taskVO.setStatus("I");
		service.updateTask(taskVO);
		target.deleteTask("1");
		Assert.assertEquals("Osho", taskVO.getFirstName());
		Assert.assertEquals("The Philosopher", taskVO.getLastName());
	}
	
	
	@Test
	public void testGetAllUsers() {
		List<UserVO> userVOList = this.mockUserVOList();
		when(service.getAllUsers()).thenReturn(userVOList);
		target.getAllUsers();
		Assert.assertEquals("Rabhindher", userVOList.get(0).getFirstName());
		Assert.assertEquals("Nath", userVOList.get(0).getLastName());
	}
	
	@Test
	public void testGetAllEmployees() {
		List<UserVO> userVOList = this.mockUserVOList();
		when(service.getDistinctUser()).thenReturn(userVOList);
		target.getAllEmployees();
		Assert.assertEquals("Rabhindher", userVOList.get(0).getFirstName());
		Assert.assertEquals("Nath", userVOList.get(0).getLastName());
	}
	
	@Test
	public void testAddUser() {
		UserVO userVO = this.mockUserVO();
		service.addUser(userVO);
		target.addUser(userVO,result);
		Assert.assertEquals("Rabhindher", userVO.getFirstName());
		Assert.assertEquals("Nath", userVO.getLastName());
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		when(service.deleteUser(1)).thenReturn(true);
		target.deleteUser(1);
	}
	
	@Test
	public void testDeleteUser_Exception() throws Exception {
		when(service.deleteUser(1)).thenReturn(false);
		target.deleteUser(1);
	}
	
	@Test
	public void testGetAllProjects() throws Exception {
		List<ProjectVO> projectVOList = mockProjectVOList();
		when(service.getAllProjects()).thenReturn(projectVOList);
		target.getAllProjects();
	}
	
	@Test
	public void testSaveOrUpdateProject() throws Exception {
		ProjectVO projectVO = mockProject();
		service.saveOrUpdateProject(projectVO);
		target.saveOrUpdateProject(projectVO, result);
	}
	
	@Test
	public void testSuspendProject() throws Exception {
		ProjectVO projectVO = mockProject();
		projectVO.setStatus("Completed");
		service.suspendProject(projectVO);
		target.saveOrUpdateProject(projectVO, result);
	}
	
	private UserVO mockUserVO() {
		UserVO userVO = new UserVO();
		userVO.setEmployeeId("1234");
		userVO.setFirstName("Rabhindher");
		userVO.setLastName("Nath");
		userVO.setTask(mockTaskVO());
		userVO.setUserId(1);
		userVO.setStatus("Active");
		userVO.setProject(mockProject());
		return userVO;
	}
	
	private List<UserVO> mockUserVOList() {
		List<UserVO> userVOList = new ArrayList<UserVO>();
		userVOList.add(mockUserVO());
		return userVOList;
	}

	private List<ProjectVO> mockProjectVOList() {
		List<ProjectVO> projectVOList = new ArrayList<ProjectVO>();
		projectVOList.add(mockProject());
		return projectVOList;
	}
	
	private ProjectVO mockProject() {
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
		return taskVO;
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
