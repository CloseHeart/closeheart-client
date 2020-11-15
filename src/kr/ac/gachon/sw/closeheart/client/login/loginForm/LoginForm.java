package kr.ac.gachon.sw.closeheart.client.login.loginForm;

import kr.ac.gachon.sw.closeheart.client.login.registerForm.RegisterForm;
import kr.ac.gachon.sw.closeheart.client.util.Util;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Properties;

/*
 * 로그인 Form
 * @author Minjae Seon
 */
public class LoginForm extends JFrame {
    private JPanel loginForm_Panel;
    private JTextField tf_id;
    private JPasswordField tf_pw;
    private JButton btn_register;
    private JButton btn_login;
    private JLabel lb_forgotpassword;
    private JLabel lb_logo;
    private JCheckBox cb_saveid;

    public static void main(String[] args) {
        // Font Setting
        Util.changeUIFont(new Font("NanumGothic", Font.PLAIN, 13));
        new LoginForm();
    }

    public LoginForm() {
        // 타이틀 설정
        setTitle(Util.getStrFromProperties("program_title"));

        // Window를 가운데 뜨게 설정..이 된다는데 왜 난 안되는거 같지..
        setLocationRelativeTo(null);

        // ContentPane 설정
        setContentPane(loginForm_Panel);

        // Close Option 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label에 Logo Image 삽입
        lb_logo.setIcon(Util.resizeImage(new ImageIcon("res/closeheart_logo_login.png").getImage(), 200, 200, Image.SCALE_SMOOTH));

        // Window 사이즈 설정
        setSize(300, 600);

        // 사이즈 조절 불가능하도록 설정
        setResizable(false);

        // 각종 Action Event을 설정
        setEvent();

        // Pack And Visible
        setVisible(true);
    }

    /*
     * 각종 Event를 설정하는 함수
     * @author Minjae Seon
     */
    private void setEvent() {
        // Login Button Action
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Login Clicked");
            }
        });

        // Register Button Action
        btn_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Register Clicked");
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
