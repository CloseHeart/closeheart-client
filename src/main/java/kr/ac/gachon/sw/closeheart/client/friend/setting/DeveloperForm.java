package kr.ac.gachon.sw.closeheart.client.friend.setting;

import kr.ac.gachon.sw.closeheart.client.base.BaseForm;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class DeveloperForm extends BaseForm {
    private JPanel developerForm_Panel;
    private JLabel lb_title;
    private JButton btn_ok;
    private JButton btn_github;

    public DeveloperForm() {
        // ContentPane 설정
        setContentPane(developerForm_Panel);

        // Window 사이즈 설정
        pack();

        // 각종 Action Event을 설정
        setEvent();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void setEvent() {
        btn_ok.addActionListener(e -> {
            this.dispose();
        });

        btn_github.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Closeheart"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
