package cn.xiaofangwei.onlinesurvey.utilstest;

import cn.xiaofangwei.onlinesurvey.utils.GenerateVertifyCode;
import org.junit.Test;

public class GenerateVetifyCodeTest {
    @Test
    public void generateCodeTest(){
        String code = GenerateVertifyCode.generateVerifyCode(6);
        System.out.println(code);
    }
}
