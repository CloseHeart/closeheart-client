package kr.ac.gachon.sw.closeheart.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class LoginForm extends JFrame {
	private JPanel contentPane;
	private JTextField tf_id;
	private JPasswordField pf_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm frame = new LoginForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginForm() {
		setAlwaysOnTop(true);
		setTitle("Close Heart - Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Logo Image Panel
		JPanel logoPanel = new JPanel();
		contentPane.add(logoPanel, BorderLayout.NORTH);
		JLabel lb_logoImage = new JLabel();
		ImageIcon logo = new ImageIcon(new ImageIcon(LoginForm.class.getResource("resource/closeheart_logo_login.png")).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
		lb_logoImage.setIcon(logo);
		lb_logoImage.setPreferredSize(new Dimension(300, 200));
		lb_logoImage.setHorizontalAlignment(JLabel.CENTER);
		logoPanel.add(lb_logoImage);
		
		// Main Content Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(20, 50, 0, 50)); 
		contentPane.add(mainPanel, BorderLayout.CENTER);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_mainPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_mainPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_mainPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_mainPanel);
		
		JLabel lb_id = new JLabel("ID");
		GridBagConstraints gbc_lb_id = new GridBagConstraints();
		gbc_lb_id.insets = new Insets(0, 0, 5, 5);
		gbc_lb_id.gridx = 1;
		gbc_lb_id.gridy = 1;
		mainPanel.add(lb_id, gbc_lb_id);
		
		tf_id = new JTextField();
		GridBagConstraints gbc_tf_id = new GridBagConstraints();
		gbc_tf_id.gridwidth = 8;
		gbc_tf_id.insets = new Insets(0, 0, 5, 5);
		gbc_tf_id.fill = GridBagConstraints.HORIZONTAL;
		gbc_tf_id.gridx = 3;
		gbc_tf_id.gridy = 1;
		mainPanel.add(tf_id, gbc_tf_id);
		tf_id.setColumns(10);
		
		JLabel lb_password = new JLabel("Password");
		GridBagConstraints gbc_lb_password = new GridBagConstraints();
		gbc_lb_password.insets = new Insets(0, 0, 5, 5);
		gbc_lb_password.gridx = 1;
		gbc_lb_password.gridy = 2;
		mainPanel.add(lb_password, gbc_lb_password);
		
		pf_password = new JPasswordField();
		GridBagConstraints gbc_pf_password = new GridBagConstraints();
		gbc_pf_password.gridwidth = 8;
		gbc_pf_password.insets = new Insets(0, 0, 5, 5);
		gbc_pf_password.fill = GridBagConstraints.HORIZONTAL;
		gbc_pf_password.gridx = 3;
		gbc_pf_password.gridy = 2;
		mainPanel.add(pf_password, gbc_pf_password);
		
		// Bottom (Button) Panel
		JPanel bottomPanel = new JPanel();
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[]{0};
		gbl_bottomPanel.rowHeights = new int[]{0};
		gbl_bottomPanel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_bottomPanel.rowWeights = new double[]{0.0};
		bottomPanel.setLayout(gbl_bottomPanel);
		
		JButton btn_findpw = new JButton("Find PW");
		GridBagConstraints gbc_btn_findpw = new GridBagConstraints();
		gbc_btn_findpw.insets = new Insets(0, 0, 0, 5);
		gbc_btn_findpw.gridx = 4;
		gbc_btn_findpw.gridy = 0;
		bottomPanel.add(btn_findpw, gbc_btn_findpw);
		
		JButton btn_login = new JButton("Log-in");
		GridBagConstraints gbc_btn_login = new GridBagConstraints();
		gbc_btn_login.gridx = 5;
		gbc_btn_login.gridy = 0;
		bottomPanel.add(btn_login, gbc_btn_login);
		
	}
}
