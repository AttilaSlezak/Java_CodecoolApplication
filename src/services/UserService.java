package services;

import pojos.User;

public interface UserService {

	public boolean getUserLoginByEmail(String email);
	
	public boolean validateUser(User user);
}