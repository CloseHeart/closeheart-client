package kr.ac.gachon.sw.closeheart.client.friend.setting;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;

import javax.swing.*;
import java.text.SimpleDateFormat;

public class FriendInfoForm extends BaseForm {
    private JPanel friendInfoForm_Panel;
    private JLabel lb_friendinfo_title;
    private JLabel lb_id_label;
    private JLabel lb_email_label;
    private JLabel lb_email;
    private JLabel lb_id;
    private JLabel lb_birthday_label;
    private JLabel lb_nick_label;
    private JLabel lb_nick;
    private JLabel lb_birthday;
    private JButton btn_close;
    private JLabel lb_lasttime_label;
    private JLabel lb_lasttime;

    private User friendUser;
    public FriendInfoForm(User friendUser) {
        this.friendUser = friendUser;

        // ContentPane 설정
        setContentPane(friendInfoForm_Panel);

        // Window 사이즈 설정
        setSize(400, 300);

        // 각종 Action Event을 설정
        setEvent();

        setUserInfo();

        this.setVisible(true);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void setEvent() {
        btn_close.addActionListener(e -> {
            this.dispose();
        });
    }

    private void setUserInfo() {
        SimpleDateFormat bdayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat lastTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        lb_nick.setText(friendUser.getUserNick());
        lb_id.setText(friendUser.getUserID());
        lb_email.setText(friendUser.getUserEmail());
        lb_birthday.setText(bdayFormat.format(friendUser.getUserBirthday()));
        lb_lasttime.setText(lastTimeFormat.format(friendUser.getUserLastTime()));
    }
}
