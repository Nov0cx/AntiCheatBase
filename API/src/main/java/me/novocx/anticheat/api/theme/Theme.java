package me.novocx.anticheat.api.theme;

import me.novocx.anticheat.api.check.ICheck;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Theme {
    String getThemeName();
    String getPrefix();
    String getAlertHoverEventText(int ping, String info, double tps, double vl, boolean dev);
    String getAlertMessage(Player player, double vl, int ping, double tps, ICheck check, boolean dev);
    String getExtensionFlagMessage(Plugin plugin, Player player, String information);

    static String t(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
