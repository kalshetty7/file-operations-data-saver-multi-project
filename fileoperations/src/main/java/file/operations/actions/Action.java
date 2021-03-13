package file.operations.actions;

import java.util.List;
import java.util.Set;

import file.operations.Checksum;
import file.operations.FileUtil;
import file.operations.actions.ActionItem.Items;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Action {
	private String name;
	private List<ActionItem> actionItems;
	private final Set<String> availableOperations = Items.valueEnumMap().keySet();
	private final List<String> checksumTypes = Checksum.CHECKSUM_TYPES;

	public void execute() {
		actionItems.forEach(c -> c.execute());
	}
	
	public String toString() {
		return FileUtil.convertToJSONString(this);
	}
}
