package parser;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import entity.ClassType;
import entity.Realization;
import entity.Subclass;
import log.Logger;

class PersistingResourceVisitor implements ResourceVisitor {

	private final EntityManager em;
	private Map<Element, entity.Class> classMap;
	private Map<Element, entity.Interface> interfaceMap;

	public PersistingResourceVisitor(EntityManager em) {
		this.classMap = new HashMap<>();
		this.interfaceMap = new HashMap<>();
		this.em = em;
	}

	@Override
	public void visit(Property umlProperty) {
		Logger.Info("found property:" + umlProperty);

		Type type = umlProperty.getType();

		entity.Association association = new entity.Association();
		Element obj = umlProperty.getOwner();
		entity.Class sourceClass = classMap.get(obj);
		entity.Class targetClass = classMap.get(type);

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
			Subclass subclass = new Subclass();
			entity.Class sourceClass = classMap.get(s);
			entity.Class targetClass = classMap.get(t);

			subclass.setSource(sourceClass);
			subclass.setTarget(targetClass);

			em.persist(subclass);
		}));

	}

	@Override
	public void visit(Class umlClass) {
		Logger.Info("found class:" + umlClass);

		entity.Class c = new entity.Class();
		c.setName(umlClass.getName());
		c.setType(umlClass.isAbstract() ? ClassType.ABSTRACT : ClassType.DEFAULT);
		em.persist(c);

		this.classMap.put(umlClass, c);
	}

	@Override
	public void visit(Interface umlInterface) {
		Logger.Info("found interface:" + umlInterface);

		entity.Interface i = new entity.Interface();
		i.setName(umlInterface.getName());
		em.persist(i);
		
		this.interfaceMap.put(umlInterface, i);

	}

	@Override
	public void visit(InterfaceRealization umInterfaceRealization) {
		Logger.Info("found interface realization: " + umInterfaceRealization);
		
		Stream<Element> sources = umInterfaceRealization.getSources().stream().filter(s -> UMLTypifier.isClass(s));
		Stream<Element> targets = umInterfaceRealization.getTargets().stream().filter(s -> UMLTypifier.isInterface(s));
		
		sources.forEach(s -> targets.forEach(t -> {
			Realization realization = new Realization();
			entity.Class sourceInterface = classMap.get(s);
			entity.Interface targetClass = interfaceMap.get(t);
			
			realization.setTarget(targetClass);
			realization.setSource(sourceInterface);

			if (sourceInterface != null && targetClass != null) {
				em.persist(realization);
			}
		}));
	}

}
