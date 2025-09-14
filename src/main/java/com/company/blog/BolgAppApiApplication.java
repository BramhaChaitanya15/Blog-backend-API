package com.company.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.company.blog.config.AppConstants;
import com.company.blog.entities.Role;
import com.company.blog.repositories.RoleRepository;

@SpringBootApplication
public class BolgAppApiApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BolgAppApiApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {

		try {
			Role roleAdmin = new Role(AppConstants.ADMIN_USER, "ROLE_ADMIN");
			Role roleNormal = new Role(AppConstants.NORMAL_USER, "ROLE_NORMAL");

			List<Role> roles = List.of(roleAdmin, roleNormal);

			this.roleRepository.saveAll(roles);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
