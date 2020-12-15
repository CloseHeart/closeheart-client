package kr.ac.gachon.sw.closeheart.client.customlayout.chat;

import kr.ac.gachon.sw.closeheart.client.object.Chat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChatRenderer extends JPanel implements ListCellRenderer<Chat> {
    JPanel chatPanel = new JPanel();
    JLabel chatSender = new JLabel();
    JPanel chatMsgPanel = new JPanel();

    JTextArea chatMessage = new JTextArea();

    public ChatRenderer() {
        this.setLayout(new BorderLayout());
        chatPanel.setLayout(new BorderLayout());
        chatMsgPanel.setLayout(new BorderLayout());

        chatPanel.add(chatSender, BorderLayout.NORTH);
        chatPanel.add(chatMsgPanel, BorderLayout.SOUTH);
        chatMsgPanel.add(chatMessage, BorderLayout.CENTER);
        chatSender.setOpaque(false);
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Chat value, int index, boolean isSelected, boolean cellHasFocus) {
        chatMessage.setRows(value.getChatMsg().length() / 20);
        chatMessage.setColumns(20);

        chatMessage.setEditable(false);
        chatMessage.setLineWrap(true);
        chatMessage.setWrapStyleWord(true);

        // Border Setting
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        chatSender.setBorder(new EmptyBorder(5, 5, 5, 5));
        chatMessage.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Background Color는 Blue
        this.setBackground(new Color(178, 199, 217));
        chatPanel.setBackground(new Color(178, 199, 217));

        // 채팅 전송자 닉네임
        if(value != null) chatSender.setText(value.getChatOwner());

        // 채팅 내용
        if(value != null) chatMessage.setText(value.getChatMsg());

        // Chat Type 0 -> 본인 메시지
        // Chat Type 1 -> 타인 메시지
        // Chat Type 2 -> 공통 메시지
        if(value.getChatType() == 0) {
            // 오른쪽
            this.add(chatPanel, BorderLayout.EAST);

            // 오렌지
            chatMsgPanel.setBackground(Color.ORANGE);
            chatMessage.setBackground(Color.ORANGE);
        }
        else if (value.getChatType() == 1) {
            // 왼쪽
            this.add(chatPanel, BorderLayout.WEST);

            // 화이트
            chatMsgPanel.setBackground(Color.WHITE);
            chatMessage.setBackground(Color.WHITE);
        }
        else {
            // 왼쪽
            this.add(chatPanel, BorderLayout.WEST);

            // 밝은 회색
            chatMsgPanel.setBackground(Color.LIGHT_GRAY);
            chatMessage.setBackground(Color.LIGHT_GRAY);
            chatSender.setText("");
        }
        // JPanel 반환
        return this;
    }
}
