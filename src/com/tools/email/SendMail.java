package com.tools.email;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import org.apache.log4j.Logger;

/**
 * java 发送邮件
 *
 * @author Fuq
 */
public class SendMail {

    private Logger logger = Logger.getLogger(SendMail.class);
    private MimeMessage mimeMsg; //MIME邮件对象
    private Session session; //邮件会话对象
    private Properties props; //系统属性
    private boolean needAuth = false; //smtp是否需要认证
    private String username = ""; //smtp认证用户名和密码
    private String password = "";
    private Multipart mp; //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

    /**
     *
     */
    public SendMail() {
        // setSmtpHost(getConfig.mailHost);//如果没有指定邮件服务器,就从getConfig类中获取
        // createMimeMessage();
    }

    public SendMail(String smtp) throws Exception {
        setSmtpHost(smtp);
        createMimeMessage();
    }

    /**
     * @param hostName String
     */
    public void setSmtpHost(String hostName) {
        //System.out.println("设置系统属性：mail.smtp.host = " + hostName);
        if (props == null) {
            props = System.getProperties(); //获得系统属性对象
        }
        props.put("mail.smtp.host", hostName); //设置SMTP主机
    }

    /**
     * @return boolean
     */
    public boolean createMimeMessage() throws Exception {
        try {
            //System.out.println("准备获取邮件会话对象！");
            session = Session.getDefaultInstance(props, null); //获得邮件会话对象
        } catch (Exception e) {
            //System.err.println("获取邮件会话对象时发生错误！" + e);
            throw new Exception("获取邮件会话对象时发生错误！", e.getCause());
        }
        //System.out.println("准备创建MIME邮件对象！");
        try {
            mimeMsg = new MimeMessage(session); //创建MIME邮件对象
            mp = new MimeMultipart();
            return true;
        } catch (Exception e) {
            //System.err.println("创建MIME邮件对象失败！" + e);
            throw new Exception("创建MIME邮件对象失败！", e.getCause());
        }
    }

    /**
     * @param need boolean
     */
    public void setNeedAuth(boolean need) {
        //System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
        if (props == null) {
            props = System.getProperties();
        }
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
    }

    /**
     * @param name String
     * @param pass String
     */
    public void setNamePass(String name, String pass) {
        username = name;
        password = pass;
    }

    /**
     * @param mailSubject String
     * @return boolean
     */
    public boolean setSubject(String mailSubject) throws Exception {
        //System.out.println("设置邮件主题！");
        try {
            mimeMsg.setSubject(mailSubject);
            return true;
        } catch (Exception e) {
            //System.err.println("设置邮件主题发生错误！");
            throw new Exception("设置邮件主题发生错误！", e.getCause());
        }
    }

    /**
     * @param mailBody String
     * @return boolean
     *
     */
    public boolean setBody(String mailBody) throws Exception {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent("" + mailBody, "text/html;charset=GB2312");
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            //System.err.println("设置邮件正文时发生错误！" + e);
            throw new Exception("设置邮件正文时发生错误！", e.getCause());
        }
    }

    /**
     * @param filename
     * @return boolean
     */
    public boolean addFileAffix(String filename) throws Exception {
        if (filename.isEmpty()) {
            //System.out.println("没有附件");
            return true;
        }
        //System.out.println("增加邮件附件：" + filename);
        try {
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            bp.setDataHandler(new DataHandler(fileds));
            bp.setFileName(fileds.getName());
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            //System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
            throw new Exception("增加邮件附件：" + filename + "发生错误！", e.getCause());
        }
    }

    /**
     * @param from
     * @return boolean
     */
    public boolean setFrom(String from) throws Exception {
        //System.out.println("设置发信人！");
        try {
            mimeMsg.setFrom(new InternetAddress(from)); //设置发信人
            return true;
        } catch (Exception e) {
            throw new Exception("设置发信人错误", e.getCause());
        }
    }

    /**
     * @param to
     * @return boolean
     */
    public boolean setTo(String to) throws Exception {
        if (to == null) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     * @param copyto
     * @return boolean
     */
    public boolean setCopyTo(String copyto) throws Exception {
        if (copyto == null) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     * @return boolean
     */
    public boolean sendout() throws Exception {
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            //System.out.println("正在发送邮件....");
            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            //transport.send(mimeMsg);
            //System.out.println("发送邮件成功！");
            transport.close();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param title 标题
     * @param body 全文
     * @param accessoriesUrl 附件地址
     * @param SMTP smtp 服务器设置
     * @param from 发件人
     * @param to 收件人
     * @param pwd 密码
     * @return String
     */
    public boolean send(String title, String body, String accessoriesUrl, String SMTP, String from, String to, String pwd) throws Exception {
        SendMail themail = new SendMail(SMTP);
        themail.setNeedAuth(true);
        themail.setNamePass(from, pwd);
//        if (themail.setSubject(title) == false) {
//            throw new Exception(Lang.getText("mail.error7"));
//        }
//        if (themail.setBody(body) == false) {
//            throw new Exception(Lang.getText("mail.error8"));
//        }
//        if (themail.setTo(to) == false) {
//            throw new Exception(Lang.getText("mail.error9"));
//        }
//        if (themail.setFrom(from) == false) {
//            throw new Exception(Lang.getText("mail.error10"));
//        }
//        if (themail.addFileAffix(accessoriesUrl) == false) {
//            throw new Exception(Lang.getText("mail.error11"));
//        }
//        if (themail.sendout() == false) {
//            throw new Exception(Lang.getText("mail.error12"));
//        } else {
//            return true;
//        }
        return true;
    }
}
