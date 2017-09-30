package com.laundrylangshop.javamailtest_studio.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * Created by Sinaan on 2017/9/29.
 * 能够创建并发送文本、图片、附件或其组合的复杂邮件
 * 需要收发件人在邮箱-设置-账户开启SMTP服务
 */

public class QQMail {
    /**
     * 发件人昵称
     */
    private static String senderName;
    /**
     * 发件人邮箱地址
     */
    private static String senderMailAddr;
    /**
     * 发件人邮箱密码
     * PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
     * 对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
     */
    private static String senderMailPwd;
    /**
     * 收件人昵称
     */
    private static String recipientsName;
    /**
     * 收件人地址
     */
    private static String recipientsMailAddr;
    /**
     * 其他收件人的昵称和地址
     */
    private static Map<String, String> recipientsMailMap;
    /**
     * 抄送人的昵称和地址
     */
    private static Map<String, String> ccRecipientsMailMap;
    /**
     * 密送人的昵称和地址
     */
    private static Map<String, String> bccRecipientsMailMap;
    /**
     * 邮箱主题
     */
    private static String subject;
    /**
     * 邮箱正文内容
     */
    private static String content;
    /**
     * 邮箱附件路径
     */
    private static List<File> attachmentsFiles;
    /**
     * 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
     */
    public static String emailSMTPHost = "smtp.qq.com";

    public static void send(Mail mail) throws Exception {
        senderName = mail.getSenderName();
        senderMailAddr = mail.getSenderMailAddr();
        senderMailPwd = mail.getSenderMailPwd();
        recipientsName = mail.getRecipientsName();
        recipientsMailAddr = mail.getRecipientsMailAddr();
        recipientsMailMap = mail.getRecipientsMailMap();
        ccRecipientsMailMap = mail.getCcRecipientsMailMap();
        bccRecipientsMailMap = mail.getBccRecipientsMailMap();
        subject = mail.getSubject();
        content = mail.getContent();
        attachmentsFiles = mail.getAttachmentsFiles();

        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", emailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

        // 开启 SSL 连接, 以及更详细的发送步骤请看上一篇: 基于 JavaMail 的 Java 邮件发送：简单邮件发送
        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
        //     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。

        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);// 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session);

        // 也可以保持到本地查看
//        OutputStream file_out_put_stream = new FileOutputStream("E:\\MyEmail.eml");
//         message.writeTo(file_out_put_stream);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器
        //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
        transport.connect(senderMailAddr, senderMailPwd);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建一封复杂邮件（文本+图片+附件）
     */
    public static MimeMessage createMimeMessage(Session session) throws Exception {
        // 1. 创建邮件对象
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(senderMailAddr, senderName, "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientsMailAddr, recipientsName, "UTF-8"));
        //    To: 增加收件人（可选）
        if (recipientsMailMap != null)
            for (Map.Entry<String, String> entry : recipientsMailMap.entrySet()) {
                message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(entry.getKey(), entry.getValue(), "UTF-8"));
            }
        //    Cc: 抄送（可选）
        int ccRecipientsNum = 0;
        if (ccRecipientsMailMap != null)
            for (Map.Entry<String, String> entry : ccRecipientsMailMap.entrySet()) {
                if (ccRecipientsNum == 0)
                    message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(entry.getKey(), entry.getValue(), "UTF-8"));
                else
                    message.addRecipient(MimeMessage.RecipientType.CC, new InternetAddress(entry.getKey(), entry.getValue(), "UTF-8"));
                ccRecipientsNum++;
            }
        //    Bcc: 密送（可选）
        int bccRecipientsNum = 0;
        if (bccRecipientsMailMap != null)
            for (Map.Entry<String, String> entry : bccRecipientsMailMap.entrySet()) {
                if (bccRecipientsNum == 0)
                    message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(entry.getKey(), entry.getValue(), "UTF-8"));
                else
                    message.addRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(entry.getKey(), entry.getValue(), "UTF-8"));
                bccRecipientsNum++;
            }

        // 4. Subject: 邮件主题
        message.setSubject(subject, "UTF-8");

        // 5.发送内容
        if(attachmentsFiles==null){
            //没有附件
            message.setContent(content, "text/html;charset=UTF-8");
        }else{
            //添加文本和附件
            // 创建文本“节点”
            MimeBodyPart text = new MimeBodyPart();
            //    这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
            text.setContent(content, "text/html;charset=UTF-8");
            // 创建附件“节点”
            ArrayList<MimeBodyPart> attachments = new ArrayList<>();
            for (int i = 0; i < attachmentsFiles.size(); i++) {
                MimeBodyPart attachment = new MimeBodyPart();
                DataHandler dh2 = new DataHandler(new FileDataSource(attachmentsFiles.get(i).getAbsolutePath()));  // 读取本地文件
                attachment.setDataHandler(dh2);                                             // 将附件数据添加到“节点”
                attachment.setFileName(MimeUtility.encodeText(dh2.getName()));              // 设置附件的文件名（需要编码）
                attachments.add(attachment);
            }
            // （文本+附件）设置 文本 和 附件 “节点”的关系（将 文本 和 附件 “节点”合成一个混合“节点”）
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text);
            for (int i = 0; i < attachments.size(); i++) {
                mm.addBodyPart(attachments.get(i)); // 如果有多个附件，可以创建多个多次添加
            }
            mm.setSubType("related");    // 关联关系
            // 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
            message.setContent(mm);
        }


//        /*
//         * 下面是文本+图片+附件邮件内容的创建:
//         */
//
//        // 创建图片“点”
//        MimeBodyPart image = new MimeBodyPart();
//        String imgPath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/附件/FairyTail.jpg";
//        DataHandler dh = new DataHandler(new FileDataSource(imgPath)); // 读取本地文件
//        image.setDataHandler(dh);                   // 将图片数据添加到“节点”
//        image.setContentID("image_fairy_tail");     // 为“节点”设置一个唯一编号（在文本“节点”将引用该ID）
//
//        // 创建文本“节点”
//        MimeBodyPart text = new MimeBodyPart();
//        //    这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
//        text.setContent("这是一张图片<br/><img src='cid:image_fairy_tail'/>", "text/html;charset=UTF-8");
//
//        // （文本+图片）设置 文本 和 图片 “节点”的关系（将 文本 和 图片 “节点”合成一个混合“节点”）
//        MimeMultipart mm_text_image = new MimeMultipart();
//        mm_text_image.addBodyPart(text);
//        mm_text_image.addBodyPart(image);
//        mm_text_image.setSubType("related");    // 关联关系
//
//        // 将 文本+图片 的混合“节点”封装成一个普通“节点”
//        //    最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
//        //    上面的 mm_text_image 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
//        MimeBodyPart text_image = new MimeBodyPart();
//        text_image.setContent(mm_text_image);
//
//        // 创建附件“节点”
//        MimeBodyPart attachment = new MimeBodyPart();
//        String pdfPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/附件/洗衣郎-国庆、中秋放假通知2017.pdf";
//        DataHandler dh2 = new DataHandler(new FileDataSource(pdfPath));  // 读取本地文件
//        attachment.setDataHandler(dh2);                                             // 将附件数据添加到“节点”
//        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));              // 设置附件的文件名（需要编码）
//
//        // 设置（文本+图片）和 附件 的关系（合成一个大的混合“节点” / Multipart ）
//        MimeMultipart mm = new MimeMultipart();
//        mm.addBodyPart(text_image);
//        mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
//        mm.setSubType("mixed");         // 混合关系
//
//        // 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
//        message.setContent(mm);


        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存上面的所有设置
        message.saveChanges();

        return message;
    }
}
