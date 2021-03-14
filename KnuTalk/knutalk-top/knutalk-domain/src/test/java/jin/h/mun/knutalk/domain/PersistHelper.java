package jin.h.mun.knutalk.domain;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
		executeInTransaction( e -> em.persist( e ) , entity );
	}
	
	public void delete( final Object entity ) {
		executeInTransaction( e -> em.remove( e ), entity );
	}
	
	public <T> T find( final Class<T> entityClass, final Object primaryKey ) {
		return em.find( entityClass, primaryKey );
	}
	
	public <T, U> void update( final BiConsumer<T, U> updateAction, final T entity, final U dto ) {
		executeInTransaction( updateAction, entity, dto );
	}
	
	public void clearEntityManager() {
		em.clear();
	}
	
	public void closeAll() {
		em.close();
		emf.close();
	}
	
	private <T> void executeInTransaction( final Consumer<T> consumer, final T entity ) {
		et.begin();
		consumer.accept( entity );
		et.commit();
	}
	
	private <T, U> void executeInTransaction( final BiConsumer<T, U> consumer, final T entity, final U dto ) {
		et.begin();
		consumer.accept( entity, dto );
		et.commit();
	}
	
}
