package kr.ibct.springboilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@Profile("")
public class SpringboilerplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringboilerplateApplication.class, args);
    }

}

