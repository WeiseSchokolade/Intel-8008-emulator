package de.schoko.intel8008scriptcompiler.statements;

import java.util.List;

import de.schoko.intel8008emulator.BIN;
import de.schoko.intel8008emulator.Condition;
import de.schoko.intel8008scriptcompiler.GenerationContext;
import de.schoko.intel8008scriptcompiler.expression.BooleanBiExpression;

public class IfStatement extends Statement {
	private BooleanBiExpression condition;
	private List<Statement> statements;

	public IfStatement(BooleanBiExpression condition, List<Statement> statements) {
		this.condition = condition;
		this.statements = statements;
	}

	@Override
	public void generate(GenerationContext context) {
		context.associateNextInstructionConstructor(this);
		Condition conditionFlag = condition.generate(context);
		Statement foreshadowedStatement = context.getForeshadowedStatement();
		context.addInstructionConstructor(() -> {
			return () -> {
				short foreshadowedAddress = 0;
				if (foreshadowedStatement != null) foreshadowedAddress = foreshadowedStatement.getAddress();
				return List.of(
						BIN.JCC(conditionFlag.getOpposite()),
						BIN.VAL(foreshadowedAddress & 0xFF),
						BIN.VAL(foreshadowedAddress & 0xFF00)
						);
			};
		});
		for (int i = 0; i < statements.size(); i++) {
			Statement statement = statements.get(i);
			context.setForeshadowedStatement(i + 1 < statements.size() ? statements.get(i + 1) : foreshadowedStatement);
			statement.generate(context);
		}
	}

	@Override
	public String toString() {
		return "IfStatement[condition=" + condition + ", statements=" + statements + "]";
	}
}
