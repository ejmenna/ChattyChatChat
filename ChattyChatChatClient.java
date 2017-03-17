import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;



public class ChattyChatChatClient {
	//this is another test comment

	public static String username = "anonymous";
	public static Socket socket;
	public static String server;
	public static int port;


	public static void main (String[] args) throws UnknownHostException, IOException
	{
		if (args.length != 2)
			System.out.println("Please indicate a port and server.");
		else
		{
			server = args[0];
			port = Integer.parseInt(args[1]);
			socket = new Socket(getServer(), getPort());
			Input receive = new Input(getSocket());
			Thread receiving = new Thread(receive);
			receiving.start();
			Output send = new Output(getSocket());
			Thread sending = new Thread(send);
			sending.start();
		}
	}


	public static Socket getSocket() { return socket; }

	public static String getServer() { return server; }

	public static int getPort() { return port; }
}

//output class for the client to send commands and messages
	class Output implements Runnable {
		private Socket socket;
		private PrintWriter printer;

		//sets socket 
		public Output(Socket socket){ setSocket(socket); }

		public Socket getSocket() { return socket; }

		public void setSocket(Socket s) { this.socket = s; }

		public PrintWriter getWriter() { return writer; }

		public void run() {
			try {
			String delim = " ";
			String[] outArray;
			writer = new PrintWriter(getSocket().getOutputStream(), true);

			while (true) {
				BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
				String out = buff.readLine();
				outArray = out.split(delim);

				if (out.equals("/quit")) {
					System.exit(1);
				}

				else if (outArray[0].equals("/nick")) {
					ChattyChatChatClient.username = outArray[1];
					getWriter().println(out); //should it print here?
					getWriter().flush();
				} 

				else {
					getWriter().println(ChattyChatChatClient.username + ": " + out);
					getWriter().flush();
				}
			}
		} catch (IOException e) { }
	}
}

class Input implements Runnable {
	private Socket socket;
	private BufferedReader buffer;

	public Input(Socket sock) {
		setSocket(sock);
	}

	public void setSocket(Socket s) { this.socket = s; }
	
	public Socket getSocket() { return socket; }
	
	public BufferedReader getBuffer() { return buffer; }

	public void run() {
		try {
			buffer = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
			String inp = "";
			while((inp = getBuffer().readLine()) != null){
				System.out.println(ChattyChatChatClient.username + ": " + inp);
			}
		} catch (IOException e) { }
	}
}
