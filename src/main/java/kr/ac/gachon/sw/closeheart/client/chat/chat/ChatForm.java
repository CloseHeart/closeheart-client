package kr.ac.gachon.sw.closeheart.client.chat.chat;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.customlayout.chat.ChatModel;
import kr.ac.gachon.sw.closeheart.client.customlayout.chat.ChatRenderer;
import kr.ac.gachon.sw.closeheart.client.object.Chat;
import kr.ac.gachon.sw.closeheart.client.object.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class ChatForm extends BaseForm {
    private JPanel chatForm_Panel;
    private JList<Chat> list_chat;
    private JButton btn_exit;
    private JButton btn_send;
    private JTextArea tf_message;

    private Socket socket;
    private User myUser;
    private ArrayList<User> friendUser;
    private ArrayList<Chat> chatList;

    public ChatForm(Socket socket, User myUser, ArrayList<User>  friendUser) {
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
        setChat();

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

    // 테스트용
    private void setChat() {
        // Friend List 설정
        chatList = new ArrayList<>();

        ChatModel chatModel = new ChatModel(chatList);
        ChatRenderer chatRenderer = new ChatRenderer();

        // Data Model 설정
        list_chat.setModel(chatModel);

        // Renderer 설정
        list_chat.setCellRenderer(chatRenderer);

        // VERTICAL하게 Item이 나오도록 함
        list_chat.setLayoutOrientation(JList.VERTICAL);

        // 친구 정보 넣기 - 임시 데이터 삽입
        int t = 0;
        for(int i = 0; i < 30; i++) {
            User user = new User("id", "Nick " + i, "dd", true);
            chatList.add(new Chat( t, user, "좀 긴 메시지 asdfdsadfasdfasdfasdfsadfasdadfsafdsfdafsdaasfdasfdasfdasdfasdfasdffsd", Calendar.getInstance()));
            if(t == 0) t = 1;
            else t = 0;
        }
    }

    private void exitChatRoom() {
        int exitOption = JOptionPane.showConfirmDialog(getContentPane(),
                "정말로 채팅방을 나가시겠습니까?", "종료",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(exitOption == JOptionPane.YES_OPTION) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.dispose();
        }
    }

    class ChatFormThread extends Thread {
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        public ChatFormThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                while(in.hasNextLine()) {

                }
            }
            catch (Exception e) {

            }
        }
    }
}
