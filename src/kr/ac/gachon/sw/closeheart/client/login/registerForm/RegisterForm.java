package kr.ac.gachon.sw.closeheart.client.login.registerForm;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/*
 * 회원가입 Form
 * @author Minjae Seon
 */
public class RegisterForm extends BaseForm {
    private JPanel RegisterForm_Panel;
    private JTextField tf_email;
    private JTextField tf_nickname;
    private JPasswordField tf_password;
    private JSpinner sp_bdayyear;
    private JSpinner sp_bdaymonth;
    private JSpinner sp_bdayday;
    private JButton btn_emailcheck;
    private JButton btn_nickcheck;
    private JButton btn_register;
    private JButton btn_cancel;
    private JPasswordField tf_pwcheck;
    private JLabel lb_register;
    private JLabel lb_email;
    private JLabel lb_password;
    private JLabel lb_pwcheck;
    private JLabel lb_pwcheckresult;
    private JLabel lb_nickname;
    private JLabel lb_birthday;

    private boolean isPasswordCorrect = false;

    public RegisterForm() {
        // ContentPane 설정
        setContentPane(RegisterForm_Panel);

        // Window 사이즈 설정
        setSize(400, 400);

        // 각종 Action Event을 설정
        setEvent();
    }

    /*
     * 각종 Event를 설정하는 함수
     * @author Minjae Seon
     */
    public void setEvent() {
        // 회원가입 버튼 Action
        btn_register.addActionListener(e -> {
            if(tf_password.getPassword().length < 8) {
                JOptionPane.showMessageDialog(this, "비밀번호는 8자 이상이여야 합니다!", "오류", JOptionPane.ERROR_MESSAGE);
            }
            else if(!isPasswordCorrect) {
                JOptionPane.showMessageDialog(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            }
            else {
                // 회원가입 처리
            }
        });

        // 취소 버튼 Action
        btn_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });



        // 비밀번호 일치 체크 Action
        tf_pwcheck.getDocument().addDocumentListener((Util.detectUpdateListener) e -> {
            String userPassword = Arrays.toString(tf_password.getPassword());
            String checkPassword = Arrays.toString(tf_pwcheck.getPassword());

            if(tf_password.getPassword().length < 8) {
                lb_pwcheckresult.setText("8자 이상의 비밀번호를 입력하세요.");
                isPasswordCorrect = false;
            }
            else if(userPassword.equals(checkPassword)) {
                lb_pwcheckresult.setText("비밀번호가 일치합니다.");
                isPasswordCorrect = true;
            }
            else {
                lb_pwcheckresult.setText("비밀번호가 일치하지 않습니다.");
                isPasswordCorrect = false;
            }
        });
    }
}