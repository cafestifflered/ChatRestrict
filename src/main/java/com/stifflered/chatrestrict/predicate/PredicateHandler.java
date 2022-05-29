package com.stifflered.chatrestrict.predicate;

import com.stifflered.chatrestrict.*;
import com.stifflered.chatrestrict.predicate.impl.common.*;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.serializer.plain.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.logging.*;

public class PredicateHandler {

    static {
        UserInputPredicateRegistry.bootstrap();
    }

    private final ConfigurationSection section;
    private final CompoundPredicate compoundPredicate;
    private boolean restricted = true;

    public PredicateHandler(Plugin plugin) {
        this.section = plugin.getConfig();
        this.compoundPredicate = CompoundPredicate.CompoundPredicateSerializer.INSTANCE.deserialize(section);
        plugin.getLogger().log(Level.INFO, "Registered %s predicates".formatted(compoundPredicate.predicates().length));
    }

    public boolean canChat(Component message, Player player) {
        if (!restricted) {
            return true;
        }
        if (player.hasPermission("chatrestrict.bypass")) {
            return true;
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

    public boolean terminateOn() {
        return section.getBoolean("predicate-terminate-on");
    }
}
