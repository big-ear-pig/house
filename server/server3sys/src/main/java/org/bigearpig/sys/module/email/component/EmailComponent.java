package org.bigearpig.sys.module.email.component;


import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class EmailComponent {

    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public Boolean sendHtmlMailNoException(String to, String subject, String content, List<MultipartFile> imgList, List<MultipartFile> fileList) {
        try {
            sendHtmlMail(to, subject, content, imgList, fileList);
            return true;
        } catch (Exception e) {
            log.error("EmailComponent.sendHtmlMailNoException",e);
            return false;
        }
    }

    private void sendHtmlMail(String to, String subject, String content, List<MultipartFile> imgList, List<MultipartFile> fileList)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // true表示构建一个可以带附件的邮件对象
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);

        helper.setSubject(subject);

        helper.setText(content, true);
        // 邮件图片
        if (ObjectUtil.isNotEmpty(imgList)) {
            for (int i = 0; i < imgList.size(); i++) {
                MultipartFile img = imgList.get(i);
                helper.addInline("img" + i, img, MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(img.getOriginalFilename()));
            }
        }
        // 附件
        if (ObjectUtil.isNotEmpty(fileList)) {
            for (int i = 0; i < fileList.size(); i++) {
                MultipartFile file = fileList.get(i);
                if(StrUtil.isNotBlank(file.getOriginalFilename())){
                    helper.addAttachment(file.getOriginalFilename(), file);
                }else{
                    helper.addAttachment("file"+i, file);
                }

            }
        }
        javaMailSender.send(mimeMessage);
    }

}
