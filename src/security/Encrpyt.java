package security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Encrpyt {
	
	public String EncrpytPassword(String password){
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}
	
	public Boolean CompareEnryptedAndRaw(String pass,String encrpyted){
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		return passwordEncoder.matches(pass, encrpyted);
	}
}
