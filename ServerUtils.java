package server;

import java.util.ArrayList;
import java.util.Random;

/**
* ServerUtils class
* 
* The ServerUtils class is a set of methods that are useful for the Game in the Server Package.
* 
* It allows to verify if certain strings are an integer number and create matrixes of the game.
* It also allows to get some statistics of the game and relevant information to display.
*
* @author  Lara Neves, Tom�s In�cio
* @since   2023
*/

public class ServerUtils
{
	/**
	 * Checks if a given string can be parsed into an integer.
	 * @param str The string to check for integer validity
	 * @return true if the string can be parsed into an integer, false otherwise
	 */
	public static boolean isInteger(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } 
        catch (NumberFormatException e) {
            return false;
        }
    }
	
	
	/**
	 * Creates a server game matrix with the specified number of rows and columns, filled with zeros.
	 * @param N The number of rows in the game matrix
	 * @param M The number of columns in the game matrix
	 * @return A 2D array representing the server game matrix with dimensions N x M
	 */
	public static int[][] createServerGameMatrix(int N, int M) {
		int[][] gameMatrix = new int[N][M];
		for (int line=0;line<N;line++) {
			for (int col=0;col<M;col++) {
				gameMatrix[line][col] = 0;
			}
		}
		return gameMatrix;
		
	}
				
	
	/**
	 * Creates a boat matrix with the specified number of rows, columns, and number of boats.
	 * @param N The number of rows in the boat matrix
	 * @param M The number of columns in the boat matrix
	 * @param num_boats The number of boats to place in the matrix
	 * @return A 2D array representing the boat matrix with dimensions N x M
	 */
	public static String[][] createBoatMatrix(int N,int M, int num_boats) {
		int NumMaxBoat1 = 1; int NumMaxBoat2 = 1; int NumMaxBoat4 = 1;
		Random rand = new Random();
		
		// Number of each type of boats
		for (int i=0;i<num_boats-3;i++) {
			int prob = rand.nextInt(3);
			if (prob == 0)
				NumMaxBoat1++;
			else if (prob == 1)
				NumMaxBoat2++;
			else
				NumMaxBoat4++;			
		}
		
		// Boat Matrix
		String[][] matrixOfBoats = new String[N][M];	
		for (int line = 0; line<N; line++) { 
			for (int col = 0; col<M; col++) {
				matrixOfBoats[line][col] = "0";
			}
		}
			
		
		// Choosing the position of the boats in the matrix of boats
		int numBoat1AlreadyDistributed=0; int numBoat2AlreadyDistributed=0; int numBoat4AlreadyDistributed=0; // number of boats already in the matrix
		
		int tries = 0;
		boolean boatsNotDistributed = true;
		boolean validPosition; // becomes false if the house is already occupied or does not allow a boat in that direction
		
		while(boatsNotDistributed) {
			tries++; 
			
			if (numBoat1AlreadyDistributed==NumMaxBoat1 && numBoat2AlreadyDistributed==NumMaxBoat2 && numBoat4AlreadyDistributed==NumMaxBoat4)
				boatsNotDistributed = false; // boats ALL Distributed 	
			else if (tries==100) {
				System.out.println("\nIt was not possible to distribute all boats across the game board.\nPlease re-enter the game conditions.\n");
				// To visualize the boat matrix, make a print statement here
				String[][] status = {{"false"}};
				return status;
			}
			
			//
			validPosition = true;
			while (numBoat1AlreadyDistributed != NumMaxBoat1 && validPosition) { // As long as all type 1 ships are not distributed
				int r_M = rand.nextInt(0,M); // random between 0 and M-1 --> index of Boat Matrix
				int r_N = rand.nextInt(0,N); // random between 0 and N-1		
				if (matrixOfBoats[r_N][r_M] == "0") {
					matrixOfBoats[r_N][r_M] = "1"; 
					numBoat1AlreadyDistributed+=1;
				}
				else
					validPosition=false; // to count as one try
				
			} // end WHILE boat1
			
			validPosition = true;
			while (numBoat2AlreadyDistributed != NumMaxBoat2 && validPosition) { 
				int r_M = rand.nextInt(0,M); // random between 0 and M-1     //column
				int r_N = rand.nextInt(0,N); // random between 0 and N-1	 //line
				int r_direcao = rand.nextInt(0,4); // random between 0 and 3: 
				//0 --> up; 1 --> right; 2 --> down; 3 --> left
				
				if (r_direcao == 0) { // UP	
					if (r_N - 1 < 0) // If the index is 0, it must give an error because it would go to position -1
						validPosition = false; 
					else if (matrixOfBoats[r_N][r_M] == "0" && matrixOfBoats[r_N-1][r_M] == "0") { //If there is free slots put a "1"
						for (int i=0; i<=1; i++){
							matrixOfBoats[r_N-i][r_M] = "1";
							}
						numBoat2AlreadyDistributed+=1;
					}
					else //if it equals "1"
						validPosition = false;
				}
				else if (r_direcao == 1) { // RIGHT	
					if (r_M + 1 > M-1)
						validPosition = false;
					else if (matrixOfBoats[r_N][r_M] == "0" && matrixOfBoats[r_N][r_M+1] == "0") {
						for (int i=0; i<=1; i++){
							matrixOfBoats[r_N][r_M+i] = "1";
						}
						numBoat2AlreadyDistributed+=1;
						
					}
					else
						validPosition = false;
				}
				else if (r_direcao == 2) { // DOWN	
					if (r_N + 1 > N-1)
						validPosition = false;
					else if (matrixOfBoats[r_N][r_M] == "0" && matrixOfBoats[r_N+1][r_M] == "0") {
						for (int i=0; i<=1; i++){
							matrixOfBoats[r_N+i][r_M] = "1";
						}
						numBoat2AlreadyDistributed+=1;
					}
					else
						validPosition = false;
				}
				else if (r_direcao == 3) { // LEFT	
					if (r_M - 1 < 0)
						validPosition = false;
					else if (matrixOfBoats[r_N][r_M] == "0" && matrixOfBoats[r_N][r_M-1] == "0") {
						for (int i=0; i<=1; i++){
							matrixOfBoats[r_N][r_M-i] = "1";
						}
						numBoat2AlreadyDistributed+=1;
					}
					else
						validPosition = false;
				}		
			} // end WHILE Boat2

			validPosition = true;
			while (numBoat4AlreadyDistributed != NumMaxBoat4 && validPosition) { 
				int r_M = rand.nextInt(0,M); // random between 0 and M-1 
				int r_N = rand.nextInt(0,N); // random between 0 and N-1	
				int r_direcao = rand.nextInt(0,4); // random between 0 and 3: 
				//0 --> UP; 1 --> RIGHT; 2 --> DOWN; 3 --> LEFT
				
				if (r_direcao == 0) { // UP	
					if (r_N - 3 < 0) // If the index is (0,1,2), it must give an error
						validPosition = false; 
					else if (matrixOfBoats[r_N][r_M] == "0" && matrixOfBoats[r_N-1][r_M] == "0" && matrixOfBoats[r_N-2][r_M] == "0" && matrixOfBoats[r_N-3][r_M] == "0") { 
						for (int i=0; i<=3; i++){
							matrixOfBoats[r_N-i][r_M] = "1";
						}
						numBoat4AlreadyDistributed+=1;
					}
					else
						validPosition = false;
				}
				else if (r_direcao == 1) { // RIGHT	
					if (r_M + 3 > M-1)
						validPosition = false;
					else if (matrixOfBoats[r_N][r_M] == "0" && matrixOfBoats[r_N][r_M+1] == "0" && matrixOfBoats[r_N][r_M+2] == "0" && matrixOfBoats[r_N][r_M+3] == "0") {
						for (int i=0; i<=3; i++){
							matrixOfBoats[r_N][r_M+i] = "1";
						}
						numBoat4AlreadyDistributed+=1;
					}
					else
						validPosition = false;
				}
				else if (r_direcao == 2) { // DOWN	
					if (r_N + 3 > N-1)
						validPosition = false;
					else if (matrixOfBoats[r_N][r_M] == "0" && matrixOfBoats[r_N+1][r_M] == "0" && matrixOfBoats[r_N+2][r_M] == "0" && matrixOfBoats[r_N+3][r_M] == "0") {
						for (int i=0; i<=3; i++){
							matrixOfBoats[r_N+i][r_M] = "1";
						}
						numBoat4AlreadyDistributed+=1;
					}
					else
						validPosition = false;
				}
				else if (r_direcao == 3) { // LEFT	
					if (r_M - 3 < 0)
						validPosition = false;
					else if (matrixOfBoats[r_N][r_M] == "0" && matrixOfBoats[r_N][r_M-1] == "0" && matrixOfBoats[r_N][r_M-2] == "0" && matrixOfBoats[r_N][r_M-3] == "0") {
						for (int i=0; i<=3; i++){
							matrixOfBoats[r_N][r_M-i] = "1";
						}
						numBoat4AlreadyDistributed+=1;
					}
					else
						validPosition = false;
				}		
			} // end WHILE boat4		
		} // end WHILE distribution of boats in the matrix of boats
		return matrixOfBoats;
	}
	
	
	/**
	 * Calculates the number of spaces taken by boats in the given matrix.
	 * @param matrix The boat matrix to calculate spaces taken by boats
	 * @param N The number of rows in the matrix
	 * @param M The number of columns in the matrix
	 * @return The number of spaces occupied by boats in the matrix
	 */
	public static int numberOfSpacesTakenByBoats(String[][] matrix, int N, int M) {
		int numberOfSpaces = 0;
		for (int line=0;line<N;line++) {
			for (int col=0;col<M;col++) {
				if (matrix[line][col].equals("1"))
					numberOfSpaces+=1;
			}
		}
		return numberOfSpaces;
	}
	
	
	/**
	 * Generates the game information string for the given user.
	 * @param user The user object for which to generate the game information
	 * @return A string containing game information 
	 */
	public static String getGameInfo(User user) {
		ArrayList<String> gameHistoryList = user.getgameHistory();
		int gameNumber = 1;
		String finalString = "";
		for (String game: gameHistoryList) {
			String[] gameInfo = game.split(";");
			finalString += gameNumber + ";" + gameInfo[0] + ";" + gameInfo[1] + "/";
			gameNumber+=1;
		}
		return finalString;
	}
	
	
	/**
	 * Generates the game statistics string for the given user.
	 * @param user The user object for which to generate the game statistics
	 * @return A string containing game statistics in the format: winPercentage;lossPercentage;leavePercentage;averageTries
	 */
	public static String getGameStats(User user) {
		
		int wins = user.getWins();
	    int losses = user.getLoses();
	    int leaves = user.getLeaves();
	    int sum = wins + losses + leaves;

	    double winPercentage = (double) wins / sum * 100.0;
	    double lossPercentage = (double) losses / sum * 100.0;
	    double leavePercentage = (double) leaves / sum * 100.0;

	    String[] tries = user.getTriesPerGame().split(";");
	    double averageTries = ServerUtils.getAverage(tries);

	    String gameVariables = winPercentage + ";" + lossPercentage + ";" + leavePercentage + ";" + averageTries;
		
		return gameVariables;
	}
	
	
	/**
	 * Calculates the average value of the elements in the given array.
	 * @param array The array of values for which to calculate the average
	 * @return The average value of the elements in the array
	 */
	public static double getAverage(String[] array) {
		int value;
		int sum = 0;
		for (String strValue: array) {
			value = Integer.valueOf(strValue);
			sum+=value;
		}
		return (1.0*sum/array.length);
	}
}
