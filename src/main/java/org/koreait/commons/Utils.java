package org.koreait.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Utils {
    private static ResourceBundle validationsBundle;

    private static ResourceBundle errorsBundle;

    private static ResourceBundle commonsBundle;

    private final HttpServletRequest request;

    private final HttpSession session;

    static {
        validationsBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");
        commonsBundle = ResourceBundle.getBundle("messages.commons");
    }

    public static String getMessage(String code, String bundleType) {
        bundleType = Objects.requireNonNullElse(bundleType, "validation");
        ResourceBundle bundle = null;

        if (bundleType.equals("common")) {
            bundle = commonsBundle;
        } else if (bundleType.equals("error")) {
            bundle = errorsBundle;
        } else {
            bundle = validationsBundle;
        }

        try {
            return bundle.getString(code);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isMobile() {
        String device = (String)session.getAttribute("device");
        if (device != null) {
            return device.equals("mobile");
        }

        boolean isMobile = request.getHeader("User-Agent").matches(".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*");

        return isMobile;
    }

    public String tpl(String tplPath) {

        return String.format("%s/" + tplPath, isMobile()?"mobile":"front");
    }

    public static void loginInit(HttpSession session) {
        session.removeAttribute("email");
        session.removeAttribute("NotBlank_email");
        session.removeAttribute("NotBlank_password");
        session.removeAttribute("globalError");
    }

    /**
     * 단일 요청 데이터 조회
     */
    public String getParam(String name) {
        return request.getParameter(name);
    }

    /**
     * 복수개 요청 데이터 조회
     *
     */
    public String[] getParams(String name) {
        return request.getParameterValues(name);
    }


    public static int getNumber(int num, int defaultValue) {

        return num <= 0 ? defaultValue : num;
    }

    /**
     * 비회원 구분 UID
     * 비회원 구분은 IP + 브라우저 종류
     *
     */
    public int guestUid() {
        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");

        return Objects.hash(ip, ua);
    }

    public static Map<String, List<String>> getMessages(Errors errors) {
        try {
            Map<String, List<String>> data = new HashMap<>();
            for (FieldError error : errors.getFieldErrors()) {
                String field = error.getField();
                List<String> messages = Arrays.stream(error.getCodes()).sorted(Comparator.reverseOrder())
                        .map(c -> getMessage(c, "validation"))
                        .filter(c -> c != null)
                        .toList();

                data.put(field, messages);
            }

            return data;

        } catch (Exception e) {
            return null;
        }
    }

    public String nl2br(String str) {
       return str.replaceAll("\\r", "")
                .replaceAll("\\n", "<br>");
    }
}
