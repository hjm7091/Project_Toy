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

	public PersistHelper( final String persistenceUnitName ) {
		emf = Persistence.createEntityManagerFactory( persistenceUnitName );
		em = emf.createEntityManager();
		et = em.getTransaction();
	}
	
	public void persist( final Object entity ) {
		et.begin();
		em.persist( entity );
		et.commit();
	}
	
	public <T> T find( final Class<T> entityClass, final Object primaryKey ) {
		return em.find( entityClass, primaryKey );
	}
	
	public <T, U> void update( final BiConsumer<T, U> updateAction, final T entityClass, final U dtoClass ) {
		et.begin();
		updateAction.accept( entityClass, dtoClass );
		et.commit();
	}
	
	public void delete( final Object entity ) {
		et.begin();
		em.remove( entity );
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
