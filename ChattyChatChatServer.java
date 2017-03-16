import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;



public class ChattyChatChatServer {

//initialize the array list of users as sockets, and the arraylist of names. 
//have the port be set to 0 and use a set port function
	public static ArrayList<Socket> users = new ArrayList<Socket>();
	public static ArrayList<String> usernames = new ArrayList<String>();
	public static int port = 0;
	
	public static void setPort(int inputPort) {
		port = inputPort;
	}
	
	public static int getPort() {
		return port;
	}
	
	//function controlServer controls Server.
	public void Control(String message, String username, int port) {
	try 
	{
		
		String delimiter = " ";
		String[] messageArray;
		
		//split the message and set it into the messageArray using the space delimiters.
		messageArray = message.split(delimiter, 3);
		
		for (int x = 0; x < users.size(); x++)
		{
			if ((username != null) && users.get(x).getPort() == port)
			{
				usernames.set(x, username);
			}
			
			else if (message.startsWith("/dm "))
			{
				if (usernames.get(x).equals(messageArray[1]))
				{
					PrintWriter write = new PrintWriter(users.get(x).getOutputStream());
					write.println(messageArray[2]);
					write.flush();
				}
				else
				{
					PrintWriter write = new PrintWriter(users.get(x).getOutputStream());
					write.flush();
				}
			}
			else
			{
				PrintWriter write = new PrintWriter(users.get(x).getOutputStream());
				write.println(message);
				write.flush();
			}
		}
		

	}//end try loop
	catch (IOException e)
		{
		
		}
	} // end function
	
	//function main inputs the port number from terminal and opens the server.
	public static void main (String[] args) throws Exception {
		int portNumber = Integer.parseInt(args[0]);
		setPort(portNumber);
		
		ServerSocket server = new ServerSocket(getPort());
		
		System.out.println("Test. The server is open and running.");
		
		while (true) {
			try{
			//add new users as Sockets.
			Socket socket = server.accept();
			users.add(socket);
			usernames.add("NewUser");
			Protocol chatter = new Protocol(socket);
			Thread chatting = new Thread(chatter);
			chatting.start();
			}
			catch (IOException e){
				
			}
		}
		

	}
	

}
	
	class Protocol implements Runnable {
		private Socket socket;
		
		//no-purpose constructor
		public Protocol()
		{
		}

		//constructor that takes a socket as a value
		public Protocol(Socket inputSocket) {
			socket = inputSocket;
		}

		public void run() {
			try {
				Scanner scanner = new Scanner(socket.getInputStream());
				ChattyChatChatServer outputServer = new ChattyChatChatServer();

				while (true) {
					if (scanner.hasNext()) {
						String delimiter = " ";
						String[] inputArray;
						String input = scanner.nextLine();
						String name = null;
						inputArray = input.split(delimiter,3);
						
						//sets the nickname if the user creates it
						if (inputArray[0].equals("/nick")) {
							name = inputArray[1];
						} 
						outputServer.Control(name, input, socket.getPort());
					}
					
					
				}
				
			} catch (IOException e) {
				
			}

		}
	
		}
	

