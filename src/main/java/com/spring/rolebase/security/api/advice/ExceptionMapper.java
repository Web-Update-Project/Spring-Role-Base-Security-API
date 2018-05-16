package com.spring.rolebase.security.api.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.spring.rolebase.security.api.exception.UnautorizedAccessException;

@ControllerAdvice
public class ExceptionMapper {

	@ExceptionHandler(UnautorizedAccessException.class)
	public ModelAndView handelException(UnautorizedAccessException exception) {
		ModelAndView mav = new ModelAndView("serviceError");
		return mav.addObject("errorMessage", exception.getMessage());
	}

}
