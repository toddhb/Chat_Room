/*Chat Application
author: Todd Brochu
Portland State University
CS300 Spring 2014*/

package chatApplication;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JPasswordField;

public class GUIEnterUserForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	String user_name, password;
	JButton btnEnter;
	private JTextField textFieldUserName;
	private JPasswordField textFieldPassword;

	public GUIEnterUserForm(String header) {
		initializeGUI(header);
	}
	
	/**
     * Requests that the administrator client block an account
     * 
     * @param   NONE
     * @return  NONE 
     */
	public void blockAnAccount() throws IOException {
		// guard condition: the username was left blank
		if (user_name.isEmpty()) {
			String message = "Please enter information into the field.";
			new GUIAlert(message).setVisible(true);
		}

		// guard condition: attempting to block the admin account
		if (user_name.equals("admin")) {
			String message = "This account cannot be blocked.  Please try again.";
			new GUIAlert(message).setVisible(true);
		}

		else if (GUIClient.updateBlocked_Users_List(user_name) == true)
			dispose();
	}

	/**
     * Request that the client check if proposed user name is already taken;
     * Request that the client add the new credentials to the properties
     * file of registered users.
     * Instantiate a new GUIClient for an accepted account.
     * 
     * @param   NONE
     * @return  NONE 
     */
	public void createAnAccount() throws Exception, IOException {

		// guard condition: either the username or the password field
		// was left blank
		if (user_name.isEmpty() || password.isEmpty()) {
			String message = "Please enter information into both fields.";
			new GUIAlert(message).setVisible(true);
		}

		else if (GUIClient.nameIsTaken(user_name) == false) {
			GUIClient.addToCredentials(user_name, password);
			new GUIClient(user_name).setVisible(true);
			dispose();
		}

		// guard condition: the proposed username has already been taken
		else {
			String message = "User name is already taken.  Please try another.";
			new GUIAlert(message).setVisible(true);
		}
	}
	
	/**
     * Request that the administrator client delete a user from
     * the properties file of registered users, so that the client
     * may continue the current chat session, but will need to 
     * re-register the next time he/she attempts to log in.
     * 
     * @param   NONE
     * @return  NONE. 
     */
	public void deleteAnAccount() throws IOException {
		// guard condition: the username was left blank
		if (user_name.isEmpty()) {
			String message = "Please enter information into the field.";
			new GUIAlert(message).setVisible(true);
		}

		// guard condition: the username does not exist
		else if (GUIClient.nameIsTaken(user_name) == false) {
			String message = "This user name does not exist.  Please try again.";
			new GUIAlert(message).setVisible(true);
		}

		// guard condition: attempting to delete the admin account
		else if (user_name.equals("admin")) {
			String message = "This account cannot be deleted.  Please try again.";
			new GUIAlert(message).setVisible(true);
		}

		else {
			GUIClient.deleteFromCredentials(user_name);
			dispose();
		}
	}
	
	/**
     * Initialize the GUI
     * 
     * @param   header    The titlebar of the GUI
     * @return  NONE 
     */
	private void initializeGUI(final String header) {
		setTitle(header);
		setResizable(false);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		
		textFieldUserName = new JTextField(10);
		textFieldUserName.setToolTipText("Enter a username.");
		textFieldUserName.addActionListener(this);
		textFieldUserName.setEditable(true);
		
		textFieldPassword = new JPasswordField(10);
		textFieldPassword.setToolTipText("Enter a password.");
		textFieldPassword.addActionListener(this);
		textFieldPassword.setEditable(true);
		
		//if this GUI is being called to either block an account or delete an account, then
		//only show the textFieldUserName and not the textFieldPassword.
		if (!header.equals("Create An Account")){
			textFieldPassword.setVisible(false);
			lblPassword.setVisible(false);
		}
		
		//Pressing this button will submit the required information.
		btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (header.equals("Create An Account")){
					user_name = textFieldUserName.getText();
					password = new String(textFieldPassword.getPassword());
					try {		
						createAnAccount();
					} catch (IOException e3) {
						System.out.println("IOException: " + e3);
						e3.printStackTrace();
					} catch (Exception e3) {
						System.out.println("Exception: " + e3);
						e3.printStackTrace();
					}
				}
				else if (header.equals("Block An Account")){
					user_name = textFieldUserName.getText();
					try {
						blockAnAccount();
					} catch (IOException e1) {
						System.out.println("IOException: " + e1);
						e1.printStackTrace();
					}
				}
				
				else if (header.equals("Delete An Account")){
					user_name = textFieldUserName.getText();
					try {
						deleteAnAccount();
					} catch (IOException e2) {
						System.out.println("IOException: " + e2);
						e2.printStackTrace();
					}
				}
			}		
		});
		
		btnEnter.registerKeyboardAction(btnEnter.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                JComponent.WHEN_FOCUSED);
		btnEnter.registerKeyboardAction(btnEnter.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
		
		//Pressing this button will close the dialog.
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblUsername)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textFieldUserName))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPassword)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textFieldPassword, 162, 162, 162)))
					.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnEnter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(28)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUsername)
								.addComponent(textFieldUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPassword)
								.addComponent(textFieldPassword, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(45)
							.addComponent(btnEnter)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnCancel)))
					.addContainerGap(20, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
		setBounds(50, 50, 350, 150);
		
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == textFieldUserName) {
			user_name = textFieldUserName.getText();
			textFieldPassword.requestFocus();
		}

		if (source == textFieldPassword) {
			password = new String(textFieldPassword.getPassword());
			btnEnter.requestFocus();
		}
	}
}