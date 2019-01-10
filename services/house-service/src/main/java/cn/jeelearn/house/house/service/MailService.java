package cn.jeelearn.house.house.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/8
 * @Version:v1.0
 */
@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    //@Async
    public void sendSimpleMail(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(title);
        message.setTo(email);
        message.setText(content);
        message.setSentDate(new Date());
        try {
            mailSender.send(message);
        } catch (MailException e) {
            logger.error("send email failed");
            e.printStackTrace();
        }
    }
}

