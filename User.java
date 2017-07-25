public class User {
	private String name;
	private int wins;
	private int losses;
	private int id;

	// default constructor
	public User() {
		this.id = -1;
		this.name = "";
		this.wins = 0;
		this.losses = 0;
	}

	public User(String name) {
		this.id = -1;
		this.name = name;
		this.wins = 0;
		this.losses = 0;
	}

	public User(String name, int wins, int losses) {
		this.id = -1;
		this.name = name;
		this.wins = wins;
		this.losses = losses;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void updateWins() {
		this.wins++;
	}

	public void updateLosses() {
		this.losses++;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public int getWins() {
		return this.wins;
	}

	public int getLosses() {
		return this.losses;
	}

	public int getId() {
		return this.id;
	}

	public String getRecord() {
		return "Your Blackjack record is currently: " + this.wins + " - " + this.losses;
	}
}