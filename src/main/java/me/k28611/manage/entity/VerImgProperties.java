package me.k28611.manage.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kaptcha")
@Data
public class VerImgProperties {

    private String imgWidth;

    private String imgHeight;

    private String codeLength;

    private String codeList;
}
