package mappers;

import org.apache.ibatis.annotations.Select;

import pojos.UserLogin;

public interface UserMapper {

	//@Select("SELECT * FROM APPLICANTS WHERE EMAIL = #{email}")
	public UserLogin getUserByEmail(String email);
}
