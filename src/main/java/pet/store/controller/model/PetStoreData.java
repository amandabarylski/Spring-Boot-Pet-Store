package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreData {

	private Long petStoreId;
	private String petStoreName;
	private String petStoreAddress;
	private String petStoreCity;
	private String petStoreState;
	private int petStoreZip;
	private String petStorePhone;
	//When I tried to add a pet store, I got an error due to the sets being null (it didn't want to use the iterator).
	//I thought it might be because I hadn't made the sets new HashSets, but on trying it again after modifications it threw the same error.
	//As it was set up the same as the example from the video I had no idea why it was upset about these sets being null when it wasn't there.
	//After I added the if statements it worked fine; however, I knew that the PetParks sets when creating the contributors in the videos were null.
	//I then realized that the sets in the original PetStore class weren't initialized like this, so I adjusted them and tried again.
	private Set<PetStoreCustomer> customers = new HashSet<>();
	private Set<PetStoreEmployee> employees = new HashSet<>();
	
	
	public PetStoreData (PetStore petStore) {
		petStoreId = petStore.getPetStoreId();
		petStoreName = petStore.getPetStoreName();
		petStoreAddress = petStore.getPetStoreAddress();
		petStoreCity = petStore.getPetStoreCity();
		petStoreState = petStore.getPetStoreState();
		petStoreZip = petStore.getPetStoreZip();
		petStorePhone = petStore.getPetStorePhone();
		
		//Since PetStoreData uses sets of PetStoreCustomer and PetStoreEmployee
		//while the PetStore used in the constructor will have Customers and Employees,
		//I finished the constructors in those classes and used them here.
		for (Customer customer : petStore.getCustomers()) {
			customers.add(new PetStoreCustomer(customer));
		}
		
		for (Employee employee : petStore.getEmployees()) {
			employees.add(new PetStoreEmployee(employee));
		}
		
//		if (petStore.getCustomers() != null) {
//			for (Customer customer : petStore.getCustomers()) {
//				customers.add(new PetStoreCustomer(customer));
//			}
//		}
//		
//		if (petStore.getEmployees() != null) {
//			for (Employee employee : petStore.getEmployees()) {
//				employees.add(new PetStoreEmployee(employee));
//			}
//		}
	}
}
