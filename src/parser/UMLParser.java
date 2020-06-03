package parser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.emf.ecore.resource.Resource;

import log.Logger;

public class UMLParser {

	private static final String PERSISTENCE_UNIT_NAME = "localhost";
	private EntityManager em;
	private Resource resource;
	private final String file;

	public UMLParser(String file) {
		this.file = file;
	}

	public void parse() {
		this.connectToDatabase();
		this.loadResource();
		this.parseResource();
	}

	private void connectToDatabase() {
		Logger.Info("connecting database...");
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		this.em = factory.createEntityManager();
	}

	private void loadResource() {
		Logger.Info("loading resource...");
		ResourceLoader loader = new ResourceLoader(file);

		this.resource = loader.load();

	}

	private void parseResource() {
		try {
			Logger.Info("parsing data");

			PersistingResourceVisitor prv = new PersistingResourceVisitor(this.em);
			ResourceParser parser = new ResourceParser(this.resource, prv);

			this.em.getTransaction().begin();
			parser.parse();
			Logger.Info("commit changes");
			this.em.getTransaction().commit();
			Logger.Info("done.");
		} catch (Exception e) {
			Logger.Error(e);
			this.em.getTransaction().rollback();
			throw e;
		} finally {
			Logger.Info("close database connection");
			this.em.close();
		}
	}

}
