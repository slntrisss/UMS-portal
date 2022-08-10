package org.jwt.userManagement;

import org.jwt.userManagement.auth.AppUserPermission;
import org.jwt.userManagement.model.AppUser;
import org.jwt.userManagement.model.Role;
import org.jwt.userManagement.enumeration.AccountStatus;
import org.jwt.userManagement.enumeration.Status;
import org.jwt.userManagement.repository.RoleRepo;
import org.jwt.userManagement.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.jwt.userManagement.auth.AppUserPermission.*;

@SpringBootApplication
public class UserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}


	@Bean
	CommandLineRunner run(RoleRepo roleRepo, UserRepo userRepo, PasswordEncoder encoder){
		return args -> {

			Role manager = new Role(null, "MANAGER", new ArrayList<AppUserPermission>(){{
				add(USER_READ);
				add(USER_UPDATE);
			}});
			Role user = new Role(null, "USER", new ArrayList<AppUserPermission>(){{
				add(USER_READ);
			}});
			Role admin = new Role(null, "ADMIN", new ArrayList<AppUserPermission>(){{
				add(USER_READ);
				add(USER_UPDATE);
				add(USER_CREATE);
				add(USER_DELETE);
			}});

			roleRepo.save(manager);
			roleRepo.save(user);
			roleRepo.save(admin);

			byte[] tomImg = getPicInBytes("src/main/resources/image/tom.png");
			byte[] andrewImg = getPicInBytes("src/main/resources/image/andrew.webp");
			byte[] tobeyImg = getPicInBytes("src/main/resources/image/tobey.jpeg");

			userRepo.save(new AppUser(null, "Tom", "Holland",
					"tom@gmail.com", "tom", tomImg, "", encoder.encode("password"), Status.INACTIVE,0,
					AccountStatus.NON_LOCKED, user));
			userRepo.save(new AppUser(null, "Andrew", "Garfield",
					"andrew@gmail.com", "andrew", andrewImg, "", encoder.encode("password"), Status.ACTIVE, 0,
					AccountStatus.NON_LOCKED, manager));
			userRepo.save(new AppUser(null, "Tobey", "Maguire",
					"tobey@gmail.com", "tobey", tobeyImg, "", encoder.encode("password"), Status.ACTIVE, 0,
					AccountStatus.NON_LOCKED, admin));
		};
	}
	private byte[] getPicInBytes(String path){
		File file = new File(path);
		byte[] picInBytes = new byte[(int)file.length()];
		try(FileInputStream fileInputStream = new FileInputStream(file)){
			fileInputStream.read(picInBytes);
		}catch (IOException exc){
			exc.getStackTrace();
		}
		return picInBytes;
	}
}
