import java.util.HashMap;

public class Deck {
	private Card deck[];
	private final int size = 52;
	private final int numOfDecks = 1;

	private String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
    private String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private String[] value = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "10", "10", "10" };
    public HashMap<String, String> values = new HashMap<String, String>();

    private int currentSize;
    private int topCardIndex;

	public Deck() {
		// basically we want to store this value because we could use 
		// some other number of decks (ex: 6 deck pile)
		int fullDeckSize = size * numOfDecks;
		currentSize = fullDeckSize;
		topCardIndex = 0;
		this.deck = new Card[fullDeckSize];
	    for (int i = 0; i < fullDeckSize; i++) {
	        Card card = new Card(ranks[i % 13], suits[i % 4]); //Instantiate a Card
	        this.deck[i] = card;   //Adding card to the Deck
	        // the hash map will allow us to obtain the actual values of the cards
	        values.put(ranks[i % 13], value[i % 13]);
	    }
	}

	public int getCurrentSize() {
		return this.currentSize;
	}

	public void shuffleDeck() {
		// Shuffle the deck
    	for (int i = 0; i < deck.length; i++) {
      		int index = (int)(Math.random() * deck.length);
      		Card temp = deck[i];
      		deck[i] = deck[index];
      		deck[index] = temp;
    	}
	}

	public void showDeck() {
		for (int i = 0; i < deck.length; i++) {
      		System.out.println(deck[i].toString());
    	}
	}

	public void showTop() {
		System.out.println("The top card is: ");
		System.out.print(deck[topCardIndex]);
		System.out.println();
	}

	public int getValue(Card c) {
		return (int)Integer.parseInt(values.get(c.getRank()));
	}

	public Card deal() {
		if (currentSize == 0 || (currentSize - 1 <= 0)) {
			System.out.println("Out of cards!");
			return null;
		}
		else {
			currentSize--;
        	Card drawnCard = deck[topCardIndex];
        	topCardIndex++;
        	return drawnCard;
		} 
	}
}