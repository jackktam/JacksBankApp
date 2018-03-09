package bankobjects;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6952809976820572048L;
	
	List<String> users;
	
	public Application(String user) {
		
		users = new LinkedList<String>();
		users.add(user);
		
	}

	public Application(List<String> user) {
		
		users = new LinkedList<String>();
		for(String s : user) {
			users.add(s);
		}
		
	}
	
	public int printInfo() {
		int i = 0;
		if(users.size()==1) {
			System.out.println("Application for single account by: " + users.get(0));
			i= 1;
		}else {
			System.out.println("Joint Account Applicants\n=======================");
			for(String s: this.getUsers()) {
				System.out.println(s);
				i++;
			}
			System.out.println("=======================");
		}
		System.out.println("1.Approve Application\n2.Deny Application\n3.Return to previous menu");
		System.out.println("Please enter an option(1-3)");
		return i;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	
	
}
