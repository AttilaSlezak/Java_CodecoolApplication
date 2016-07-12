package pojos;

import java.util.Date;

public class User {
	private String userID;
	private String surname;
	private String firstName;
	private String email;
	private String address;
	private String phone;
	private String userPassword;
	private int stateOfApplication;
	private int resultTestAcceptance;
	private int resultTestEngligh;
	private int resultTestLogic;
	private int resultTestMotivation;
	private Date dateOfRegistration;
	private Date dateOfLastLogin;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public int getStateOfApplication() {
		return stateOfApplication;
	}
	public void setStateOfApplication(int stateOfApplication) {
		this.stateOfApplication = stateOfApplication;
	}
	public int getResultTestAcceptance() {
		return resultTestAcceptance;
	}
	public void setResultTestAcceptance(int resultTestAcceptance) {
		this.resultTestAcceptance = resultTestAcceptance;
	}
	public int getResultTestEngligh() {
		return resultTestEngligh;
	}
	public void setResultTestEngligh(int resultTestEngligh) {
		this.resultTestEngligh = resultTestEngligh;
	}
	public int getResultTestLogic() {
		return resultTestLogic;
	}
	public void setResultTestLogic(int resultTestLogic) {
		this.resultTestLogic = resultTestLogic;
	}
	public int getResultTestMotivation() {
		return resultTestMotivation;
	}
	public void setResultTestMotivation(int resultTestMotivation) {
		this.resultTestMotivation = resultTestMotivation;
	}
	public Date getDateOfRegistration() {
		return dateOfRegistration;
	}
	public void setDateOfRegistration(Date dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
	}
	public Date getDateOfLastLogin() {
		return dateOfLastLogin;
	}
	public void setDateOfLastLogin(Date dateOfLastLogin) {
		this.dateOfLastLogin = dateOfLastLogin;
	}
}
