package kr.ac.gachon.sw.closeheart.client.chat.chat;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.object.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

public class ChatForm extends BaseForm {
    private JPanel chatForm_Panel;
    private JList list_chat;
    private JButton btn_exit;
    private JTextArea tf_messageinput;
    private JButton btn_send;

    private Socket socket;
    private User myUser;
    private User[] friendUser;

    public ChatForm(Socket socket, User myUser, User[] friendUser) {
        this.socket = socket;
        this.myUser = myUser;
        this.friendUser = friendUser;

        // ContentPane 설정
        setContentPane(chatForm_Panel);

        // Window 사이즈 설정
        setSize(500, 800);


        // 각종 Action Event을 설정
        setEvent();

        setVisible(true);

        // 친구 초대는 ChatForm에서 이루어져야 함
        // 그 전에! 무조건 친구폼에서 온라인인지 체크 필수!!!!

        // 엔터키 누르면 채팅 전송
        this.getRootPane().setDefaultButton(btn_send);
    }

    @Override
    public void setEvent() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                exitChatRoom();
            }
        });

        btn_exit.addActionListener(e -> {
            exitChatRoom();
        });

    }

    private void exitChatRoom() {
        int exitOption = JOptionPane.showConfirmDialog(getContentPane(),
                "정말로 채팅방을 나가시겠습니까?", "종료",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if(exitOption == JOptionPane.YES_OPTION) {
            // 나갈 때 이벤트
            try {
                socket.close();
            } catch (Exception e) {

            }
            this.dispose();
        }
    }
}
