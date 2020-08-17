package std;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackageClasses = BootTestApp.class)
public class BootTestApp {

	public static void main(String[] args) {
		SpringApplication.run(BootTestApp.class, args);
	}
}
