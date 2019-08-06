package com.cts.project.management.vo;

public class UserVO {
	private int userId;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String status;
	private ProjectVO project;
	private TaskVO task;
	
	
	 
	public UserVO(int userId, String employeeId, String firstName, String lastName, String status) {
		super();
		this.userId = userId;
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = status;
	}
	/**
	 * 
	 */
	public UserVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param employeeId
	 * @param firstName
	 * @param lastName
	 */
	public UserVO(String employeeId, String firstName, String lastName) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ProjectVO getProject() {
		return project;
	}
	public void setProject(ProjectVO project) {
		this.project = project;
	}
	public TaskVO getTask() {
		return task;
	}
	public void setTask(TaskVO task) {
		this.task = task;
	}
	 
	
	
}
