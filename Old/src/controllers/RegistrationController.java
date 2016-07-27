package controllers;

import pojos.Error;
import pojos.Success;
import pojos.User;
import pojos.UserLogin;
import security.Encrypt;
import services.UserService;
import services.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import json.JsonConverter;
import mappers.UserMapper;
import password.GmailSender;
import password.PasswordGenerator;

@RestController
public class RegistrationController {
	
	@Autowired
	UserMapper usermapper;
	JsonConverter jconverter = new JsonConverter();
	Encrypt encrypter = new Encrypt();
	UserService userservice = new UserServiceImpl();
	@Autowired
	GmailSender emailSender;

	@RequestMapping(value = "/registration", method = RequestMethod.POST, headers = "Accept=application/json")
	public String handleRegistration(@RequestBody User jsonUser, HttpSession session) {
		//User jsonUser = jconverter.convertJsonToUserObject(jsonUserString);
		UserLogin dbUser = usermapper.getUserLoginByEmail(jsonUser.getEmail());
		if (jsonUser.getEmail() != null && jsonUser.getEmail() != "") {
			if (dbUser == null) {
				if (userservice.validateUser(jsonUser)) {
					String pswd = PasswordGenerator.GeneratePassword();
					String encrypted = encrypter.encryptPassword(pswd);
					String uniqueID = UUID.randomUUID().toString();
					try {
						emailSender.sendPassword(jsonUser.getEmail(), pswd);
					} catch (MessagingException | IOException e) {
						e.printStackTrace();
						return jconverter.convertObjectToJsonString(new Error("registration", "unable to send email"));
					}
					jsonUser.setUserPassword(encrypted);
					jsonUser.setUserID(uniqueID);
					jsonUser.setStateOfApplication(0);
					jsonUser.setAddress("");
					jsonUser.setPhone("");
					usermapper.insertUser(jsonUser);
					return jconverter
							.convertObjectToJsonString(new Success("registration", "Successful registration."));
				}
				return jconverter.convertObjectToJsonString(new Error("registration", "Registration form is invalid."));
			}
			return jconverter
					.convertObjectToJsonString(new Error("registration", "The given email is already registrated."));
		}
		return jconverter.convertObjectToJsonString(new Error("registration", "Wrong HTTP request format."));
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String handleLogout(HttpSession session) {
		if (session.getAttribute("user") != null) {
			session.invalidate();
			return jconverter.convertObjectToJsonString(new Success("logout", "You have been successfully logged out."));
		}
		return jconverter.convertObjectToJsonString(new Error("logout", "You are already logged out."));
	}
}
