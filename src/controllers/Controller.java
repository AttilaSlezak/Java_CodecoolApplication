package controllers;
  
  

import pojos.UserLogin;
import security.Encrypt;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.RestController;

import mappers.UserMapper;  
  
@RestController  
public class Controller {  
   
	@Autowired
	UserMapper usermapper;
	
 @RequestMapping(value = "/login", method = RequestMethod.POST,headers="Accept=application/json")  
 public String checkUser(@RequestBody String user) throws JsonParseException, JsonMappingException, IOException  
 {  
	 ObjectMapper mapper = new ObjectMapper();
	 UserLogin login = mapper.readValue(user, UserLogin.class);
	 System.out.println(user.toString());
	 System.out.println(login.toString());
	 System.out.println(login.getEmail().toString());
	 UserLogin valami = usermapper.getUserByEmail("henrik.ibsen@gmail.com");
	 if (valami != null)
		 System.out.println(valami.getPassword());
	 else
		 System.out.println("Nincs ilyen.");
	 return user.toString();
 }
 
 @RequestMapping(value = "/login", method = RequestMethod.GET,headers="Accept=application/json")  
 public String sendUser() 
 {
	 return "";  
 }  
}