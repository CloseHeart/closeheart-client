package kr.ac.gachon.sw.closeheart.client.friend.setting;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class PasswordForm extends BaseForm {
    private JPanel passwordForm_Panel;
    private JLabel lb_password_title;
    private JPasswordField tf_newpassword;
    private JPasswordField tf_newpassword_check;
    private JButton btn_ok;
    private JButton btn_close;
    private JLabel lb_check_password;
    private JPasswordField tf_current_password;

    private Socket socket;
    private User user;
    private PrintWriter out;

    private boolean isPasswordCorrect = false;

    public PasswordForm(Socket socket, User user) {
        this.socket = socket;
        this.user = user;

        // ContentPane 설정
        setContentPane(passwordForm_Panel);

        // Window 사이즈 설정
        setSize(350, 400);

        // 각종 Action Event을 설정
        setEvent();

        try {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void setEvent() {
        // 비밀번호 일치 체크 Action
        tf_newpassword.getDocument().addDocumentListener((Util.detectUpdateListener) e -> {
            String userPassword = Arrays.toString(tf_newpassword.getPassword());
            String checkPassword = Arrays.toString(tf_newpassword_check.getPassword());

            if(tf_newpassword.getPassword().length < 8) {
                lb_check_password.setText("8자 이상의 비밀번호를 입력하세요.");
                isPasswordCorrect = false;
            }
            else if(userPassword.equals(checkPassword)) {
                lb_check_password.setText("비밀번호가 일치합니다.");
                isPasswordCorrect = true;
            }
            else {
                lb_check_password.setText("비밀번호가 일치하지 않습니다.");
                isPasswordCorrect = false;
            }
        });

        btn_close.addActionListener(e -> {
            this.dispose();
        });

        btn_ok.addActionListener(e -> {
            String userPassword = Arrays.toString(tf_newpassword.getPassword());
            String beforePassword = Arrays.toString(tf_current_password.getPassword());

            if(!userPassword.equals(beforePassword) && isPasswordCorrect) {
                HashMap<String, Object> pwChangeMap = new HashMap<>();
                pwChangeMap.put("token", user.getUserToken());
                pwChangeMap.put("beforePassword", Util.encryptSHA512(beforePassword));
                pwChangeMap.put("newPassword", Util.encryptSHA512(userPassword));
                out.println(Util.createJSON(307, pwChangeMap));
                this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "이전에 사용하던 비밀번호와 같습니다! 다른 비밀번호를 사용해주세요.",
                        Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
