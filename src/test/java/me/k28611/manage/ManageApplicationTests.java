package me.k28611.manage;

import me.k28611.manage.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManageApplicationTests {

    @Test
    void contextLoads() {

        JwtTokenUtils j = new JwtTokenUtils();
        System.out.println(j.toString());

    }



}
