/*Chat Application
author: Todd Brochu
Portland State University
CS300 Spring 2014*/

package chatApplication;

import java.awt.Insets;
import java.awt.SystemColor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DisplayFileContentsGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	FileInputStream credentials;
	JButton btnClose;
	JPanel displayPanel = new JPanel();
	JScrollPane scrollPane;
	JTextArea textArea;
	Properties prop_credentials;// the ASCII database of usernames and passwords

	DisplayFileContentsGUI(String header, ArrayList<String> activityLog,
			String user_name) throws IOException {
		super(header);
		initComponents();

		if (header.equals("Chat Activity"))
			showChatActivity(activityLog, user_name);
		else if (header.equals("User Database"))
			showUserDatabase();
	}

	/**
	 * Initialize the GUI
	 * 
	 * @param NONE
	 * @return NONE
	 */
	private void initComponents() throws IOException {
		setResizable(false);
		setBounds(50, 50, 325, 600);

		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setBorder(new MatteBorder(1, 1, 1, 1,
				(Color) SystemColor.windowBorder));

		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						groupLayout.createSequentialGroup()
								.addContainerGap(250, Short.MAX_VALUE)
								.addComponent(btnClose).addContainerGap())
				.addGroup(
						groupLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(scrollPane,
										GroupLayout.PREFERRED_SIZE, 270,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(39, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								516, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btnClose).addContainerGap()));
		getContentPane().setLayout(groupLayout);
	}

	/**
	 * Display the log of a client's chat activity in the GUI
	 * 
	 * @param activityLog
	 *            The log of the client's chat activity.
	 * @param user_name
	 *            The name of the client.
	 * @return NONE
	 */
	private void showChatActivity(ArrayList<String> activityLog,
			String user_name) {
		textArea.append("User name: " + user_name + "\n");
		for (int i = 0; i < activityLog.size(); i++) {
			textArea.append(activityLog.get(i));
		}
	}

	/**
	 * Display the contents of the credentials.properties file in the GUI
	 * 
	 * @param NONE
	 * @return NONE
	 */
	@SuppressWarnings("rawtypes")
	private void showUserDatabase() throws IOException {
		Properties prop_credentials = new Properties();// the ASCII database of
														// usernames and
														// passwords
		InputStream input = null;

		try {
			input = new FileInputStream("credentials.properties");
			prop_credentials.load(input);
			@SuppressWarnings({ "unchecked" })
			ArrayList<String> listOfUserNames = new ArrayList(
					prop_credentials.keySet());
			@SuppressWarnings({ "unchecked" })
			ArrayList<String> listOfPasswords = new ArrayList(
					prop_credentials.values());

			for (int i = 0; i < listOfUserNames.size(); i++) {
				textArea.append(" " + listOfUserNames.get(i) + " = "
						+ listOfPasswords.get(i) + "\n");
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}