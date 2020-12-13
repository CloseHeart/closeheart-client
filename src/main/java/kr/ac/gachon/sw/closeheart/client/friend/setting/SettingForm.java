package kr.ac.gachon.sw.closeheart.client.friend.setting;

import com.github.lgooddatepicker.components.DatePicker;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

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
    private JLabel lb_statusmsg_label;
    private JTextField tf_statusmsg;
    private JButton btn_close;

    private Socket socket;
    private User user;
    private PrintWriter out;
    private PasswordForm passwordForm;

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
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUserData();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void setEvent() {
        btn_save.addActionListener(e -> {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                HashMap<String, Object> infoChange = new HashMap<>();

                if (!user.getUserNick().equals(tf_nick.getText())) {
                    infoChange.put("requestNick", tf_nick.getText());
                    user.setUserNick(tf_nick.getText());
                }
                if (!user.getUserMsg().equals(tf_statusmsg.getText())) {
                    infoChange.put("requestMSG", tf_statusmsg.getText());
                    user.setUserMsg(tf_statusmsg.getText());
                }
                if(!simpleDateFormat.format(user.getUserBirthday()).equals(dp_birthday.getDate().format(DateTimeFormatter.ISO_DATE))) {
                    infoChange.put("requestBirth", dp_birthday.getDate().format(DateTimeFormatter.ISO_DATE));
                    user.setUserBirthday(Date.from(dp_birthday.getDate().atStartOfDay(TimeZone.getDefault().toZoneId()).toInstant()));
                }

                if(infoChange.size() > 0) {
                    infoChange.put("token", user.getUserToken());
                    infoChange.put("requestID", user.getUserID());
                    out.println(Util.createJSON(306, infoChange));
                }
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        passwordForm = new PasswordForm(socket, user);
        btn_changepw.addActionListener(e -> {
            passwordForm.setVisible(true);
        });

        btn_close.addActionListener(e -> {
            passwordForm.dispose();
            this.dispose();
        });
    }

    private void setUserData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate bday = LocalDate.parse(simpleDateFormat.format(user.getUserBirthday()));

        lb_id.setText(user.getUserID());
        lb_email.setText(user.getUserEmail());
        tf_statusmsg.setText(user.getUserMsg());
        tf_nick.setText(user.getUserNick());
        dp_birthday.setDate(bday);
    }
}
