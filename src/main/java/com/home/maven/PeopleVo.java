package com.home.maven;

public class PeopleVo {
	private String name;
	private String age;
	private String job;
	
	
	public PeopleVo() {
		super();
	}

	public PeopleVo(String name, String age, String job) {
		super();
		this.name = name;
		this.age = age;
		this.job = job;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Override
	public String toString() {
		return "peopleVo [name=" + name + ", age=" + age + ", job=" + job + "]";
	}
	
	
}
