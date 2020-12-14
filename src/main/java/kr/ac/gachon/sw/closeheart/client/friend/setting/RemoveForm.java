package kr.ac.gachon.sw.closeheart.client.friend.setting;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.io.PrintWriter;
import java.util.HashMap;

public class RemoveForm extends BaseForm {
    private JPanel removeForm_Panel;
    private JPasswordField tf_password;
    private JLabel lb_password_label;
    private JButton btn_remove;
    private JButton btn_close;
    private JLabel lb_remove_title;
    private JLabel lb_notice1;
    private JLabel lb_notice2;
    private JLabel lb_notice3;

    private User myUser;
    private PrintWriter out;

    public RemoveForm(User myUser, PrintWriter out) {
        this.myUser = myUser;
        this.out = out;

        // ContentPane 설정
        setContentPane(removeForm_Panel);

        // Window 사이즈 설정
        setSize(400, 300);

        // 각종 Action Event을 설정
        setEvent();

        this.setVisible(true);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void setEvent() {
        btn_close.addActionListener(e -> {
            this.dispose();
        });

        btn_remove.addActionListener(e -> {
            String pwText = String.valueOf(tf_password.getPassword());

            if(pwText.length() >= 8) {
                String password = Util.encryptSHA512(pwText);
                HashMap<String, Object> removeMap = new HashMap<>();
                removeMap.put("token", myUser.getUserToken());
                removeMap.put("password", password);
                out.println(Util.createJSON(312, removeMap));
                this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(
                        null,
                        "올바른 비밀번호를 입력해주세요!",
                        "에러",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
