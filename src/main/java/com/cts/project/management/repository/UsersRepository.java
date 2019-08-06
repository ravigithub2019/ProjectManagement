package com.cts.project.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.project.management.model.Users;
import com.cts.project.management.vo.ManagerVO;

/**
 * @author Admin
 *
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
	@Query("select u from Users u where u.employeeId=:empId and u.projectId=0 and u.taskId=0")
	Users findUserByEmployeeId(@Param("empId") String empId);

	@Query("select  new com.cts.project.management.vo.ManagerVO(u.employeeId, u.firstName,u.lastName) from Users u")
	List<ManagerVO> findAllManagers();

	@Query("select users from Users users where users.projectId=0 and users.taskId=0")
	List<Users> findUsers();

	@Query("select users from Users users where users.projectId=:projectId and users.taskId =0")
	Users findUserByProjectId(@Param("projectId") int projectId);

	@Query("select users from Users users where users.projectId=:projectId and users.userId=:userId")
	void deleteUserByProjectAndUserId(@Param("projectId") int projectId, @Param("userId") int userId);

	@Query("select users from Users users where users.taskId in (:taskIds)")
	List<Users> getUsersByTask(@Param("taskIds") List<Integer> taskIds);

	@Query("select users from Users users where users.taskId = :taskIds")
	Users getUserByTaskId(@Param("taskIds") Integer taskIds);

	@Query("select users from Users users where users.userId = :userId")
	Users findUsersById(@Param("userId") int userId);

	@Query("select users from Users users where users.firstName = :firstName and "
			+ "users.lastName = :lastName and users.employeeId = :employeeId")
	List<Users> findUsersByNameandEmpId(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("employeeId") String employeeId);

}
