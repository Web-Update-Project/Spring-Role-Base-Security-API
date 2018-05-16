package com.spring.rolebase.security.api.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.rolebase.security.api.dto.JobForm;
import com.spring.rolebase.security.api.model.JobDetails;
import com.spring.rolebase.security.api.model.Role;
import com.spring.rolebase.security.api.model.User;
import com.spring.rolebase.security.api.repository.JobPortalRepository;
import com.spring.rolebase.security.api.repository.RoleRepository;
import com.spring.rolebase.security.api.repository.UserRepository;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private JobPortalRepository jobRepository;

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepository.findByRole("USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		user.setAddedBy(0);
		userRepository.save(user);
	}

	@Override
	public void saveJob(JobForm form, int id) {
		JobDetails jobDetails = new JobDetails();
		jobDetails.setJobID(form.getJobID());
		jobDetails.setTeamSize(form.getTeamSize());
		jobDetails.setCompany(form.getCompany());
		jobDetails.setClient(form.getClient());
		jobDetails.setDescription(form.getDescription());
		jobDetails.setDesignation(form.getDesignation());
		jobDetails.setLocation(form.getLocation());
		jobDetails.setProjectIG(form.getProjectIG());
		jobDetails.setProjectName(form.getProjectName());
		jobDetails.setTechnology(form.getTechnology());
		jobDetails.setPostDate(new java.sql.Date(new Date().getTime()));
		jobDetails.setPostedBy(id);
		System.out.println(jobDetails);
		jobRepository.saveNewJob(jobDetails);

	}

	@Override
	public List<JobDetails> getJobsByPostUser(int id) {
		List<JobDetails> jobDetails = jobRepository.getJobs();
		if (id == 0) {
			return jobDetails;
		} else {
			return jobDetails.stream().filter(job -> job.getPostedBy() == id).sorted(new Comparator<JobDetails>() {
				@Override
				public int compare(JobDetails o1, JobDetails o2) {
					return o2.getPostDate().compareTo(o1.getPostDate());
				}
			}).collect(Collectors.toList());
		}
	}

	@Override
	public String deleteJob(int id) {
		return jobRepository.deleteJob(id);
	}

	@Override
	public String updateJob(JobForm form) {
		return jobRepository.updateJob(form);
	}

	public JobDetails getJobById(int id) {
		System.out.println(jobRepository.getJobs());
		return jobRepository.getJobs().stream().filter(job -> job.getJobID() == id).collect(Collectors.toList()).get(0);
	}

	@Override
	public String addModerator(User user, String loggedUser, int adminId) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepository.findByRole("ADMIN");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		user.setAddedBy(adminId);
		userRepository.save(user);
		return user.getName() + " added successfully as Moderator";
	}
}
