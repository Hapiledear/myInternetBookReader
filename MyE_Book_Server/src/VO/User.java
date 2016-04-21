package VO;

public class User {
	private int userid;
	private String name;
	private String password;
	private String email;
	private String phone;
	private int state;
	private String acidCode;
	@Override
	public String toString() {
		return "User [userid=" + userid + ", name=" + name + ", password="
				+ password + ", email=" + email + ", phone=" + phone
				+ ", state=" + state + ", acidCode=" + acidCode + "]";
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAcidCode() {
		return acidCode;
	}
	public void setAcidCode(String acidCode) {
		this.acidCode = acidCode;
	}
	public int getUserid(){
		return userid;
	}
	public void setUserid(int i){
		this.userid=i;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password=password;
	}

}
