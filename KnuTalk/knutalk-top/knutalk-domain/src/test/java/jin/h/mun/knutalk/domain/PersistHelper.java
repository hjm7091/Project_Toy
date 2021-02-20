package jin.h.mun.knutalk.domain;

import java.util.function.BiConsumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PersistHelper {

	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	private EntityTransaction et;

	public PersistHelper(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		em = emf.createEntityManager();
		et = em.getTransaction();
	}
	
	public void persist(Object entity) {
		et.begin();
		em.persist(entity);
		et.commit();
	}
	
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}
	
	public <T, U> void update(BiConsumer<T, U> updateAction, T entityClass, U dtoClass) {
		et.begin();
		updateAction.accept(entityClass, dtoClass);
		et.commit();
	}
	
	public void delete(Object entity) {
		et.begin();
		em.remove(entity);
		et.commit();
	}
	
	public void clearEntityManager() {
		em.clear();
	}
	
	public void closeAll() {
		em.close();
		emf.close();
	}
	
}
