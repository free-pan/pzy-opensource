package org.pzy.opensource.email.support.util;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.pzy.opensource.email.domain.bo.EmailMessageBO;
import org.pzy.opensource.email.domain.bo.EmailServerPropertiesBO;

import java.util.List;

/**
 * 邮件发送帮助类
 *
 * @author 潘志勇
 * @date 2019-01-28
 */
public class EmailUtil {

    private EmailUtil() {
    }

    public static EmailUtil INSTANCE = new EmailUtil();


    /**
     * 发送邮件
     *
     * @param emailServerPropertiesBO 邮件服务器配置
     * @param messageBO               邮件内容
     */
    public static void send(EmailServerPropertiesBO emailServerPropertiesBO, EmailMessageBO messageBO) {
        HtmlEmail email = new HtmlEmail();
        // 填充邮件发送人以及服务器相关信息
        fitSendInfo(emailServerPropertiesBO, email);

        // 填充邮件主体内容
        fitContent(messageBO, email);

        // 添加附件
        fitAttachment(messageBO, email);

        // 设置收件人
        List<String> toAddress = messageBO.getToAddr();
        if (null != toAddress && toAddress.size() > 0) {
            for (int i = 0; i < toAddress.size(); i++) {
                try {
                    email.addTo(toAddress.get(i));
                } catch (EmailException e) {
                    throw new RuntimeException("邮件收件人地址设置异常!" + e.getMessage(), e);
                }
            }
        }

        // 设置抄送人
        List<String> ccAddress = messageBO.getCopyAddress();
        if (null != ccAddress && ccAddress.size() > 0) {
            for (int i = 0; i < ccAddress.size(); i++) {
                try {
                    email.addCc(ccAddress.get(i));
                } catch (EmailException e) {
                    throw new RuntimeException("邮件抄送人地址设置异常!" + e.getMessage(), e);
                }
            }
        }

        //设置密送人
        List<String> bccAddress = messageBO.getSecretAddress();
        if (null != bccAddress && bccAddress.size() > 0) {
            for (int i = 0; i < bccAddress.size(); i++) {
                try {
                    email.addBcc(ccAddress.get(i));
                } catch (EmailException e) {
                    throw new RuntimeException("邮件密送人地址设置异常!" + e.getMessage(), e);
                }
            }
        }

        try {
            email.send();
        } catch (EmailException e) {
            throw new RuntimeException("邮件发送失败!" + e.getMessage(), e);
        }

    }

    /**
     * 填充主体内容
     *
     * @param messageBO
     * @param email
     */
    private static void fitContent(EmailMessageBO messageBO, HtmlEmail email) {
        email.setSubject(messageBO.getTitle());
        try {
            email.setHtmlMsg(messageBO.getContent());
        } catch (EmailException e) {
            throw new RuntimeException(String.format("邮件内容异常: [%s]", messageBO.getContent()), e);
        }
    }

    /**
     * 填充附件
     *
     * @param messageBO
     * @param email
     */
    private static void fitAttachment(EmailMessageBO messageBO, HtmlEmail email) {
        List<EmailAttachment> attachments = messageBO.getAttachments();
        if (null != attachments && !attachments.isEmpty()) {
            for (int i = 0; i < attachments.size(); i++) {
                try {
                    email.attach(attachments.get(i));
                } catch (EmailException e) {
                    throw new RuntimeException("邮件附件添加异常!" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 填充邮件发送人以及服务器相关信息
     *
     * @param emailServerPropertiesBO 邮件服务器配置
     * @param email                   待发送邮件
     */
    private static void fitSendInfo(EmailServerPropertiesBO emailServerPropertiesBO, HtmlEmail email) {
        email.setHostName(emailServerPropertiesBO.getMailServerHost());
        try {
            email.setFrom(emailServerPropertiesBO.getMailSenderAddress());
        } catch (EmailException e) {
            throw new RuntimeException("发件邮箱设置异常" + e.getMessage(), e);
        }
        email.setAuthentication(emailServerPropertiesBO.getMailSenderUsername(), emailServerPropertiesBO.getPassword());
        email.setCharset("UTF-8");
    }
}
