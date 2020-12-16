package kr.ac.gachon.sw.closeheart.client.chat.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.customlayout.chat.ChatRenderer;
import kr.ac.gachon.sw.closeheart.client.object.Chat;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class ChatForm extends BaseForm {
    private JPanel chatForm_Panel;
    private JList<Chat> list_chat;
    private JButton btn_exit;
    private JButton btn_send;
    private JTextArea tf_message;
    private JLabel lb_chatname;
    private JScrollPane sp_chatlist;
    private JLabel lb_code;

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private User myUser;
    private String roomNumber;

    private DefaultListModel<Chat> chatModel;
    private ChatRenderer chatRenderer;
    private int curUser = 0;
    private boolean isCreatedNow;

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

        isCreatedNow = true;

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
            if(curUser > 1) {
                if(!tf_message.getText().trim().isEmpty()) {
                    long tempTime = System.currentTimeMillis();
                    long intervalTime = tempTime - lastSendTime;
                    System.out.println(System.currentTimeMillis() + " / " + intervalTime);
                    if (intervalTime >= 800) {
                        lastSendTime = System.currentTimeMillis();
                        HashMap<String, Object> sendMsgMap = new HashMap<>();
                        sendMsgMap.put("token", myUser.getUserToken());
                        sendMsgMap.put("msg", tf_message.getText());
                        out.println(Util.createJSON(211, sendMsgMap));
                        tf_message.setText("");
                        tf_message.setCaretPosition(0);
                    } else {
                        chatModel.addElement(new Chat(2, null, "너무 빠릅니다. 잠시 후 전송하세요.", Calendar.getInstance()));
                        chatRenderer.repaint();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                sp_chatlist.getVerticalScrollBar().setValue(sp_chatlist.getVerticalScrollBar().getMaximum());
                            }
                        });
                    }
                }
            }
            else {
                chatModel.addElement(new Chat(2, null, "채팅방에 아무도 없습니다.", Calendar.getInstance()));
                chatRenderer.repaint();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        sp_chatlist.getVerticalScrollBar().setValue(sp_chatlist.getVerticalScrollBar().getMaximum());
                    }
                });
            }
        });
    }

    private void initialSetting() {
        lb_code.setText("채팅방 코드 : " + roomNumber);
        lastSendTime = 0;
        try {
            in = new Scanner(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            HashMap<String, Object> connectMap = new HashMap<>();
            connectMap.put("token", myUser.getUserToken());
            connectMap.put("nickName", myUser.getUserMsg());
            connectMap.put("roomNumber", roomNumber);
            out.println(Util.createJSON(210, connectMap));

            setChat();

            Thread thread = new ChatFormThread(in, out);
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

        tf_message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            tf_message.setCaretPosition(0);
                            tf_message.setText("");
                        }
                    });

                    btn_send.doClick();
                }
            }
        });
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
        private Scanner in;
        private PrintWriter out;

        public ChatFormThread(Scanner in, PrintWriter out) {
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
                            updateUserList(serverInput.get("userlist").getAsString());
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
                            updateUserList(serverInput.get("userlist").getAsString());
                            break;
                    }

                    // 새 채팅 적용
                    chatRenderer.repaint();

                    // 자동 스크롤
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            sp_chatlist.getVerticalScrollBar().setValue(sp_chatlist.getVerticalScrollBar().getMaximum());
                        }
                    });
                }
            }
            catch (IllegalStateException e) {

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void updateUserList(String listStr) {
            curUser = 0;
            StringBuilder userList = new StringBuilder("");
            JsonArray jsonArray = JsonParser.parseString(listStr).getAsJsonArray();

            Iterator<JsonElement> userArray = jsonArray.iterator();
            while(userArray.hasNext()) {
                curUser++;
                userList.append(userArray.next().getAsString());
                if(userArray.hasNext()) {
                    userList.append(", ");
                }
            }

            lb_chatname.setText(userList.toString());

            if(!isCreatedNow && curUser == 1) {
                try {
                    out.println(Util.createSingleKeyValueJSON(212, "token", myUser.getUserToken()));
                    out.close();
                    in.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ChatForm.this.dispose();
            }

            if(isCreatedNow) {
                isCreatedNow = false;
            }
        }
    }
}
