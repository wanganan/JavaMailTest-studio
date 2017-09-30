package com.laundrylangshop.javamailtest_studio.mail;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Sinaan on 2017/9/29.
 * 需要收发件人在邮箱-设置-账户开启SMTP服务
 * 邮件发送bean，默认是自己
 */

public class Mail {
    /**
     * 发件人昵称
     */
    private String senderName = "小不点";
    /**
     * 发件人邮箱地址
     */
    private String senderMailAddr = "xxx@qq.com";
    /**
     * 发件人邮箱密码
     * PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
     *     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
     */
    private String senderMailPwd = "xxx";
    /**
     * 收件人昵称
     */
    private String recipientsName = "小不点";
    /**
     * 收件人地址(必须)
     */
    private String recipientsMailAddr = "xxx@qq.com";
    /**
     * 其他收件人的昵称和地址(可选)
     */
    private Map<String,String> recipientsMailMap;
    /**
     * 抄送人的昵称和地址(可选)
     */
    private Map<String,String> ccRecipientsMailMap;
    /**
     * 密送人的昵称和地址(可选)
     */
    private Map<String,String> bccRecipientsMailMap;
    /**
     * 邮箱主题
     */
    private String subject;
    /**
     * 邮箱正文内容
     */
    private String content;
    /**
     * 邮箱附件
     */
    private List<File> attachmentsFiles;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMailAddr() {
        return senderMailAddr;
    }

    public void setSenderMailAddr(String senderMailAddr) {
        this.senderMailAddr = senderMailAddr;
    }

    public String getSenderMailPwd() {
        return senderMailPwd;
    }

    public void setSenderMailPwd(String senderMailPwd) {
        this.senderMailPwd = senderMailPwd;
    }

    public String getRecipientsName() {
        return recipientsName;
    }

    public void setRecipientsName(String recipientsName) {
        this.recipientsName = recipientsName;
    }

    public String getRecipientsMailAddr() {
        return recipientsMailAddr;
    }

    public void setRecipientsMailAddr(String recipientsMailAddr) {
        this.recipientsMailAddr = recipientsMailAddr;
    }

    public Map<String, String> getRecipientsMailMap() {
        return recipientsMailMap;
    }

    public void setRecipientsMailMap(Map<String, String> recipientsMailMap) {
        this.recipientsMailMap = recipientsMailMap;
    }

    public Map<String, String> getCcRecipientsMailMap() {
        return ccRecipientsMailMap;
    }

    public void setCcRecipientsMailMap(Map<String, String> ccRecipientsMailMap) {
        this.ccRecipientsMailMap = ccRecipientsMailMap;
    }

    public Map<String, String> getBccRecipientsMailMap() {
        return bccRecipientsMailMap;
    }

    public void setBccRecipientsMailMap(Map<String, String> bccRecipientsMailMap) {
        this.bccRecipientsMailMap = bccRecipientsMailMap;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<File> getAttachmentsFiles() {
        return attachmentsFiles;
    }

    public void setAttachmentsFiles(List<File> attachmentsFiles) {
        this.attachmentsFiles = attachmentsFiles;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "senderName='" + senderName + '\'' +
                ", senderMailAddr='" + senderMailAddr + '\'' +
                ", senderMailPwd='" + senderMailPwd + '\'' +
                ", recipientsName='" + recipientsName + '\'' +
                ", recipientsMailAddr='" + recipientsMailAddr + '\'' +
                ", recipientsMailMap=" + recipientsMailMap +
                ", ccRecipientsMailMap=" + ccRecipientsMailMap +
                ", bccRecipientsMailMap=" + bccRecipientsMailMap +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", attachmentsFiles=" + attachmentsFiles +
                '}';
    }
}
