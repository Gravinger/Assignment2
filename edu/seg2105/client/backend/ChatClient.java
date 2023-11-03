// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  int loginId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(int loginId, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if (message.startsWith("#")) {
    		handleCommand(message);
    	}
    	
    	else {
    		sendToServer(message);	
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand (String line) {
	  String[] command = line.split(" ");
	  
	  if (command[0].equals("#quit")) {
		  quit();
	  }
	  
	  else if (command[0].equals("#logoff")) {
		  if (!isConnected()) {
			  clientUI.display("You are already logged off.");
		  }
		  
		  else {
			  try {
				  closeConnection();
			  } 
			  catch (IOException e) {
				  clientUI.display("You are already logged off.");
			  }  
		  }
	  }
	  
	  else if (command[0].equals("#sethost")) { 
		  if (!isConnected()) {
			  setHost(command[1]);
		  }
		  
		  else {
			  clientUI.display("You cannot change the host while logged in.");		  
		  }
	  }
	  
	  else if (command[0].equals("#setport")) { 
		  if (!isConnected()) {
			  try {
				  setPort(Integer.parseInt(command[1]));
			  }
			  
			  catch(NumberFormatException e) {
				  clientUI.display("Invalid port.");
			  }
		  }
		  
		  else {
			  clientUI.display("You cannot change port while logged in.");
		  }
	  }
	  
	  else if (command[0].equals("#login")) { 
		  if (isConnected()) {
			  clientUI.display("You are already logged in.");
		  }
		  
		  try {
			  openConnection();
		  }
		  catch (IOException e) {
			  clientUI.display("Failed to connect.");
		  }
	  }
	  
	  else if (command[0].equals("#gethost")) { 
		  //MIGHT HAVE TO MAKE SURE THEY ARE CONNECTED. NOT SURE YET
		  clientUI.display("Host: " + getHost());    
	  }
	  
	  else if (command[0].equals("#getport")) { 
		  //MIGHT HAVE TO MAKE SURE THEY ARE CONNECTED. NOT SURE YET
		  clientUI.display("Port: " + getPort());
	  }
	  
	  else {
		  clientUI.display("This is an invalid command.");
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
   * Implements the hook method called each time an exception is thrown by the client's
   * thread that is waiting for messages from the server. The method may be
   * overridden by subclasses.
   * 
   * @param exception
   *            the exception raised.
   */
  @Override
  protected void connectionException(Exception exception) {
	  clientUI.display("The server has shut down");
	  System.exit(0);;
  }
  
  protected void connectionEstablished() {	  
	  try {
		  sendToServer("#login " + loginId);
	  }
	  
	  catch(IOException e)
	  {
		  clientUI.display("Could not send message to server.  Terminating client.");
		  quit();
	  }
  }
  
  /**
   * Implements the hook method called after the connection has been closed. The default
   * implementation does nothing. The method may be overriden by subclasses to
   * perform special processing such as cleaning up and terminating, or
   * attempting to reconnect.
   */
  protected void connectionClosed() {
	  clientUI.display("Connection closed");
  }
}
//End of ChatClient class
