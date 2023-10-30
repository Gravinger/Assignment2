package server.ui;

import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole implements ChatIF {

	EchoServer server;
	
	Scanner fromConsole;
	
	public ServerConsole (int port) {
		server = new EchoServer(port, this);
		
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	}
	
	@Override
	public void display(String message) {
	    System.out.println(message);

	}
	
	/**
	 * This method waits for input from the console.  Once it is 
	 * received, it sends it to the client's message handler.
	 */
	public void accept() 
	{
		try
		{

			String message;

			while (true) 
			{
				message = fromConsole.nextLine();
				server.handleMessageFromServerUI(message);
			}
		} 
		catch (Exception ex) 
		{
			System.out.println
			("Unexpected error while reading from console!");
		}
	}
	
	
	public void startListening() {
	    try 
	    {
	    	server.listen(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	    	System.out.println("ERROR - Could not listen for clients!");
	    }
	}

	//Class methods ***************************************************
	  
	/**
	 *
	 * @param args[0] The port number to listen on.  Defaults to 5555 
	 *          if no argument is entered.
	 */
	public static void main(String[] args) 
	{
		int port = 0; //Port to listen on

		try
		{
			port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	    	port = 5555; //Set port to 5555
	    }
		
	    ServerConsole serverChat = new ServerConsole(port);
	    serverChat.startListening();
	    serverChat.accept();
	  }
}
