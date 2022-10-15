package com.stifflered.chatrestrict.predicate;

import com.stifflered.chatrestrict.ChatRestrictPlugin;
import com.stifflered.chatrestrict.predicate.impl.common.CompoundPredicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class PredicateHandler {

    static {
        UserInputPredicateRegistry.bootstrap();
    }

    private final ConfigurationSection section;
    private final CompoundPredicate compoundPredicate;
    private final Map<String, KeyedPredicate> predicateMap = new HashMap<>();
    private boolean restricted = false;

    public PredicateHandler(ChatRestrictPlugin plugin) {
        this.section = plugin.getRules();
        this.compoundPredicate = CompoundPredicate.CompoundPredicateSerializer.INSTANCE.deserialize(section);

        for (KeyedPredicate predicate : this.compoundPredicate.predicates()) {
            this.predicateMap.put(predicate.getName(), predicate);
        }
        plugin.getLogger().log(Level.INFO, "Registered %s rules".formatted(compoundPredicate.predicates().length));
    }

    public RuleResult canChat(Component message, Player player) {
        if (player.hasPermission("chatrestrict.bypass.all")) {
            return new RuleResult(true, null);
        }
        if (restricted) {
            return new RuleResult(false, null);
        }

        String stringMsg = PlainTextComponentSerializer.plainText().serialize(message);
        return compoundPredicate.get(stringMsg, player);
    }

    public void setIsChatRestricted(boolean override) {
        this.restricted = override;
    }

    public boolean isChatRestricted() {
        return this.restricted;
    }

    // Debug only
    @ApiStatus.Internal
    public CompoundPredicate getCompoundPredicate() {
        return compoundPredicate;
    }

    @Nullable
    public KeyedPredicate getRule(String ruleKey) {
        return this.predicateMap.get(ruleKey);
    }

    public void setAllRules(boolean enabled) {
        for (KeyedPredicate predicate : compoundPredicate.predicates()) {
            predicate.setEnabled(enabled);
        }
    }

    public KeyedPredicate[] getRules() {
        return this.compoundPredicate.predicates();
    }
}
