package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.RuleResult;
import com.stifflered.chatrestrict.predicate.UserInputPredicate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public record PlayerPermissionPredicate(String permission) implements UserInputPredicate {

    @Override
    public RuleResult get(String input, Player sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(this.permission)) {
                return RuleResult.TRUE;
            }
        }

        return RuleResult.FALSE;
    }

    @Override
    public Component getRichDescription() {
        return Component.text("- If there is a player online with permission: " + this.permission);
    }

    public static class PlayerPermissionPredicateDeserializer implements PredicateDeserializer<PlayerPermissionPredicate> {

        @Override
        public PlayerPermissionPredicate deserialize(ConfigurationSection section) {
            String permission = section.getString("permission");
            return new PlayerPermissionPredicate(permission);
        }
    }
}
