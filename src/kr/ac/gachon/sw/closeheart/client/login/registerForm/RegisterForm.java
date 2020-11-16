package kr.ac.gachon.sw.closeheart.client.login.registerForm;

import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.*;

public class RegisterForm {
    private JPanel RegisterForm_Panel;

    public static void main(String[] args) {
        // Font Setting
        Util.changeUIFont(new Font("NanumGothic", Font.PLAIN, 13));

        // Frame Setting
        RegisterForm registerForm = new RegisterForm();
        JFrame frame = new JFrame();
        frame.setTitle("마음을 가까이");
        frame.setContentPane(registerForm.RegisterForm_Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 600);
        frame.setResizable(false);

        // Pack And Visible
        frame.pack();
        frame.setVisible(true);
    }
}
