package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;


public class Client
{
	public static void main(String args[]) throws IOException {
		Socket socket = new Socket("localhost",12345);
		Scanner sc = new Scanner(System.in);
		
		InputStream in = socket.getInputStream();
		DataInputStream dataIn = new DataInputStream(in);
		OutputStream out = socket.getOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		
		String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD"};
		
		boolean weAreStillPlaying;
		int M=0; int N=0;
		int clientNumberIndex = 0;
		int clientLetterIndex = 0;
		
		String rcvAnswer;
		String sndAnswer;
		
		boolean weAreLoggingIn = true;

		
		// APP
		boolean weWantToStayInTheApp = true;
		String menuOption = "";
		
		while (weWantToStayInTheApp) {
			weAreStillPlaying = true;
			
			while (weAreLoggingIn) {  // Login of the client
				String user = "";
				String pass = "";
				System.out.print("Insert username: ");
				user = sc.nextLine();
				System.out.print("Insert password: ");
				pass = sc.nextLine();
				String finalAnswer = "LOGIN:" + user + "/" + pass;
				
				dataOut.writeUTF(finalAnswer); 
				dataOut.flush();		

				rcvAnswer = dataIn.readUTF();
				if (rcvAnswer.endsWith("VALID")) {
					weAreLoggingIn = false;  // Login accepted
					System.out.println("Login accepted");
				}
				else if (rcvAnswer.endsWith("INVALID_USER_ALREADY_LOGGED")) {
					System.out.println("This user account is already being used elsewhere. Try again.\n");
				}
				else if (rcvAnswer.endsWith("INVALID_USER_NOT_FOUND")) {
					System.out.println("The credentials were invalid. Try again \n");
				}
			}
			
			boolean weAreInTheMenu = true;
			while (weAreInTheMenu) {
				String menu_inicial = "\n----- MENU -----\n" + "1) Play a new game" + "\n" + "2) Game history " + "\n" + "3) Quit" + "\n" + "Answer: ";
				System.out.print(menu_inicial);
				sndAnswer = sc.nextLine();
				dataOut.writeUTF("MENU:"+sndAnswer);
				dataOut.flush();
				
				rcvAnswer = dataIn.readUTF();
				if (rcvAnswer.startsWith("MENU:VALID")) {
					menuOption = sndAnswer;
					weAreInTheMenu = false;		
				}
				else {
					System.out.println("The answer is not valid. Try again.\n");
				}
				
			}
			
			if (menuOption.equals("1")) { // New Game
				
				while (weAreStillPlaying) {
		
					boolean weAreChoosingN = true; //Choose the number of lines
					while (weAreChoosingN) { 
						System.out.print("Vertical Dimension (N): ");	
						String sndAnswer2 = sc.nextLine(); 
						dataOut.writeUTF("NLINES:"+sndAnswer2); 
						dataOut.flush();
						
						rcvAnswer = dataIn.readUTF();
						
						if (rcvAnswer.endsWith("VALID")) {
							weAreChoosingN = false;
							N = Integer.valueOf(sndAnswer2);
						}
						else if (rcvAnswer.endsWith("INVALID_OUT_OF_BOUNDS")) {
							System.out.println("The number is not between 15 an 30. Please try again.\n");
						}
						else if (rcvAnswer.endsWith("INVALID_NOT_INTEGER")) {
							System.out.println("The number is not an integer. Please try again.\n");
						}
						else {
							System.out.println("The answer is not valid. Try again.\n");
						}
						
					}
				
					boolean weAreChoosingM = true; 
					while (weAreChoosingM) { 
						System.out.print("Horizontal Dimension (M): ");	
						String sndAnswer3 = sc.nextLine();
						dataOut.writeUTF("NCOL:"+sndAnswer3); 
						dataOut.flush();
						
						
						rcvAnswer = dataIn.readUTF();
						
						if (rcvAnswer.endsWith("VALID")) {
							weAreChoosingM = false;
							M = Integer.valueOf(sndAnswer3);
						}
						else if (rcvAnswer.endsWith("INVALID_OUT_OF_BOUNDS")) {
							System.out.println("The number is not between 15 an 30. Please try again.\n");
						}
						else if (rcvAnswer.endsWith("INVALID_NOT_INTEGER")) {
							System.out.println("The number is not an integer. Please try again.\n");
						}
						else {
							System.out.println("The answer is not valid. Try again.\n");
						}
						
					}
					
					
					boolean weAreChoosingBoats = true; 
					while (weAreChoosingBoats) { 
						System.out.print("Number of boats: ");	
						String boatNumber = sc.nextLine();
						dataOut.writeUTF("NBOAT:"+ boatNumber); 
						dataOut.flush();
						
						rcvAnswer = dataIn.readUTF();
						if (rcvAnswer.endsWith("VALID")) {
							weAreChoosingBoats = false;
						}
						else if (rcvAnswer.endsWith("INVALID_OUT_OF_BOUNDS")) {
							System.out.println("The number must be an integer greater than 3. Try again.\n");
						}
						else if (rcvAnswer.endsWith("INVALID_NOT_INTEGER")) {
							System.out.println("The number is not an integer. Please try again.\n");
						}
						else {
							System.out.println("The answer is not valid. Try again.\n");
						}
					}
					
					
					boolean theClientCanPlay;
					rcvAnswer = dataIn.readUTF();
					if (rcvAnswer.split(":")[1].startsWith("VALID")) {
						theClientCanPlay = true;
					}
					else {
						theClientCanPlay = false;
						System.out.println("It was not possible to distribute all boats across the game board.\n");
					}
					
					
					if (theClientCanPlay) {
						int shotNumber = 0;
						boolean weAreChoosingShots = true;
						while (weAreChoosingShots) {
							System.out.print("Number of shots: ");
							String sndAnswer4 = sc.nextLine();
							dataOut.writeUTF("NSHOT:" + sndAnswer4);
							dataOut.flush();
							
							rcvAnswer = dataIn.readUTF();
							//System.out.print(rcvAnswer);
							if (rcvAnswer.endsWith("VALID")) {
								weAreChoosingShots = false;
								shotNumber = Integer.valueOf(sndAnswer4);
							}
							else if (rcvAnswer.endsWith("INVALID_OUT_OF_BOUNDS")) {
								System.out.println("The number is out of bounds. Try again.\n");
							}
							else if (rcvAnswer.endsWith("INVALID_NOT_INTEGER")) {
								System.out.println("The number was not an integer. Please try again.\n");
							}
							else {
								System.out.println("The answer is not valid. Try again.\n");
							}	
						}
						

						String[][] gameMatrix = ClientUtils.createMatrixGame(N,M);

						
						// --------- Client's Move ------------
						boolean theClientIsMakingMoves = true;
						int tries = 0;
						
						while (theClientIsMakingMoves) {
							
							rcvAnswer = dataIn.readUTF();
							if (rcvAnswer.endsWith("WIN")) {
								theClientIsMakingMoves = false;
								ClientUtils.printGameMatrix(N,M,gameMatrix,shotNumber,tries);
								System.out.println("YOU WIN!\n");
							}
							else if (rcvAnswer.endsWith("LOSE")) {
								theClientIsMakingMoves = false;		
								System.out.println("YOU LOSE!");
							}
							else if (rcvAnswer.endsWith("CONTINUE")) {
								ClientUtils.printGameMatrix(N,M,gameMatrix,shotNumber,tries);
								tries+=1;
								
								boolean weAreChoosingLetter = true;
								while (weAreChoosingLetter) {
									System.out.print("\nChoose the horizontal coordenate (letter): ");
									String clientLetter = sc.nextLine();
									clientLetter = clientLetter.toUpperCase();
									dataOut.writeUTF("CLIENT_PLAY_LETTER:" + clientLetter);
									
									rcvAnswer = dataIn.readUTF();
									if (rcvAnswer.endsWith("VALID")) {
										weAreChoosingLetter = false;
										clientLetterIndex = Arrays.asList(letters).indexOf(clientLetter) + 1;
									}
									else if (rcvAnswer.endsWith("INVALID_LETTER_UNAVAILABLE")) {
										System.out.println("The choosen letter is not available in the game. Try again.\n");
									}
									else if (rcvAnswer.endsWith("EXIT")) {
										System.out.println("Leaving the game...\n");
										weAreChoosingLetter = false;
										theClientIsMakingMoves = false;
									}
									else {
										System.out.println("Error in communication");
									}
								}
								
								
								boolean weAreChoosingNumber = theClientIsMakingMoves;
								while (weAreChoosingNumber) {
									System.out.print("Choose the vertical coordenate (number): ");
									String clientNumber = sc.nextLine();
									clientNumber = clientNumber.toUpperCase();
									dataOut.writeUTF("CLIENT_PLAY_NUMBER:" + clientNumber);
									
									rcvAnswer = dataIn.readUTF();
									if (rcvAnswer.endsWith("VALID")) {
										weAreChoosingNumber = false;
										clientNumberIndex = Integer.valueOf(clientNumber);
									}
									else if (rcvAnswer.endsWith("INVALID_OUT_OF_BOUNDS")) {
										System.out.println("The choosen number is not available in the game. Try again.\n");
									}
									else if (rcvAnswer.endsWith("INVALID_NOT_INTEGER")) {
										System.out.println("The choosen number is not an integer. Try again.\n");
									}
									else if (rcvAnswer.endsWith("EXIT")) {
										System.out.println("Leaving the game...\n");
										weAreChoosingNumber = false;
										theClientIsMakingMoves = false;
									}
									else {
										System.out.println("Error in communication");
									}
								}
								
								
								// Update matrix
								if (theClientIsMakingMoves) {
									rcvAnswer = dataIn.readUTF();
									
									if (rcvAnswer.endsWith("HIT")) {
										gameMatrix[clientNumberIndex][clientLetterIndex] = "X";
									}
									else if (rcvAnswer.endsWith("MISS")) {
										gameMatrix[clientNumberIndex][clientLetterIndex] = "O";
									}
									else if (rcvAnswer.endsWith("INVALID_POSITION_ALREADY_IN_USE")) {
										System.out.println("This position is already in use. Try again.\n");
										tries-=1;
									}				
								}
							
							
								
							} // end else if ("CONTINUE")
						} // end while(clientIsMakingMoves)
					}

					
					boolean theGameIsOver = true; 
					while (theGameIsOver && theClientCanPlay) {
						System.out.print("\nDo you want to play again? ('yes' or 'no'): ");
						String playAgainOption = sc.nextLine();
						dataOut.writeUTF("PLAY_AGAIN:" + playAgainOption);
						dataOut.flush();
						
						rcvAnswer = dataIn.readUTF();
						if (rcvAnswer.endsWith("YES")) {
							theGameIsOver = false;
							System.out.println("Prepare to play again!.\n");
						}
						else if (rcvAnswer.endsWith("NO")) {
							theGameIsOver = false;
							weAreStillPlaying = false;
						}
						else {
							System.out.println("The answer was incorrect. Try again\n");
						}
						
					}
					
				} // END WHILE(weAreStillPlaying)
			}	// END IF("1")
			
			else if (menuOption.equals("2")) { // History
				System.out.println("\n----- STATISTICS -----");
				
				rcvAnswer = dataIn.readUTF().split(":")[1];
				if (rcvAnswer.equals("INVALID")) {
					System.out.println("You haven't played any matches yet.\n");
				}
				else {
					ClientUtils.showGameInfo(rcvAnswer);				
					rcvAnswer = dataIn.readUTF().split(":")[1]; 
					ClientUtils.showGameStats(rcvAnswer);
				}
			}
			
			
			else if (menuOption.equals("3")) { // Quit
				boolean checkIfLoginAgain = true; // this boolean is used to check the condition of logging in again or not
				
				while (checkIfLoginAgain) {
					System.out.println("Do you want to login with another account? ('yes' or 'no') ");
					System.out.println("[make sure to select 'no' before leaving the app]");
					System.out.print("Answer: ");
					String loginAgainOption = sc.nextLine();
					dataOut.writeUTF("LOGIN_AGAIN:" + loginAgainOption);
					dataOut.flush();
					
					rcvAnswer = dataIn.readUTF();
					
					if (rcvAnswer.endsWith("NO")) {
						System.out.println("\nThe game has been closed.");
						weWantToStayInTheApp = false;
						checkIfLoginAgain = false; 
						dataOut.close();
						dataIn.close();
						socket.close();
					}
					else if (rcvAnswer.endsWith("YES")) {
						System.out.println("\nGet ready to login again...\n");
						checkIfLoginAgain = false; 
						weAreLoggingIn = true;  // so the cycle in the beginning can restart
					}
					else {
						System.out.println("The answer was incorrect. Try again\n");
					}
				}
			}
			
		}	
		sc.close();	
		
	} // end public void main
	

	
}
