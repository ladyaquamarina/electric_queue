package queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "queue")
public class ElectricQueueApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectricQueueApplication.class, args);
    }
}
