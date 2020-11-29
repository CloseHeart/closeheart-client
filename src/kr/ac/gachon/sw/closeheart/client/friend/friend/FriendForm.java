package kr.ac.gachon.sw.closeheart.client.friend.friend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.customlayout.friendlist.FriendListModel;
import kr.ac.gachon.sw.closeheart.client.customlayout.friendlist.FriendListRenderer;
import kr.ac.gachon.sw.closeheart.client.user.User;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FriendForm extends BaseForm {
    private JPanel friendForm_panel;
    private JList<User> list_friend;
    private JLabel lb_nickname;
    private JLabel lb_statusmsg;
    private JLabel lb_covid19;
    private JButton btn_addfriend;
    private JButton btn_setting;
    private JButton btn_refresh;

    private Socket socket;
    private Scanner serverInput;
    private PrintWriter serverOutput;
    private ArrayList<User> friendList;
    private User myUserInfo;
    private FriendFormThread thread;

    public FriendForm(Socket socket, String authToken) {
        this.socket = socket;
        myUserInfo = new User("", authToken, "", "");

        // ContentPane 설정
        setContentPane(friendForm_panel);

        // Window 사이즈 설정
        setSize(500, 800);

        // 설정 아이콘 사이즈 줄이기
        btn_setting.setIcon(Util.resizeImage(new ImageIcon(getClass().getResource("/res/img/baseline_settings_black_18dp.png")).getImage(), 18, 18, Image.SCALE_SMOOTH));

        // 각종 Action Event을 설정
        setEvent();

        // 닫기 이벤트 설정
        setClosingEvent();

        // 쓰레드 시작
        startThread();
    }

    @Override
    public void setEvent() {
        // 친구 추가 버튼 액션
        btn_addfriend.addActionListener(e -> {
            System.out.println("Add Friend Clicked");
        });

        // 설정 버튼 액션
        btn_setting.addActionListener(e -> {
            System.out.println("Setting Clicked");
        });

        // 새로고침 버튼 액션
        btn_refresh.addActionListener(e -> {
            System.out.println("Refresh Clicked");
        });

        // 친구추가 버튼 액션
        btn_addfriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddFriendForm(socket, "유저토큰자리");
            }
        });
    }


    /*
     * Thread 시작
     * @author Minjae Seon
     * @param authToken 인증 토큰
     */
    private void startThread() {
        // 소켓이 잘 연결되어있고 토큰이 비어있지 않다면
        if(socket.isConnected() && !myUserInfo.getUserToken().isEmpty()) {
            try {
                // Input / Output 생성
                serverInput = new Scanner(new InputStreamReader(socket.getInputStream()));
                serverOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                // 쓰레드 관련 설정
                thread = new FriendFormThread(serverInput, serverOutput);
                thread.start();

                // 창 활성화
                this.setVisible(true);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "오류가 발생했습니다!\n오류명" + e.getMessage(),
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

    /*
     * 친구 목록 가져오기
     * @author Minjae Seon
     */
    private void getFriendList() {
        // Friend List 설정
        friendList = new ArrayList<>();

        FriendListModel friendListModel = new FriendListModel(friendList);
        FriendListRenderer friendListRenderer = new FriendListRenderer();

        // Data Model 설정
        list_friend.setModel(friendListModel);

        // Renderer 설정
        list_friend.setCellRenderer(friendListRenderer);

        // VERTICAL하게 Item이 나오도록 함
        list_friend.setLayoutOrientation(JList.VERTICAL);

        // 친구 정보 넣기 - 임시 데이터 삽입
        for(int i = 0; i < 30; i++) {
            friendList.add(new User("dd", "User" + i, "상메", true));
        }
    }

    /*
     * 닫기 버튼 Event 설정
     * @author Minjae Seon
     */
    private void setClosingEvent() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
               int exitOption = JOptionPane.showConfirmDialog(getContentPane(),
                       "정말로 종료하시겠습니까?", "종료",
                       JOptionPane.YES_NO_OPTION,
                       JOptionPane.QUESTION_MESSAGE);

               if(exitOption == JOptionPane.YES_OPTION) {
                   thread.interrupt();
               }
            }
        });
    }

    class FriendFormThread extends Thread {
        private Scanner in;
        private PrintWriter out;

        public FriendFormThread(Scanner in, PrintWriter out) {
            this.in = in;
            this.out = out;
        }

        public void run() {
            try {
                while(in.hasNextLine()) {
                    String line = in.nextLine();
                    if(line.isEmpty()) line = in.nextLine();

                    JsonObject serverInput = JsonParser.parseString(line).getAsJsonObject();
                    int code = serverInput.get("code").getAsInt();

                    // 로그아웃 코드
                    if(code == 301) {
                        break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        FriendForm.this,
                        "오류가 발생했습니다!\n오류명" + e.getMessage(),
                        "에러",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();

            // 인터럽트 발생시 로그아웃 요청
            out.println(Util.createSingleKeyValueJSON(301, "token", myUserInfo.getUserToken()));
            try {
                // 통신 관련 다 닫기
                in.close();
                out.close();
                socket.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        FriendForm.this,
                        "오류가 발생했습니다!\n오류명" + e.getMessage(),
                        "에러",
                        JOptionPane.ERROR_MESSAGE);
            }

            // 종료
            System.exit(0);
        }
    }
}
