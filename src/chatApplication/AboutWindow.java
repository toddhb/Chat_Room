/*Chat Application
author: Todd Brochu
Portland State University
CS300 Spring 2014*/

package chatApplication;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.MatteBorder;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AboutWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton btnOK;
	JTextPane txtpnAbout;

	public AboutWindow() {
		initComponents();
	}

	/**
	 * Initialize the GUI
	 * 
	 * @param NONE
	 * @return NONE
	 */
	private void initComponents() {
		setResizable(false);
		setTitle("About");
		setBounds(50, 50, 325, 165);

		txtpnAbout = new JTextPane();
		txtpnAbout
				.setText(" CS300 Chat\n Author: Todd Brochu\n Class: PSU CS300 Spring 2014\n Version: 1.0\n Date: June 4, 2014");
		txtpnAbout.setBorder(new MatteBorder(1, 1, 1, 1,
				(Color) SystemColor.windowBorder));

		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																txtpnAbout,
																GroupLayout.DEFAULT_SIZE,
																299,
																Short.MAX_VALUE)
														.addComponent(
																btnOK,
																Alignment.TRAILING))
										.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(txtpnAbout, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnOK)
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		getContentPane().setLayout(groupLayout);
	}
}