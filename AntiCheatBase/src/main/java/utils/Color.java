package utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class Color {

    public static final String Red = ChatColor.RED.toString();
    public static final String Yellow = ChatColor.YELLOW.toString();
    public static final String Gold = ChatColor.GOLD.toString();
    public static final String Green = ChatColor.GREEN.toString();
    public static final String Aqua = ChatColor.AQUA.toString();
    public static final String Gray = ChatColor.GRAY.toString();
    public static final String Dark_Gray = ChatColor.DARK_GRAY.toString();
    public static final String Bold = ChatColor.BOLD.toString();
    public static final String Italics = ChatColor.ITALIC.toString();
    public static final String Strikethrough = ChatColor.STRIKETHROUGH.toString();
    public static final String White = ChatColor.WHITE.toString();
    public static final String Dark_Red = ChatColor.DARK_RED.toString();
    public static final String Dark_Green = ChatColor.DARK_GREEN.toString();
    public static final String Blue = ChatColor.BLUE.toString();
    public static final String Dark_Blue = ChatColor.DARK_BLUE.toString();
    public static final String Pink = ChatColor.LIGHT_PURPLE.toString();
    public static final String Purple = ChatColor.DARK_PURPLE.toString();
    public static final String Black = ChatColor.BLACK.toString();
    public static final String Underline = ChatColor.UNDERLINE.toString();

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String strip(String string) {
        return ChatColor.stripColor(string);
    }

    public static String getColorFromString(String string) {
        if (string.contains("&")) {
            return ChatColor.translateAlternateColorCodes('&', string);
        } else {
            String color = (String) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(null, string), null);

            if (color == null) {
                Bukkit.getLogger().log(Level.WARNING, "The color '" + string + "' does not exist.");
                return Strikethrough;
            }
            return color;
        }
    }
}

