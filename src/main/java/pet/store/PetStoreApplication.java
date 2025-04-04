package pet.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Due to the way the name was set up in the instructions, everything is technically inside the package "store" which is inside the "pet" package.
//If I had realized how strange it would look (and it doesn't need to be nested so deeply with nothing in "pet"),
//I would have named the top package pet-store and placed the dots for inner packages after that as this makes it more tedious to access the actual code in GitHub.

@SpringBootApplication
public class PetStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetStoreApplication.class, args);
	}

}
