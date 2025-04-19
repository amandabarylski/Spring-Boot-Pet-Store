package pet.store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore (@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store: {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping("/pet_store/{petStoreId}")
	public PetStoreData modifyPetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Modifying pet store with ID={}", petStoreId);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PostMapping("/pet_store/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee addEmployee(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee employee) {
		log.info("Creating employee: {}", employee);
		return petStoreService.saveEmployee(petStoreId, employee);
	}
	
	@PostMapping("/pet_store/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer addCustomer(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer customer) {
		log.info("Creating customer: {}", customer);
		return petStoreService.saveCustomer(petStoreId, customer);
	}
	
	@GetMapping("/pet_store")
	public List<PetStoreData> listAllPetStores() {
		log.info("Getting all pet stores.");
		return petStoreService.retrieveAllPetStores();
	}
	
	@GetMapping("/pet_store/{petStoreId}")
	public PetStoreData getPetStoreById(@PathVariable Long petStoreId) {
		log.info("Fetching pet store with ID={}", petStoreId);
		return petStoreService.getPetStoreById(petStoreId);
	}
	
	@DeleteMapping("/pet_store/{petStoreId}")
	public Map<String, String> deletePetStore(@PathVariable Long petStoreId) {
		log.info("Deleting pet store with ID={}", petStoreId);
		petStoreService.deletePetStoreById(petStoreId);
		
		Map<String, String> deleteMessage = new HashMap<>();
		deleteMessage.put("message", "Successfully deleted pet store with ID=" + petStoreId);
		return deleteMessage;
	}
}
