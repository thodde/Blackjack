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

		d = new Deck();
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
			play_game(player);

			System.out.println("Do you want to play again? <y/n>");
			choice = sc.next().charAt(0);
		} while(choice != 'n');
	}

	public static void play_game(User u) {
		boolean playerTurn = true;
		int playerResult = 0;
		int computerResult = 0;
		char choice = 'n';
		ArrayList<Card> playersHand = new ArrayList<Card>();
		ArrayList<Card> computersHand = new ArrayList<Card>();

		if(playerTurn) {
			System.out.println("------------------------------");
			System.out.println("You are beginning your turn.");
			// starting the round
			playersHand.add(d.deal());
			playersHand.add(d.deal());
			System.out.println("Your hand: " + playersHand.get(0) + " and " + playersHand.get(1));
			playerResult = d.getValue(playersHand.get(0)) + d.getValue(playersHand.get(1));
			System.out.println("Value: " + playerResult);

			computersHand.add(d.deal());
			computersHand.add(d.deal());

			System.out.println("Computers hand: " + computersHand.get(0));
			computerResult = d.getValue(computersHand.get(0));
			System.out.println("Value: " + computerResult);

			if(playerResult > 21) {
				System.out.println("You busted!");
			}
			else if(playerResult == 21) {
				System.out.println("Blackjack!");
			}
			else {
				do {
					if(playerTurn) {
						System.out.print("Would you like a hit? <y/n>");
						choice = sc.next().charAt(0);

						if(choice == 'y') {
							if(playerResult < 21)
								playerResult += takeHit(playerTurn, playerResult);

							if(playerResult > 21) {
								System.out.println("You busted!");
								break;
							}
						}
						else {
							System.out.println("Good choice. Staying with " + playerResult);
							break;
						}
					}
				} while(choice == 'y'); // end of hit loop
			}

			playerTurn = false;
		}

		System.out.println("Computer is revealing its hand.");
		System.out.println("Computers hand: " + computersHand.get(0) + " and " + computersHand.get(1));
		computerResult += d.getValue(computersHand.get(1));
		System.out.println("Value: " + computerResult);

		if(computerResult > 21) {
			if(playerResult <= 21) {
				System.out.println("Computer busted, congratulations, you won!");
			}
			else {
				System.out.println("You both busted. It's a push.");
			}
		}
		else if(computerResult == 21) {
			if(playerResult > 21 || playerResult < 21) {
				System.out.println("Computer got Blackjack! You lost.");
			}
			else {
				System.out.println("Computer got Blackjack too! It's a push.");
			}
		}
		else if(computerResult >= 17 && computerResult < 21) {
			System.out.println("Computer will stay.");
		}
		else {
			do {
				computerResult += takeHit(playerTurn, computerResult);
			} while(computerResult < 17);
		}

		playerTurn = true;

		if(playerResult > computerResult) {
			System.out.println("Congratulations! You won!");
		}
		else if(playerResult == computerResult) {
			System.out.println("It's a push. You both had " + playerResult);
		}
		else {
			System.out.println("You lost.");
		}
	}

	public static int takeHit(boolean playerTurn, int sum) {
		Card temp = d.deal();

		if(playerTurn) {
			System.out.println("You drew " + temp);
			sum += d.getValue(temp);
			System.out.println("Value: " + sum);
		}
		else {
			System.out.println("Computer drew " + temp);
			sum += d.getValue(temp);
			System.out.println("Value: " + sum);
		}

		return sum;
	}
}