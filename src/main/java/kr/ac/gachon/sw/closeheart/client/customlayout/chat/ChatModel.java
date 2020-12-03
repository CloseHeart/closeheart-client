package kr.ac.gachon.sw.closeheart.client.customlayout.chat;

import kr.ac.gachon.sw.closeheart.client.object.Chat;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

public class ChatModel implements ListModel<Chat> {
    private ArrayList<Chat> chatList;

    public ChatModel(ArrayList<Chat> friendList) {
        this.chatList = friendList;
    }

    @Override
    public int getSize() {
        return chatList.size();
    }

    @Override
    public Chat getElementAt(int index) {
        return chatList.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

    public void add(Chat chat) {
        chatList.add(chat);
    }

    public void remove(int index) {
        chatList.remove(index);
    }
}
