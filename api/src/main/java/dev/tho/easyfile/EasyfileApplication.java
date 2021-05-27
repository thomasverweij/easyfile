package dev.tho.easyfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.logging.Logger;


@SpringBootApplication
public class EasyfileApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	public static Logger logger = Logger.getLogger("tho.dev.easyfile");

	public static void main(String[] args) {
		SpringApplication.run(EasyfileApplication.class, args);
	}

}
