package cn.xiaofangwei.onlinesurvey;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
@MapperScan("cn.xiaofangwei.onlinesurvey.mapper")
public class OnlinesurveyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinesurveyApplication.class, args);
    }

}
