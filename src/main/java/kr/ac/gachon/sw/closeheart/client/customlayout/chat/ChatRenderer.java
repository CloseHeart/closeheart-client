package kr.ac.gachon.sw.closeheart.client.customlayout.chat;

import kr.ac.gachon.sw.closeheart.client.object.Chat;
import kr.ac.gachon.sw.closeheart.client.object.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChatRenderer extends JPanel implements ListCellRenderer<Chat> {

    public ChatRenderer() {
        this.setLayout(new BorderLayout());
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Chat value, int index, boolean isSelected, boolean cellHasFocus) {
        // 으악 여기다가 채팅 메시지 레이아웃 제작
        // 어떻게 만들지 고민중

        // Border Setting
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        // JPanel 반환
        return this;
    }
}
