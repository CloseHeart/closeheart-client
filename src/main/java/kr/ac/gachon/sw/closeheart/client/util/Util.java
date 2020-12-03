package kr.ac.gachon.sw.closeheart.client.util;

import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
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
    public static String getStrFromProperties(Class c, String name) {
        Properties properties = new Properties();
        try {
            properties.load(c.getResourceAsStream("/string.properties"));
            return new String(properties.getProperty(name).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Textfield 업데이트 감지
     * @original https://stackoverflow.com/questions/28913312/change-listener-for-a-jtextfield
     */
    @FunctionalInterface
    public interface detectUpdateListener extends DocumentListener {
        void update(DocumentEvent e);

        @Override
        default void insertUpdate(DocumentEvent e) {
            update(e);
        }
        @Override
        default void removeUpdate(DocumentEvent e) {
            update(e);
        }
        @Override
        default void changedUpdate(DocumentEvent e) {
            update(e);
        }
    }

    /*
     * Server에 전송할 Request JSON 생성
     * @author Minjae Seon
     * @param type
     * @param code 코드
     * @param elements 응답 내에 작성할 Property (속성)
     * @return JSON 형태로 되어있는 String
     */
    public static String createJSON(int code, HashMap<String, String> elements) {
        JsonObject json = new JsonObject();
        json.addProperty("code", code);

        Iterator<String> keyIterator = elements.keySet().iterator();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            String value = elements.get(key);
            json.addProperty(key, value);
        }
        return json.toString();
    }

    /*
     * Code와 단일 Key-Value만을 담은 Response JSON 생성
     * @author Minjae Seon
     * @param code 코드
     * @param key Key
     * @param value Value
     * @return JSON 형태로 되어있는 String
     */
    public static String createSingleKeyValueJSON(int code, String key, String value) {
        JsonObject json = new JsonObject();
        json.addProperty("code", code);
        json.addProperty(key, value);
        return json.toString();
    }

    /*
     * SHA-512 암호화
     * @author Minjae Seon
     * @param originalString 원본 문자열
     * @return SHA-512로 암호화된 문자열
     */
    public static String encryptSHA512(String originalString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] stringBytes = originalString.getBytes(StandardCharsets.UTF_8);
            messageDigest.update(stringBytes);
            return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}
