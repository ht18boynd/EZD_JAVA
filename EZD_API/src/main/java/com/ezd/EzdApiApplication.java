package com.ezd;

import com.ezd.Dto.Role;
import com.ezd.models.Auth;
import com.ezd.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@EnableWebMvc
@SpringBootApplication(scanBasePackages = "com.ezd")
public class EzdApiApplication implements CommandLineRunner {

	@Autowired
	private AuthRepository authRepository;

	public static void main(String[] args) {
		SpringApplication.run(EzdApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Auth adminAccount = authRepository.findByRole(Role.ADMIN);
		if(null == adminAccount) {
			Auth auth = new Auth();

			auth.setEmail("admin@gmail.com");
			auth.setRole(Role.ADMIN);

			auth.setPassword(new BCryptPasswordEncoder().encode("admin1234"));
			authRepository.save(auth);

		}
	}
}
