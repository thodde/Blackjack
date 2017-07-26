import java.sql.*;

/**
 * This class exists to manage the database connection and two way communication
 */
public class DatabaseManager {

	// IP Address of local mysql connection
	private final String host = "192.168.64.2";
	public static Connection connection;

	// immediately create a connection upon instantiation
	public DatabaseManager() {
		createDatabaseConnection();
	}

	// keep this private, we don't want random classes connecting to databases
	private Connection createDatabaseConnection() {
		String url = "jdbc:mysql://" + host + ":3306/scoreboard?useSSL=true";
		// created a java username because doing things as root is not ideal (note: creds could be changed to root/"")
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

	// this is a singleton so there cannot be multiple database connections
	public Connection getCurrentConnection() {
		if(connection != null) {
			return connection;
		}
		else {
			return createDatabaseConnection();
		}
	}

	/**
	 * This method tries to select a user from the database based on their name
	 * If it exists, we populate a user object with the data retrieved
	 * else, we create a new user and add it to the database
	 */
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

	public void updateWinsInDatabase(User u) {
		try {
			// find the user and update their wins
			String query = "UPDATE scoreboard SET wins = ? WHERE name = ?";
			Connection conn = getCurrentConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt   (1, u.getWins());
      		preparedStmt.setString(2, u.getName());

      		preparedStmt.executeUpdate();

      		System.out.println("Your record has been updated in the database!");
		}
		catch(Exception e) {
			System.out.println("Something is wrong with the database connection.");
			e.printStackTrace();
		}
	}

	public void updateLossesInDatabase(User u) {
		try {
			// find the user and update their losses
			String query = "UPDATE scoreboard SET losses = ? WHERE name = ?";
			Connection conn = getCurrentConnection();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt   (1, u.getLosses());
      		preparedStmt.setString(2, u.getName());

      		preparedStmt.executeUpdate();

      		System.out.println("Your record has been updated in the database!");
		}
		catch(Exception e) {
			System.out.println("Something is wrong with the database connection.");
			e.printStackTrace();
		}
	}
}