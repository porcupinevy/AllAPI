package com.tutorial.apidemo.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tutorial.apidemo.models.Product;
import com.tutorial.apidemo.repositories.ProductRepository;
// now connect with mySQL using JPA
/*
 * docker run -d --rm --name mysql-spring-boot-tutorial\
 * -e MYSQL_ROOT_PASSWORD= 123456\
 * -e MYSQL_USER = truongpv\
 * -e MYSQL_PASSWORD= 123456\
 * -e MYSQL_DATABASE = test_db\
 * -p 3309:3306\
 * -- volume mysql-spring-boot-tutorial-demo:/var/lib/mysql\
 * mysql
 * 
 */
@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
@Bean
CommandLineRunner initDatabase(ProductRepository productRepository) {
    return new CommandLineRunner() {
        
        @Override
        public void run(String... args) throws Exception {
    	Product productA = new Product("Macabsjfgas",2022,2400.0,"");
    	Product productB = new Product("Macabsjasasfgas",2021,40.0,"");
    	logger.info("insert data: "+productRepository.save(productA));
    	logger.info("insert data: "+productRepository.save(productB));
        }
    };
}
}
