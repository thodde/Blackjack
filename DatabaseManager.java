import java.sql.*;

/**
 * This class exists to manage the database connection and two way communication
 */
public class DatabaseManager {

	private final String host = "192.168.64.2";
	public static Connection connection;

	public DatabaseManager() {
		createDatabaseConnection();
	}

	// keep this private, we don't want random classes connecting to databases
	private Connection createDatabaseConnection() {
		// this should be more generic
		String url = "jdbc:mysql://" + host + ":3306/scoreboard?useSSL=true";
		String username = "java";
		String password = "password";

		System.out.println("Loading driver...");
		// load the driver for mysql
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Driver loaded!");
		}
		catch (ClassNotFoundException e) {
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}

		// try to connect to the local database
		System.out.println("Connecting database...");
		try {
			connection = DriverManager.getConnection(url, username, password);
		    System.out.println("Database connected!");
		    return connection;
		}
		catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	public Connection getCurrentConnection() {
		if(connection != null) {
			return connection;
		}
		else {
			return createDatabaseConnection();
		}
	}

	public User updateUserInDatabase(String name) {
		try {
			String query = "SELECT * FROM scoreboard";
			Connection conn = getCurrentConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			// iterate through the java resultset
	      	while (rs.next()) {
	      		if(name.equals(rs.getString("name"))) {
	      			User u = new User(rs.getString("name"), rs.getInt("wins"), rs.getInt("losses"));
	        		u.setId(rs.getInt("id"));
	        		return u;
	        	}
	      	}

	      	// you should only ever get here if there were no users in the result set
	      	System.out.println("You are a new user. Creating account...");
	      	query = "INSERT INTO scoreboard(name, wins, losses) VALUES ('" + name + "', 0, 0)";
			st.executeUpdate(query);
			return new User(name, 0, 0);
	  	}
	  	catch(Exception e) {
	  		e.printStackTrace();
	  	}
	  	System.out.println("Error reading database.");
	  	return null;
	}
}