package server;
import java.util.ArrayList;


/**
* User class
* 
* Each User has a username, a password, a login State (true or false), 
* a number of tries associated to each game played, an history of all the games, 
* a value of total wins, total losses and leaves.
*
* @author  Lara Neves, Tomás Inácio
* @since   2023
*/


public class User {
	
	protected String username;
	protected String password;
	protected boolean loggedInState;
	protected String triesPerGame;
	protected ArrayList<String> gameHistory;
	protected int wins;
	protected int loses;
	protected int leaves;
	
	
	/**
	* User constructor.
	* 
	* It will create an instance of the User class
	* 
	* @param username The username of the User
	* @param password The password of the User
	* @param loggedInState State of the user login (true if already logged, false otherwise)
	* @param triesPerGame  Number of tries in each game played
	* @param gameHistory History of all the games played
	* @param wins Number of total wins
	* @param loses Number of total losses
	* @param leaves Number of total leaves (quit or exit)
	* 
	* @return an instance of class User
	*/
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.loggedInState = false;
		this.triesPerGame = "";
		this.gameHistory = new ArrayList<String>(); //Saves the history of plays with the corresponding number of tries
		this.wins = 0;
		this.loses = 0;
		this.leaves = 0;
	}
	
	
	/**
	 * Returns the username of the User.
	 * @return The username of the User
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Returns the password of the User.
	 * @return The password of the User
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Returns the login State of the User.
	 * @return The login State of the User
	 */
	public boolean getLoggedInState() {
		return this.loggedInState;
	}
	
	/**
	 * Returns the number of wins of the User.
	 * @return The number of wins of the User
	 */
	public int getWins() {
		return this.wins;
	}
	
	/**
	 * Returns the number of losses of the User.
	 * @return The number of losses of the User
	 */
	public int getLoses() {
		return this.loses;
	}
	
	/**
	 * Returns the number of leaves or exits of the User.
	 * @return The number of leaves of the User
	 */
	public int getLeaves() {
		return this.leaves;
	}
	
	/**
	 * Returns the history of all the games played of the User.
	 * @return The history of the User
	 */
	public ArrayList<String> getgameHistory(){
		return this.gameHistory;
	}
	
	/**
	 * Adds the result of a game to the history of the User and the corresponding number of tries.
	 * @param gameResult The result of the game to be added to the history of the User
	 * @param numberOfTries Number of tries in the game played
	 */
	public void addGameHistory(String gameResult, int numberOfTries) {
		this.gameHistory.add(gameResult + ";" + numberOfTries);
	}
	
	/**
	 * Adds a win to the number of total wins of the User.
	 */
	public void addWin() {
		this.wins += 1;
	}
	
	/**
	 * Adds a loss to the number of total losses of the User.
	 */
	public void addLose() {
		this.loses += 1;
	}
	
	/**
	 * Adds a leave to the number of total leaves of the User.
	 */
	public void addLeave() {
		this.leaves += 1;
	}
	
	/**
	*Sets the logged-in state of the User.
	* @param state The state to set for the logged-in state (true for logged in, false for logged out)
	*/
	public void setLoggedInState(boolean state) {
		this.loggedInState = state;
	}
	
	/**
	Adds the number of tries for a game to the User's total tries per game.
	@param tries The number of tries to add to the User's total tries per game
	*/
	public void addTriesPerGame(int tries) {
		this.triesPerGame += tries+";";
	}
	
	
	/**
	*Returns the User's total tries per game.
	*@return The User's total tries per game 
	*/
	public String getTriesPerGame() {
		return this.triesPerGame;
	}
	
	
	
}
