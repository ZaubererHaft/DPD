package parser;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

class ResourceLoader {
	private final String path;

	public ResourceLoader(String path) {
		this.path = path;
	}

	public Resource load() {
		URI typesUri = URI.createFileURI(path);
		ResourceSet set = new ResourceSetImpl();

		set.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		set.createResource(typesUri);
		Resource r = set.getResource(typesUri, true);

		if (r == null) {
			throw new IllegalArgumentException("unable to parse resource of path " + path);
		}

		return r;
	}
}
