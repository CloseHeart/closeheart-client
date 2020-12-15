package kr.ac.gachon.sw.closeheart.client.friend.friend;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;

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
    private JButton btn_friendadd;

    private boolean isFriend;
    private PrintWriter printWriter;

    private User myUser;
    private User friendUser;

    public FriendInfoForm(User myUser, User friendUser, PrintWriter printWriter) {
        this.myUser = myUser;
        this.friendUser = friendUser;
        this.isFriend = isFriend;
        this.printWriter = printWriter;

        // ContentPane 설정
        setContentPane(friendInfoForm_Panel);

        // Window 사이즈 설정
        setSize(400, 300);

        // 각종 Action Event을 설정
        setEvent();

        setUserInfo();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void setEvent() {
        btn_close.addActionListener(e -> {
            this.dispose();
        });

        btn_friendadd.addActionListener(e -> {
            boolean isFriend = false;
            for(User friend :  myUser.getFriends()) {
                if(friend.getUserID().equals(lb_id.getText())) {
                    isFriend = true;
                    break;
                }
            }

            if(!isFriend) {
                // 친구 요청 JSON 생성
                HashMap<String, Object> requestFriendMap = new HashMap<>();
                requestFriendMap.put("token", myUser.getUserToken());
                requestFriendMap.put("requestID", lb_id.getText());
                String requestFriendJSON = Util.createJSON(302, requestFriendMap);

                // 요청 전송
                printWriter.println(requestFriendJSON);
            }
            else {
                JOptionPane.showMessageDialog(this, "이미 친구인 유저입니다.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void setUserInfo() {
        SimpleDateFormat bdayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat lastTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        lb_nick.setText(friendUser.getUserNick());
        lb_id.setText(friendUser.getUserID());
        lb_email.setText(friendUser.getUserEmail());
        lb_birthday.setText(bdayFormat.format(friendUser.getUserBirthday()));
        lb_lasttime.setText(lastTimeFormat.format(friendUser.getUserLastTime()));
    }
}
