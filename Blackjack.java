/**
 * Need to run like this: java -cp .:./mysql_connector/mysql-connector-java-5.1.43-bin.jar Blackjack
 */
import java.util.Scanner;
import java.util.ArrayList;

public class Blackjack {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		DatabaseManager dm = new DatabaseManager();
		char choice;

		Deck deck = new Deck();
		deck.shuffleDeck();

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
			play_game(deck, player);

			System.out.println("Do you want to play again? <y/n>");
			choice = sc.next().charAt(0);
		} while(choice != 'n');
	}

	public static void play_game(Deck d, User u) {
		boolean playerTurn = true;
		char choice;
		ArrayList<Card> playersHand = new ArrayList<Card>();
		Card computersHand[];

		do {
			// starting the players turn
			System.out.println("--------------------------");
			playersHand.add(d.deal());
			playersHand.add(d.deal());
			System.out.println("Your hand: " + playersHand.get(0) + " and " + playersHand.get(1));
			int sum = d.getValue(playersHand.get(0)) + d.getValue(playersHand.get(1));
			System.out.println("Value: " + sum);

			if(sum > 21) {
				System.out.println("You busted!");
				playerTurn = false;
				break;
			}
			else if( sum == 21 ) {
				System.out.println("Blackjack! We have a winner!");
				playerTurn = false;
				// TODO: need to remember to update the user results
				break;
			}
			else {
				do {
					System.out.print("Would you like a hit? <y/n>");
					choice = sc.next().charAt(0);

					if(choice == 'y' && sum <= 21) {
						playersHand.add(d.deal());
						System.out.println("You drew " + playersHand.get(2));
						sum += d.getValue(playersHand.get(2));
						System.out.println("Value: " + sum);
					}
					else {
						System.out.println("Good choice.");
						playerTurn = false;
						break;
					}
				}
				while(choice == 'y'); // end of hit loop
			}
		} while(playerTurn); // end of player turn loop
	}
}