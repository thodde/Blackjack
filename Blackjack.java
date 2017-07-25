/**
 * Need to run like this: java -cp .:./mysql_connector/mysql-connector-java-5.1.43-bin.jar Blackjack
 */
import java.util.Scanner;

public class Blackjack {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		DatabaseManager dm = new DatabaseManager();
		char choice;

		Deck d = new Deck();
		d.shuffleDeck();

		System.out.print("Enter your full name: ");
		String username = sc.nextLine();

		// get the user from the database based on their name
		User player = dm.updateUserInDatabase(username);
		if(player == null) {
			System.out.println("Playing offline.");
			player = new User(username, 0, 0);
		}

		System.out.println("Welcome, " + player.getName());
		System.out.println(player.getRecord());

		do {
			//game play
			System.out.println("Do you want to play again? <y/n>");
			choice = sc.next();

		} while(choice != 'n');
	}
}