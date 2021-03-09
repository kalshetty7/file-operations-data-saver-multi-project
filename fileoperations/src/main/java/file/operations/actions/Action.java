package file.operations.actions;

import java.util.List;
import java.util.Set;

import file.operations.actions.ActionItem.Items;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Action {
	private String name;
	private List<ActionItem> actionItems;
	private final Set<String> availableOperations = Items.valueEnumMap().keySet();

	public void execute() {
		actionItems.forEach(c -> c.execute());
	}
}
