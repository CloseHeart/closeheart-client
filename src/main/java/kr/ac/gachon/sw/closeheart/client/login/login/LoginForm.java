package kr.ac.gachon.sw.closeheart.client.login.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.login.find.FindForm;
import kr.ac.gachon.sw.closeheart.client.object.ConnectionInfo;
import kr.ac.gachon.sw.closeheart.client.friend.friend.FriendForm;
import kr.ac.gachon.sw.closeheart.client.login.register.RegisterForm;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

/*
 * 로그인 Form
 * @author Minjae Seon
 */
public class LoginForm extends BaseForm {
    private JPanel loginForm_Panel;
    private JTextField tf_id;
    private JPasswordField tf_pw;
    private JButton btn_register;
    private JButton btn_login;
    private JLabel lb_forgotpassword;
    private JLabel lb_logo;
    private JCheckBox cb_saveid;
    private JButton btn_findpw;
    private Socket loginServerSocket;
    private Scanner serverInput;
    private PrintWriter serverOutput;
    private ConnectionInfo info;

    public LoginForm(ConnectionInfo connectionInfo) {
        // 파일 읽어오기
        try{
            FileReader rw = new FileReader("saveId.txt");
            BufferedReader br = new BufferedReader( rw );
            String readLine = null; String saveCurrentID = null;
            while( ( readLine =  br.readLine()) != null ){ saveCurrentID = readLine; }
            // 가장 최근에 저장된 ID값만 보이게
            tf_id.setText(saveCurrentID);
        }catch(IOException e) {
            System.out.println(e);
        }finally {
            this.info = connectionInfo;

            // ContentPane 설정
            setContentPane(loginForm_Panel);

            // Label에 Logo Image 삽입
            lb_logo.setIcon(Util.resizeImage(new ImageIcon(getClass().getResource("/img/closeheart_logo_login.png")).getImage(), 200, 200, Image.SCALE_SMOOTH));

            // Window 사이즈 설정
            setSize(300, 600);

            // 각종 Action Event을 설정
            setEvent();

            // Close Option 설정
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 로그인 서버 연결
            connectLoginServer();
        }
    }

    /*
     * 각종 Event를 설정하는 함수
     * @author Minjae Seon
     */
    public void setEvent() {
        // Login Button Action
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = tf_id.getText();
                String pw = String.valueOf(tf_pw.getPassword());
                try{
                    FileWriter fw = new FileWriter( "saveId.txt" ,true);
                    BufferedWriter bw = new BufferedWriter( fw );
                    bw.write(id);
                }catch(IOException exception){
                    System.out.println(exception.getMessage());
                }
                requestLogin(id, pw);
            }
        });


        // Register Button Action
        btn_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Register Clicked");
                RegisterForm registerForm = new RegisterForm(loginServerSocket);
                registerForm.setVisible(true);
            }
        });

        // Forgot Password Clicked Event
        btn_findpw.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Find PW Clicked");
                FindForm findForm = new FindForm(loginServerSocket);
                findForm.setVisible(true);
            }
        });

        // Save ID CheckBox Event
        cb_saveid.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox saveid = (JCheckBox) e.getItem();
                System.out.println("Save ID State Change : " + saveid.isSelected());
            }
        });
    }

    /*
     * Login 서버 Socket 연결
     * @author Minjae Seon
     */
    private void connectLoginServer() {
        // Login 서버 연결
        try {
            // 연결 시도
            loginServerSocket = new Socket(info.serverAddress, info.serverPort);

            // 성공하면 창 보이기
            setVisible(true);

            // Input / Output Stream 받기
            serverInput = new Scanner(new InputStreamReader(loginServerSocket.getInputStream()));
            serverOutput = new PrintWriter(new OutputStreamWriter(loginServerSocket.getOutputStream()), true);

            // 엔터키 누르면 로그인 하도록 설정
            this.getRootPane().setDefaultButton(btn_login);
        } catch (Exception e) {
            // 에러 발생시 에러 Dialog를 띄우고 프로그램 종료함
            JOptionPane.showMessageDialog(
                    this,
                    "서버 연결에 실패했습니다! 서버 설정 파일을 확인해보시거나 잠시 후 다시 시도해주세요."
                            + "\n접속 주소 : " + info.serverAddress + ":" + info.serverPort,
                    Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /*
     * 로그인 요청
     * @param id 아이디
     * @param password 비밀번호
     */
    public void requestLogin(String id, String password) {
        if (!id.isEmpty() && !password.isEmpty()) {
            // 서버 연결이 잘 되어있다면
            if (loginServerSocket.isConnected()) {
                // 아이디와 비밀번호 (암호화)를 서버에 전송
                HashMap<String, Object> loginInfo = new HashMap<>();
                loginInfo.put("id", id);
                loginInfo.put("pw", Util.encryptSHA512(password));

                String loginRequest = Util.createJSON(100, loginInfo);
                serverOutput.println(loginRequest);

                while (serverInput.hasNextLine()) {
                    String line = "";
                    try {
                        line = serverInput.nextLine();
                        if (line.isEmpty()) line = serverInput.nextLine();
                    } catch (Exception e1) {
                        // 에러 발생시 에러 출력 후 종료
                        JOptionPane.showMessageDialog(this, "서버에 문제가 발생했습니다.", "오류", JOptionPane.WARNING_MESSAGE);
                        this.dispose();
                        break;
                    }

                    // JsonObject로 변환
                    JsonObject object = JsonParser.parseString(line).getAsJsonObject();

                    // ResponseCode를 가져옴
                    int responseCode = object.get("code").getAsInt();

                    // 200일경우
                    if (responseCode == 200) {
                        // authToken과 메인 (친구) 서버 포트 가져옴
                        String authToken = object.get("authToken").getAsString();
                        int mainPort = object.get("mainServerPort").getAsInt();

                        // 서버 연결 시도 후 성공하면 친구창으로 넘김
                        try {
                            Socket mainServerSocket = new Socket(info.serverAddress, mainPort);
                            new FriendForm(mainServerSocket, authToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 연결 실패시 에러 출력
                            JOptionPane.showMessageDialog(
                                    this,
                                    "친구 서버와의 연결에 실패했습니다. 잠시 후 다시 시도해주세요.\n에러명 : " + e.getMessage(),
                                    Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                        // 로그인 관련 통신 다 Close
                        try {
                            loginServerSocket.close();
                            serverInput.close();
                            serverOutput.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 창닫기
                        this.dispose();
                        // 401 (아이디 or 비밀번호 틀렸을 경우)
                    } else if (responseCode == 401) {
                        // 틀렸다고 출력
                        JOptionPane.showMessageDialog(
                                this,
                                "아이디나 비밀번호가 틀렸습니다.",
                                Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        // 알려지지 않은 코드의 경우 서버 문제이므로 잠시 후 다시 시도해달라고 알림
                        JOptionPane.showMessageDialog(
                                this,
                                "문제가 발생했습니다. 잠시 후 다시 시도해주세요.",
                                Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            } else {
                // 에러 발생시 에러 Dialog를 띄우고 프로그램 종료함
                JOptionPane.showMessageDialog(
                        this,
                        "서버와의 연결이 비정상적으로 종료되었습니다. 프로그램을 다시 실행해주세요.",
                        Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        else {
            // 아이디와 비밀번호 둘 중 하나라도 입력하지 않으면 경고창
            JOptionPane.showMessageDialog(
                    this,
                    "아이디와 비밀번호를 입력해주세요.",
                    Util.getStrFromProperties(getClass(), "program_title"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
