package kr.ac.gachon.sw.closeheart.client.login.find;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class FindForm extends BaseForm {
    private JPanel findForm_Panel;
    private JLabel lb_find_title;
    private JTextField tf_find_email;
    private JLabel lb_email;
    private JButton btn_send;
    private JButton btn_close;

    private Socket socket;
    private Scanner serverInput;
    private PrintWriter serverOutput;

    public FindForm(Socket socket) {
        // 소켓 설정
        this.socket = socket;

        try {
            serverInput = new Scanner(new InputStreamReader(socket.getInputStream()));
            serverOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "로그인 서버와의 연결에 실패했습니다. 잠시 후 다시 시도해주세요.",
                    Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                    JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }

        // ContentPane 설정
        setContentPane(findForm_Panel);

        // 닫기 이벤트 설정
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Window 사이즈 설정
        setSize(300, 250);

        // 각종 Action Event을 설정
        setEvent();
    }


    @Override
    public void setEvent() {
        btn_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindForm.this.dispose();
            }
        });

        btn_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findPassword();
            }
        });
    }

    private void findPassword() {
        if(socket.isConnected()) {
            serverOutput.println(Util.createSingleKeyValueJSON(105, "email", tf_find_email.getText()));

            if(serverInput.hasNextLine()) {
                String line = "";
                line = serverInput.nextLine();
                if(line.isEmpty()) line = serverInput.nextLine();

                JsonObject jsonObject = JsonParser.parseString(line).getAsJsonObject();

                int responseCode = jsonObject.get("code").getAsInt();

                // 전송 성공
                if(responseCode == 200) {
                    JOptionPane.showMessageDialog(
                            this,
                            tf_find_email.getText() + "로 새로운 비밀번호를 전송했습니다!",
                            "알림",
                            JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
                // 찾을 수 없는 아이디
                else if(responseCode == 400) {
                    JOptionPane.showMessageDialog(
                            this,
                            "등록된 이메일이 아닙니다. 이메일을 다시 한번 확인해주세요.",
                            "경고",
                            JOptionPane.WARNING_MESSAGE);
                }
                // 에러
                else {
                    JOptionPane.showMessageDialog(
                            this,
                            "문제가 발생했습니다. 잠시 후 다시 시도해주세요.",
                            Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
