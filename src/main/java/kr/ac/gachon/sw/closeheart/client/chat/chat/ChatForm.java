package kr.ac.gachon.sw.closeheart.client.chat.chat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.customlayout.chat.ChatRenderer;
import kr.ac.gachon.sw.closeheart.client.object.Chat;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

public class ChatForm extends BaseForm {
    private JPanel chatForm_Panel;
    private JList<Chat> list_chat;
    private JButton btn_exit;
    private JButton btn_send;
    private JTextArea tf_message;
    private JLabel lb_chatname;

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private User myUser;
    private String roomNumber;

    private DefaultListModel<Chat> chatModel;
    private ChatRenderer chatRenderer;

    private long lastSendTime;

    public ChatForm(String ipAddress, int portNumber, User myUser, String roomNumber) {
        this.myUser = myUser;
        this.roomNumber = roomNumber;

        // ContentPane 설정
        setContentPane(chatForm_Panel);

        // Window 사이즈 설정
        setSize(500, 800);

        // 각종 Action Event을 설정
        setEvent();

        try {
            this.socket = new Socket(ipAddress, portNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);

        // 초기 설정
        initialSetting();

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

        btn_send.addActionListener(e -> {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - lastSendTime;
            System.out.println(System.currentTimeMillis() + " / " + intervalTime);
            if(intervalTime >= 2000) {
                lastSendTime = System.currentTimeMillis();
                HashMap<String, Object> sendMsgMap = new HashMap<>();
                sendMsgMap.put("token", myUser.getUserToken());
                sendMsgMap.put("msg", tf_message.getText());
                tf_message.setText("");
                out.println(Util.createJSON(211, sendMsgMap));
            }
            else {
                chatModel.addElement(new Chat(2, null, "채팅은 2초마다 보낼 수 있습니다!", Calendar.getInstance()));
                list_chat.ensureIndexIsVisible(chatModel.getSize());
            }
        });
    }

    private void initialSetting() {
        lb_chatname.setText(roomNumber);
        lastSendTime = System.currentTimeMillis();
        try {
            in = new Scanner(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            HashMap<String, Object> connectMap = new HashMap<>();
            connectMap.put("token", myUser.getUserToken());
            connectMap.put("nickName", myUser.getUserMsg());
            connectMap.put("roomNumber", roomNumber);
            out.println(Util.createJSON(210, connectMap));

            setChat();

            Thread thread = new ChatFormThread(socket, in, out);
            thread.start();
        } catch (Exception e) {

        }
    }

    // 채팅 설정
    private void setChat() {
        chatModel = new DefaultListModel<Chat>();
        chatRenderer = new ChatRenderer();

        // Data Model 설정
        list_chat.setModel(chatModel);

        // Renderer 설정
        list_chat.setCellRenderer(chatRenderer);

        // VERTICAL하게 Item이 나오도록 함
        list_chat.setLayoutOrientation(JList.VERTICAL);
    }

    private void exitChatRoom() {
        int exitOption = JOptionPane.showConfirmDialog(getContentPane(),
                "정말로 채팅방을 나가시겠습니까?", "종료",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(exitOption == JOptionPane.YES_OPTION) {
            try {
                out.println(Util.createSingleKeyValueJSON(212, "token", myUser.getUserToken()));

                out.close();
                in.close();

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

        public ChatFormThread(Socket socket, Scanner in, PrintWriter out) {
            this.socket = socket;
            this.in = in;
            this.out = out;
        }

        public void run() {
            try {
                while(in.hasNextLine()) {
                    String line = in.nextLine();
                    if(line.isEmpty()) line = in.nextLine();

                    JsonObject serverInput = JsonParser.parseString(line).getAsJsonObject();
                    System.out.println(serverInput);

                    String type = serverInput.get("type").getAsString();
                    String user = serverInput.get("user").getAsString();

                    // 접속 처리
                    switch (type) {
                        case "join":
                            System.out.println(user + " join");
                            chatModel.addElement(new Chat(2, user, user + "님이 입장하셨습니다.", Calendar.getInstance()));
                            break;
                        // 메시지 처리
                        case "message":
                            String msg = serverInput.get("msg").getAsString();
                            if(!user.equals(myUser.getUserNick())) chatModel.addElement(new Chat(1, user, msg, Calendar.getInstance()));
                            else chatModel.addElement(new Chat(0, user, msg, Calendar.getInstance()));
                            break;
                        // 퇴장 처리
                        case "exit":
                            System.out.println(user + " exit");
                            chatModel.addElement(new Chat(2, user, user + "님이 퇴장하셨습니다.", Calendar.getInstance()));
                            break;
                    }
                    chatRenderer.repaint();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
