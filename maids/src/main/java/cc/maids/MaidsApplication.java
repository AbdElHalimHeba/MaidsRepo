package cc.maids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MaidsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaidsApplication.class, args);
	}

}
