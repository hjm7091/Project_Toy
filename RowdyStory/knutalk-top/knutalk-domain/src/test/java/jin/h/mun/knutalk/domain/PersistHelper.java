package jin.h.mun.knutalk.domain;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class PersistHelper {

	private final EntityManagerFactory emf;
	
	private final EntityManager em;
	
	public PersistHelper() {
		emf = Persistence.createEntityManagerFactory( "knutalk" );
		em = emf.createEntityManager();
	}
	
	public void persist( final Object entity ) {
		executeInTransaction( em::persist, entity );
	}
	
	public void persist( final Object... entities ) {
		for ( Object entity : entities ) {
			executeInTransaction( em::persist, entity );
		}
	}
	
	public void delete( final Object entity ) {
		executeInTransaction( em::remove, entity );
	}
	
	public <T> T find( final Class<T> entityClass, final Object primaryKey ) {
		return em.find( entityClass, primaryKey );
	}
	
	public <T> void update( final Consumer<T> updateAction, final T updateRequest ) {
		executeInTransaction( updateAction, updateRequest );
	}
	
	public void update( final Performer performer ) {
		executeInTransaction( performer );
	}
	
	public <T> void deleteAll( final Class<T> entityClass ) {
		Query deleteAllQuery = em.createQuery( "DELETE FROM " + entityClass.getSimpleName() );
		executeInTransaction( deleteAllQuery::executeUpdate );
	}
	
	public <T> Long countRow( final Class<T> entityClass ) {
		return em.createQuery( "SELECT count(e) FROM " + entityClass.getSimpleName() + " e", Long.class ).getSingleResult();
	}

	private <T> void executeInTransaction( final Consumer<T> consumer, final T updateRequest ) {
		executeInTransaction( () -> consumer.accept( updateRequest ) );
	}

	private void executeInTransaction( final Performer performer ) {
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			performer.perform();
			tx.commit();
		} catch ( Exception e ) {
			if ( tx != null ) tx.rollback();
			throw e;
		}
	}

	public void clearEntityManager() {
		em.clear();
	}

	public void closeAll() {
		em.close();
		emf.close();
	}
	
}
