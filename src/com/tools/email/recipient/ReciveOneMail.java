package com.tools.email.recipient;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMessage;

/**
 * java 收取邮件
 */
public abstract class ReciveOneMail {

    final Properties props = System.getProperties();
    Session session = null;
    URLName urln = null;
    Store store = null;

    /**
     *
     * @param mailType 收取邮件方式：pop、imap
     * <B>注意：收件方式要与收件服务器地址(receiveAddress)相吻合</B>
     * @param receiveAddress 收件服务器地址
     * @param poir 端口 提示：pop默认端口为110, imap默认端口为 143
     * @param file 不知道作用，传null
     * @param eMail email地址
     * @param password email 密码
     * @param smtpAddress smtp发送邮件服务器地址
     */
    public ReciveOneMail(MailType mailType, String receiveAddress, int poir, String file, String eMail, String password, String smtpAddress) throws Exception {
        props.put("mail.smtp.auth", "true");//smtp验证
        props.put("mail.smtp.host", smtpAddress);
        urln = new URLName(mailType.toString(), receiveAddress, poir, null,
                eMail, password);
        //创建session
        session = Session.getDefaultInstance(props, null);
        getMail();
    }

    /**
     * 回调处理邮件内容
     *
     * @param folder
     * @param messages 邮件内容使用(new ReciveOneMail().new
     * MailInfo(messages[i]))得到一个邮件实体
     */
    public abstract void Callback(Folder folder, Message messages[]);

    public final void getMail() throws MessagingException {
        store = session.getStore(urln);
        store.connect();
        Folder folder = store.getFolder("INBOX");
        //Folder.READ_ONLY
        folder.open(Folder.READ_WRITE);
        Message messages[] = folder.getMessages();
        Callback(folder, messages);
        if (folder != null) {
            folder.close(true);
        }
        if (store != null) {
            store.close();
        }
    }

    /**
     * PraseMimeMessage类测试
     */
    public static void main(String args[]) throws Exception {

        new ReciveOneMail(MailType.imap, "imap.163.com", 143, null, "gstd_pv_uv@163.com", "asdfghjkl;'", "smtp.163.com") {

            @Override
            public void Callback(Folder folder, Message[] messages) {
                try {
                    System.out.println("共有邮件：" + folder.getMessageCount() + "封");
                    System.out.println("新邮件：" + folder.getNewMessageCount() + "封");
                    System.out.println("未读邮件：" + folder.getUnreadMessageCount() + "封");
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                for (int i = 0; i < messages.length; i++) {
                    MailInfo mailInfo = new MailInfo((MimeMessage) messages[i]);
                    System.out.println(mailInfo.toString());
                }
            }
        };

    }
}
