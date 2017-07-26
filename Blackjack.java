/**
 * Need to run like this: java -cp .:./mysql_connector/mysql-connector-java-5.1.43-bin.jar Blackjack
 */
import java.util.Scanner;
import java.util.ArrayList;

public class Blackjack {
	static Scanner sc = new Scanner(System.in);
	static Deck d;

	public static void main(String[] args) {
		DatabaseManager dm = new DatabaseManager();
		char choice;
		boolean result;

		// create a new deck and shuffle the cards up
		d = new Deck();
		d.shuffleDeck();

		System.out.print("Enter your full name: ");
		String username = sc.nextLine();

		// get the user from the database based on their name
		// if the name doesn't exist, add a new user to the database
		User player = dm.updateUserInDatabase(username);

		// if the player still doesn't exist after creation, there is something
		// wrong, so we will play without the database
		if(player == null) {
			System.out.println("Playing offline.");
			player = new User(username, 0, 0);
		}

		System.out.println("Welcome, " + player.getName());
		System.out.println(player.getRecord());

		do {
			//game play
			result = play_game(player);

			// update wins and losses in the database
			if(result) {
				player.updateWins();
				dm.updateWinsInDatabase(player);
			}
			else {
				player.updateLosses();
				dm.updateLossesInDatabase(player);
			}

			System.out.println("Do you want to play again? <y/n>");
			choice = sc.next().charAt(0);
		} while(choice != 'n');

		System.out.println("Your record is now: " + player.getWins() + " - " + player.getLosses());
		System.out.println("Thanks for playing!");
	}

	public static boolean play_game(User u) {
		int playerResult = 0;
		int computerResult = 0;
		char choice = 'n';
		Card temp;
		ArrayList<Card> playersHand = new ArrayList<Card>();
		ArrayList<Card> computersHand = new ArrayList<Card>();

		System.out.println("------------------------------");
		System.out.println("You are beginning your turn.");

		// starting the round -- deal both hands
		playersHand.add(d.deal());
		playersHand.add(d.deal());
		computersHand.add(d.deal());
		computersHand.add(d.deal());

		// display the current state of the two hands
		System.out.println("Your hand: " + playersHand.get(0) + " and " + playersHand.get(1));
		playerResult = d.getValue(playersHand.get(0)) + d.getValue(playersHand.get(1));
		System.out.println("Value: " + playerResult);

		System.out.println("Computers hand: " + computersHand.get(0));
		computerResult = d.getValue(computersHand.get(0));
		System.out.println("Value: " + computerResult);
		computerResult += d.getValue(computersHand.get(1));

		// check right away for a winner
		if(computerResult == 21) {
			System.out.println("Computer has Blackjack. You lose.");
			System.out.println("Computers hand: " + computersHand.get(0) + " and " + computersHand.get(1));
			return false;
		}

		if(playerResult == 21) {
			System.out.println("Player has Blackjack. You win!");
			System.out.println("Players hand: " + playersHand.get(0) + " and " + playersHand.get(1));
			return true;
		}

		//  If neither player has Blackjack, play the game.  The user gets a chance
    	//	to draw cards (to "Hit").  The while loop ends when the user
    	//  chooses to "Stay" or when the user goes over 21.
		do {
			System.out.print("Would you like a hit? <y/n>");
			choice = sc.next().charAt(0);

			// if the user chooses to stay, exit the loop
			if(choice == 'n') {
				System.out.println("Good choice. Staying with " + playerResult);
				break;
			}
			else {
				System.out.println("Player hits.");
				temp = d.deal();
				System.out.println("Player drew " + temp);
				playerResult += d.getValue(temp);
				System.out.println("Value: " + playerResult);

				if(playerResult > 21) {
					System.out.println("You busted!");
					return false;
				}
			}
		} while(choice == 'y'); // end of hit loop

		// If we get to this point, the user has Stood with 21 or less.  Now, it's
        // the dealer's chance to draw.  Dealer draws cards until the dealer's total is > 17

		System.out.println("Computer is revealing its hand.");
		System.out.println("Computers hand: " + computersHand.get(0) + " and " + computersHand.get(1));
		System.out.println("Value: " + computerResult);

		while(computerResult < 17) {
			System.out.println("Computer will hit!");
			temp = d.deal();
			System.out.println("Computer drew " + temp);
			computerResult += d.getValue(temp);
			System.out.println("Value: " + computerResult);
		}

		System.out.println("Computer's total is: " + computerResult);

		// Now, the winner can be declared
		if(computerResult > 21) {
			System.out.println("Computer busted!");
			// you win
			return true;
		}
		else {
			// if there is a tie, it goes to the dealer
			if(playerResult == computerResult) {
				System.out.println("Computer wins on a push. You both had " + playerResult);
				return false;
			}
			else {
				// either you win based on card ranks, or the computer wins
				if (computerResult > playerResult) {
                    System.out.println("Computer wins, " + computerResult + " beats " + playerResult);
                    return false;
                } 
                else {
                    System.out.println("You win, " + playerResult + " beats " + computerResult);
                    return true;
                }
			}
		}
	}
}