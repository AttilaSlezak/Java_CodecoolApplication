package mappers;

import org.apache.ibatis.annotations.Select;

import pojos.User;
import pojos.UserLogin;

public interface UserMapper {

	//@Select("SELECT * FROM APPLICANTS WHERE EMAIL = #{email}")
	public UserLogin getUserLoginByEmail(String email);
	public User getUserByEmail(String email);
	public void insertUser(User user);
}
