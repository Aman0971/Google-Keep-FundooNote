package org.fundoonotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FundooNotesAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(FundooNotesAppApplication.class, args);
    }

}
