package springmvc.services.jpaservices;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import springmvc.domain.Customer;
import springmvc.services.CustomerService;
import springmvc.services.security.EncryptionService;

@Service
@Profile("jpadao")
public class CustomerserviceJPADaoImpl extends AbstractJpaDaoService implements CustomerService {
	private EncryptionService encryptionService;

	public CustomerserviceJPADaoImpl(EncryptionService encryptionService) {
		super();
		this.encryptionService = encryptionService;
	}

	@Override
	public List<Customer> listAll() {
		EntityManager em = emf.createEntityManager();
		return em.createQuery("from Customer", Customer.class).getResultList();
	}

	@Override
	public Customer getById(Integer id) {
		EntityManager em = emf.createEntityManager();
		return em.find(Customer.class, id);
	}

	@Override
	public Customer saveOrUpdate(Customer domainObject) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		if (domainObject.getUser() != null && domainObject.getUser().getPassword() != null) {
			domainObject.getUser()
					.setEncryptedPassword(encryptionService.encryptString(domainObject.getUser().getPassword()));
		}

		Customer savedCustomer = em.merge(domainObject);
		em.getTransaction().commit();

		return savedCustomer;
	}

	@Override
	public void delete(Integer id) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		em.remove(em.find(Customer.class, id));
		em.getTransaction().commit();
	}

}
