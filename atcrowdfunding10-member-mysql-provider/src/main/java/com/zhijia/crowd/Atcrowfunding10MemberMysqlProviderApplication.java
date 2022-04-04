package com.zhijia.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zhijia.crowd.mapper")
@SpringBootApplication
public class Atcrowfunding10MemberMysqlProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(Atcrowfunding10MemberMysqlProviderApplication.class, args);
	}

}
