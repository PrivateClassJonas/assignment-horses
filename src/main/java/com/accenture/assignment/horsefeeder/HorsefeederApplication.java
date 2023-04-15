package com.accenture.assignment.horsefeeder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class HorsefeederApplication {

	public static void main(String[] args) {
		//System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
		SpringApplication.run(HorsefeederApplication.class, args);

	}

}
