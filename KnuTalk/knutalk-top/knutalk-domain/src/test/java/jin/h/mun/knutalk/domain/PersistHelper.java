package jin.h.mun.knutalk.domain;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class PersistHelper {

	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	private EntityTransaction et;

	public PersistHelper() {
		emf = Persistence.createEntityManagerFactory( "knutalk" );
		em = emf.createEntityManager();
		et = em.getTransaction();
	}
	
	public void persist( final Object entity ) {
		executeInTransaction( e -> em.persist( e ) , entity );
	}
	
	public void persit( final Object... entities ) {
		for ( Object entity : entities ) {
			executeInTransaction( e -> em.persist( e ) , entity );
		}
	}
	
	public void delete( final Object entity ) {
		executeInTransaction( e -> em.remove( e ), entity );
	}
	
	public <T> T find( final Class<T> entityClass, final Object primaryKey ) {
		return em.find( entityClass, primaryKey );
	}
	
	public <T> void update( final Consumer<T> updateAction, final T updateRequest ) {
		executeInTransaction( updateAction, updateRequest );
	}
	
	public <T, U> void update( final BiConsumer<T, U> updateAction, final T entity, final U dto ) {
		executeInTransaction( updateAction, entity, dto );
	}
	
	public void update( final Performer performer ) {
		et.begin();
		performer.perform();
		et.commit();
	}
	
	public <T> void deleteAll( final Class<T> entityClass ) {
		Query deleteAllQuery = em.createQuery( "DELETE FROM " + entityClass.getSimpleName() );
		et.begin();
		deleteAllQuery.executeUpdate();
		et.commit();
	}
	
	public <T> Long countRow( final Class<T> entityClass ) {
		return em.createQuery( "SELECT count(e) FROM " + entityClass.getSimpleName() + " e", Long.class ).getSingleResult();
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
