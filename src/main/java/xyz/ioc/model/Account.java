package xyz.ioc.model;

import java.util.List;
import java.util.ArrayList;
import xyz.ioc.common.ApplicationBeanLookup;
import org.springframework.context.ApplicationContext;


public class Account {

	private long id;

	private String uuid;

	private String name;

	private String imageUri;

	private String username;

	private String password;

	private String passwordConfirm;

	private String location;

	private String age;




	public long getId(){
		return id;
	}

	public void setId(long id){
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getAge() { return age; }

	public void setAge(String age) {
		this.age = age;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNameUsername(){
		if(name != null && !name.equals("")) return name;
		return username;
	}

	public String toString(){
		return this.id + ": " + this.name + " " + this.username + " ";
	}

}

