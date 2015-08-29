/*Chat Application
author: Todd Brochu
Portland State University
CS300 Spring 2014*/

package chatApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

public class GUIChangeForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	FileInputStream credentials;
	Properties prop_credentials;
	String oldEntry, newEntry;
	String titleBar;
	private JLabel textFieldOldEntry;
	private JTextField textFieldNewEntry;
	private JButton btnEnter, btnCancel;
	String un;

	public GUIChangeForm(String header, String old, String user_name) {
		un = user_name;
		oldEntry = old;
		titleBar = "Change " + header;
		initializeGUI(titleBar, old);
	}
	
	/**
     * change the user name of a client
     * 
     * @param   old_user_name   The user name to be changed.
     * @param   new_user_name 	The user name to be assigned.
     * @return  NONE 
     */
	public void changeUserName(String old_user_name, String new_user_name)
			throws IOException {
		File file = new File("credentials.properties");
		String password;

		prop_credentials = new Properties();
		credentials = new FileInputStream(file);
		prop_credentials.load(credentials);

		// store the password of the old user name for future use
		password = prop_credentials.getProperty(old_user_name);

		// append a new line item
		prop_credentials.setProperty(new_user_name, password);

		// remove the old line item
		prop_credentials.remove(old_user_name);

		// overwrite the existing credentials file
		PrintWriter printWriter = new PrintWriter("credentials.properties");
		for (Entry<Object, Object> e : prop_credentials.entrySet()) {
			printWriter.println(e);
		}
		printWriter.close();
	}
	
	/**
     * change the password of a client
     * 
     * @param   old_pw    	The password to be changed.
     * @param   new_pw 		The password to be assigned.
     * @return  NONE 
     */
	public void changePassword(String old_pw, String new_pw, String un)
			throws IOException {
		prop_credentials = new Properties();
		credentials = new FileInputStream("credentials.properties");
		prop_credentials.load(credentials);
		prop_credentials.setProperty(un, new_pw);

		// overwrite the existing credentials file
		PrintWriter printWriter = new PrintWriter("credentials.properties");
		for (Entry<Object, Object> e : prop_credentials.entrySet()) {
			printWriter.println(e);
		}
		printWriter.close();
	}
	
	/**
     * Initialize the GUI
     * 
     * @param   title    	The title bar of the GUI and the determinant of the purpose of the GUI
     * @param   user_name 	The user name of the client.
     * @return  NONE 
     */
	private void initializeGUI(String title, String user_name) {
		titleBar = title;
		setTitle(titleBar);
		setBounds(50, 50, 325, 200);

		setResizable(false);

		JLabel lblOld = new JLabel("Old:");

		textFieldOldEntry = new JLabel("  " + oldEntry);

		JLabel lblNew = new JLabel("New:");

		textFieldNewEntry = new JTextField();
		textFieldNewEntry.setColumns(10);
		textFieldNewEntry.addActionListener(this);
		textFieldNewEntry.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
						newEntry = textFieldNewEntry.getText();
						btnEnter.requestFocus();
				}
		});
		

		btnEnter = new JButton("Enter");
		btnEnter.addActionListener(this);
		btnEnter.registerKeyboardAction(btnEnter
				.getActionForKeyStroke(KeyStroke.getKeyStroke(
						KeyEvent.VK_SPACE, 0, false)), KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

		btnEnter.registerKeyboardAction(btnEnter
				.getActionForKeyStroke(KeyStroke.getKeyStroke(
						KeyEvent.VK_SPACE, 0, true)), KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_FOCUSED);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnCancel.registerKeyboardAction(btnCancel
				.getActionForKeyStroke(KeyStroke.getKeyStroke(
						KeyEvent.VK_SPACE, 0, false)), KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

		btnCancel.registerKeyboardAction(btnCancel
				.getActionForKeyStroke(KeyStroke.getKeyStroke(
						KeyEvent.VK_SPACE, 0, true)), KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_FOCUSED);

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblOld)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textFieldOldEntry,GroupLayout.PREFERRED_SIZE,247,GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNew)
							.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnEnter)
							.addPreferredGap(ComponentPlacement.RELATED,82,Short.MAX_VALUE)
							.addComponent(btnCancel))
							.addComponent(textFieldNewEntry))))
							.addContainerGap(32,GroupLayout.PREFERRED_SIZE)));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(30)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblOld)
							.addComponent(textFieldOldEntry,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
							.addGap(27)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNew)
							.addComponent(textFieldNewEntry,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
							.addGap(26)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnEnter)
							.addComponent(btnCancel))
							.addContainerGap(26, Short.MAX_VALUE)));
		getContentPane().setLayout(groupLayout);
	}

	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();

		if (source == btnEnter) {
			newEntry = textFieldNewEntry.getText();
			
			if (titleBar.equals("Change Username")) {
				try {
					// guard condition: the field was left blank
					if (textFieldNewEntry.getText().isEmpty()) {
						String message = "Please enter information into the field.";
						new GUIAlert(message).setVisible(true);
					}

					else if (GUIClient.nameIsTaken(newEntry) == false) {
						changeUserName(oldEntry, newEntry);
						dispose();
					}

					// guard condition: the chosen user name is already taken
					else {
						String message = "User name is already taken.  Please try another.";
						new GUIAlert(message).setVisible(true);
					}

				} catch (IOException e) {
					System.out.println(arg0);
					e.printStackTrace();
				}
			}

			else if (titleBar.equals("Change Password")) {
				// guard condition: the field was left blank
				if (textFieldNewEntry.getText().isEmpty()) {
					String message = "Please enter information into the field.";
					new GUIAlert(message).setVisible(true);
					dispose();
				}

				else {
					try {
						changePassword(oldEntry, newEntry, un);
					} catch (IOException e) {
						System.out.println("Error: " + e);
						e.printStackTrace();
					}
					dispose();
				}
			}
		}

		else if (source == btnCancel) {
			dispose();
		}
	}
}