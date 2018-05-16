package com.spring.rolebase.security.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROJECT_SUMMARY")
public class JobDetails {
	@Id
	private int jobID;
	private String technology;
	private String location;
	private String company;
	private String designation;
	private String projectName;
	private String client;
	private String projectIG;
	private String teamSize;
	private String description;
	private Date postDate;
	@Column(name = "POSTED_BY")
	private int postedBy;

	public int getJobID() {
		return jobID;
	}

	public void setJobID(int jobID) {
		this.jobID = jobID;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getProjectIG() {
		return projectIG;
	}

	public void setProjectIG(String projectIG) {
		this.projectIG = projectIG;
	}

	public String getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(String teamSize) {
		this.teamSize = teamSize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public int getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(int postedBy) {
		this.postedBy = postedBy;
	}

	@Override
	public String toString() {
		return "JobDetails [jobID=" + jobID + ", technology=" + technology
				+ ", location=" + location + ", company=" + company
				+ ", designation=" + designation + ", projectName="
				+ projectName + ", client=" + client + ", projectIG="
				+ projectIG + ", teamSize=" + teamSize + ", description="
				+ description + ", postDate=" + postDate + ", postedBy="
				+ postedBy + "]";
	}

}
