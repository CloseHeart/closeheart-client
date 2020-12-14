package kr.ac.gachon.sw.closeheart.client.login.register;

import com.github.lgooddatepicker.components.DatePicker;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.ac.gachon.sw.closeheart.client.base.BaseForm;
import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 회원가입 Form
 * @author Minjae Seon
 */
public class RegisterForm extends BaseForm {
    private JPanel RegisterForm_Panel;
    private JTextField tf_email;
    private JTextField tf_nickname;
    private JPasswordField tf_password;
    private JButton btn_emailcheck;
    private JButton btn_nickcheck;
    private JButton btn_register;
    private JButton btn_cancel;
    private JPasswordField tf_pwcheck;
    private JLabel lb_register;
    private JLabel lb_email;
    private JLabel lb_password;
    private JLabel lb_pwcheck;
    private JLabel lb_pwcheckresult;
    private JLabel lb_nickname;
    private JLabel lb_birthday;
    private JTextField tf_id;
    private JButton btn_idcheck;
    private DatePicker dp_birthday;

    private Socket socket;
    private Scanner serverInput;
    private PrintWriter serverOutput;

    private boolean isPasswordCorrect = false;
    private boolean isCheckID = false;
    private boolean isCheckEmail = false;
    private boolean isCheckNick = false;

    public RegisterForm(Socket socket) {
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
        setContentPane(RegisterForm_Panel);

        // Window 사이즈 설정
        setSize(400, 400);

        // 각종 Action Event을 설정
        setEvent();
    }

    /*
     * 각종 Event를 설정하는 함수
     * @author Minjae Seon
     */
    public void setEvent() {
        // 회원가입 버튼 Action
        btn_register.addActionListener(e -> {
            // 비밀번호 8자 이상인지 체크
            if(tf_password.getPassword().length <= 8) {
                JOptionPane.showMessageDialog(this, "비밀번호는 8자 이상이여야 합니다!", "오류", JOptionPane.ERROR_MESSAGE);
            }
            // 비밀번호 확인이 틀리지 않았는지 체크
            else if(!isPasswordCorrect) {
                JOptionPane.showMessageDialog(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            }
            else {
                // 아이디 중복확인을 했는지 체크
                if(isCheckID) {
                    // 이메일 중복확인을 했는지 체크
                   if(isCheckEmail) {
                       // 닉네임 중복확인을 했는지 체크
                       if(isCheckNick) {
                           if(Timestamp.valueOf(dp_birthday.getDate().atStartOfDay()).getTime() < System.currentTimeMillis()) {
                               DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                               HashMap<String, Object> registerHashMap = new HashMap<>();
                               registerHashMap.put("id", tf_id.getText());
                               registerHashMap.put("pw", Util.encryptSHA512(String.valueOf(tf_password.getPassword())));
                               registerHashMap.put("email", tf_email.getText());
                               registerHashMap.put("nick", tf_nickname.getText());
                               registerHashMap.put("birthday", dp_birthday.getDate().format(dateTimeFormatter));

                               String registerJSON = Util.createJSON(104, registerHashMap);
                               serverOutput.println(registerJSON);

                               if (serverInput.hasNextLine()) {
                                   String line = "";
                                   try {
                                       line = serverInput.nextLine();
                                       if (line.isEmpty()) line = serverInput.nextLine();
                                   } catch (Exception e1) {
                                       JOptionPane.showMessageDialog(this, "서버에 문제가 발생했습니다.", "오류", JOptionPane.WARNING_MESSAGE);
                                       this.dispose();
                                   }

                                   JsonObject object = JsonParser.parseString(line).getAsJsonObject();

                                   int responseCode = object.get("code").getAsInt();

                                   if (responseCode == 200) {
                                       JOptionPane.showMessageDialog(this, "가입되었습니다! 입력하신 정보로 로그인 해주세요.", "안내", JOptionPane.INFORMATION_MESSAGE);
                                   } else if (responseCode == 403) {
                                       JOptionPane.showMessageDialog(this, "중복값이 발견되었습니다. 다시 시도해주세요.", "오류", JOptionPane.WARNING_MESSAGE);
                                   } else {
                                       JOptionPane.showMessageDialog(this, "서버 에러가 발생했습니다. 잠시 후 다시 시도해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                                   }
                                   this.dispose();
                               }
                           }
                           else {
                               JOptionPane.showMessageDialog(this, "올바른 생일을 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                           }
                       }
                       else {
                           JOptionPane.showMessageDialog(this, "닉네임 중복 확인을 해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                       }
                   }
                   else {
                       JOptionPane.showMessageDialog(this, "이메일 중복 확인을 해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                   }
                }
                else {
                    JOptionPane.showMessageDialog(this, "아이디 중복 확인을 해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 취소 버튼 Action
        btn_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        // 비밀번호 일치 체크 Action
        tf_pwcheck.getDocument().addDocumentListener((Util.detectUpdateListener) e -> {
            String userPassword = Arrays.toString(tf_password.getPassword());
            String checkPassword = Arrays.toString(tf_pwcheck.getPassword());

            if(tf_password.getPassword().length <= 8) {
                lb_pwcheckresult.setText("8자 이상의 비밀번호를 입력하세요.");
                isPasswordCorrect = false;
            }
            else if(userPassword.equals(checkPassword)) {
                lb_pwcheckresult.setText("비밀번호가 일치합니다.");
                isPasswordCorrect = true;
            }
            else {
                lb_pwcheckresult.setText("비밀번호가 일치하지 않습니다.");
                isPasswordCorrect = false;
            }
        });

        // Email TextField 변경시
        tf_email.getDocument().addDocumentListener((Util.detectUpdateListener) e -> {
            isCheckEmail = false;
        });

        // 닉네임 TextField 변경시
        tf_nickname.getDocument().addDocumentListener((Util.detectUpdateListener) e -> {
            isCheckNick = false;
        });

        // 아이디 TextField 변경시
        tf_id.getDocument().addDocumentListener((Util.detectUpdateListener) e -> {
            isCheckID = false;
        });

        // ID Check
        btn_idcheck.addActionListener(e ->  {
            Pattern idPattern = Pattern.compile("^[a-zA-Z0-9]{6,15}$");
            Matcher matcher = idPattern.matcher(tf_id.getText());
            if(matcher.find()) {
                String idCheckJSON = Util.createSingleKeyValueJSON(101, "id", tf_id.getText());
                serverOutput.println(idCheckJSON);

                if(serverInput.hasNextLine()) {
                    String line = "";
                    try {
                        line = serverInput.nextLine();
                        if (line.isEmpty()) line = serverInput.nextLine();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(this, "서버에 문제가 발생했습니다.", "오류", JOptionPane.WARNING_MESSAGE);
                        this.dispose();
                    }

                    JsonObject object = JsonParser.parseString(line).getAsJsonObject();

                    int responseCode = object.get("code").getAsInt();

                    if(responseCode == 200) {
                        String check = object.get("idcheck").getAsString();
                        if(check.equals("true")) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "이미 존재하는 아이디입니다.",
                                    Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                                    JOptionPane.WARNING_MESSAGE);
                            isCheckID = false;
                        }
                        else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "사용할 수 있는 아이디입니다!",
                                    Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                                    JOptionPane.INFORMATION_MESSAGE);
                            isCheckID = true;
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                this,
                                "에러가 발생했습니다. 잠시 후 다시 시도해주세요.",
                                Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                                JOptionPane.ERROR_MESSAGE);
                        isCheckID = false;
                        this.dispose();
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "아이디는 6-15자로 된 알파벳 / 숫자만 가능합니다!",
                        Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                        JOptionPane.WARNING_MESSAGE);
                isCheckID = false;
            }
        });

        btn_emailcheck.addActionListener(e -> {
            Pattern mailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
            Matcher matcher = mailPattern.matcher(tf_email.getText());
            if(matcher.find()) {
                String emailCheckJSON = Util.createSingleKeyValueJSON(102, "email", tf_email.getText());
                serverOutput.println(emailCheckJSON);

                if (serverInput.hasNextLine()) {
                    String line = "";
                    try {
                        line = serverInput.nextLine();
                        if (line.isEmpty()) line = serverInput.nextLine();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(this, "서버에 문제가 발생했습니다.", "오류", JOptionPane.WARNING_MESSAGE);
                        this.dispose();
                    }

                    JsonObject object = JsonParser.parseString(line).getAsJsonObject();

                    int responseCode = object.get("code").getAsInt();

                    if (responseCode == 200) {
                        String check = object.get("emailcheck").getAsString();
                        if (check.equals("true")) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "이미 존재하는 이메일입니다.",
                                    Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                                    JOptionPane.WARNING_MESSAGE);
                            isCheckEmail = false;
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "사용할 수 있는 이메일입니다!",
                                    Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                                    JOptionPane.INFORMATION_MESSAGE);
                            isCheckEmail = true;
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "에러가 발생했습니다. 잠시 후 다시 시도해주세요.",
                                Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                                JOptionPane.ERROR_MESSAGE);
                        isCheckEmail = false;
                        this.dispose();
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "올바른 이메일을 입력해주세요!",
                        Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                        JOptionPane.WARNING_MESSAGE);
                isCheckEmail = false;
            }
        });

        btn_nickcheck.addActionListener(e -> {
            Pattern nickPattern = Pattern.compile("^[a-zA-Z0-9가-힣]{3,20}$");
            Matcher matcher = nickPattern.matcher(tf_nickname.getText());

            if(matcher.find()) {
                String nickCheckJSON = Util.createSingleKeyValueJSON(103, "nick", tf_nickname.getText());
                serverOutput.println(nickCheckJSON);

                if (serverInput.hasNextLine()) {
                    String line = "";
                    try {
                        line = serverInput.nextLine();
                        if (line.isEmpty()) line = serverInput.nextLine();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(this, "서버에 문제가 발생했습니다.", "오류", JOptionPane.WARNING_MESSAGE);
                        this.dispose();
                    }

                    JsonObject object = JsonParser.parseString(line).getAsJsonObject();

                    int responseCode = object.get("code").getAsInt();

                    if (responseCode == 200) {
                        String check = object.get("nickcheck").getAsString();
                        if (check.equals("true")) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "이미 존재하는 닉네임입니다.",
                                    Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                                    JOptionPane.WARNING_MESSAGE);
                            isCheckNick = false;
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "사용할 수 있는 닉네임입니다!",
                                    Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                                    JOptionPane.INFORMATION_MESSAGE);
                            isCheckNick = true;
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "에러가 발생했습니다. 잠시 후 다시 시도해주세요.",
                                Util.getStrFromProperties(getClass(), "program_title") + " - 에러",
                                JOptionPane.ERROR_MESSAGE);
                        isCheckNick = false;
                        this.dispose();
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "닉네임은 3~20글자의 영어, 숫자, 한글만 가능합니다!",
                        Util.getStrFromProperties(getClass(), "program_title") + " - 알림",
                        JOptionPane.WARNING_MESSAGE);
                isCheckNick = false;
            }
        });
    }
}