package controllers;
  
  
import pojos.User;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.RestController;  
  
@RestController  
public class Controller {  
   
 @RequestMapping(value = "/login", method = RequestMethod.POST,headers="Accept=application/json")  
 public void checkUser(@RequestBody User user)  
 {  
  System.out.println(user.toString());
  //return true;  
 }
 
 @RequestMapping(value = "/login", method = RequestMethod.GET,headers="Accept=application/json")  
 public String sendUser() throws JsonGenerationException, JsonMappingException, IOException  
 {
	 ObjectMapper mapper = new ObjectMapper();
	 User testUser = new User("sajt","pass");
	 String jsonInString = mapper.writeValueAsString(testUser);
	 return jsonInString;  
 }  
}