package de.schoko.intel8008scriptcompiler;

import java.util.List;

import de.schoko.intel8008emulator.BinaryValue;
import de.schoko.intel8008scriptcompiler.statements.HaltStatement;
import de.schoko.intel8008scriptcompiler.statements.Statement;

public class MemoryGenerator {
	public List<BinaryValue> generate(List<Statement> statements) {
		if (!(statements.getLast() instanceof HaltStatement)) {
			statements.add(new HaltStatement());
		}
		GenerationContext context = new GenerationContext();
		
		for (int i = 0; i < statements.size(); i++) {
			Statement statement = statements.get(i);
			context.setForeshadowedStatement(i + 1 < statements.size() ? statements.get(i + 1) : null);
			statement.generate(context);
		}
		context.mapMemorySize();
		List<BinaryValue> memory = context.generateMemory();
		return memory;
	}
}
