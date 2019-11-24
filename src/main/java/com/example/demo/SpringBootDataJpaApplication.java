package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.example.demo.models.service.IUploadFileService;

@SpringBootApplication
@Controller
public class SpringBootDataJpaApplication implements CommandLineRunner {

	private static final Logger log = 	LoggerFactory.getLogger(SpringBootDataJpaApplication.class);
	@Autowired
	IUploadFileService iUploadFileService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	//crear la carpeta uploads de forma automática y no manual a través de comandlinerunner y este método
	@Override
	public void run(String... args) throws Exception {
		
		iUploadFileService.deleteAll();
		iUploadFileService.init();
		
		//encryptar password
		String password = "12345";
		for(int i=0; i<2; i++) {
			String bcryptpassword = passwordEncoder.encode(password);
			System.out.println(bcryptpassword);
		}
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return  new BCryptPasswordEncoder();
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	/*@Bean
	public CommandLineRunner run(RestTemplate restTemplate, Model model ) throws Exception{
		return args -> {
			Quote quote = restTemplate.getForObject("https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
			
			log.info(quote.toString());
		
		};
	} */

}
