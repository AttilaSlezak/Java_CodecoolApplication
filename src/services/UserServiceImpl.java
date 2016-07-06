package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mappers.UserMapper;
import pojos.UserLogin;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	public boolean getUserByEmail(String email) {
		UserLogin userLogin = userMapper.getUserByEmail(email);
		return (userLogin != null);
	}
}