package pattern;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PatternDefinitionExtractor {
	private File path;

	public PatternDefinitionExtractor(File path) {
		this.path = path;
	}

	public Collection<PatternDefinition> extractDefinitions() {
		Stream<File> fileDefinitions = Arrays.stream(path.listFiles()).filter(f -> f.getName().endsWith(".json"));
		return fileDefinitions.map(f -> createWith(f)).collect(Collectors.toList());
	}

	private PatternDefinition createWith(File file) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(file, PatternDefinition.class);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
