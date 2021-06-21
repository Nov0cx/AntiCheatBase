package me.novocx.anticheat.api.theme.impl;

import me.novocx.anticheat.api.check.ICheck;
import me.novocx.anticheat.api.theme.Theme;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DefaultTheme implements Theme {
    @Override
    public String getThemeName() {
        return "default";
    }

    @Override
    public String getPrefix() {
        return Theme.t("&8[&bPrefix&8] ");
    }

    @Override
    public String getAlertHoverEventText(int ping, String info, double tps, double vl, boolean dev) {
        return Theme.t("&2ping: " + ping + "\n&2tps: " + tps + "\n&2info: " + info + "\n&2vl: " + vl);
    }

    @Override
    public String getAlertMessage(Player player, double vl, int ping, double tps, ICheck check, boolean dev) {
        return Theme.t(getPrefix() + "&6" + player.getName() + "&7 has failed &6" + check.getCheckInfo().name() + "&7." + (dev ? " &c[DEV]" : ""));
    }

    @Override
    public String getExtensionFlagMessage(Plugin plugin, Player player, String information) {
        return Theme.t("&e[" + plugin.getName() + " Check Extension] "
                + player.getName()
                + " failed a check §8(Info: " + information + ")");
    }
}
