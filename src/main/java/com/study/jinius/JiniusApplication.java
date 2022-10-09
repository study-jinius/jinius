package com.study.jinius;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @EnableJpaAuditing : JPA 감시 기능 활성화.
// DB에 데이터가 저장되거나 수정될 때 언제 누가 했는지를 자동으로 관리할 수 있게 한다.
@EnableJpaAuditing
@SpringBootApplication
public class JiniusApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiniusApplication.class, args);
	}

}
