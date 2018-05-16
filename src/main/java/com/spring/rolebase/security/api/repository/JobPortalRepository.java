package com.spring.rolebase.security.api.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.rolebase.security.api.dto.JobForm;
import com.spring.rolebase.security.api.model.AccessDelegate;
import com.spring.rolebase.security.api.model.JobDetails;

@Repository
public class JobPortalRepository {
	@Autowired
	private SessionFactory factory;

	private Session getSession() {
		Session session = factory.getCurrentSession();
		if (session == null) {
			session = factory.openSession();
		}
		return session;
	}

	public void saveNewJob(JobDetails jobDetails) {
		getSession().save(jobDetails);
	}

	@SuppressWarnings("unchecked")
	public List<JobDetails> getJobs() {
		return getSession().createCriteria(JobDetails.class).list();
	}

	public String deleteJob(int id) {
		String message = "";
		JobDetails job = getSession().get(JobDetails.class, id);
		if (job != null) {
			getSession().delete(job);
			message = "Post removed successfully..";
		} else {
			message = "Invalid Access ! Please contact to Adminstrator..";
		}
		return message;
	}

	public String updateJob(JobForm form) {
		String message = "";
		JobDetails jobDetails = getSession().get(JobDetails.class, form.getJobID());
		if (jobDetails != null) {
			jobDetails.setTeamSize(form.getTeamSize());
			jobDetails.setCompany(form.getCompany());
			jobDetails.setClient(form.getClient());
			jobDetails.setDescription(form.getDescription());
			jobDetails.setDesignation(form.getDesignation());
			jobDetails.setLocation(form.getLocation());
			jobDetails.setProjectIG(form.getProjectIG());
			jobDetails.setProjectName(form.getProjectName());
			jobDetails.setTechnology(form.getTechnology());
			getSession().update(jobDetails);
			message = "Post Updated successfully..";
		} else {
			message = "Invalid Access ! Please contact to Adminstrator..";
		}
		return message;
	}

	public void addModerator(AccessDelegate delegate) {
		delegate.setAddedDate(new SimpleDateFormat("dd/mm/YYYY").format(new Date()));
		getSession().save(delegate);
	}
}
