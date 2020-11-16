package kr.ac.gachon.sw.closeheart.client.login.loginForm;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.connection.ConnectionInfo;
import kr.ac.gachon.sw.closeheart.client.login.registerForm.RegisterForm;
import kr.ac.gachon.sw.closeheart.client.util.Util;;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * 로그인 Form
 * @author Minjae Seon
 */
public class LoginForm extends BaseForm {
    private JPanel loginForm_Panel;
    private JTextField tf_id;
    private JPasswordField tf_pw;
    private JButton btn_register;
    private JButton btn_login;
    private JLabel lb_forgotpassword;
    private JLabel lb_logo;
    private JCheckBox cb_saveid;

    public LoginForm(ConnectionInfo connectionInfo) {
        // ContentPane 설정
        setContentPane(loginForm_Panel);

        // Label에 Logo Image 삽입
        lb_logo.setIcon(Util.resizeImage(new ImageIcon(getClass().getResource("/res/closeheart_logo_login.png")).getImage(), 200, 200, Image.SCALE_SMOOTH));

        // Window 사이즈 설정
        setSize(300, 600);

        // 각종 Action Event을 설정
        setEvent();

        // Close Option 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Visible
        setVisible(true);
    }

    /*
     * 각종 Event를 설정하는 함수
     * @author Minjae Seon
     */
    public void setEvent() {
        // Login Button Action
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Login Clicked");
            }
        });

        RegisterForm registerForm = new RegisterForm();
        // Register Button Action
        btn_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Register Clicked");
                if(!registerForm.isShowing()) registerForm.setShowing();
            }
        });

        // Forgot Password Clicked Event
        lb_forgotpassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Find PW Clicked");
                super.mouseClicked(e);
            }
        });

        // Save ID CheckBox Event
        cb_saveid.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox saveid = (JCheckBox) e.getItem();
                System.out.println("Save ID State Change : " + saveid.isSelected());
            }
        });
    }
}
