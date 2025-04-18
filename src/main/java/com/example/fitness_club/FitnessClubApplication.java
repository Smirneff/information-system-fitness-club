// src/main/java/com/example/fitness_club/FitnessClubApplication.java
package com.example.fitness_club;

import com.example.fitness_club.model.Role;
import com.example.fitness_club.model.User;
import com.example.fitness_club.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FitnessClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessClubApplication.class, args);
	}

	@Bean
	public CommandLineRunner seedAdmin(UserRepository userRepo,
									   PasswordEncoder passwordEncoder) {
		return args -> {
			String adminEmail = "admin@example.com";
			if (userRepo.findByEmail(adminEmail).isEmpty()) {
				User admin = new User(
						"Super", "Admin",
						adminEmail,
						passwordEncoder.encode("admin123"),
						"70000000000"
				);
				admin.setRole(Role.ADMIN);
				userRepo.save(admin);
				System.out.println("Default ADMIN created: " + adminEmail + " / admin123");
			}
		};
	}
}
