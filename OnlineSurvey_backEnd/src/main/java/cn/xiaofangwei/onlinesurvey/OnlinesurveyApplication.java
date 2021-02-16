package cn.xiaofangwei.onlinesurvey;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableEncryptableProperties
@EnableTransactionManagement
public class OnlinesurveyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinesurveyApplication.class, args);
    }

}
