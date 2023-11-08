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
		  try {
			  close();
		  }
		  
		  catch (IOException e) {
			  System.out.println("Failed to close server.");
		  }
		  
		  System.exit(0);
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
	  serverUI.display("Message received: " + msg + " from " + client.getInfo("loginID"));
	  
	  if (msg.toString().startsWith("#disconnect")) {
		  clientDisconnected(client);
	  }
	  
	  else if (msg.toString().startsWith("#login")) {		  
		  if (client.getInfo("loginID") == null) {
			  String[] loginCommand = msg.toString().split(" ");
			  
			  client.setInfo("loginID", loginCommand[1]);  
			  serverUI.display(client.getInfo("loginID") + " has logged on.");
			  this.sendToAllClients(client.getInfo("loginID") + " has logged on.");
		  }
		  
		  else {			  
			  try {
				  client.sendToClient("You have already logged in and can not again.");
			  }
			  
			  catch (IOException e) {
				  serverUI.display("Could not display error to client.");
			  }
			  
			  try {
				  client.close();
			  }
			  
			  catch (IOException e) {
				  serverUI.display("Could not close client connection.");
			  }
			  
		  }
	  }
	  
	  else {
		  this.sendToAllClients(client.getInfo("loginID") + "> " + msg);  
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
	  String conMsg = "A new client has connected to the server.";
	  System.out.println(conMsg);
  }
  
  /**
   * Implements the hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  String discMsg = client.getInfo("loginID") + " has disconnected.";
	  System.out.println(discMsg);
	  this.sendToAllClients(discMsg);
  }
}
//End of EchoServer class
