package controllers;

import pojos.Error;
import pojos.UserLogin;
import security.Encrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

import json.JsonConverter;
import mappers.UserMapper;

@RestController
public class LoginController {

	@Autowired
	UserMapper usermapper;
	JsonConverter jconverter;
	Encrypt encrypter;

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	public String handleLogin(@RequestBody String jsonUserString,HttpSession session){
		// Bind the incoming json to class.
		UserLogin jsonUser = jconverter.ConvertJsonToUserLoginObject(jsonUserString);
		
		if (jsonUser != null) {
			// Get the user with the email that came from json from db.
			UserLogin dbUser = usermapper.getUserLoginByEmail(jsonUser.getEmail());
			
			if (dbUser != null) {
				// Compare the two password.
				String rawPassword = jsonUser.getPassword();
				String encryptedPassword = dbUser.getPassword();
				
				if (encrypter.compareEncryptedAndRaw(rawPassword, encryptedPassword)) {
					session.setAttribute("user", dbUser.getEmail());
					session.setMaxInactiveInterval(45);
					//Now we need to give him session and return his data.
					return "true";
				}
				return jconverter.ConvertObjectToJsonString(new Error("login","Password does not match."));
			}
			return jconverter.ConvertObjectToJsonString(new Error("login","No user with this email."));
		}
		return jconverter.ConvertObjectToJsonString(new Error("login","Wrong json format."));
	}

	@RequestMapping(value = "/forgottenPassword", method = RequestMethod.POST, headers = "Accept=application/json")
	public String handleForgottenPassword(@RequestBody String emailJson) {
		UserLogin jsonUser = jconverter.ConvertJsonToUserLoginObject(emailJson);
		
		if (jsonUser != null) {
			UserLogin dbUser = usermapper.getUserLoginByEmail(jsonUser.getEmail());
			if (dbUser != null) {
				//Send email to the dbuser.getEmail();
				return "";
			}
			return jconverter.ConvertObjectToJsonString(new Error("forgottenPassword","No user with this email."));
		}
		return jconverter.ConvertObjectToJsonString(new Error("forgottenPassword","Wrong json format."));
	}
	
	@RequestMapping(value = "/keepalive", method = RequestMethod.POST)
	public void dummyFunction() { }
}