package detection;

import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entity.Classifier;
import entity.ClassifierJoinedClassifier;
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
				Query q = em.createNativeQuery(definition.getQuery(), "ClassifierJoinedClassifier.class");
				
				List<ClassifierJoinedClassifier> result = new LinkedList();
				List<Object[]> results = q.getResultList();

				results.stream().forEach((record) -> {
					Long id1 = (Long) record[0];
					String name1 = (String) record[1];
					String type1 = (String) record[2];

					Long id2 = (Long) record[3];
					String name2 = (String) record[4];
					String type2 = (String) record[5];		
					
					ClassifierJoinedClassifier cjc = new ClassifierJoinedClassifier();
					Classifier parent = new Classifier();
					Classifier child = new Classifier();			
					
					parent.setId(id1);
					parent.setName(name1);
					parent.setType(ClassifierType.valueOf(type1));
					
					child.setId(id2);
					child.setName(name2);
					child.setType(ClassifierType.valueOf(type2));
										
					cjc.setChild(child);
					cjc.setParent(parent);
					result.add(cjc);
				});

				Logger.Info("result: " + result);

				Paragraph p = new Paragraph(definition, result);
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
