package com.stifflered.chatrestrict.predicate;

import org.jetbrains.annotations.Nullable;

public class KeyedPredicate {

    private final String name;
    private final UserInputPredicate predicate;

    private boolean enabled = true;
    private final String description;

    public KeyedPredicate(String name, String description, UserInputPredicate predicate) {
        this.name = name;
        this.predicate = predicate;
        this.description = description;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public UserInputPredicate getPredicate() {
        return predicate;
    }

    @Nullable
    public String getDescription() {
        return description;
    }
}
