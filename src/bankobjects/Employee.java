package bankobjects;

import java.io.Serializable;

public class Employee extends Staff implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6609440617877203920L;

	public Employee(String username, String password) {
		super(username, password);
		// TODO Auto-generated constructor stub
	}

}
