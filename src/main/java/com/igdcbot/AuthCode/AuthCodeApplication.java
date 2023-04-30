package com.igdcbot.AuthCode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Arrays;

@CrossOrigin
@SpringBootApplication(scanBasePackages = {
		"com.igdcbot.AuthCode",
		"com.igdcbot.AuthCode.controller",
		"com.igdcbot.AuthCode.controller.error",
		"com.igdcbot.AuthCode.service"
})
@ServletComponentScan
public class AuthCodeApplication {

	public static void main(String[] args) throws Exception {

		if (args.length > 0) {

			if (!Arrays.stream(args).anyMatch(s -> s.contains("tester.username"))) {
				throw new Exception("Argument tester.username is missing.");
			}

			if (!Arrays.stream(args).anyMatch(s -> s.contains("tester.password"))) {
				throw new Exception("Argument tester.password is missing.");
			}

			try {
				 Arrays.stream(args).filter(s -> s.contains("tester.username"))
						.findFirst().map(s -> s.split("=")[1]);
			} catch (ArrayIndexOutOfBoundsException ex) {
				throw new NullPointerException("Arguments tester.username value cannot be null or empty");
			}

			try {
				Arrays.stream(args).filter(s -> s.contains("tester.password"))
						.findFirst().map(s -> s.split("=")[1]);
			} catch (ArrayIndexOutOfBoundsException ex) {
				throw new NullPointerException("Arguments tester.password value cannot be null or empty");
			}

			SpringApplication.run(AuthCodeApplication.class, args);

		} else {
			throw new Exception("Arguments are missing.");
		}

	}

}
