package ai.smartcity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class SmartcityBackendApplication {
	
	@Autowired
    private ObjectMapper objectMapper;
	
	
    public static void main(String[] args) {
        SpringApplication.run(SmartcityBackendApplication.class, args);
    }
    
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }
}
