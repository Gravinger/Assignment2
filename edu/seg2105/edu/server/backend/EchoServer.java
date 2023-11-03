package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF serverUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * @param serverUI The interface type variable.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message)
  {
	  if (message.startsWith("#")) {
		  handleCommand(message);
	  }
	  
	  else {
		  serverUI.display("SERVER MSG> " + message);
		  this.sendToAllClients("SERVER MSG> " + message);
	  }
  }
  
  private void handleCommand (String line) {
	  String[] command = line.split(" ");
	  
	  if (command[0].equals("#quit")) {
		  //NOT SURE ABOUT THIS ONE
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  //DON'T FORGET
	  }
	  
	  else if (command[0].equals("#stop")) {
		  stopListening();
	  }
	  
	  else if (command[0].equals("#close")) { 
		  try {
			  close();
		  }
		  
		  catch (IOException e) {
			  System.out.println("Failed to close server.");
		  }
	  }
	  
	  else if (command[0].equals("#setport")) { 
		  //NOT SURE WHAT TO MAKE THIS RIGHT NOW. MIGHT HAVE TO IMPLEMENT THE serverClosed()
		  //METHOD
		  if (!isListening()) {
			  try {
				  setPort(Integer.parseInt(command[1]));
			  }
			  
			  catch(NumberFormatException e) {
				  serverUI.display("Invalid port.");
			  }
		  }
		  
		  else {
			  serverUI.display("You cannot change the port while the server is running.");
		  }
	  }
	  
	  else if (command[0].equals("#start")) { 
		  //NOT SURE IF THE IF STATEMENT IS NEEDED BECAUSE THE METHOD DOES NOTHING IF THE
		  //SERVER IS ALREADY LISTENING
		  if (!isListening()) {
			  try {
				  listen();
			  }
			  
			  catch (IOException e) {
				  System.out.println("Failed to start listening.");
			  }
		  }
	  }
	  
	  else if (command[0].equals("#getport")) { 
		  //MIGHT HAVE TO MAKE SURE THEY ARE CONNECTED. DON'T THINK SO
		  serverUI.display("Port: " + getPort());
	  }
	  
	  else {
		  serverUI.display("This is an invalid command.");
	  }
  }
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  if (msg.toString().startsWith("#login")) {
		  System.out.println("Message received: " + msg + " from " + client);
		  String[] loginCommand = msg.toString().split(" ");
		  
		  client.setInfo("loginID", loginCommand[1]);
	  }
	  
	  else {
		  this.sendToAllClients(client.getInfo("loginID") + ": " + msg);  
	  }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * Implements the hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
	  String conMsg = "A client has connected.";
	  System.out.println(conMsg);
	  this.sendToAllClients(conMsg);
  }
  
  /**
   * Implements the hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  String discMsg = "A client has disconnected.";
	  System.out.println(discMsg);
	  this.sendToAllClients(discMsg);
  }
}
//End of EchoServer class
