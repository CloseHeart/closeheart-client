package kr.ac.gachon.sw.closeheart.client.friend.newchat;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewChatForm extends BaseForm {
    private JPanel NewChatForm_Panel;
    private JLabel lb_newchat_title;
    private JButton btn_go;
    private JTextField tf_code;
    private JButton btn_close;
    private JLabel lb_code_label;

    private User user;
    private PrintWriter out;

    public NewChatForm(User user, PrintWriter out) {
        this.user = user;
        this.out = out;

        // ContentPane 설정
        setContentPane(NewChatForm_Panel);

        // Window 사이즈 설정
        pack();

        // 각종 Action Event을 설정
        setEvent();

        this.getRootPane().setDefaultButton(btn_go);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void setEvent() {
        btn_close.addActionListener(e -> {
            this.dispose();
        });

        btn_go.addActionListener(e -> {
            Pattern idPattern = Pattern.compile("^[a-zA-Z0-9]{8}$");
            Matcher matcher = idPattern.matcher(tf_code.getText());
            if(tf_code.getText().length() == 8 && matcher.find()) {
                try {
                    HashMap<String, Object> newChatMap = new HashMap<>();
                    newChatMap.put("token", user.getUserToken());
                    newChatMap.put("roomNumber", tf_code.getText());
                    out.println(Util.createJSON(313, newChatMap));
                    this.dispose();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(
                            this,
                            "오류가 발생했습니다! 잠시 후 다시 시도해주세요.\n에러명 : " + exception.getMessage(),
                            Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                            JOptionPane.ERROR_MESSAGE);
                    this.dispose();
                }
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "방 코드는 알파벳 및 숫자로 이루어진 8글자만 가능합니다.",
                        "알림",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
