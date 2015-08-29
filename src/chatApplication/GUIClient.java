/*Chat Application
author: Todd Brochu
Portland State University
CS300 Spring 2014*/

package chatApplication;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.MatteBorder;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class GUIClient extends JFrame {
	private static final long serialVersionUID = 1L;

	private static DefaultListModel<String> model;
	static Vector<String> listVector;
	static String user_name;
	String message;
	String recipient;

	static final int port = 8089;

	ArrayList<String> activityLog;
	
	static FileInputStream credentials;
	static Properties prop_credentials;

	static FileInputStream active_users;
	static Properties prop_active_users;

	static FileInputStream blocked_users;
	static Properties prop_blocked_users;

	DataInputStream dis;
	DataOutputStream dos;

	private BufferedReader reader;
	private PrintWriter writer;

	JButton btnSendToActive, btnSend;
	JLabel lblActiveUsers;
	JList<String> jlistActiveUsers;
	JMenuBar menuBar;
	JPanel userPanel;
	// JScrollPane activeUsersScrollPane, compositionScrollPane,
	// threadScrollPane;
	JTextArea txtareaComposition;
	static JTextArea txtareaThread;

	private JTextField textFieldRecipient;
	private JLabel lblRecipient;
	private JLabel lblMessage;
	private JMenu mnOptions, mnSupport;
	private JMenuItem mntmLogOut, mntmOpenChatLog, mntmChangePassword,
			mntmChangeUsername, mntmOpenUserDatabase, mntmBlockAnAccount,
			mntmDeleteAnAccount, mntmAbout;
	private Socket clientSocket;

	public GUIClient(String un) throws UnknownHostException, IOException {
		setUserName(un);

		activityLog = new ArrayList<String>();
		listVector = new Vector<String>();
		
		model = new DefaultListModel<String>();
		
		prop_active_users = new Properties();
		// the ASCII database of active users
		
		prop_credentials = new Properties();
		// the ASCII database of usernames and passwords
		
		prop_blocked_users = new Properties();
		// the ASCII database of blocked accounts

		addNewClientToActiveUsersList(un);
		seedTheJListOfUsers();
		initComponents(un);
		join();
	}

	/**
     * call the GUI which displays the log of a client's chat activity
     * 
     * @param   NONE
     * @return  NONE 
     */
	public void accessLog() {
		String header = "Chat Activity";
		try {
			new DisplayFileContentsGUI(header, activityLog, user_name)
					.setVisible(true);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			e.printStackTrace();
		}
	}

	/**
     * Appends a new client to the properties file of active users
     * 
     * @param   user_name   The client to be added.
     * @return  NONE 
     */
	public void addNewClientToActiveUsersList(String user_name) {
		File active_users = new File("active_users.properties");
		FileWriter filewriter = null;

		try {
			// Below constructor argument decides whether to append or override
			filewriter = new FileWriter(active_users, true);
			filewriter.write(user_name);
			filewriter.write(System.lineSeparator());

		} catch (IOException e) {
			System.out.println("IO Exception: " + e);
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
     * Append a new client to properties file of registered users
     * 
     * @param   user_name   The user name of the client to be added.
     * @param   password	The password of the client to be added.
     * @return  NONE 
     */
	public static void addToCredentials(String user_name, String password) {
		File credentials = new File("credentials.properties");
		FileWriter filewriter = null;

		try {
			// Below constructor argument decides whether to append or override
			filewriter = new FileWriter(credentials, true);
			filewriter.write(user_name + "=" + password);
			filewriter.write(System.lineSeparator());

		} catch (IOException e) {
			System.out.println("IOException: " + e);
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
     * Remove a client from the properties file of registered users.
     * 
     * @param   user_name    The name of the client to be removed.
     * @return  NONE 
     */
	public static void deleteFromCredentials(String user_name)
			throws IOException {
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
     * Get the password of the client
     * 
     * @param   NONE    

     * @return  The password of the client 
     */
	public String getPassword() {
		FileInputStream credentials;
		Properties prop_credentials = new Properties();
		// the ASCII database of usernames and passwords
		String password = null;

		try {
			credentials = new FileInputStream("credentials.properties");
			prop_credentials.load(credentials);
			password = prop_credentials.getProperty(user_name.toString());

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return password;
	}

	/**
     * Get the user name of the client
     * 
     * @param   NONE
     * @return  The user name of the client. 
     */
	public String getUserName() {
		return GUIClient.user_name;
	}

	/**
     * Initialize the GUI
     * 
     * @param   user_name    The user name of the client
     * @return  NONE 
     */

	private void initComponents(final String user_name) {
		setResizable(false);

		setTitle(user_name);
		setBounds(50, 50, 750, 610);
		getContentPane().setBackground(SystemColor.info);

		userPanel = new JPanel();
		userPanel.setBackground(SystemColor.control);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addComponent(userPanel,
				GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addComponent(userPanel,
				GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE));

		//Pressing this button sends an announcement to all active users
		btnSendToActive = new JButton("Send to everyone");
		btnSendToActive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message = txtareaComposition.getText();
				if (message.isEmpty())
					txtareaThread.append("");

				try {
					sendMessageToAllActiveClients(message);
				} catch (IOException e1) {
					System.out.println("IOException: " + e1);
					e1.printStackTrace();
				}
				txtareaComposition.setText("");
			}
		});

		//Pressing this button sends a message to a designated recipient
		btnSend = new JButton("Send");
		btnSend.setToolTipText("First pick a recipient from the list.");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message = txtareaComposition.getText();
				recipient = textFieldRecipient.getText();
				if (txtareaComposition.getText().isEmpty()
						|| textFieldRecipient.getText().isEmpty()) {
					txtareaThread.append("");
				}

				else {
					try {

						sendMessage(message, recipient);
					} catch (IOException e1) {
						System.out.println("IOException: " + e1);
						e1.printStackTrace();
					}
					txtareaComposition.setText("");
				}
			}
		});

		txtareaComposition = new JTextArea();
		txtareaComposition.setLineWrap(true);
		txtareaComposition.setMargin(new Insets(5, 5, 5, 5));
		txtareaComposition.setBorder(new MatteBorder(1, 1, 1, 1,
				(Color) SystemColor.windowBorder));
		/*
		 * compositionScrollPane = new JScrollPane(txtareaComposition,
		 * JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		 * JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		 */

		txtareaThread = new JTextArea();
		txtareaThread.setEditable(false);
		txtareaThread.setLineWrap(true);
		txtareaThread.setMargin(new Insets(5, 5, 5, 5));
		txtareaThread.setBorder(new MatteBorder(1, 1, 1, 1,
				(Color) SystemColor.windowBorder));
		/*
		 * threadScrollPane = new JScrollPane(txtareaThread,
		 * JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		 * JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		 */

		jlistActiveUsers = new JList<String>(model);
		jlistActiveUsers.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				String choice = jlistActiveUsers.getSelectedValue()
						.toString();
				textFieldRecipient.setText(choice);
			}

			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		jlistActiveUsers.setBorder(new MatteBorder(1, 1, 1, 1,
				(Color) SystemColor.windowBorder));
		/*
		 * activeUsersScrollPane = new JScrollPane(jlistActiveUsers,
		 * JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		 * JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		 */

		lblActiveUsers = new JLabel("Active Users:");
		lblActiveUsers.setLabelFor(jlistActiveUsers);

		textFieldRecipient = new JTextField();
		textFieldRecipient.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldRecipient.setColumns(10);
		textFieldRecipient.setEditable(false);

		lblRecipient = new JLabel("Recipient:");
		lblRecipient.setLabelFor(textFieldRecipient);

		lblMessage = new JLabel("Message:");
		lblMessage.setLabelFor(txtareaComposition);
		GroupLayout gl_userPanel = new GroupLayout(userPanel);
		gl_userPanel.setHorizontalGroup(
				gl_userPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_userPanel.createSequentialGroup()
								.addContainerGap()
						.addGroup(gl_userPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_userPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_userPanel.createSequentialGroup()
						.addGroup(gl_userPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtareaThread,Alignment.LEADING)
								.addComponent(txtareaComposition,Alignment.LEADING)
						.addGroup(Alignment.LEADING,gl_userPanel.createSequentialGroup()
								.addComponent(textFieldRecipient,GroupLayout.PREFERRED_SIZE,188,GroupLayout.PREFERRED_SIZE)
										.addGap(18)
								.addComponent(btnSend)
										.addGap(18)
								.addComponent(btnSendToActive)))
										.addGap(18))
						.addGroup(gl_userPanel.createSequentialGroup()
								.addComponent(lblRecipient)
										.addPreferredGap(ComponentPlacement.RELATED)))
						.addGroup(gl_userPanel.createSequentialGroup()
								.addComponent(lblMessage)
										.addPreferredGap(ComponentPlacement.RELATED)))
						.addGroup(gl_userPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblActiveUsers)
								.addComponent(jlistActiveUsers,GroupLayout.PREFERRED_SIZE,208,GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));
		gl_userPanel.setVerticalGroup(
				gl_userPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_userPanel.createSequentialGroup()
										.addGap(15)
						.addGroup(gl_userPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblActiveUsers)
								.addComponent(lblMessage))
										.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_userPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(jlistActiveUsers,GroupLayout.DEFAULT_SIZE,508,Short.MAX_VALUE)
						.addGroup(gl_userPanel.createSequentialGroup()
								.addComponent(txtareaComposition,GroupLayout.PREFERRED_SIZE,79,GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblRecipient)
										.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_userPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldRecipient,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSend,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
								.addComponent(btnSendToActive,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE))
										.addGap(11)
								.addComponent(txtareaThread,GroupLayout.PREFERRED_SIZE,369,GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
		userPanel.setLayout(gl_userPanel);
		getContentPane().setLayout(groupLayout);

		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(0, 153, 102));
		setJMenuBar(menuBar);

		mnOptions = new JMenu("Options");
		mnOptions.setForeground(new Color(255, 255, 255));
		mnOptions.setMnemonic(KeyEvent.VK_O);
		menuBar.add(mnOptions);

		mnSupport = new JMenu("Support");
		mnSupport.setForeground(new Color(255, 255, 255));
		mnSupport.setMnemonic(KeyEvent.VK_S);
		menuBar.add(mnSupport);

		mntmLogOut = new JMenuItem("Log out");
		mntmLogOut.setMnemonic(KeyEvent.VK_L);
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					logOut();
				} catch (IOException e) {
					System.out.println("IOException: " + e);
					e.printStackTrace();
				}
			}
		});

		mntmOpenChatLog = new JMenuItem("Open chat log");
		mntmOpenChatLog.setMnemonic(KeyEvent.VK_O);
		mntmOpenChatLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accessLog();
			}
		});

		mntmChangePassword = new JMenuItem("Change password");
		mntmChangePassword.setMnemonic(KeyEvent.VK_P);
		mntmChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pw = "Password";
				openGUIChangeForm(pw, getPassword(), user_name);
			}
		});

		mntmChangeUsername = new JMenuItem("Change username");
		mntmChangeUsername.setMnemonic(KeyEvent.VK_U);
		mntmChangeUsername.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String un = "Username";
				openGUIChangeForm(un, getUserName(), null);
			}
		});

		mntmOpenUserDatabase = new JMenuItem("Open user database");
		mntmOpenUserDatabase.setMnemonic(KeyEvent.VK_U);
		mntmOpenUserDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String header = "User Database";
				try {
					new DisplayFileContentsGUI(header, null, null)
							.setVisible(true);
				} catch (Exception e) {
					System.out.println("Exception: " + e);
					e.printStackTrace();
				}
			}
		});

		mntmBlockAnAccount = new JMenuItem("Block an account");
		mntmBlockAnAccount.setMnemonic(KeyEvent.VK_B);
		mntmBlockAnAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String header = "Block An Account";
				new GUIEnterUserForm(header).setVisible(true);
			}
		});

		mntmDeleteAnAccount = new JMenuItem("Delete an account");
		mntmDeleteAnAccount.setMnemonic(KeyEvent.VK_D);
		mntmDeleteAnAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String header = "Delete An Account";
				new GUIEnterUserForm(header).setVisible(true);
			}
		});

		mntmAbout = new JMenuItem("About");
		mntmAbout.setMnemonic(KeyEvent.VK_A);
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AboutWindow().setVisible(true);
			}
		});

		mnOptions.add(mntmLogOut);
		mnOptions.add(mntmOpenChatLog);
		mnOptions.add(mntmChangePassword);

		if (getUserName().equals("admin")) {
			mnOptions.add(mntmOpenUserDatabase);
			mnOptions.add(mntmBlockAnAccount);
			mnOptions.add(mntmDeleteAnAccount);
			mnSupport.add(mntmAbout);
		}

		else {
			mnOptions.add(mntmChangeUsername);
			mnSupport.add(mntmAbout);
		}
	}

	/**
     * Connect to the socket and begin communications
     * 
     * @param   NONE
     * @return  NONE
     */
	public void join() throws IOException {
		clientSocket = new Socket();
		String host = "localhost";
		writer = null;
		reader = null;

		try {
			clientSocket = new Socket(host, port);

			// writer for socket
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
			// reader for socket
			reader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
		}

		// Host not found
		catch (UnknownHostException e) {
			System.out.println("Don't know about host : " + host);
			System.exit(1);
		}

		// pass the user name to the server so that server can announce
		// the new client
		writer.println(user_name);

		new Thread() {
			@Override
			public void run() {
				try {
					receiveData();
				} catch (IOException iex) {
					throw new RuntimeException("Interrupted", iex);
				}
			}
		}.start();
	}

	/**
     * Log the client off the socket
     * 
     * @param   NONE
     * @return  NONE 
     */
	public void logOut() throws IOException {
		dispose();
		removeClientFromActiveUsersList(user_name);
		writer.println(user_name + "œœ " + " has logged out. ***");
		// the œ characters are delimiters
		
		// close the i/o streams
		writer.close();
		reader.close();

		// close the socket
		clientSocket.close();

		new GUIFrontEnd().setVisible(true);
	}

	/**
     * Check a given user name against the properties file of registered users
     * 
     * @param   user_name    The name to be checked against the file
     * @return  Whether or not the name already is registered by someone else 
     */
	public static boolean nameIsTaken(String user_name) {
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
     * Call the GUI for changing a client's credentials
     * 
     * @param   header    	The title bar of the GUI and the determinant of the GUI' purpose
     * @param   oldEntry 	The credential to be changed.
     * @param	user_name	The user name of the client
     * @return  NONE 
     */
	public void openGUIChangeForm(String header, String oldEntry,
			String user_name) {
		new GUIChangeForm(header, oldEntry, user_name).setVisible(true);
	}

	/**
     * Read data from the socket and print to this GUI and the activity log
     * 
     * @param   NONE
     * @return  NONE 
     */
	private void receiveData() throws IOException {
		@SuppressWarnings("unused")
		Set<Object> users;
		// the ASCII database of usernames and passwords
		
		String line = "";
		
		try {
			while (true) {
				line = reader.readLine() + "\n";
				txtareaThread.append(line); // show in the GUI
				activityLog.add(line); // store in the log

				// Load the existing active users into the Jlist
				active_users = new FileInputStream("active_users.properties");
				prop_active_users.load(active_users);
				users = prop_active_users.keySet();
				
				jlistActiveUsers.getModel();
					//update the active users list when clients log in or out.
					String[] splitMessage = line.split(" ");
					
					if (line.contains("has logged in. ***") && !splitMessage[0].contentEquals(user_name)) {
						model.addElement(splitMessage[0]);
					}
					
					else if (line.contains("has logged out. ***")) {
						model.removeElement(splitMessage[0]);
					}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * Delete a client from the properties file of active users
     * 
     * @param   user_name    The name of the client to be deleted from the list.
     * @return  NONE 
     */
	public static void removeClientFromActiveUsersList(String user_name) {
		try {
			active_users = new FileInputStream("active_users.properties");

			prop_active_users.load(active_users);
			prop_active_users.remove(user_name);

			// overwrite the existing credentials file
			PrintWriter printWriter = new PrintWriter("active_users.properties");
			for (Entry<Object, Object> e : prop_active_users.entrySet()) {
				printWriter.println(e);
			}
			printWriter.close();
		} catch (IOException e1) {
			System.out.println("IOException: " + e1);
			e1.printStackTrace();
		}
	}
	
	/**
     * fills out the GUIClient's JList of active users
     * 
     * @param   NONE
     * @return  NONE 
	 * @throws IOException 
     */
	private void seedTheJListOfUsers() throws IOException {	
		active_users = new FileInputStream("active_users.properties");
		prop_active_users.load(active_users);
		  
		//populate the list with all registered users  
		for (Object activeUser : prop_active_users.keySet()) {
			  model.addElement(activeUser.toString());
		}		
	}

	/**
     * Send a message to all currently active users
     * 
     * @param   message    The message to be broadcast
     * @return  NONE 
     */
	public void sendMessageToAllActiveClients(String message)
			throws IOException {
		try {
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
		}

		// Host not found
		catch (UnknownHostException e) {
			System.out.println("UnknownHostException : " + e);
			System.exit(1);
		}

		if (!message.isEmpty()) {
			writer.println(user_name + "œœ: " + message);
			// the œ character is a delimiter
			
			activityLog.add(user_name + " to all: " + message + "\n"); 
			// store in the log
		}
	}

	/**
     * Send a message to a designated recipient
     * 
     * @param   message    	The message to be sent.
     * @param   recipient 	The client who should recieve the message
     * @return  NONE 
     */
	public void sendMessage(String message, String recipient)
			throws IOException {
		try {
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
		}

		// Host not found
		catch (UnknownHostException e) {
			System.out.println("UnknownHostException : " + e);
			System.exit(1);
		}

		if (!message.isEmpty() && !recipient.isEmpty()
				&& !recipient.contentEquals(user_name)) {
			writer.println(user_name + "œ" + recipient + "œ: " + message);
			// the œ character is a delimiter
			
			activityLog.add(user_name + " to " + recipient + ": " + message + "\n"); 
			// store in the log
		}
		txtareaThread.append(user_name + ": " + message + "\n");
	}

	/**
     * Set the user name of this client
     * 
     * @param   un    The user name of this client
     * @return  NONE 
     */
	public void setUserName(String un) {
		GUIClient.user_name = un;
	}

	/**
     * update the properties file of blocked users
     * 
     * @param   user_name    The name of the blocked user.
     * @return  Whether or not the file was successfully updated. 
     */
	public static Boolean updateBlocked_Users_List(String user_name)
			throws IOException {
		blocked_users = new FileInputStream("blocked_users.properties");
		prop_blocked_users.load(blocked_users);

		File credentials = new File("blocked_users.properties");
		FileWriter filewriter = null;

		try {
			// Below constructor argument decides whether to append or override
			filewriter = new FileWriter(credentials, true);

			// guard condition: the user is already on the list of blocked users
			if (prop_blocked_users.getProperty(user_name) != null) {
				String message = "That user is already blocked.";
				new GUIAlert(message).setVisible(true);
				return false;
			}

			else {
				filewriter.write(user_name);
				filewriter.write(System.lineSeparator());
			}

		} catch (IOException e) {
			System.out.println("IOException: " + e);
			e.printStackTrace();
		} finally {
			try {
				filewriter.close();
			} catch (IOException e) {
				System.out.println("IOException: " + e);
				e.printStackTrace();
			}
		}
		return true;
	}
}