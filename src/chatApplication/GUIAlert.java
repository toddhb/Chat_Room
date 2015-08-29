/*Chat Application
author: Todd Brochu
Portland State University
CS300 Spring 2014*/

package chatApplication;

import javax.swing.JFrame;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUIAlert extends JFrame {

	private static final long serialVersionUID = 1L;
	String message;

	public GUIAlert(String msg) {
		initializeGUI(msg);
	}

	/**
	 * Initialize the GUI
	 * 
	 * @param msg
	 *            The message to displayed in the GUI
	 * @return NONE
	 */
	private void initializeGUI(String msg) {
		getContentPane().setBackground(SystemColor.info);

		JLabel lblAlert = new JLabel("!!  " + msg);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(29)
					.addComponent(lblAlert, GroupLayout.PREFERRED_SIZE, 498, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(67, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(426, Short.MAX_VALUE)
					.addComponent(btnOk)
					.addGap(54))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addComponent(lblAlert)
					.addPreferredGap(ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
					.addComponent(btnOk)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		setBounds(50, 50, 500, 125);

		setTitle("Alert");
		setResizable(false);
	}
}