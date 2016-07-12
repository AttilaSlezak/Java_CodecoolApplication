package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.*;

import mappers.UserMapper;
import pojos.User;
import pojos.UserLogin;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	public boolean getUserLoginByEmail(String email) {
		UserLogin userLogin = userMapper.getUserLoginByEmail(email);
		return (userLogin != null);
	}

	@Override
	public boolean validateUser(User user) {
		String emailStrPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern emailPattern = Pattern.compile(emailStrPattern);
        Matcher emailMatcher = emailPattern.matcher(user.getEmail());
        
        String nameStrPattern = "^[A-Za-z¡…Õ”÷’‹€·ÈÌÛˆ’˙¸˚]+$";
        Pattern namePattern = Pattern.compile(nameStrPattern);
        Matcher surnameMatcher = namePattern.matcher(user.getSurname());
        Matcher firstNameMatcher = namePattern.matcher(user.getFirstName());
        
        return emailMatcher.matches() && surnameMatcher.matches() && firstNameMatcher.matches();
	}
}