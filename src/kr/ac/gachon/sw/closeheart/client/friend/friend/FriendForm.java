package kr.ac.gachon.sw.closeheart.client.friend.friend;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.customlayout.friendlist.FriendListModel;
import kr.ac.gachon.sw.closeheart.client.customlayout.friendlist.FriendListRenderer;
import kr.ac.gachon.sw.closeheart.client.user.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class FriendForm extends BaseForm {
    private JPanel friendForm_panel;
    private JList list_friend;
    private JButton btn_setting;
    private JLabel lb_nickname;
    private JLabel lb_email;
    private JLabel lb_statusmsg;
    private JLabel lb_covid19;

    private Socket socket;
    private Scanner serverInput;
    private PrintWriter serverOutput;
    private String authToken;

    public FriendForm(Socket socket, String authToken) {
        this.socket = socket;
        this.authToken = authToken;

        // ContentPane 설정
        setContentPane(friendForm_panel);

        // Window 사이즈 설정
        setSize(500, 800);

        // 각종 Action Event을 설정
        setEvent();

        // Friend List 설정
        FriendListModel friendListModel = new FriendListModel();
        FriendListRenderer friendListRenderer = new FriendListRenderer();

        // 친구 정보 넣기 - 임시 데이터 삽입
        for(int i = 0; i < 30; i++) {
            friendListModel.add(new User("dd", "User", "상메", true));
        }

        // Data Model 설정
        list_friend.setModel(friendListModel);

        // Renderer 설정
        list_friend.setCellRenderer(friendListRenderer);

        // VERTICAL하게 Item이 나오도록 함
        list_friend.setLayoutOrientation(JList.VERTICAL);



        // 소켓이 잘 연결되어있고 토큰이 비어있지 않다면
        if(socket.isConnected() && !authToken.isEmpty()) {
            try {
                // Input / Output 생성
                serverInput = new Scanner(new InputStreamReader(socket.getInputStream()));
                serverOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                // 창 활성화
                this.setVisible(true);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "오류가 발생했습니다!\n오류명" + e.getMessage(),
                        Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(
                    this,
                    "서버와 연결이 끊어졌습니다!",
                    Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void setEvent() {

    }
}
