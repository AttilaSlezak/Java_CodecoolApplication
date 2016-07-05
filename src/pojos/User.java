package pojos;

import java.util.Date;

public class User {
	private String user_id;
	private String user_name;
	private String email;
	private String address;
	private String phone;
	private String user_password;
	private int state_of_application;
	private Date date_of_registration;
	private Date date_of_last_login;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return user_name;
	}

	public void setName(String name) {
		this.user_name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return user_password;
	}

	public void setPassword(String password) {
		this.user_password = password;
	}

	public int getState_of_application() {
		return state_of_application;
	}

	public void setState_of_application(int state_of_application) {
		this.state_of_application = state_of_application;
	}

	public Date getDate_of_registration() {
		return date_of_registration;
	}

	public void setDate_of_registration(Date date_of_registration) {
		this.date_of_registration = date_of_registration;
	}

	public Date getDate_of_last_login() {
		return date_of_last_login;
	}

	public void setDate_of_last_login(Date date_of_last_login) {
		this.date_of_last_login = date_of_last_login;
	}

}
