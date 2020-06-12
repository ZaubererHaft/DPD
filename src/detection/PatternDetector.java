package detection;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entity.Classifier;
import entity.ClassifierType;
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
				Query q = em.createNativeQuery(definition.getQuery());

				@SuppressWarnings("unchecked")
				List<Object[]> results = q.getResultList();
				Collection<String> joinedResult = mapResults(results);

				Logger.Info("result: " + joinedResult);
				Paragraph p = new Paragraph(definition, joinedResult);
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

	private Collection<String> mapResults(List<Object[]> results) {
		List<String> result = new LinkedList<>();

		results.stream().forEach((record) -> {
			result.add(Arrays.stream(record).map(Object::toString).reduce((a, b) -> a + "," + b).get());
		});

		return result;
	}
}
