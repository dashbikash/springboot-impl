package dashbikash.spring.springlearn;


public class Employee {

	private String id;
	private String fname;
	private String lname;
	private String email;
	private int age;
	private float experience;
	private float salary;
	private String gender;
	private String phone;
	private String job_title;
	private String department;

	// Getter Methods

	@Override
	public String toString() {
		return "Employee [id=" + id + ", fname=" + fname + ", lname=" + lname + ", email=" + email + ", age=" + age
				+ ", experience=" + experience + ", salary=" + salary + ", gender=" + gender + ", phone=" + phone
				+ ", job_title=" + job_title + ", department=" + department + "]";
	}

	public String getId() {
		return id;
	}

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

	public float getExperience() {
		return experience;
	}

	public float getSalary() {
		return salary;
	}

	public String getGender() {
		return gender;
	}

	public String getPhone() {
		return phone;
	}

	public String getJob_title() {
		return job_title;
	}

	public String getDepartment() {
		return department;
	}

	// Setter Methods

	public void setId(String id) {
		this.id = id;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setExperience(float experience) {
		this.experience = experience;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Employee( String fname, String lname, String email, int age, float experience, float salary,
			String gender, String phone, String job_title, String department) {
		
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.age = age;
		this.experience = experience;
		this.salary = salary;
		this.gender = gender;
		this.phone = phone;
		this.job_title = job_title;
		this.department = department;
	}

	public Employee() {
	}

	
}