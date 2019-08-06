/**
 * 
 */
package com.cts.project.management.vo;

import java.util.Date;

/**
 * @author Admin
 *
 */
public class ProjectVO {

	/**
	 * 
	 */
	private int projectId;
	private String project;
	private Date startDate;
	private Date endDate;
	private String priority;
	private String status;
	private String employeeId;
	private int noOfTask;
	private ManagerVO managerVO;
	private String employeeName;
	
	private String firstName;
	private String lastName;
	private boolean hasSetDefaultDate;
	
	public boolean isHasSetDefaultDate() {
		return hasSetDefaultDate;
	}

	public void setHasSetDefaultDate(boolean hasSetDefaultDate) {
		this.hasSetDefaultDate = hasSetDefaultDate;
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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the managerVO
	 */
	public ManagerVO getManagerVO() {
		return managerVO;
	}

	/**
	 * @param managerVO the managerVO to set
	 */
	public void setManagerVO(ManagerVO managerVO) {
		this.managerVO = managerVO;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public int getNoOfTask() {
		return noOfTask;
	}

	public void setNoOfTask(int noOfTask) {
		this.noOfTask = noOfTask;
	}

}
