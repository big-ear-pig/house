package org.bigearpig.sys.module.email.service;


import org.bigearpig.base.mybatis.BasePageVo;
import org.bigearpig.sys.module.email.db.entity.EmailRecordEntity;
import org.bigearpig.sys.module.email.controller.mo.AddEmailTemplateMo;
import org.bigearpig.sys.module.email.controller.mo.SendEmailMo;
import org.bigearpig.sys.module.email.controller.mo.UpdateEmailTemplateMo;
import org.bigearpig.sys.module.email.controller.qo.EmailRecordPageQo;
import org.bigearpig.sys.module.email.controller.qo.EmailTemplatePageQo;
import org.bigearpig.sys.module.email.controller.vo.EmailRecordVo;
import org.bigearpig.sys.module.email.controller.vo.EmailTemplateVo;


import java.util.Map;

public interface EmailService {


    EmailTemplateVo addEmailTemplate(AddEmailTemplateMo addEmailTemplateMo);
    
    Boolean delEmailTemplate(Long tableId);
    
    EmailTemplateVo updateEmailTemplate(UpdateEmailTemplateMo updateEmailTemplateMo);
    
    BasePageVo<EmailRecordVo> queryPageEmailTemplate(EmailTemplatePageQo emailTemplatePageQo);
    
    
    
    
    EmailRecordVo sendEmail(SendEmailMo sendEmailMo);

    EmailRecordEntity sendEmailByEmailTemplateCode(String emailTemplateCode, String email, Map<String, Object> map);

    
    BasePageVo<EmailRecordVo> queryPageEmailRecord(EmailRecordPageQo emailRecordPageQo);
}
