package com.spring.rolebase.security.api.service;

import java.util.List;

import com.spring.rolebase.security.api.dto.JobForm;
import com.spring.rolebase.security.api.model.JobDetails;
import com.spring.rolebase.security.api.model.User;

public interface UserService {
	public User findUserByEmail(String email);

	public void saveUser(User user);

	public void saveJob(JobForm form, int id);

	public List<JobDetails> getJobsByPostUser(int id);

	public String deleteJob(int id);

	public String updateJob(JobForm form);

	public JobDetails getJobById(int id);

	public String addModerator(User user, String loggedUser,int adminId);
}
