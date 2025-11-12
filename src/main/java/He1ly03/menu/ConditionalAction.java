package He1ly03.menu;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConditionalAction {
    private final List<ConditionalBlock> blocks;
    private final List<String> elseActions;

    public ConditionalAction(List<ConditionalBlock> blocks, List<String> elseActions) {
        this.blocks = blocks != null ? blocks : new ArrayList<>();
        this.elseActions = elseActions != null ? elseActions : new ArrayList<>();
    }

    public List<String> getActionsToExecute(Player player) {
        for (ConditionalBlock block : blocks) {
            if (block.checkConditions(player)) {
                return block.getActions();
            }
        }
        return elseActions;
    }

    public static class ConditionalBlock {
        private final List<String> conditions;
        private final List<String> actions;

        public ConditionalBlock(List<String> conditions, List<String> actions) {
            this.conditions = conditions != null ? conditions : new ArrayList<>();
            this.actions = actions != null ? actions : new ArrayList<>();
        }

        public boolean checkConditions(Player player) {
            if (conditions.isEmpty()) {
                return true;
            }

            for (String condition : conditions) {
                if (!MenuUtils.evaluateCondition(condition, player)) {
                    return false;
                }
            }
            return true;
        }

        public List<String> getActions() {
            return actions;
        }
    }
}

