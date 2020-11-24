package kr.ac.gachon.sw.closeheart.client.customlayout.friendlist;

import kr.ac.gachon.sw.closeheart.client.user.User;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class FriendListRenderer implements ListCellRenderer {
    JPanel jPanel;
    public FriendListRenderer() {
        this.jPanel = new JPanel();
        jPanel.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // User Object인지 체크
        if(value instanceof User) {
            // User Object 변수 생성 후 Object 담음
            User user = (User) value;

            // BorderLayout으로 설정
            jPanel.setLayout(new BorderLayout());

            // Background Color는 White
            jPanel.setBackground(new Color(255, 255, 255));

            // LineBorder 적용
            LineBorder lineBorder = new LineBorder(Color.BLACK, 1);
            jPanel.setBorder(lineBorder);

            // 닉네임 표시 Label
            JLabel userNick = new JLabel(user.getUserNick());
            userNick.setFont(new Font(null, Font.PLAIN, 25));

            // 상태 메시지 표시 Label
            JLabel userMsg = new JLabel(user.getUserMsg());
            userMsg.setFont(new Font(null, Font.PLAIN, 15));

            // 패널에 추가
            jPanel.add(userNick, BorderLayout.NORTH);
            jPanel.add(userMsg, BorderLayout.SOUTH);

            // 선택되면 Background Color를 회색으로
            if (isSelected) {
                jPanel.setBackground(new Color(189, 189, 189));
            }
            else {
                jPanel.setBackground(new Color(255, 255, 255));
            }
        }
        return jPanel;
    }
}
