package controllers;

import pojos.Error;
import pojos.Success;
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

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import json.JsonConverter;
import mappers.UserMapper;
import password.GmailSender;
import password.PasswordGenerator;

@RestController
public class LoginController {

	@Autowired
	UserMapper usermapper;
	JsonConverter jconverter = new JsonConverter();
	Encrypt encrypter = new Encrypt();
	UserService userservice = new UserServiceImpl();
	@Autowired
	GmailSender emailSender;

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	public String handleLogin(@RequestBody UserLogin jsonUser, HttpSession session) {
		// Bind the incoming json to class.
		//UserLogin jsonUser = jconverter.convertJsonToUserLoginObject(jsonUserString);
		
		if (jsonUser != null) {
			// Get the user with the email that came from json from db.
			UserLogin dbUser = usermapper.getUserLoginByEmail(jsonUser.getEmail());
			
			if (session.getAttribute("user") != null) {
				return jconverter.convertObjectToJsonString(new Error("login", "You are already logged in."));
			}

			if (dbUser != null) {
				// Compare the two password.
				String rawPassword = jsonUser.getPassword();
				String encryptedPassword = dbUser.getPassword();

				if (encrypter.compareEncryptedAndRaw(rawPassword, encryptedPassword)) {
					session.setAttribute("user", dbUser.getEmail());
					session.setMaxInactiveInterval(45);
					usermapper.updateLastLogin(dbUser);
					return jconverter.convertObjectToJsonString(new Success("login", dbUser.getEmail() + " logged in successfully"));
				}
				return jconverter.convertObjectToJsonString(new Error("login", "Wrong email and/or password."));
			}
			return jconverter.convertObjectToJsonString(new Error("login", "Wrong email and/or password."));
		}
		return jconverter.convertObjectToJsonString(new Error("login", "Wrong HTTP request format."));
	}

	@RequestMapping(value = "/forgottenPassword", method = RequestMethod.POST, headers = "Accept=application/json")
	public String handleForgottenPassword(@RequestBody UserLogin jsonUser) {
		//UserLogin jsonUser = jconverter.convertJsonToUserLoginObject(emailJson);

		if (jsonUser != null) {
			UserLogin dbUser = usermapper.getUserLoginByEmail(jsonUser.getEmail());
			if (dbUser != null) {
				String pswd = PasswordGenerator.GeneratePassword();
				String encrypted = encrypter.encryptPassword(pswd);
				dbUser.setPassword(encrypted);
				try {
					usermapper.updatePassword(dbUser);
					emailSender.sendPassword(dbUser.getEmail(), pswd);
					return jconverter.convertObjectToJsonString(new Success("forgottenPassword", "Message sent to: " + dbUser.getEmail()));
				} catch (MessagingException | IOException e) {
					e.printStackTrace();
					return jconverter.convertObjectToJsonString(new Error("forgottenPassword", "unable to send email"));
				}
			}
			return jconverter.convertObjectToJsonString(new Error("forgottenPassword", "No user with this email."));
		}
		return jconverter.convertObjectToJsonString(new Error("forgottenPassword", "Wrong HTTP request format."));
	}
	
	@RequestMapping(value = "/keepalive", method = RequestMethod.POST)
	public String dummyFunction(HttpSession session) {
		if (session.getAttribute("user") == null)
			return jconverter.convertObjectToJsonString(new Error("keepalive", "You are not logged in."));
		UserLogin dbUser = usermapper.getUserLoginByEmail((String) session.getAttribute("user"));
		if (dbUser == null)
			return jconverter.convertObjectToJsonString(new Error("keepalive", "You are not logged in."));
		//System.out.println(session.getMaxInactiveInterval());
		return jconverter.convertObjectToJsonString(new Success("keepalive", dbUser.getEmail()));
	}
}