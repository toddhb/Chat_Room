/*Chat Application
author: Todd Brochu
Portland State University
CS300 Spring 2014*/

package chatApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;

public class Server {
	protected static DateFormat dateFormat;
	protected static Date date;

	FileInputStream credentials;
	FileInputStream blocked_users;
	FileInputStream active_users;

	OutputStream credentials_out;
	Vector<String> listVector;
	public static Vector<ClientHandler> clientVector = null;
	Vector<String> clients;

	static final int port = 8089;
	private ServerSocket serverSocket;

	static Properties prop_credentials;
	static Properties prop_blocked_users;
	static Properties prop_active_users;

	public static Server server;

	public static void main(String[] args) throws IOException {
		server = new Server();
	}

	public Server() throws IOException {
		clientVector = new Vector<ClientHandler>();
		prop_credentials = new Properties();
		// the ASCII database of usernames and passwords
		
		prop_blocked_users = new Properties();
		// the ASCII database of blocked accounts
		
		prop_active_users = new Properties();
		// the ASCII database of active accounts
		initializeTheActiveUsersPropertiesFile();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		date = new Date();

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Starting server...");
			while (true) {
				// blocks until a connection occurs
				Socket socket = serverSocket.accept();

				// Manage the threads
				ClientHandler ch = new ClientHandler(socket);
				System.out.println("New Client: " + socket);
				// print to the server's console

				// add the new client to the vector of active client threads
				clientVector.add(ch);
				
				ch.start();
			}

		} catch (IOException e) {
			System.out.println("IOException: " + e);
			e.printStackTrace();
		}
	}

	// A nested class within Server for managing the client threads
	public static class ClientHandler extends Thread implements Runnable {
		private Socket connection;
		private BufferedReader reader;
		private PrintWriter writer;
		private String user_name;

		public ClientHandler(Socket conn) throws IOException {
			this.connection = conn;

			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			writer = new PrintWriter(connection.getOutputStream(), true);
		}

		/**
	     * Runs the management of the threads
	     * 
	     * @param   NONE
	     * @return  NONE 
	     */
		public void run() {
			String line = "";
	         
	        try
	        {
	            //Send welcome message to client
	        	date = new Date();
	            getWriter().println((dateFormat.format(date)) + "\nWelcome to the Chat Room.\n");
	            
	            user_name = reader.readLine();
	            line = user_name + "œœ has logged in. ***";
	            sendAnnouncement(line);
	            
	          //add the new client to the array of active users
				//addNewClientToActiveUsersList(user_name);
	 
	            //Now start reading input from client
	            while(!connection.isInputShutdown()){
	            	line = reader.readLine();
	            	if (reader.equals("EXIT")) break;
	            	System.out.println("Chat: " + line);//print to the server's console
	            	
	            	sendMessage(line);
	            }

	            //client has disconnected
	            line = reader.readLine();
	            sendAnnouncement(line);
	            
	            /*
	             * Clean up. Set the current thread variable to null so that a new client
	             * could be accepted by the server.
	             */
	            for (int i = 0; i < clientVector.size(); i++) {
	              if (clientVector.get(i) == this) {
	                clientVector.set(i, null);
	              }
	            }
	            
	          //remove the outgoing client from the array of active users
				//removeClientFromActiveUsersList(user_name);
	             
	            /*
	             * Close the output stream, close the input stream, close the socket.
	             */
	            reader.close();
	            writer.close();
	            connection.close();
	        }
	       
	        catch (IOException e)
	        {
	            System.out.println("IOException on socket : " + e);
	            e.printStackTrace();
	        }		
		}
		
		/**
	     * Getter method for the Thread's PrintWriter
	     * 
	     * @param   NONE
	     * @return  the writer 
	     */
		public PrintWriter getWriter() {
			return this.writer;
		}

		/**
		 * Setter method for the Thread's PrintWriter
		 * 
		 * @param	writer the writer to set
		 * @return 	NONE
		 */
		public void setWriter(PrintWriter writer) {
			this.writer = writer;
		}


		/**
	     * Sends a message to a single client
	     * 
	     * @param   message    The message to be sent.
	     * @return  NONE 
	     */
		static void sendMessage(String message) {
			Vector<ClientHandler> chV = (getClientVector());
			String[] splitMessage = message.split("œ");

			if (splitMessage[1].isEmpty())
				sendAnnouncement(message);

			for (ClientHandler client : chV) {
				if (client.getUserName().contentEquals(splitMessage[1])) {
					// find the recipient in the vector

					// the first section represents the user name, the second is
					// the recipient, the third is the message
					client.getWriter().println(
							splitMessage[0] + splitMessage[2]);
				}
  }
	    }

		/**
	     * Sends a message to all active users except the sender
	     * 
	     * @param   message    The message to be sent.
	     * @return  NONE 
	     */
		static void sendAnnouncement(String message) {
			Vector<ClientHandler> chV = (getClientVector());
			String[] splitMessage = message.split("œ");

			for (ClientHandler client : chV) {
				if (!client.getUserName().equals(splitMessage[0])) {
					// don't send announcement to the sender

					// the first section represents the user name, the third is
					// the message
					client.getWriter().println(
							splitMessage[0] + splitMessage[2]);
				}
			}
		}
	    
		/**
	     * Getter method for the user name
	     * 
	     * @param   NONE
	     * @return  The user name 
	     */
		public String getUserName(){
	    	return this.user_name;
	    }
	}
	
	/**
     * Adds an account to the properties file of registered users
     * and to the GUIClient's JList
     * 
     * @param   user_name   The user name of the account to be added
     * @param   password	The password to be added.
     * @return  NONE 
     */
	public void addToCredentials(String user_name, String password) {
		File credentials = new File("credentials.properties");
		FileWriter filewriter = null;

		try {
			// Below constructor argument decides whether to append or override
			filewriter = new FileWriter(credentials, true);
			filewriter.write(user_name + "=" + password);
			filewriter.write(System.lineSeparator());

		} catch (IOException e) {
			System.out.println("IO Exception at Server.addToCredentials");
			e.printStackTrace();
		} finally {
			try {
				filewriter.close();
			} catch (IOException e) {
				System.out.println("IOException: " + e);
				e.printStackTrace();
			}
		}
	}

	/**
     * Removes an account from the properties file of registered users
     * 
     * @param   user_name    The user name of the account to be removed.
     * @return  NONE 
     */
	public void deleteFromCredentials(String user_name) throws IOException {
		credentials = new FileInputStream("credentials.properties");
		prop_credentials.load(credentials);
		prop_credentials.remove(user_name);

		// overwrite the existing credentials file
		PrintWriter printWriter = new PrintWriter("credentials.properties");
		for (Entry<Object, Object> e : prop_credentials.entrySet()) {
			printWriter.println(e);
		}
		printWriter.close();
	}
	
	/**
     * Getter method for the server
     * 
     * @param   NONE
     * @return  the Server object 
     */
	public static Server getServer() {
		return server;
	}

	/**
     * Getter method for the vector of client threads
     * 
     * @param   NONE
     * @return  the vector of client threads 
     */
	public static Vector<ClientHandler> getClientVector() {
		return clientVector;
	}
	
	/**
     * Creates a properties file to store a list of active users
     * 
     * @param   NONE
     * @return  NONE 
     */
	private void initializeTheActiveUsersPropertiesFile() throws IOException {
		FileOutputStream fos = new FileOutputStream("active_users.properties");
		fos.close();
	}

	/**
     * Checks a given user name and reports if it is in the properties file
     * of registered users
     * 
     * @param   user_name    The name to be checked.
     * @return  Whether or not it is in the properties file 
     */
	public boolean nameIsTaken(String user_name) {
		boolean isTaken = false;
		prop_credentials = new Properties();
		// the ASCII database of usernames and passwords

		try {
			credentials = new FileInputStream("credentials.properties");
			prop_credentials.load(credentials);

			if (prop_credentials.getProperty(user_name) != null) {
				isTaken = true;
			}

		} catch (IOException ex) {
			System.out.println("IOException: " + ex);
			ex.printStackTrace();
		}

		return isTaken;
	}

	/**
     * Instantiates an object of the GUIFrontEnd class after an active
     * user logs out
     * 
     * @param   NONE
     * @return  NONE 
     */
	public void openGUIFrontEnd() {
		new GUIFrontEnd().setVisible(true);// open the introductory dialog
	}
}