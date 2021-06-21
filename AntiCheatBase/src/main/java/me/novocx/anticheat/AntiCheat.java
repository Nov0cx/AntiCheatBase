package me.novocx.anticheat;

import lombok.Getter;
import me.novocx.anticheat.api.AntiCheatAPI;
import me.novocx.anticheat.api.AntiCheatLoader;
import me.novocx.anticheat.api.theme.Theme;
import me.novocx.anticheat.api.theme.ThemeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AntiCheat extends JavaPlugin {
    @Getter
    private static AntiCheat instance;

    private ThemeManager themeManager;
    private AntiCheatAPI api;

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        saveDefaultConfig();

        loadAPI();
    }

    public void onDisable() {

    }

    private void loadAPI() {
        themeManager = new ThemeManager();
        api = new AntiCheatAPI() {
            @Override
            public ThemeManager getThemeManager() {
                return themeManager;
            }

            @Override
            public void flag(Plugin plugin, Player player, String info) {
                /*for (PlayerData data : alerts) {
                    data.getPlayer().sendMessage(themeManager
                            .getCurrentTheme()
                            .getExtensionFlagMessage(plugin, player, info));
                }*/
            }
        };
        AntiCheatLoader.setApi(api);
    }

    public static void c(String s) {
        Bukkit.getConsoleSender().sendMessage(t(s));
    }

    public static String t(String s) {
        return Theme.t(s);
    }
}
