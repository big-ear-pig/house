package org.bigearpig.sys.module.template.component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Component
public class FreemarkerTemplateComponent {
    public String generateStringFreeMarkerTemplate(String templateContent, Map<String, Object> map) {
        String result = "";
        try {
            Configuration configuration = new Configuration(Configuration.getVersion());
            Template template = new Template("id", templateContent, configuration);
            StringWriter stringWriter = new StringWriter();
            template.process(map, stringWriter);
            result = stringWriter.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }


    public void generateFileFreeMarkerTemplate(String filePath, String templateContent, Map<String, Object> map) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                // 如果路径不存在创建路径
                boolean f = parent.mkdirs();
                if (!f) {
                    throw new RuntimeException("创建文件夹失败");
                }
            }
            Configuration configuration = new Configuration(Configuration.getVersion());
            Template template = new Template("id", templateContent, configuration);
            FileWriter fileWriter = new FileWriter(filePath);
            template.process(map, fileWriter);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
