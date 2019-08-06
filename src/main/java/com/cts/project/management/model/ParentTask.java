/**
 * 
 */
package com.cts.project.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author 444158
 *
 */
@Entity(name = "parent_task")
public class ParentTask {
	@Id
	@Column(name="parent_id", nullable=false)
	private int parentId;
	
	@Column(name="parentTask")
	private String parentTask;

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getParentTask() {
		return parentTask;
	}

	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}
	
	
}
