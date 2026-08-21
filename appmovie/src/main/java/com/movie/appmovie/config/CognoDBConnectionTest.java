package com.movie.appmovie.config;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CognoDBConnectionTest implements CommandLineRunner {

	private final Driver driver;

	public CognoDBConnectionTest(Driver driver) {
		this.driver = driver;
	}

	@Override
	public void run(String... args) {

		try (Session session = driver.session()) {

			String result = session.run("RETURN 'CognoDB Connected Successfully!' AS message").single().get("message")
					.asString();

			System.out.println("======================================");
			System.out.println(result);
			System.out.println("======================================");
		}
	}
}
