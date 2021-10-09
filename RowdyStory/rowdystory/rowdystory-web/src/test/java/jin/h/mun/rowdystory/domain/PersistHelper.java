package jin.h.mun.rowdystory.domain;

import org.junit.jupiter.api.function.Executable;

import java.util.function.Consumer;

import javax.persistence.*;

public class PersistHelper {

	private final EntityManagerFactory emf;
	
	private final EntityManager em;
	
	public PersistHelper() {
		emf = Persistence.createEntityManagerFactory( "rowdystory" );
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
		executeInTransaction( () -> updateAction.accept( updateRequest ) );
	}
	
	public void update( final Executable executable ) {
		executeInTransaction( executable );
	}
	
	public <T> void deleteAll( final Class<T> entityClass ) {
		Query deleteAllQuery = em.createQuery( "DELETE FROM " + entityClass.getSimpleName() );
		executeInTransaction( deleteAllQuery::executeUpdate );
	}
	
	public <T> Long countRow( final Class<T> entityClass ) {
		return em.createQuery( "SELECT count(e) FROM " + entityClass.getSimpleName() + " e", Long.class ).getSingleResult();
	}

	private <T> void executeInTransaction( final Consumer<T> consumer, final T parameter ) {
		executeInTransaction( () -> consumer.accept( parameter ) );
	}

	private void executeInTransaction( final Executable executable ) {
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			executable.execute();
			tx.commit();
		} catch ( Throwable e ) {
			if ( tx != null ) tx.rollback();
			throwExceptionByInstance( e );
		}
	}

	private void throwExceptionByInstance( Throwable e ) {
		if ( e instanceof PersistenceException )
			throw ( PersistenceException ) e;

		if ( e instanceof NullPointerException )
			throw ( NullPointerException ) e;

		throw new RuntimeException( e );
	}

	public void clearEntityManager() {
		em.clear();
	}

	public void closeAll() {
		em.close();
		emf.close();
	}
	
}
