package detection;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entity.Classifier;
import log.Logger;
import pattern.PatternDefinition;

public class PatternDetector {

	private static final String PERSISTENCE_UNIT_NAME = "localhost";
	private final Collection<PatternDefinition> definitions;

	private EntityManager em;

	public PatternDetector(Collection<PatternDefinition> definitions) {
		this.definitions = definitions;
	}

	public DetectionReport detect() {
		// 1. establish database connection
		// 2. run queries for all definitions
		// 3. create and return report
		this.connectToDatabase();
		return this.runQueries();
	}

	private void connectToDatabase() {
		Logger.Info("connecting database...");
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		this.em = factory.createEntityManager();
	}

	private DetectionReport runQueries() {
		try {
			Logger.Info("run queries...");
			
			DetectionReport report = new DetectionReport();

			definitions.forEach(definition -> {
				Logger.Info("run query " + definition.getQuery());
				Query q = em.createNativeQuery(definition.getQuery(), Classifier.class);
				List<entity.Classifier> result = q.getResultList();
				Logger.Info("result: " + result);
				
				Paragraph p = new Paragraph(definition.getPatternName(), result.size());
				report.addParagraph(p);			
			});
			
			Logger.Info("done");
			return report;

		} catch (Exception e) {
			Logger.Error(e);
			throw e;
		} finally {
			Logger.Info("close database connection");
			this.em.close();
		}
	}
}
