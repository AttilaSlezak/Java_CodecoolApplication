package controllers;

import pojos.Error;
import pojos.Response;
import pojos.Success;
import pojos.User;
import pojos.UserLogin;
import security.Encrypt;
import services.UserService;
import services.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	@RequestMapping(value = "/registration", method = RequestMethod.POST, headers = "Accept=application/json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> handleRegistration(@RequestBody User jsonUser, HttpSession session) {
		
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
						return new ResponseEntity<Response>(new Error("registration", "unable to send email"), HttpStatus.INTERNAL_SERVER_ERROR);
					}
					jsonUser.setUserPassword(encrypted);
					jsonUser.setUserID(uniqueID);
					jsonUser.setStateOfApplication(0);
					jsonUser.setAddress("");
					jsonUser.setPhone("");
					usermapper.insertUser(jsonUser);
					return new ResponseEntity<Response>(new Success("registration", "Successful registration."), HttpStatus.OK);
				}
				return new ResponseEntity<Response>(new Error("registration", "Registration form is invalid."), HttpStatus.UNPROCESSABLE_ENTITY);
			}
			return new ResponseEntity<Response>(new Error("registration", "The given email is already registrated."), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Response>(new Error("registration", "Wrong HTTP request format."), HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@RequestMapping(value = "/forgottenPassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> handleForgottenPassword(@RequestBody UserLogin jsonUser) {

		if (jsonUser != null) {
			UserLogin dbUser = usermapper.getUserLoginByEmail(jsonUser.getEmail());
			if (dbUser != null) {
				String pswd = PasswordGenerator.GeneratePassword();
				String encrypted = encrypter.encryptPassword(pswd);
				dbUser.setPassword(encrypted);
				try {
					usermapper.updatePassword(dbUser);
					emailSender.sendPassword(dbUser.getEmail(), pswd);
					return new ResponseEntity<Response>(new Success("forgottenPassword", "Message sent to: " + dbUser.getEmail()), HttpStatus.OK);
				} catch (MessagingException | IOException e) {
					e.printStackTrace();
					return new ResponseEntity<Response>(new Error("forgottenPassword", "unable to send email"), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return new ResponseEntity<Response>(new Error("forgottenPassword", "No user with this email."), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Error("forgottenPassword", "Wrong HTTP request format."), HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
