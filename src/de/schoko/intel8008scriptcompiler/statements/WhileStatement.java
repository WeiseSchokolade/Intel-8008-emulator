package de.schoko.intel8008scriptcompiler.statements;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.Condition;
import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.expression.BooleanBiExpression;

public class WhileStatement extends Statement {
	private BooleanBiExpression condition;
	private List<Statement> statements;

	public WhileStatement(BooleanBiExpression condition, List<Statement> statements) {
		this.condition = condition;
		this.statements = statements;
	}

	@Override
	public void generate(GenerationContext context) {
		Statement foreshadowedStatement = context.getForeshadowedStatement();
		context.associateNextInstructionConstructor(this);
		for (int i = 0; i < statements.size(); i++) {
			Statement statement = statements.get(i);
			context.setForeshadowedStatement(i + 1 < statements.size() ? statements.get(i + 1) : foreshadowedStatement);
			statement.generate(context);
		}
		Condition conditionFlag = condition.generate(context);
		context.addInstructionConstructor(() -> {
			return () -> {
				short foreshadowedAddress = 0;
				if (foreshadowedStatement != null) foreshadowedAddress = this.getAddress();
				return List.of(
						BIN.JCC(conditionFlag),
						BIN.VAL(foreshadowedAddress & 0xFF),
						BIN.VAL(foreshadowedAddress & 0xFF00)
						);
			};
		});
	}

	@Override
	public String toString() {
		return "WhileStatement[condition=" + condition + ", statements=" + statements + "]";
	}
}
