package cn.hamm.airpower.util;

import cn.hamm.airpower.result.Result;
import cn.hutool.extra.mail.MailUtil;

/**
 * <h1>邮件助手类</h1>
 *
 * @author Hamm
 */
public class EmailUtil {

    /**
     * <h2>发送邮件验证码</h2>
     *
     * @param email 邮箱
     * @param title 标题
     * @param code  验证码
     * @param sign  签名
     */
    public static void sendCode(String email, String title, String code, String sign) {
        String content = "<div style='border-radius:20px;padding: 20px;background-color:#f5f5f5;color:#333;display:inline-block;'>" +
                "<div style='font-size:24px;font-weight:bold;color:orangered;'>" + code + "</div>" +
                "<div style='margin-top:20px;font-size:16px;font-weight:300'>上面是你的验证码，请注意不要转发给他人，五分钟内有效，请尽快使用。</div>" +
                "<div style='margin-top:10px;font-size:12px;color:#aaa;font-weight:300'>" + sign + "</div></div>";
        EmailUtil.sendEmail(email, title, content);
    }

    /**
     * <h2>发送邮件验证码</h2>
     *
     * @param email 邮箱
     * @param title 标题
     * @param code  验证码
     */
    public static void sendCode(String email, String title, String code) {
        sendCode(email, title, code, "Hamm");
    }

    /**
     * <h2>发送邮件</h2>
     *
     * @param email   邮箱
     * @param title   标题
     * @param content 内容
     */
    public static void sendEmail(String email, String title, String content) {
        try {
            MailUtil.send(email, title, content, true);
        } catch (Exception e) {
            Result.EMAIL_ERROR.show();
        }
    }
}