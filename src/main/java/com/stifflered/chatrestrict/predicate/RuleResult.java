package com.stifflered.chatrestrict.predicate;

import org.jetbrains.annotations.Nullable;

public record RuleResult(boolean result, @Nullable KeyedPredicate broken) {

    public static final RuleResult TRUE = new RuleResult(true, null);
    public static final RuleResult FALSE = new RuleResult(false, null);

    public static RuleResult of(boolean result) {
        return result ? TRUE : FALSE;
    }

    public boolean hasDescription() {
        return this.broken != null && this.broken.getDescription() != null;
    }
}
