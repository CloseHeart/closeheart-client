package kr.ac.gachon.sw.closeheart.client.friend.setting;

import com.github.lgooddatepicker.components.DatePicker;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SettingForm extends BaseForm {
    private JPanel settingForm_Panel;
    private JTextField tf_nick;
    private JLabel lb_email;
    private JLabel lb_id;
    private JLabel lb_nick_label;
    private JLabel lb_birthday_label;
    private JButton btn_save;
    private JButton btn_changepw;
    private JLabel lb_id_label;
    private JLabel lb_email_label;
    private DatePicker dp_birthday;
    private JButton btn_developer;
    private JTextField tf_statusmsg;
    private JPasswordField tf_currentpw;
    private JLabel lb_currentpw_label;
    private JLabel lb_newpw_label;
    private JPasswordField tf_newpw;
    private JPasswordField tf_newpw_check;
    private JLabel lb_newpw_check_label;
    private JLabel lb_newpw_check_msg;
    private JButton btn_removeid;

    private Socket socket;
    private User user;
    private PrintWriter out;
    private RemoveForm removeForm;
    private DeveloperForm developerForm;
    private boolean isPasswordCorrect = false;

    public SettingForm(Socket socket, User user) {
        this.socket = socket;
        this.user = user;

        // ContentPane 설정
        setContentPane(settingForm_Panel);

        // Window 사이즈 설정
        setSize(350, 400);

        // 각종 Action Event을 설정
        setEvent();

        try {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUserData();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    @Override
    public void setEvent() {
        btn_save.addActionListener(e -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(user.getUserNick().equals(tf_nick.getText())
                    && simpleDateFormat.format(user.getUserBirthday()).equals(dp_birthday.getDate().format(DateTimeFormatter.ISO_DATE))
                    && String.valueOf(tf_newpw.getPassword()).isEmpty()) {
                this.dispose();
            }
            else {
                try {
                    if (tf_currentpw.getPassword().length < 8) {
                        JOptionPane.showMessageDialog(
                                this,
                                "정보를 변경하시려면 현재 비밀번호는 필수적으로 입력되어야 합니다.",
                                "알림",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    HashMap<String, Object> infoChange = new HashMap<>();

                    // 현재 비번
                    String currentPW = String.valueOf(tf_currentpw.getPassword());

                    // 이전 비번
                    String newPW = String.valueOf(tf_newpw.getPassword());

                    // 닉네임 text가 비어있지 않고 현재랑 동일하지 않으면 전송
                    if (!tf_nick.getText().isEmpty() && !user.getUserNick().equals(tf_nick.getText())) {
                        infoChange.put("requestNick", tf_nick.getText());
                    }

                    // 원래 생일과 이전 날짜가 동일하지 않으면 전송
                    if (!simpleDateFormat.format(user.getUserBirthday()).equals(dp_birthday.getDate().format(DateTimeFormatter.ISO_DATE))) {
                        infoChange.put("requestBirth", dp_birthday.getDate().format(DateTimeFormatter.ISO_DATE));
                    }

                    // 현재 비밀번호와 새 비밀번호가 다르고 비밀번호 확인이 되었으면 전송
                    if (!currentPW.equals(newPW) && isPasswordCorrect) {
                        infoChange.put("requestPW", Util.encryptSHA512(newPW));
                    }

                    // HashMap에 들어간게 있다면
                    if (infoChange.size() > 0) {
                        // 토큰 및 현재 비밀번호도 담아서 306으로 전송
                        infoChange.put("token", user.getUserToken());
                        infoChange.put("currentPW", Util.encryptSHA512(currentPW));
                        out.println(Util.createJSON(306, infoChange));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        // 비밀번호 일치 체크 Action
        tf_newpw_check.getDocument().addDocumentListener((Util.detectUpdateListener) e -> {
            String userPassword = Arrays.toString(tf_newpw.getPassword());
            String checkPassword = Arrays.toString(tf_newpw_check.getPassword());

            if(tf_newpw.getPassword().length < 8) {
                lb_newpw_check_msg.setText("8자 이상의 비밀번호를 입력하세요.");
                isPasswordCorrect = false;
            }
            else if(userPassword.equals(checkPassword)) {
                lb_newpw_check_msg.setText("비밀번호가 일치합니다.");
                isPasswordCorrect = true;
            }
            else {
                lb_newpw_check_msg.setText("비밀번호가 일치하지 않습니다.");
                isPasswordCorrect = false;
            }
        });

        btn_developer.addActionListener(e -> {
            if(developerForm == null) developerForm = new DeveloperForm();
            developerForm.setVisible(true);
        });

        btn_removeid.addActionListener(e -> {
            if(removeForm == null) {
                removeForm = new RemoveForm(user, out);
            }
            removeForm.setVisible(true);
        });
    }

    private void setUserData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate bday = LocalDate.parse(simpleDateFormat.format(user.getUserBirthday()));

        lb_id.setText(user.getUserID());
        lb_email.setText(user.getUserEmail());
        tf_nick.setText(user.getUserNick());
        dp_birthday.setDate(bday);
    }
}
