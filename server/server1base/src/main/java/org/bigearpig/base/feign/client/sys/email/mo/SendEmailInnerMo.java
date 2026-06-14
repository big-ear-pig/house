package org.bigearpig.base.feign.client.sys.email.mo;

import lombok.Data;

import java.util.Map;

@Data
public class SendEmailInnerMo {

    private  String emailTemplateCode;

    private String addrTo;

    private Map<String,Object> paramMap;

}
