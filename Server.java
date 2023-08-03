package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class Server
{
	public static void main(String[] args) {
		
		int id=1;
		User user1 = new User("jorge","jorge123");
		User user2 = new User("marta","marta123");
		User user3 = new User("maria","maria123");
		ArrayList<User> userList = new ArrayList<User>(Arrays.asList(user1,user2,user3));
		
		try {
			@SuppressWarnings("resource")
			ServerSocket listen = new ServerSocket(12345);
			while (true) {
				Socket clientSocket = listen.accept();
				Thread c = new Thread(new ConnectionHandler(clientSocket,id,userList));
				c.start();
				id++;
			}
		}
		catch (Exception e) {
			System.out.println("Server terminated. Error:");
			e.printStackTrace();
		}
		
	}

}