package me.novocx.anticheat.api;

import me.novocx.anticheat.api.theme.ThemeManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface AntiCheatAPI {
    ThemeManager getThemeManager();
    void flag(Plugin plugin, Player player, String info);
}
