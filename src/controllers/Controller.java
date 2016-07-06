package controllers;
  
  

import pojos.UserLogin;
import security.Encrpyt;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.RestController;  
  
@RestController  
public class Controller {  
   
 @RequestMapping(value = "/login", method = RequestMethod.POST,headers="Accept=application/json")  
 public void checkUser(@RequestBody UserLogin user)  
 {  
	 System.out.println(user.toString());
	 //return true;  
 }
 
 @RequestMapping(value = "/login", method = RequestMethod.GET,headers="Accept=application/json")  
 public String sendUser() 
 {
	 return "";  
 }  
}