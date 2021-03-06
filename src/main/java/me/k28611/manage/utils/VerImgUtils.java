package me.k28611.manage.utils;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import me.k28611.manage.entity.VerImgProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;
@Configuration
public class VerImgUtils {

    @Bean
    public Producer captcha(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","150");
        properties.setProperty("kaptcha.image.height","50");
        properties.setProperty("kaptcha.textproducer.char.string","0123456789");
        properties.setProperty("kaptcha.textproducer.char.length","4");

        Config config = new Config(properties);

        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
