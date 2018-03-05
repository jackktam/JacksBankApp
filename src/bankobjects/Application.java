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
	
}
