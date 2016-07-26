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
public class LoginController {

	@Autowired
	UserMapper usermapper;
	JsonConverter jconverter = new JsonConverter();
	Encrypt encrypter = new Encrypt();
	UserService userservice = new UserServiceImpl();
	@Autowired
	GmailSender emailSender;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> handleLogin(@RequestBody UserLogin jsonUser, HttpSession session) {
		
		if (jsonUser != null) {
			// Get the user with the email that came from json from db.
			System.out.println(jsonUser.getEmail());
			UserLogin dbUser = usermapper.getUserLoginByEmail(jsonUser.getEmail());
			
			if (session.getAttribute("user") != null) {
				return new ResponseEntity<Response>(new Error("login", "You are already logged in."), HttpStatus.CONFLICT);
			}

			if (dbUser != null) {
				// Compare the two password.
				String rawPassword = jsonUser.getPassword();
				String encryptedPassword = dbUser.getPassword();

				if (encrypter.compareEncryptedAndRaw(rawPassword, encryptedPassword)) {
					session.setAttribute("user", dbUser.getEmail());
					session.setMaxInactiveInterval(45);
					usermapper.updateLastLogin(dbUser);
					return new ResponseEntity<Response>(new Success("login", dbUser.getEmail() + " logged in successfully"), HttpStatus.OK);
				}
				return new ResponseEntity<Response>(new Error("login", "Wrong email and/or password."), HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<Response>(new Error("login", "Wrong email and/or password."), HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<Response>(new Error("login", "Wrong HTTP request format."), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@RequestMapping(value = "/forgottenPassword", method = RequestMethod.POST, headers = "Accept=application/json")
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

	@RequestMapping(value = "/registration", method = RequestMethod.POST, headers = "Accept=application/json")
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

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<Response> handleLogout(HttpSession session) {
		if (session.getAttribute("user") != null) {
			session.invalidate();
			return new ResponseEntity<Response>(new Success("logout", "You have been successfully logged out."), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Error("logout", "You are already logged out."), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/keepalive", method = RequestMethod.POST)
	public ResponseEntity<Response> dummyFunction(HttpSession session) {
		if (session.getAttribute("user") == null)
			return new ResponseEntity<Response>(new Error("keepalive", "You are not logged in."), HttpStatus.UNPROCESSABLE_ENTITY);
		UserLogin dbUser = usermapper.getUserLoginByEmail((String) session.getAttribute("user"));
		if (dbUser == null)
			return new ResponseEntity<Response>(new Error("keepalive", "You are not logged in."), HttpStatus.UNPROCESSABLE_ENTITY);
		//System.out.println(session.getMaxInactiveInterval());
		return new ResponseEntity<Response>(new Success("keepalive", dbUser.getEmail()), HttpStatus.OK);
	}
}