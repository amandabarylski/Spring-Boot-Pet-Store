package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

	@Autowired
	private PetStoreDao petStoreDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private CustomerDao customerDao;

	//It wasn't in the instructions, but I put the transactional annotation here since it was in the videos.
	//Oddly enough, when I got it working, the IDs I got for the two created pet stores were 8 and 9.
	//I might have tried 7 times when it wasn't working, or it might have just decided to start counting at 8, I'm not sure.
	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		//While working on week 15, I edited the return statement to be separate from the save statement.
		copyPetStoreFields(petStore, petStoreData);
		PetStore dbPetStore = petStoreDao.save(petStore);
		return new PetStoreData(dbPetStore);
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		if(Objects.isNull(petStoreId)) {
			return new PetStore();
		} else {
			return findPetStoreById(petStoreId);
		}
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " does not exist."));
	}

	//To start with, I followed what I did last week for savePetStore, including the additional methods called.
	//I then added the other methods required due to the employee being a child of the pet store.
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		
		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(findPetStoreById(petStoreId));
		findPetStoreById(petStoreId).getEmployees().add(employee);
		
		Employee dbEmployee = employeeDao.save(employee);
		return new PetStoreEmployee(dbEmployee);
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if(Objects.isNull(employeeId)) {
			return new Employee();
		} else {
			return findEmployeeById(petStoreId, employeeId);
		}
	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " does not exist."));
		if(employee.getPetStore().getPetStoreId() != petStoreId) {
			throw new IllegalArgumentException("Employee with Id=" + employeeId + " does not work for pet store with ID=" + petStoreId +
					", or the pet store with this Id does not exist.");
		} else {
			return employee;
		}
	}

	//I began by copying and pasting the contents of saveEmployee, and then went through and made the necessary changes.
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		
		copyCustomerFields(customer, petStoreCustomer);
		customer.getPetStores().add(findPetStoreById(petStoreId));
		findPetStoreById(petStoreId).getCustomers().add(customer);
		
		Customer dbCustomer = customerDao.save(customer);
		return new PetStoreCustomer(dbCustomer);
	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if(Objects.isNull(customerId)) {
			return new Customer();
		} else {
			return findCustomerById(petStoreId, customerId);
		}
	}

	//In order to loop through the pet stores, I first used the findAll method to create a list.
	private Customer findCustomerById(Long petStoreId, Long customerId) {
		List<PetStore> petStores = petStoreDao.findAll();
		for(PetStore petStore : petStores) {
			if (petStore.getPetStoreId() == petStoreId) {
				return customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " does not exist."));
			}
		}
		
		throw new IllegalArgumentException("Pet store with Id=" + petStoreId + " does not exist.");
	}

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> petStoreDataList = new LinkedList<>();
		
		for(PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			
			psd.getEmployees().clear();
			psd.getCustomers().clear();
			
			petStoreDataList.add(psd);
		}
		
		return petStoreDataList;
	}

	//Although I don't like the similarity between getPetStoreById and findPetStoreById, they are both describing what they do.
	@Transactional(readOnly = true)
	public PetStoreData getPetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		return new PetStoreData(petStore);
	}

	public void deletePetStoreById(Long petStoreId) {
		PetStore petStoreToDelete = findPetStoreById(petStoreId);
		petStoreDao.delete(petStoreToDelete);
	}
	
}
