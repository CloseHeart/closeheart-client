package kr.ac.gachon.sw.closeheart.client.friend.addfriend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class AddFriendForm extends BaseForm {
    private JTextField tf_requestID;
    private JButton btn_addfriend;
    private JButton btn_close;
    private JPanel addfriendForm_Panel;
    private JLabel lb_addfriend_title;
    private JLabel lb_friendid;
    private Socket friendServerSocket;
    private User user;

    public AddFriendForm(Socket friendServerSocket, User user) {
        this.friendServerSocket = friendServerSocket;
        this.user = user;

        // ContentPane 설정
        setContentPane(addfriendForm_Panel);

        // Window 사이즈 설정
        setSize(300, 150);

        // 각종 Action Event을 설정
        setEvent();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setVisible(true);
    }

    @Override
    public void setEvent() {
        btn_addfriend.addActionListener(e -> {
            if(!tf_requestID.getText().equals(user.getUserID())) {
                if (!tf_requestID.getText().isEmpty()) {
                    try {
                        PrintWriter output = new PrintWriter(new OutputStreamWriter(friendServerSocket.getOutputStream()), true);

                        // 친구 요청 JSON 생성
                        HashMap<String, String> requestFriendMap = new HashMap<>();
                        requestFriendMap.put("token", user.getUserToken());
                        requestFriendMap.put("requestID", tf_requestID.getText());
                        String requestFriendJSON = Util.createJSON(302, requestFriendMap);

                        // 요청 전송
                        output.println(requestFriendJSON);
                        this.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "에러가 발생했습니다!\n에러 내용 : " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(this, "아이디를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "본인에게는 친구 요청을 보낼 수 없습니다!", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
