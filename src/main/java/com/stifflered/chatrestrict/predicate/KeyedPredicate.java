package com.stifflered.chatrestrict.predicate;

public class KeyedPredicate {

    private final String name;
    private final UserInputPredicate predicate;

    private boolean enabled = true;

    public KeyedPredicate(String name, UserInputPredicate predicate) {
        this.name = name;
        this.predicate = predicate;
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
}
