package cn.edu.zju.temp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "cn.edu.zju" })
public class TempMavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(TempMavenApplication.class, args);
	}

}
