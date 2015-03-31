package com.tools.email.recipient;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.apache.log4j.Logger;

/**
 * 邮件实体类以及邮件的操作
 *
 * @author Fuq 2013-2-27
 */
public class MailInfo {

    private static final Logger logger = Logger.getLogger(MailInfo.class);
    //TODO 删除、标记为已读等未完成

    public MailInfo(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
        init();
    }
    private MimeMessage mimeMessage = null;
    private String saveAttachPath = null; //附件下载后的存放目录
    private StringBuffer bodytext = new StringBuffer();//存放邮件内容
    private String dateformat = "yyyy-MM-dd HH:mm:ss"; //默认的日前显示格式
    //-------------------------
    /**
     * 当前邮件的messageId
     */
    private String messageId;
    private String title;
    private String fullText;
    /**
     * 未读为true
     */
    private boolean newMail;
    /**
     * key:发件人名称<br/>value:发件人地址
     */
    private Map<String, String> from;
    /**
     * 收件人
     */
    private Map<String, String> to;
    /**
     * 抄送
     */
    private Map<String, String> cc;
    /**
     * 密送
     */
    private Map<String, String> bcc;
    /**
     * 发送时间
     */
    private String sendDate;
    /**
     * 是否包含附件
     */
    private boolean containAttach;

    /**
     * 邮件实体赋值
     */
    private void init() {
        //邮件是否已读
        try {
            newMail = false;
            Flags flags = mimeMessage.getFlags();
            if (flags.contains(Flags.Flag.SEEN)) {
                //   System.out.println("【已读】");
            } else {
                //未读
                newMail = true;
                //  System.out.println("【未读】");
                //设置为已读
                // mimeMessage.setFlag(Flags.Flag.SEEN, true);
            }
        } catch (Exception ex) {
            logger.error("获取邮件是否未读失败", ex);
        }
        //获取邮件messageID
        try {
            messageId = getId();
        } catch (Exception ex) {
            logger.error("获取邮件messageID失败", ex);
        }
        //得到主题
        try {
            title = this.getSubject();
        } catch (Exception ex) {
            logger.error("获取邮件主题失败", ex);
        }
        //得到邮件内容
        try {
            getMailContent((Part) mimeMessage);
            fullText = getBodyText();
        } catch (Exception ex) {
            logger.error("获取邮件内容失败", ex);
        }

        //得到发件人
        try {
            getMailFrom();
        } catch (Exception ex) {
            logger.error("获取邮件发件人失败", ex);
        }
        //获取发送时间
        getMailAddress();
        try {
            sendDate = getMailSentDate();
        } catch (Exception ex) {
            logger.error("获取邮件发送时间失败", ex);
        }
        //获取发送时间
        getMailAddress();
        //获取是否包含附件
        try {
            isContainAttach((Part) mimeMessage);
        } catch (Exception ex) {
            logger.error("获取邮件是否包含附件失败", ex);
        }
        try {
            //TODO 附件保存
            saveAttachMent((Part) mimeMessage);
        } catch (Exception ex) {
            logger.error("保存附件失败", ex);
        }
    }

    /**
     * 获得发件人的地址和姓名
     */
    private void getMailFrom() throws Exception {
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
        String fromAddress = address[0].getAddress();
        if (fromAddress == null) {
            fromAddress = "";
        }
        String personal = address[0].getPersonal();
        if (personal == null) {
            personal = "";
        }
        // String fromaddr = personal + "<" + from + ">";
        this.from = new LinkedHashMap<String, String>();
        this.from.put(personal, fromAddress);
    }

    /**
     * 获得邮件的收件人，抄送，和密送的地址和姓名 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
     */
    private void getMailAddress() {
        //获取收件人
        try {
            InternetAddress[] toAddress = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);

            to = new LinkedHashMap<String, String>();

            if (toAddress != null) {
                for (int i = 0; i < toAddress.length; i++) {
                    String email = toAddress[i].getAddress();
                    if (email == null) {
                        email = "";
                    } else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = toAddress[i].getPersonal();
                    if (personal == null) {
                        personal = "";
                    } else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    // String compositeto = personal + "<" + email + ">";
                    to.put(personal, email);
                }
            }
        } catch (Exception ex) {
            logger.error("获取收件人失败", ex);
        }
        //获取抄送
        try {
            InternetAddress[] ccAddress = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
            cc = new LinkedHashMap<String, String>();
            if (ccAddress != null) {
                for (int i = 0; i < ccAddress.length; i++) {
                    String email = ccAddress[i].getAddress();
                    if (email == null) {
                        email = "";
                    } else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = ccAddress[i].getPersonal();
                    if (personal == null) {
                        personal = "";
                    } else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    // String compositeto = personal + "<" + email + ">";
                    cc.put(personal, email);
                }
            }
        } catch (Exception ex) {
            logger.error("获取抄送失败", ex);
        }
        //获取密送
        try {
            InternetAddress[] bccAddress = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
            bcc = new LinkedHashMap<String, String>();
            if (bccAddress != null) {
                for (int i = 0; i < bccAddress.length; i++) {
                    String email = bccAddress[i].getAddress();
                    if (email == null) {
                        email = "";
                    } else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = bccAddress[i].getPersonal();
                    if (personal == null) {
                        personal = "";
                    } else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    // String compositeto = personal + "<" + email + ">";
                    bcc.put(personal, email);
                }
            }
        } catch (Exception ex) {
            logger.error("获取密送失败", ex);
        }
    }

    /**
     * 获得邮件主题
     */
    private String getSubject() throws Exception {
        String subject = "";
        subject = MimeUtility.decodeText(mimeMessage.getSubject());
        if (subject == null) {
            subject = "";
        }
        return subject;
    }

    /**
     * 获得邮件发送日期
     */
    private String getMailSentDate() throws Exception {
        Date sentdate = mimeMessage.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        return format.format(sentdate);
    }

    /**
     * 获得邮件正文内容
     */
    private String getBodyText() {
        return bodytext.toString();
    }

    /**
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
     */
    private void getMailContent(Part part) throws Exception {
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1) {
            conname = true;
        }
        // System.out.println("CONTENTTYPE: " + contenttype);
        if (part.isMimeType("text/plain") && !conname) {
            bodytext.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
            bodytext.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent());
        } else {
        }
    }

    /**
     * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
     */
    public boolean getReplySign() throws MessagingException {
        boolean replysign = false;
        String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");
        if (needreply != null) {
            replysign = true;
        }
        return replysign;
    }

    /**
     * 获得此邮件的Message-ID
     */
    private String getId() throws MessagingException {
        return mimeMessage.getMessageID();
    }

    /**
     * 判断此邮件是否包含附件
     */
    private boolean isContainAttach(Part part) throws Exception {
        containAttach = false;
        String contentType = part.getContentType();
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
                    containAttach = true;
                } else if (mpart.isMimeType("multipart/*")) {
                    containAttach = isContainAttach((Part) mpart);
                } else {
                    String contype = mpart.getContentType();
                    if (contype.toLowerCase().indexOf("application") != -1) {
                        containAttach = true;
                    }
                    if (contype.toLowerCase().indexOf("name") != -1) {
                        containAttach = true;
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            containAttach = isContainAttach((Part) part.getContent());
        }
        return containAttach;
    }

    /**
     * 【保存附件】
     */
    private void saveAttachMent(Part part) throws Exception {
        String fileName = "";
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
                    fileName = mpart.getFileName();
                    if (fileName.toLowerCase().indexOf("gb2312") != -1) {
                        fileName = MimeUtility.decodeText(fileName);
                    }
                    saveFile(fileName, mpart.getInputStream());
                } else if (mpart.isMimeType("multipart/*")) {
                    saveAttachMent(mpart);
                } else {
                    fileName = mpart.getFileName();
                    if ((fileName != null)
                            && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
                        fileName = MimeUtility.decodeText(fileName);
                        saveFile(fileName, mpart.getInputStream());
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachMent((Part) part.getContent());
        }
    }

    /**
     * 【获得附件存放路径】
     */
    public String getAttachPath() {
        return saveAttachPath;
    }

    /**
     * 保存附件到系统临时目录
     */
    private void saveFile(String fileName, InputStream in) throws Exception {
        String storedir = System.getProperty("java.io.tmpdir");
        File storefile = new File(storedir + File.separatorChar + fileName);
        this.saveAttachPath = storefile.getPath();
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(storefile));
            bis = new BufferedInputStream(in);
            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
                bos.flush();
            }
        } catch (Exception exception) {
            logger.error("文件保存失败", exception);
        } finally {
            bos.close();
            bis.close();
        }
    }

    /**
     * 邮件主题
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 邮件内容(html)
     *
     * @return fullText
     */
    public String getFullText() {
        return fullText;
    }

    /**
     * 是否未读 (未读为true)
     *
     * @return isNewMail
     */
    public boolean isNewMail() {
        return newMail;
    }

    /**
     * 发件人 key:名称,value:邮箱地址
     *
     * @return from
     */
    public Map<String, String> getFrom() {
        return from;
    }

    /**
     * 收件人 key:名称,value:邮箱地址
     *
     * @return the to
     */
    public Map<String, String> getTo() {
        return to;
    }

    /**
     * 抄送 key:名称,value:邮箱地址
     *
     * @return the cc
     */
    public Map<String, String> getCc() {
        return cc;
    }

    /**
     * 密送 key:名称,value:邮箱地址
     *
     * @return the bcc
     */
    public Map<String, String> getBcc() {
        return bcc;
    }

    /**
     * 发送时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public String getSendDate() {
        return sendDate;
    }

    /**
     * 邮件的消息id
     *
     * @return messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * 是否包含附件
     *
     * @return containAttach
     */
    public boolean isContainAttach() {
        return containAttach;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("邮件id:");
        sb.append(getMessageId());
        sb.append("\n");
        sb.append("是否未读:");
        sb.append(isNewMail());
        sb.append("\n");
        sb.append("主题:");
        sb.append(getTitle());
        sb.append("\n");
        sb.append("发件人:");
        Map<String, String> from1 = getFrom();
        Iterator<String> iterator0 = from1.keySet().iterator();
        while (iterator0.hasNext()) {
            String key = iterator0.next();
            sb.append(key);
            sb.append("<");
            sb.append(from1.get(key));
            sb.append(">;");
        }
        sb.append("\n");
        sb.append("收件人:");
        Map<String, String> to1 = getTo();
        Iterator<String> iterator = to1.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            sb.append(key);
            sb.append("<");
            sb.append(to1.get(key));
            sb.append(">;");
        }
        sb.append("\n");
        sb.append("抄送:");
        Map<String, String> cc1 = getCc();
        Iterator<String> iterator1 = cc1.keySet().iterator();
        while (iterator1.hasNext()) {
            String key = iterator1.next();
            sb.append(key);
            sb.append("<");
            sb.append(cc1.get(key));
            sb.append(">;");
        }
        sb.append("\n");
        sb.append("密送:");
        Map<String, String> bcc = getBcc();
        Iterator<String> iterator2 = bcc.keySet().iterator();
        while (iterator2.hasNext()) {
            String key = iterator2.next();
            sb.append(key);
            sb.append("<");
            sb.append(bcc.get(key));
            sb.append(">;");
        }
        sb.append("\n");
        sb.append("发送时间:");
        sb.append(getSendDate());
        sb.append("\n");
        sb.append("是否包含附件:");
        sb.append(isContainAttach());
        sb.append("\n");
        sb.append("附件路径:");
        sb.append(getAttachPath());
        sb.append("\n");
        sb.append("正文:");
        //sb.append(getFullText());
        sb.append("\n");
        return sb.toString();
    }
}
