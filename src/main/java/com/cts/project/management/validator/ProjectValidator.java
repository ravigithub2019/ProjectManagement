package com.cts.project.management.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cts.project.management.vo.ProjectVO;


@Component
public class ProjectValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return ProjectVO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}

}
