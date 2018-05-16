package com.spring.rolebase.security.api.util;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.spring.rolebase.security.api.common.UserRequest;
import com.spring.rolebase.security.api.model.User;

@Component
public class NotificationUtil {

	@Autowired
	private JavaMailSender sender;
	@Autowired
	private TemplateEngine templateEngine;

	public String sendEmail(String to, String cc, UserRequest<User> userRequest) throws Exception {
		String templateName = "email/addModeratorTemplate";
		Context context = new Context();
		context.setVariable("user", userRequest.getRequest());
		context.setVariable("name", userRequest.getName());
		String body = templateEngine.process(templateName, context);
		MimeMessage mail = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		helper.setTo(to);
		helper.setCc(cc);
		helper.setSubject("JobPortal Access");
		helper.setText(body, true);
		helper.setFrom("globalinfo.bh@gmail.com");
		sender.send(mail);
		return "mail send successfully to : (" + to + ")";
	}

}
