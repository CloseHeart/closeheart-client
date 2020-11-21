package kr.ac.gachon.sw.closeheart.client.login.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.connection.ConnectionInfo;
import kr.ac.gachon.sw.closeheart.client.friend.friend.FriendForm;
import kr.ac.gachon.sw.closeheart.client.login.register.RegisterForm;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
    private Socket loginServerSocket;
    private Scanner serverInput;
    private PrintWriter serverOutput;

    public LoginForm(ConnectionInfo connectionInfo) {
        // ContentPane 설정
        setContentPane(loginForm_Panel);

        // Label에 Logo Image 삽입
        lb_logo.setIcon(Util.resizeImage(new ImageIcon(getClass().getResource("/res/closeheart_logo_login.png")).getImage(), 200, 200, Image.SCALE_SMOOTH));

        // Window 사이즈 설정
        setSize(300, 600);

        // 각종 Action Event을 설정
        setEvent();

        // Close Option 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 로그인 서버 연결
        connectLoginServer(connectionInfo);
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
                requestLogin(id, pw);
            }
        });

        RegisterForm registerForm = new RegisterForm();
        // Register Button Action
        btn_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Register Clicked");
                if(!registerForm.isShowing()) registerForm.setShowing();
            }
        });

        // Forgot Password Clicked Event
        lb_forgotpassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Find PW Clicked");
                super.mouseClicked(e);
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
    private void connectLoginServer(ConnectionInfo info) {
        // Login 서버 연결
        try {
            // 연결 시도
            loginServerSocket = new Socket(info.serverAddress, info.serverPort);

            // 성공하면 창 보이기
            setVisible(true);

            // Input / Output Stream 받기
            serverInput = new Scanner(new InputStreamReader(loginServerSocket.getInputStream()));
            serverOutput = new PrintWriter(new OutputStreamWriter(loginServerSocket.getOutputStream()), true);
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

    public void requestLogin(String id, String password) {
        HashMap<String, String> loginInfo = new HashMap<>();
        loginInfo.put("id", id);
        loginInfo.put("pw", Util.encryptSHA512(password));

        String loginRequest = Util.createRequestJSON(100, loginInfo);
        serverOutput.println(loginRequest);

        while(serverInput.hasNextLine()) {
            String line = serverInput.nextLine();
            if(line.isEmpty()) line = serverInput.nextLine();
            JsonObject object = JsonParser.parseString(line).getAsJsonObject();

            int responseCode = object.get("responseCode").getAsInt();

            if(responseCode == 200) {
                String authToken = object.get("authToken").getAsString();
                new FriendForm(loginServerSocket, authToken);
                this.dispose();
            }
            else if(responseCode == 403) {
                JOptionPane.showMessageDialog(
                        this,
                        "아이디나 비밀번호가 틀렸습니다.",
                        Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                        JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "문제가 발생했습니다. 잠시 후 다시 시도해주세요.",
                        Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                        JOptionPane.ERROR_MESSAGE);
            }
            break;
        }
    }
}
