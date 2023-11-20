package ru.kata.spring.boot_security.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.kata.spring.boot_security.demo.repositories")
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

		RestTemplate restTemplate = new RestTemplate();
		Map<String,String> json = new HashMap<>();
		json.put("name","Test name");
		json.put("job","Test job");
		HttpEntity<Map<String , String>> request = new HttpEntity<>(json);
		String url = "https://reqres.in/api/users?page=2";
		String response  = restTemplate.getForObject(url, String.class);
		System.out.println(response);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response);
		JsonNode rolesNode = node.get("data");
		if (rolesNode != null && rolesNode.isArray()) {
			for (JsonNode role : rolesNode) {
				String roleName = role.get("email").asText();
				System.out.println("Возвращаем: " + roleName);
			}
		} else {
			System.out.println("Поле 'roles' отсутствует или не является массивом");
		}

	}

}
