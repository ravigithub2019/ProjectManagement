package com.cts.project.management.model;

public class ProjectManagementVO {

	Users user;
	Project project;
	Task task;
	ParentTask parenTask;
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public ParentTask getParenTask() {
		return parenTask;
	}
	public void setParenTask(ParentTask parenTask) {
		this.parenTask = parenTask;
	}
	
	
	
}
