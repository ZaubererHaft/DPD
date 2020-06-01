package parser;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import entity.Classifier;
import entity.ClassifierType;
import entity.Derivation;
import entity.Method;
import log.Logger;

class PersistingResourceVisitor implements ResourceVisitor {

	private final EntityManager em;
	private Map<Element, Classifier> classifierMap;

	public PersistingResourceVisitor(EntityManager em) {
		this.classifierMap = new HashMap<>();

		this.em = em;
	}

	@Override
	public void visit(Property umlProperty) {
		Logger.Info("found property:" + umlProperty);

		Type type = umlProperty.getType();

		entity.Association association = new entity.Association();
		Element obj = umlProperty.getOwner();
		Classifier sourceClass = classifierMap.get(obj);
		Classifier targetClass = classifierMap.get(type);

		association.setSource(sourceClass);
		association.setTarget(targetClass);

		if (sourceClass != null && targetClass != null) {
			em.persist(association);
		}
	}

	@Override
	public void visit(Generalization umlGeneralization) {
		Logger.Info("found generalization:" + umlGeneralization);

		Stream<Element> sources = umlGeneralization.getSources().stream().filter(s -> UMLTypifier.isClass(s));
		Stream<Element> targets = umlGeneralization.getTargets().stream().filter(s -> UMLTypifier.isClass(s));

		sources.forEach(s -> targets.forEach(t -> {
			Derivation derivation = new Derivation();
			Classifier sourceClass = classifierMap.get(s);
			Classifier targetClass = classifierMap.get(t);

			derivation.setSource(sourceClass);
			derivation.setTarget(targetClass);

			em.persist(derivation);
		}));

	}

	@Override
	public void visit(Class umlClass) {
		Logger.Info("found class:" + umlClass);

		Classifier c = new Classifier();
		c.setName(umlClass.getName());
		c.setType(umlClass.isAbstract() ? ClassifierType.ABSTRACT : ClassifierType.DEFAULT);
		em.persist(c);

		this.classifierMap.put(umlClass, c);
	}

	@Override
	public void visit(Interface umlInterface) {
		Logger.Info("found interface:" + umlInterface);

		Classifier c = new Classifier();
		c.setName(umlInterface.getName());
		c.setType(ClassifierType.INTERFACE);

		em.persist(c);

		this.classifierMap.put(umlInterface, c);

	}

	@Override
	public void visit(InterfaceRealization umInterfaceRealization) {
		Logger.Info("found interface realization: " + umInterfaceRealization);

		Stream<Element> sources = umInterfaceRealization.getSources().stream().filter(s -> UMLTypifier.isClass(s));
		Stream<Element> targets = umInterfaceRealization.getTargets().stream().filter(s -> UMLTypifier.isInterface(s));

		sources.forEach(s -> targets.forEach(t -> {
			Derivation derivation = new Derivation();

			Classifier sourceInterface = classifierMap.get(s);
			Classifier targetClass = classifierMap.get(t);

			derivation.setTarget(targetClass);
			derivation.setSource(sourceInterface);

			if (sourceInterface != null && targetClass != null) {
				em.persist(derivation);
			}
		}));
	}

	@Override
	public void visit(Operation operation) {
		Logger.Info("found operation: " + operation);

		Method method = new Method();
		method.setName(operation.getName());
		method.setStatic(operation.isStatic());

		Element owner = operation.getOwner();
		Classifier c = this.classifierMap.get(owner);

		if (c != null) {
			method.setClassifier(c);
			em.persist(method);
		}

	}

	@Override
	public void visit(Behavior behavior) {
		Logger.Info("found behavior: " + behavior);

	}

}
