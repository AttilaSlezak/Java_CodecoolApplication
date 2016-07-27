package mappers;

import org.apache.ibatis.annotations.Select;

import pojos.User;
import pojos.UserLogin;

public interface UserMapper {

	//@Select("SELECT * FROM APPLICANTS WHERE EMAIL = #{email}")
	public UserLogin getUserLoginByEmail(String email);
	public User getUserByEmail(String email);
	public void insertUser(User user);
	public void updatePassword(UserLogin userLogin);
	public void updateLastLogin(UserLogin userLogin);
	// TODO how to sign if a request is unsuccessful (especially if the function has a return type of void)? Maybe an (unchecked) exception? Or a boolean value?
}
