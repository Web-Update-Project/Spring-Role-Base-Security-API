package com.spring.rolebase.security.api.controller;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.rolebase.security.api.common.UserRequest;
import com.spring.rolebase.security.api.dto.JobForm;
import com.spring.rolebase.security.api.exception.UnautorizedAccessException;
import com.spring.rolebase.security.api.model.JobDetails;
import com.spring.rolebase.security.api.model.User;
import com.spring.rolebase.security.api.service.UserService;
import com.spring.rolebase.security.api.util.NotificationUtil;
import com.spring.rolebase.security.api.util.VerifyRecaptchaUtils;

@Controller
public class PortalController {

	@Autowired
	private UserService userService;
	protected static String LOGGED_USER_ROLE = "";
	protected static int LOGGED_ID = 0;
	protected static User LOGGEDUSER = null;
	@Autowired
	private NotificationUtil notification;

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("login");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully login now");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("login");

		}
		return modelAndView;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		String accessMessage = "";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		LOGGEDUSER = user;
		modelAndView.addObject("userName",
				"Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("email", user.getEmail());
		String userRole = user.getRoles().stream().collect(Collectors.toList()).get(0).getRole();
		LOGGED_USER_ROLE = userRole;
		LOGGED_ID = user.getId();
		if (userRole.equalsIgnoreCase("ADMIN")) {
			if (LOGGEDUSER.getAddedBy() != 0) {
				modelAndView.addObject("jobs", getJobPostedByLoggedUser(0));
			} else {
				modelAndView.addObject("jobs", getJobPostedByLoggedUser(LOGGED_ID));
			}
			accessMessage = "Content Available Only for Users with Admin/Moderator Role";
			modelAndView.addObject("accessMessage", accessMessage);
			modelAndView.setViewName("admin/home");
		}

		if (userRole.equalsIgnoreCase("USER")) {
			accessMessage = "Content Available Only for Users with User Role";
			modelAndView.addObject("accessMessage", accessMessage);
			modelAndView.addObject("jobs", getJobPostedByLoggedUser(0));
			modelAndView.setViewName("user/home");
		}

		return modelAndView;
	}

	@RequestMapping(value = "/admin/addJobView")
	public String addJob(Model model) {
		User user = userService.findUserByEmail(LOGGEDUSER.getEmail());
		model.addAttribute("user", user);
		model.addAttribute("jobID", new Random().nextInt(3653956));
		return "admin/jobUpload";
	}

	@RequestMapping(value = "/admin/postJob")
	public String postJob(Model model, @ModelAttribute("form") JobForm form) {
		userService.saveJob(form, LOGGED_ID);
		model.addAttribute("userName", "Welcome " + LOGGEDUSER.getName() + " " + LOGGEDUSER.getLastName() + " ("
				+ LOGGEDUSER.getEmail() + ")");
		model.addAttribute("message", "Job Uploaded Successfully");
		model.addAttribute("jobs", getJobPostedByLoggedUser(LOGGED_ID));
		return "admin/home";
	}

	@RequestMapping("/admin/deletePost")
	public String removeJob(Model model, @RequestParam("id") int id) throws UnautorizedAccessException {
		String deleteMessage = "";
		if (LOGGEDUSER.getAddedBy() == 0) {
			deleteMessage = userService.deleteJob(id);
			model.addAttribute("message", deleteMessage);
			model.addAttribute("jobs", getJobPostedByLoggedUser(LOGGED_ID));
		} else {
			throw new UnautorizedAccessException("Hi," + LOGGEDUSER.getName()
					+ " Sorry to inform you that you don't have access to Delete any data in portal,Please contact to Admistration ");
		}
		return "admin/home";

	}

	@RequestMapping("/admin/updatePostView")
	public String updateJobView(Model model, @RequestParam("id") int id) throws UnautorizedAccessException {
		if (LOGGEDUSER.getAddedBy() == 0) {
			JobDetails jobDetails = userService.getJobById(id);
			model.addAttribute("job", jobDetails);
		} else {
			throw new UnautorizedAccessException("Hi," + LOGGEDUSER.getName()
					+ " Sorry to inform you that you don't have access to Update any data in portal,Please contact to Admistration ");
		}
		return "admin/updateJobUpload";
	}

	@RequestMapping("/admin/updatePost")
	public String updateJob(Model model, @ModelAttribute("form") JobForm form) {
		String updateMessage = "";
		updateMessage = userService.updateJob(form);
		model.addAttribute("message", updateMessage);
		model.addAttribute("jobs", getJobPostedByLoggedUser(LOGGED_ID));
		return "admin/home";
	}

	@RequestMapping("/admin/addModerator")
	public String AddModerator(Model model, @ModelAttribute("user") User user, HttpServletRequest request) {
		UserRequest<User> addUser = new UserRequest<User>();
		addUser.setName(LOGGEDUSER.getName());
		addUser.setUserRole(LOGGED_USER_ROLE);
		addUser.setRequest(user);
		String message = "";
		boolean verify;
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		System.out.println(gRecaptchaResponse);
		try {
			verify = VerifyRecaptchaUtils.verify(gRecaptchaResponse);
			if (verify) {
				message = userService.addModerator(user, LOGGEDUSER.getName(), LOGGED_ID);
				notification.sendEmail(user.getEmail(), LOGGEDUSER.getEmail(), addUser);
			} else {
				message = "Please retry again";
			}
		} catch (Exception e) {
		}

		model.addAttribute("message", message);
		model.addAttribute("jobs", getJobPostedByLoggedUser(LOGGED_ID));
		return "admin/home";
	}

	private List<JobDetails> getJobPostedByLoggedUser(int id) {
		List<JobDetails> jobDetails = userService.getJobsByPostUser(id);
		return jobDetails;

	}
}
