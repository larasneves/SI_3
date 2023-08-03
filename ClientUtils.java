package client;


/**
 * ClientUtils class
 * 
 * The ClientUtils class provides utility methods for the client-side of the game.
 * It includes methods for creating a game matrix, printing the game matrix, and displaying game information and statistics.
 * 
 * @author  Lara Neves, Tomás Inácio
 * @since   2023
*/

public class ClientUtils
{
	
	
	/**
	 * Creates a game matrix with the specified number of rows and columns.
	 * @param N The number of rows in the game matrix
	 * @param M The number of columns in the game matrix
	 * @return A 2D array representing the game matrix with dimensions (N+1) x (M+1)
	 */
	public static String[][] createMatrixGame(int N, int M){
		
		String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD"};
		String[][] gameMatrix = new String[N+1][M+1];
		for (int line = 0; line<=N; line++) { 
			for (int col = 0; col<=M; col++) {
				if (line==0 && col==0)
					gameMatrix[line][col] = "  ";
				else if (col == 0) {
					if (line<10) //--> to keep spaces in order after 10
						gameMatrix[line][col] = String.valueOf(" "+line);
					else
						gameMatrix[line][col] = String.valueOf(line);
				}
				else if (line == 0)
					gameMatrix[line][col] = letters[col-1];
				else { // Necessary for positions AA,...
					if (col<=26) // index of AA is 26, of AB is 27, ...
						gameMatrix[line][col] = ".";	
					else
						gameMatrix[line][col] = " .";	
				}
			}
		}
		return gameMatrix;
	}
	
	
	/**
	 * Prints the game matrix along with the remaining number of tries.
	 * @param N The number of rows in the game matrix
	 * @param M The number of columns in the game matrix
	 * @param matrix The game matrix to print
	 * @param numberOfShots The total number of shots set
	 * @param current_tries The number of tries taken so far
	 */
	public static void printGameMatrix(int N, int M, String[][] matrix, int numberOfShots, int current_tries) {
		System.out.println("\n------------ GAME BOARD ------------\n");
		for (int line=0;line<=N;line++) {
			for (int col=0;col<=M;col++) {
				System.out.print(matrix[line][col]+" ");
			}
			System.out.println("");
		}
		System.out.println("\nYou have " + (numberOfShots-current_tries) + " tries remaining!"); // CRIAR METODO SO COM ISTO e apenas nao meter no GAME:WIN
	}

	
	/**
	 * Displays the game information for each game played.
	 * @param str The game information string in the format: gameNumber; gameResult; numberOfTries
	 */
	public static void showGameInfo(String str) {
		String[] allGamesList = str.split("/");
		for (String game: allGamesList) {
			String [] gameInfo = game.split(";");
			String gameNumber = gameInfo[0];
			String gameResult = gameInfo[1];
			String gameTries = gameInfo[2];
			
			System.out.println("Game " + gameNumber + ": " + gameResult + " (tries: " + gameTries + ")");
		}
	}
	
	
	/**
	 * Displays the game statistics for the user.
	 * @param str The game statistics string in the format: winPercentage;lossPercentage;leavePercentage;averageTries
	 */
	public static void showGameStats(String str) {
		String[] gameVariables = str.split(";");
		
		System.out.println("\nWinning percentage: " + gameVariables[0] + "%");
		System.out.println("Losses percentage: " + gameVariables[1] + "%");
		System.out.println("Abandoned games percentage: " + gameVariables[2] + "%");
		System.out.println("\nAverage moves per game: " + gameVariables[3]);
	}
	
}
