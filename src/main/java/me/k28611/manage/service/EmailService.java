package me.k28611.manage.service;

import org.springframework.mail.MailSendException;

/**
 * @author K28611
 * @date 2020/5/3 20:53
 */
public interface EmailService {

    //发送简单邮件
    public void sendSimpleEmail(String to, String subject, String content);

    //发送Html格式邮件
    public void sendHtmlEmail(String to, String subject, String content);

    //发送带附件的邮件
    public void sendAttachmentsEmail(String to,String subject,String content,String filePath);

    //发送带静态资源的邮件
    public void sendInlineResourceEmail(String to,String subject,String content,String rscPath,String rscId);

    //发送验证码服务
    public void sendEmailVerCode(String to,String verCode) throws MailSendException;
}
