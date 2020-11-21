package kr.ac.gachon.sw.closeheart.client.base;

import kr.ac.gachon.sw.closeheart.client.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * Form 기본 베이스 설정
 * @author Minjae Seon
 */
public abstract class BaseForm extends JFrame {
    // 현재 창이 열려있는지 체크하기 위한 변수
    boolean isShowing = false;

    public BaseForm() {
        // Font Setting
        Util.changeUIFont(new Font("NanumGothic", Font.PLAIN, 13));

        // 타이틀 설정
        setTitle(Util.getStrFromProperties(getClass(), "program_title"));

        // 위치를 Platform에 따라 조정함
        setLocationByPlatform(true);

        // 사이즈 조절 불가능하도록 설정
        setResizable(false);

        // Window의 열리고 닫힘에 따라 isShowing 변수가 변하도록 함
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isShowing = false;
                super.windowClosing(e);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                isShowing = false;
                super.windowClosed(e);
            }

            @Override
            public void windowOpened(WindowEvent e) {
                isShowing = true;
                super.windowOpened(e);
            }
        });
    }

    /*
     * Window 열기 / 닫기 함수
     * @author Minjae Seon
     */
    public void setShowing() {
        setVisible(!isShowing);
    }

    /*
     * Event를 따로 나누어 관리할 수 있도록 하기 위한 Abstract 함수
     * @author Minjae Seon
     */
    public abstract void setEvent();
}
