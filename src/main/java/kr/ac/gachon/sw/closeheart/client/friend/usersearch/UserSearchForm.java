package kr.ac.gachon.sw.closeheart.client.friend.addfriend;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.customlayout.friendlist.FriendListRenderer;
import kr.ac.gachon.sw.closeheart.client.object.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class UserSearchForm extends BaseForm {
    private JTextField tf_userid;
    private JButton btn_search;
    private JButton btn_close;
    private JPanel userSearchForm_Panel;
    private JLabel lb_addfriend_title;
    private JLabel lb_userid_label;
    private Socket friendServerSocket;
    private User user;

    public JList<User> list_search;
    private FriendListRenderer searchFormRenderer;
    private DefaultListModel<User> searchFormModel;

    public UserSearchForm(Socket friendServerSocket, User user)  {
        this.friendServerSocket = friendServerSocket;
        this.user = user;

        // ContentPane 설정
        setContentPane(userSearchForm_Panel);

        // Window 사이즈 설정
        setSize(500, 500);

        // 각종 Action Event을 설정
        setEvent();

        searchFormModel = new DefaultListModel<>();
        searchFormRenderer = new FriendListRenderer();

        list_search.setModel(searchFormModel);
        list_search.setCellRenderer(searchFormRenderer);
        list_search.setLayoutOrientation(JList.VERTICAL);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.getRootPane().setDefaultButton(btn_search);
    }

    @Override
    public void setEvent() {
        btn_search.addActionListener(e -> {
            if (!tf_userid.getText().isEmpty()) {
                try {
                    PrintWriter output = new PrintWriter(new OutputStreamWriter(friendServerSocket.getOutputStream()), true);

                    // 해당 유저 정보 요청 JSON 생성
                    HashMap<String, Object> requestFriendMap = new HashMap<>();
                    requestFriendMap.put("token", user.getUserToken());
                    requestFriendMap.put("searchStr", tf_userid.getText());
                    String requestFriendJSON = Util.createJSON(311, requestFriendMap);

                    // 요청 전송
                    output.println(requestFriendJSON);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "에러가 발생했습니다!\n에러 내용 : " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "아이디를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        btn_close.addActionListener(e -> {
            this.dispose();
        });
    }

    public void addElement(User user) {
        searchFormModel.addElement(user);
    }

    public void repaint() {
        searchFormRenderer.repaint();
    }

    public void clear() {
        searchFormModel.clear();
    }
}
