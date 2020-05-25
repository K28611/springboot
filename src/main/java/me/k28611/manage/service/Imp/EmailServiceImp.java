package me.k28611.manage.service.Imp;

import me.k28611.manage.entity.Audience;
import me.k28611.manage.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


/**
 * @author K28611
 * @date 2020/5/3 20:57
 */
@Service("emailService")
public class EmailServiceImp implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Audience audience;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(subject);
        message.setTo(to);
        message.setText(content);
        try{
            mailSender.send(message);
            logger.info("简单邮件已发送");
        } catch (MailException e) {
            logger.error("简单邮件发送异常！",e);
        }

    }

    @Override
    public void sendHtmlEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try{
            //true表示创建Multipart Message
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setText(content);
            helper.setSubject(subject);
            helper.setTo(to);
            mailSender.send(message);
            logger.info("HTML邮件发送成功");
        } catch (MessagingException e) {
            logger.error("HTML邮件发送异常！",e);
        }

    }

    @Override
    public void sendAttachmentsEmail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setText(content);
            helper.setSubject(subject);
            helper.setTo(to);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName,file);
            mailSender.send(message);
            logger.info("带附件的邮箱已发送！");
        } catch (MessagingException e) {
            logger.info("带附件的邮件发送失败！",e);
        }
    }

    @Override
    public void sendInlineResourceEmail(String to, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();
        try
        {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText(content);
            helper.setSubject(subject);
            FileSystemResource file = new FileSystemResource(new File(rscPath));
            //添加内联资源，(id <<=>> resource)/* findResourceById */
            helper.addInline(rscId,file);
            mailSender.send(message);
            logger.info("嵌入静态资源的邮件发送成功！");
        } catch (MessagingException e) {
            logger.error("嵌入静态资源的邮件发送失败！",e);
        }
    }

    @Override
    public void sendEmailVerCode(String to, String verCode) throws MailSendException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("验证码");
        message.setText("尊敬的用户您好：\n"
        +"\n本次请求的验证码为"+verCode+",3分钟内有效\n"
        +"\n如非本人操作，请忽略本邮件。\n(这是系统自动发送的邮件，请勿回复)");
        message.setTo(to);
        message.setFrom(from);
        mailSender.send(message);
    }

}
