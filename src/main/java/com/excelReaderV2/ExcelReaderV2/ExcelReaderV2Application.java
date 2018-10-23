package com.excelReaderV2.ExcelReaderV2;

import com.excelReaderV2.ExcelReaderV2.Service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import javax.annotation.Resource;

@SpringBootApplication
public class ExcelReaderV2Application implements CommandLineRunner {

	@Resource
	StorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(ExcelReaderV2Application.class, args);
	}

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory(){
		return new HibernateJpaSessionFactoryBean();
	}

	@Override
	public void run(String... args) throws Exception{
		storageService.deleteAll();
		storageService.init();
	}
}
