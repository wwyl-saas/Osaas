package com.fate.api.merchant;

import com.fate.api.merchant.dto.JwtToken;
import com.fate.api.merchant.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class Owntest {
    @Autowired
    JwtService jwtService;

    @Test
    public  void test111() {
        JwtToken tocken=new JwtToken();
        tocken.setUserId(8L);
        String aaa=jwtService.createToken(tocken);
        System.out.println(aaa);

    }
}
