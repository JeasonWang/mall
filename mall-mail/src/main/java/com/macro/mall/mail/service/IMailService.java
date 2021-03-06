package com.macro.mall.mail.service;

import com.macro.mall.mail.domain.OmsOrder;

/**
 * @Author: jeason
 * @Description: 封装一个发邮件的接口，后边直接调用即可
 * @Description:
 * @Date: 2020/12/27 14:02
 */
public interface IMailService {

    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param order 内容
     */
    void sendTemplateHtmlMail(String to, String subject, OmsOrder order);

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath);
}
