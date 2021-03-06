package parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.VisibilityKind;

import entity.Classifier;
import entity.ClassifierType;
import entity.Derivation;
import entity.Method;
import entity.MethodInvocation;
import entity.MethodParameter;
import entity.MethodReturnType;
import entity.Visibility;
import log.Logger;

class PersistingResourceVisitor implements ResourceVisitor {

	private final EntityManager em;
	private Map<Element, Classifier> classifierMap;
	private Map<Element, Method> methodMap;

	private Map<Element, List<Property>> propertyMap;

	public PersistingResourceVisitor(EntityManager em) {
		this.classifierMap = new HashMap<>();
		this.propertyMap = new HashMap<>();
		this.methodMap = new HashMap<>();

		this.em = em;
	}

	@Override
	public void visit(Property umlProperty) {
		Logger.Info("found property:" + umlProperty);

		int lowerBound = umlProperty.lowerBound();
		int upperBound = umlProperty.upperBound();

		String lowerMultiplicity = "" + lowerBound;
		String upperMultiplicity = "" + upperBound;

		if (lowerBound < 0) {
			lowerMultiplicity = "*";
		}
		if (upperBound < 0) {
			upperMultiplicity = "*";
		}

		Type type = umlProperty.getType();

		entity.Association association = new entity.Association();
		Element obj = umlProperty.getOwner();
		Classifier sourceClass = classifierMap.get(obj);
		Classifier targetClass = classifierMap.get(type);

		association.setSource(sourceClass);
		association.setTarget(targetClass);
		association.setLowerMultiplicity(lowerMultiplicity);
		association.setUpperMultiplicity(upperMultiplicity);

		if (sourceClass != null && targetClass != null) {
			em.persist(association);

			List<Property> props = propertyMap.get(obj);
			if (props == null) {
				props = new LinkedList<Property>();
				propertyMap.put(obj, props);
			}

			props.add(umlProperty);
		} else {
			Logger.Warn("skip property as no belonging source and target classifier was found");
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
			} else {
				Logger.Warn("skip interface realization as no belonging source and target classifier was found");
			}
		}));
	}

	@Override
	public void visit(Operation operation) {
		Logger.Info("found operation: " + operation);

		Method method = new Method();
		method.setName(operation.getName());
		method.setStatic(operation.isStatic());

		if (operation.getVisibility() == VisibilityKind.PRIVATE_LITERAL) {
			method.setVisibility(Visibility.PRIVATE);
		} else {
			method.setVisibility(Visibility.PUBLIC);
		}

		Element owner = operation.getOwner();
		Classifier c = this.classifierMap.get(owner);

		if (c != null) {
			method.setClassifier(c);
			this.methodMap.put(operation, method);

			em.persist(method);

			EList<Parameter> parameters = operation.getOwnedParameters();
			parameters.forEach(p -> {
				Logger.Info("found parameter " + p + " for operation");
				Classifier type = this.classifierMap.get(p.getType());

				if (p.getDirection() == ParameterDirectionKind.RETURN_LITERAL) {
					Logger.Info("parameter is return value");

					if (type != null) {
						MethodReturnType mrt = new MethodReturnType();
						mrt.setClassifier(type);
						mrt.setMethod(method);

						em.persist(mrt);
					} else {
						Logger.Warn("skip return value as no belonging classifier was found");
					}
				} else {
					MethodParameter mp = new MethodParameter();
					mp.setName(p.getName());
					mp.setMethod(method);

					if (type != null) {
						mp.setClassifier(type);
						em.persist(mp);
					} else {
						Logger.Warn("skip parameter as no belonging classifier was found");
					}
				}

			});

		} else {
			Logger.Warn("skip operation as no belonging classifier was found");
		}
	}

	@Override
	public void visit(OpaqueBehavior behavior) {
		Logger.Info("found behavior: " + behavior);

		List<String> bodies = behavior.getBodies();

		// owner of spec is class
		Element owner = behavior.getSpecification().getOwner();
		List<Property> props = this.propertyMap.get(owner);

		// ToDo: method invocation don't depend on properties alone (consider parameters
		// for example)
		if (props != null) {
			for (Property property : props) {
				for (String body : bodies) {
					if (body.contains(property.getName())) {
						MethodInvocation invocation = new MethodInvocation();

						Classifier classifier = classifierMap.get(property.getType());
						invocation.setClassifier(classifier);

						Method method = methodMap.get(behavior.getSpecification());
						invocation.setMethod(method);

						if (method != null && classifier != null) {
							em.persist(invocation);
						}
					}
				}
			}
		}
	}

}
