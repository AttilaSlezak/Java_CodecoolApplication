package password;

import org.apache.commons.lang.RandomStringUtils;

public class PasswordGenerator {
	
	public static String GeneratePassword(){
		
		return RandomStringUtils.randomAlphanumeric(10);
	}
}
