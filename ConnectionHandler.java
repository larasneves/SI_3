package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class ConnectionHandler extends Thread
{
	private Socket clientSocket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	@SuppressWarnings("unused")
	private int id;
	private ArrayList<User> userList;


	public ConnectionHandler(Socket sock, int id, ArrayList<User> userList) 
	{
		this.clientSocket = sock;
		this.id=id;
		this.userList = userList;
		try {
			this.dataIn = new DataInputStream(clientSocket.getInputStream());
			this.dataOut = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void run()
	{
		try {
			
			String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD"};
			int N=0;
			int M=0;
			int boatNumber=0;
			int shotNumber=0;
			int clientNumberIndex=0;
			int clientLetterIndex=0;
			
			boolean theClientCanPlay = true;
			boolean weAreStillPlaying;
			boolean weAreLoggingIn = true;
			
			User currentUser = null;
			
			boolean weWantToStayInTheApp = true;
			while (weWantToStayInTheApp) {
	
				while (weAreLoggingIn) {	
					currentUser = null;
					String rcvAnswer = dataIn.readUTF();			
					if (rcvAnswer.startsWith("LOGIN")) {	
						String[] loginInfoList = rcvAnswer.split(":")[1].split("/");
				        
				        for (User user: this.userList) {
				        	if (user.getUsername().equals(loginInfoList[0]) && user.getPassword().equals(loginInfoList[1])) {
				        		currentUser = user;
				        		//break;
				        	}
				        }
				        
				        if (currentUser == null) {
				        	System.out.println("Unable to find user with those credentials");
				            dataOut.writeUTF("LOGIN:INVALID_USER_NOT_FOUND");
				            dataOut.flush();
				        }
				        else {
				        	if (currentUser.getLoggedInState() == true) {
				        		 System.out.println(currentUser.getUsername() + ": This account is already being used elsewhere. Try again.\n");
						         dataOut.writeUTF("LOGIN:INVALID_USER_ALREADY_LOGGED");
						         dataOut.flush();
				        	}
				        	else {
				        		weAreLoggingIn = false;
								currentUser.setLoggedInState(true);
								dataOut.writeUTF("LOGIN:VALID");
								dataOut.flush();
				        	}
				        }	       
					}
					else {
						dataOut.writeUTF("LOGIN:INVALID");
				        dataOut.flush();
				        System.out.println("LOGIN: There was an error during communication");
					}
				} // end while(weAreLoggingIn)
			
				System.out.println(currentUser.getUsername() + ": Login accepted!");
				
				
				
				weAreStillPlaying = true;
				String menuOption = "";
				
				boolean weAreInTheMenu = true;
				while (weAreInTheMenu) {
					String rcvAnswer = dataIn.readUTF();
					
					System.out.println(currentUser.getUsername() +": The menu answer was: " + rcvAnswer);
					if (rcvAnswer.startsWith("MENU")) {
						menuOption = rcvAnswer.split(":")[1];						
						if (menuOption.equals("1") || menuOption.equals("2") || menuOption.equals("3")) {
							weAreInTheMenu = false;
							dataOut.writeUTF("MENU:VALID"); 
							dataOut.flush();
						}		
						else {
							dataOut.writeUTF("MENU:INVALID");
							dataOut.flush();
						}
					}
					else {
						dataOut.writeUTF("MENU:INVALID");
						dataOut.flush();
					}
				}
				
				if (menuOption.equals("1")) { // New Game
					
					while (weAreStillPlaying) {		
						System.out.println(currentUser.getUsername() +" is about to start playing");
						boolean weAreChoosingN = true; // Choose the number of lines
						while (weAreChoosingN) { 
							String rcvAnswer = dataIn.readUTF();
							if (rcvAnswer.startsWith("NLINES")) {		
								boolean isInt = ServerUtils.isInteger(rcvAnswer.split(":")[1]);
								if (isInt) {		
									N = Integer.valueOf(rcvAnswer.split(":")[1]);
									if (N>30 || N<15) { // Answer is not OK
										dataOut.writeUTF("NLINES:INVALID_OUT_OF_BOUNDS");
										dataOut.flush();
									}
									else { // Answer is OK
										dataOut.writeUTF("NLINES:VALID");
										dataOut.flush();
										weAreChoosingN = false;
									}
								}
								else {
									dataOut.writeUTF("NLINES:INVALID_NOT_INTEGER");
									dataOut.flush();
								}
							}
							else {
								dataOut.writeUTF("NLINES:INVALID");
								dataOut.flush();
							}
						}
						
						boolean weAreChoosingM = true; // Choose the number of columns				
						while (weAreChoosingM) { 
							String rcvAnswer = dataIn.readUTF();
							if (rcvAnswer.startsWith("NCOL")) {					
								boolean isInt = ServerUtils.isInteger(rcvAnswer.split(":")[1]);
								if (isInt) {		
									M = Integer.valueOf(rcvAnswer.split(":")[1]);
									if (M>30 || M<15) {// Answer is not OK
										dataOut.writeUTF("NCOL:INVALID_OUT_OF_BOUNDS");
										dataOut.flush();
									}
									else { // Answer is OK
										dataOut.writeUTF("NCOL:VALID");
										dataOut.flush();
										weAreChoosingM = false;
									}
								}
								else {
									dataOut.writeUTF("NCOL:INVALID_NOT_INTEGER");
									dataOut.flush();
								}
							}
							else {
								dataOut.writeUTF("NCOL:INVALID");
								dataOut.flush();
							}
						}
	
						
						boolean weAreChoosingBoats = true;
						while (weAreChoosingBoats) { // Choose the number of boats
							String rcvAnswer = dataIn.readUTF();
							if (rcvAnswer.startsWith("NBOAT")) {
								boolean isInt = ServerUtils.isInteger(rcvAnswer.split(":")[1]);
								if (isInt) {
									boatNumber = Integer.valueOf(rcvAnswer.split(":")[1]);
									if (boatNumber < 3) { // Answer is not OK
										dataOut.writeUTF("NBOAT:INVALID_OUT_OF_BOUNDS");
										dataOut.flush();			
									}
									else { // Answer is OK
										dataOut.writeUTF("NBOAT:VALID");
										dataOut.flush();
										weAreChoosingBoats = false;
									}
								}
								else {
									dataOut.writeUTF("NBOAT:INVALID_NOT_INTEGER");
								}
							}
							else {
								dataOut.writeUTF("NBOAT:INVALID"); // Comunication error
								dataOut.flush();
							}
						}
						
						String[][] boatMatrix = ServerUtils.createBoatMatrix(N, M, boatNumber); 
						int[][] serverGameMatrix = ServerUtils.createServerGameMatrix(N,M);
		
						if (boatMatrix[0][0].equals("false")) {
							theClientCanPlay = false;
							dataOut.writeUTF("CAN_PLAY:INVALID");
						}
						else {
							theClientCanPlay = true;
							dataOut.writeUTF("CAN_PLAY:VALID");
							
							// Print boatMatrix
							/*
							for (int line = 0; line<N; line++) { 
								for (int col = 0; col<M; col++) {
									System.out.print(boatMatrix[line][col]);
								}
								System.out.println("");
							}
							System.out.println("");
							*/
							
						}
						
						
						if (theClientCanPlay) {
							
							boolean weAreChoosingShots = true;
							while (weAreChoosingShots) { 
								String rcvAnswer = dataIn.readUTF();
								if (rcvAnswer.startsWith("NSHOT")) {
									boolean isInt = ServerUtils.isInteger(rcvAnswer.split(":")[1]);
									if (isInt) {
										shotNumber = Integer.valueOf(rcvAnswer.split(":")[1]);
										if (shotNumber > (N*M) || shotNumber < ServerUtils.numberOfSpacesTakenByBoats(boatMatrix,N,M)) { // Answer is not OK
											dataOut.writeUTF("NSHOT:INVALID_OUT_OF_BOUNDS");
											dataOut.flush();
										}
										else { // Answer is OK
											dataOut.writeUTF("NSHOT:VALID");
											dataOut.flush();
											weAreChoosingShots = false;				
										}
									}
									else {
										dataOut.writeUTF("NSHOT:INVALID_NOT_INTEGER");
										dataOut.flush();
									}		
								}
								else { // Comunication error
									dataOut.writeUTF("NSHOT:INVALID");
									dataOut.flush();
								}
									
							}
	
							
							// ------- Client's moves ---------
							boolean theClientIsMakingMoves = true;
							int tries = 0;
							int numberOfCorrectShots = 0;
							
							while (theClientIsMakingMoves) {	
								// Check if game is over
								if (tries == shotNumber) { // LOSS
									theClientIsMakingMoves = false;
									currentUser.addLose();
									currentUser.addGameHistory("Lose",tries);
									currentUser.addTriesPerGame(tries);
									dataOut.writeUTF("GAME:LOSE");
									dataOut.flush();
								}
								else if (numberOfCorrectShots == ServerUtils.numberOfSpacesTakenByBoats(boatMatrix,N,M)) { // WIN
									theClientIsMakingMoves = false;
									currentUser.addWin();
									currentUser.addGameHistory("Win",tries);
									currentUser.addTriesPerGame(tries);
									dataOut.writeUTF("GAME:WIN");
									dataOut.flush();
								}
								else {
									dataOut.writeUTF("GAME:CONTINUE");
									dataOut.flush();
									tries+=1;
									
									boolean weAreChoosingLetter = true;
									while (weAreChoosingLetter) {
										String rcvAnswer = dataIn.readUTF();
										if (rcvAnswer.startsWith("CLIENT_PLAY_LETTER")) {
											String clientLetter = rcvAnswer.split(":")[1];
											clientLetterIndex = Arrays.asList(letters).indexOf(clientLetter);
											if (clientLetter.equals("EXIT") || clientLetter.equals("QUIT")) { // Quit the game
												weAreChoosingLetter = false;
												theClientIsMakingMoves = false;
												currentUser.addLeave();
												currentUser.addGameHistory("Leave",tries-1);
												currentUser.addTriesPerGame(tries-1);											
												dataOut.writeUTF("CLIENT_PLAY_LETTER:EXIT");
											}
											else if (clientLetterIndex == -1 || clientLetterIndex >= M) { // Answer is not OK
												dataOut.writeUTF("CLIENT_PLAY_LETTER:INVALID_LETTER_UNAVAILABLE");
											}
											else { // Answer is OK
												weAreChoosingLetter = false;
												dataOut.writeUTF("CLIENT_PLAY_LETTER:VALID");
											}
										}
										else {
											dataOut.writeUTF("CLIENT_PLAY_LETTER:INVALID"); // Comunication error
										}
									}
									
									boolean weAreChoosingNumber = theClientIsMakingMoves;
									while (weAreChoosingNumber) {
										String rcvAnswer = dataIn.readUTF();
										if (rcvAnswer.startsWith("CLIENT_PLAY_NUMBER")) {
											/////PROBLEMA//////////////////////////////////////////////////////////////////
											boolean isInt = ServerUtils.isInteger(rcvAnswer.split(":")[1]);
											if (rcvAnswer.split(":")[1].equals("EXIT") || rcvAnswer.split(":")[1].equals("QUIT")) { // Quit the game
												weAreChoosingNumber = false;
												theClientIsMakingMoves = false;
												currentUser.addLeave();
												currentUser.addGameHistory("Leave",tries-1);
												currentUser.addTriesPerGame(tries-1);			
												dataOut.writeUTF("CLIENT_PLAY_NUMBER:EXIT");
												dataOut.flush();
											}
											else if (isInt) {
												clientNumberIndex = Integer.valueOf(rcvAnswer.split(":")[1]);
												if (clientNumberIndex >= 1 && clientNumberIndex <= N) { // Answer is OK
													clientNumberIndex-=1;
													weAreChoosingNumber = false;
													dataOut.writeUTF("CLIENT_PLAY_NUMBER:VALID");
													dataOut.flush();
												} 
												else { // Answer is not OK
													dataOut.writeUTF("CLIENT_PLAY_NUMBER:INVALID_OUT_OF_BOUNDS");
													dataOut.flush();
												}
											}
											else {
												dataOut.writeUTF("CLIENT_PLAY_NUMBER:INVALID_NOT_INTEGER");
												dataOut.flush();
											}
										}
										else { // Comunication error
											dataOut.writeUTF("CLIENT_PLAY_NUMBER:INVALID");
										}
									}
									
									if (theClientIsMakingMoves) { 
										if (serverGameMatrix[clientNumberIndex][clientLetterIndex] == 0) {
											serverGameMatrix[clientNumberIndex][clientLetterIndex] = 1;
											if (boatMatrix[clientNumberIndex][clientLetterIndex].equals("1")) {
												numberOfCorrectShots+=1;
												dataOut.writeUTF("MATRIX:HIT");
												dataOut.flush();
											}
											else {
												dataOut.writeUTF("MATRIX:MISS");
												dataOut.flush();
											}
										}
										else {
											tries-=1;
											dataOut.writeUTF("MATRIX:INVALID_POSITION_ALREADY_IN_USE");
											dataOut.flush();
										}
									}
									
								}
								
								
							} // end while(theClientIsMakingMoves)
						}
						
						boolean theGameIsOver = true;
						while (theGameIsOver && theClientCanPlay) {
							String rcvAnswer = dataIn.readUTF(); 
							if (rcvAnswer.startsWith("PLAY_AGAIN")) {
								String clientAnswer = rcvAnswer.split(":")[1];
								if (clientAnswer.startsWith("yes")) { // Client WANTS to play again 
									theGameIsOver=false; 
									dataOut.writeUTF("PLAY_AGAIN:YES");	
								}	
								else if (clientAnswer.startsWith("no")) { // Client DOES NOT want to play again
									theGameIsOver=false; 
									weAreStillPlaying = false;
									dataOut.writeUTF("PLAY_AGAIN:NO");
								}
								else {
									dataOut.writeUTF("PLAY_AGAIN:INVALID"); 
								}
							}
							else {
								dataOut.writeUTF("PLAY_AGAIN:INVALID"); // Error in communication
							} // End While GameIsOver
						} 
					} // END WHILE(weAreStillPlaying)		
				} // END IF("1")
				
				else if (menuOption.equals("2")) { // History	
					if (currentUser.getgameHistory().size() == 0) {
						dataOut.writeUTF("STATS:INVALID");
						dataOut.flush();
					}
					else {
						String finalString = ServerUtils.getGameInfo(currentUser);
						dataOut.writeUTF("STATS:" + finalString);
						dataOut.flush();
						
						String gameVariables = ServerUtils.getGameStats(currentUser);
						dataOut.writeUTF("STATS:" + gameVariables);
						dataOut.flush();
					}
				}
				
				else if (menuOption.equals("3")) { // Quit
					boolean weWantToLoginAgain = true;
					while (weWantToLoginAgain) {
						String rcvAnswer = dataIn.readUTF();
						if (rcvAnswer.startsWith("LOGIN_AGAIN")) {
							String loginAgainOption = rcvAnswer.split(":")[1];
							if (loginAgainOption.startsWith("yes")) {
								weWantToLoginAgain = false;
								weAreLoggingIn = true;
								currentUser.setLoggedInState(false); 
								dataOut.writeUTF("LOGIN_AGAIN:YES");
							}
							else if (loginAgainOption.startsWith("no")) {
								weWantToLoginAgain = false;
								dataOut.writeUTF("LOGIN_AGAIN:NO");
								System.out.println(currentUser.getUsername() + " left the game.");
								weWantToStayInTheApp = false;
								currentUser.setLoggedInState(false); 
								dataOut.close();
								dataIn.close();
								clientSocket.close();
							}
							else {
								dataOut.writeUTF("LOGIN_AGAIN:INVALID"); 
							}
						}
						else {
							dataOut.writeUTF("LOGIN_AGAIN:INVALID");
						}
					}
				}
					
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	


}