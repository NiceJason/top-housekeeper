package com.tophousekeeper.system.configuration;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;


import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.*;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/5/27 13:09
 */

@org.springframework.context.annotation.Configuration
public class FreemarkerConfig {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(@Value("${auto_import}") String autoImport, @Value("${auto_include}") String autoInclude) {
        FreeMarkerConfigurer config = new FreeMarkerConfigurer();
        writerProperties(config);
        Configuration configuration = null;
        try {
            configuration = config.createConfiguration();
        } catch (IOException e) {
            System.out.println("freemarker配置bean，IO异常 "+e);
        } catch (TemplateException e) {
            System.out.println("freemarker配置bean异常 "+e);
        }
        setAutoImport(autoImport,configuration);
        setAutoInclude(autoInclude,configuration);
        config.setConfiguration(configuration);
        return config;
    }
    @Autowired
    private FreeMarkerProperties properties;

    private void writerProperties(FreeMarkerConfigurer config) {
        config.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
        config.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
        config.setDefaultEncoding(this.properties.getCharsetName());
        Properties settings = new Properties();
        settings.putAll(this.properties.getSettings());
        config.setFreemarkerSettings(settings);
    }
    private void setAutoImport(String autoImport,Configuration configuration) {
        if("_".equals(autoImport.trim())) {
            return;
        }
        String[] imports = autoImport.split(";");
        Map<String,String> importMap = new HashMap<String,String>(imports.length);
        for (String s : imports) {
            String[] keyValue = s.split("as");
            if (keyValue.length != 2) {
                System.out.println("freemarker配置auto_import格式不正确");
            }
            importMap.put(keyValue[1].trim(),keyValue[0].trim());
        }
        configuration.setAutoImports(importMap);
    }
    private void setAutoInclude(final String autoInclude,Configuration configuration) {
        if ("_".equals(autoInclude.trim())) {
            return;
        }
        String[] includes = autoInclude.split(";");
        for (String s : includes) {
            System.out.println(s);
        }
        List list = new ArrayList<String>(Arrays.asList(includes));
        configuration.setAutoIncludes(list);
    }
}

