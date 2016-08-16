package controllers;

import pojos.Error;
import pojos.Response;
import pojos.Success;
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

import javax.servlet.http.HttpSession;

import json.JsonConverter;
import mappers.UserMapper;
import password.GmailSender;


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

	@RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> handleLogout(HttpSession session) {
		if (session.getAttribute("user") != null) {
			session.invalidate();
			return new ResponseEntity<Response>(new Success("logout", "You have been successfully logged out."), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Error("logout", "You are not logged in."), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/keepalive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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