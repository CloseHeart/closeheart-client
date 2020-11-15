package kr.ac.gachon.sw.closeheart.client.util;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;

/*
 * 여러 클래스에서 동시에 사용할법한 함수를 담는 공통 Class
 */
public class Util {
    /*
     * Image 사이즈 조절 함수
     * @author Minjae Seon
     * @param originalImage Image
     * @param width int
     * @param height int
     * @return ImageIcon
     */
    public static ImageIcon resizeImage(Image originalImage, int width, int height, int scaleOption) {
        return new ImageIcon(originalImage.getScaledInstance(width, height, scaleOption));
    }

    /*
     * 전체 Font 변경 함수
     * @author Minjae Seon
     * @original https://makalu.tistory.com/200
     * @param font Font
     */
    public static void changeUIFont(Font font) {
        try {
            Enumeration<Object> uiManagerKey = UIManager.getDefaults().keys();
            while (uiManagerKey.hasMoreElements()) {
                Object key = uiManagerKey.nextElement();
                Object value = UIManager.get(key);

                if (value instanceof FontUIResource) {
                    UIManager.put(key, font);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * String 정보가 담긴 properties 파일에서 String 읽기
     * @author Minjae Seon
     * @param font Font
     */
    public static String getStrFromProperties(String name) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("res/string.properties"));
            return new String(properties.getProperty(name).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
