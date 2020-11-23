package kr.ac.gachon.sw.closeheart.client.friend.friend;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FriendForm extends BaseForm {
    private JPanel friendForm_panel;

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

        // Close Option 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                        "서버와 연결이 끊어졌습니다!",
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
