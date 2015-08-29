/*Chat Application
author: Todd Brochu
Portland State University
CS300 Spring 2014*/

package chatApplication;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class GUIFrontEnd extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	String user_name;
	String password;
	private JButton btnCreateAnAccount, btnLogIn, btnExit;
	private JPanel contentPane;
	private JTextField textFieldUserName;
	private JPasswordField textFieldPassword;

	FileInputStream credentials;
	FileInputStream blocked_users;
	OutputStream credentials_out;

	Properties prop_credentials;
	Properties prop_blocked_users;

	public GUIFrontEnd() {
		prop_credentials = new Properties();
		// the ASCII database of usernames and passwords
		prop_blocked_users = new Properties();
		// the ASCII database of blocked accounts
		
		initializeGUI();
	}
	
	/**
     * Terminate the client's interface with the chat server;
     * Called by pressing the Exit button
     * 
     * @param   NONE
     * @return  NONE 
     */
	public void exitProgram() {
		dispose();
		System.exit(0);
	}

	/**
     * Initialize the GUI
     * 
     * @param   NONE
     * @return  NONE 
     */
	private void initializeGUI() {
		setResizable(false);
		setTitle("CS300 Chat");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
		);
		
		setBounds(50, 50, 325, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//Pressing this button will call the GUI for entering a user information
		btnCreateAnAccount = new JButton("Create an Account");
		btnCreateAnAccount.addActionListener(new ActionListener() {//call the EnterUserForm screen if this button is clicked
			public void actionPerformed(ActionEvent arg0) {
				String header = "Create An Account";
				openGUIEnterUserForm(header);
			}
		});
		btnCreateAnAccount.registerKeyboardAction(btnCreateAnAccount.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                JComponent.WHEN_FOCUSED);

		btnCreateAnAccount.registerKeyboardAction(btnCreateAnAccount.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
		
		//Pressing this button will collect the user name and call the logIn method.
		btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user_name = textFieldUserName.getText();
				password = new String(textFieldPassword.getPassword());
				try {
					logIn(user_name, password);
				} catch (IOException e1) {
					System.out.println("IOException: " + e1);
					e1.printStackTrace();
				} catch (Exception e1) {
					System.out.println("Exception: " + e1);
					e1.printStackTrace();
				}
			}
		});
		btnLogIn.registerKeyboardAction(btnLogIn.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                JComponent.WHEN_FOCUSED);

		btnLogIn.registerKeyboardAction(btnLogIn.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
		
		//Pressing this button will call the exitProgram method.
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {//terminate the program if this button is clicked
			public void actionPerformed(ActionEvent e) {
				exitProgram();
			}
		});
		btnExit.registerKeyboardAction(btnExit.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                JComponent.WHEN_FOCUSED);

		btnExit.registerKeyboardAction(btnExit.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
		
		JLabel lblUsername = new JLabel("Username:");		
		JLabel lblPassword = new JLabel("Password:");
		
		textFieldUserName = new JTextField(10);
		textFieldUserName.setToolTipText("Enter your username.");
		textFieldUserName.addActionListener(this);
		textFieldUserName.setEditable(true);
		
		textFieldPassword = new JPasswordField(10);
		textFieldUserName.setToolTipText("Enter your password.");
		textFieldPassword.addActionListener(this);
		textFieldPassword.setEditable(true);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPassword)
						.addComponent(lblUsername))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(textFieldPassword, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
						.addComponent(textFieldUserName, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(btnLogIn)
					.addGap(20))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(80)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnExit, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
						.addComponent(btnCreateAnAccount, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
					.addGap(81))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(29)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUsername)
								.addComponent(textFieldUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(11)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPassword)
								.addComponent(textFieldPassword, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(41)
							.addComponent(btnLogIn, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
					.addComponent(btnCreateAnAccount)
					.addGap(18)
					.addComponent(btnExit, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addGap(28))
		);
		contentPane.setLayout(gl_contentPane);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == textFieldUserName) {
			user_name = textFieldUserName.getText();
			textFieldPassword.requestFocus();
		}

		if (source == textFieldPassword) {
			password = new String(textFieldPassword.getPassword());
			btnLogIn.requestFocus();
		}
	}

	/**
     * Calls the validateCredentials method;
     * Instantiates an object of the GUIClient class
     * 
     * @param   user_name   The name to be passed to validateCredentials
     * @param   password 	The password to be passed to validateCredentials
     * @return  NONE 
     */
	public void logIn(String user_name, String password) throws Exception,
			IOException {
		if (validateCredentials(user_name, password) == true) {

			dispose();
			new GUIClient(user_name).setVisible(true);
		}
	}

	/**
     * Instantiates a GUIEnterUserForm 
     * 
     * @param   header    The title bar of the GUI and the determinant of the GUI's purpose
     * @return  NONE 
     */
	public void openGUIEnterUserForm(String header) {
		dispose();
		new GUIEnterUserForm(header).setVisible(true);
	}

	/**
     * Validates a set of credentials against the properties file of registered users
     * 
     * @param   user_name   The user name to be validated.
     * @param   password	The password to be validated.
     * @return  Whether or not the credentials are valid 
     */
	public Boolean validateCredentials(String user_name, String password) {
		Boolean isValid = false;

		try {
			credentials = new FileInputStream("credentials.properties");
			prop_credentials.load(credentials);

			blocked_users = new FileInputStream("blocked_users.properties");
			prop_blocked_users.load(blocked_users);

			String pw = prop_credentials.getProperty(user_name.toString());
			 
			// guard condition: user is on the list of blocked users
			if (prop_blocked_users.getProperty(user_name) != null) {
				String message = "This account has been blocked.  Please contact the administrator.";
				new GUIAlert(message).setVisible(true);
			}

			// guard condition: no such user name
			else if (prop_credentials.getProperty(user_name) == null) {
				String message = "User name does not exist.  Please try again.";
				new GUIAlert(message).setVisible(true);
			}

			// guard condition: user name and password don't match
			else if (pw != null && !pw.equals(password)) {
				String message = "User name and password do not match.  Please try again.";
				new GUIAlert(message).setVisible(true);
			}

			// success; valid user
			else if (pw != null && pw.equals(password)) {
				isValid = true;
			}

		} catch (IOException ex) {
			System.out.println("IOException: " + ex);
			ex.printStackTrace();
		}
		return isValid;
	}
}