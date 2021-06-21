package utils;

import cc.funkemunky.api.utils.types.WrappedClass;
import me.novocx.lex.Lex;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MiscUtils {

    public static Material[] array = Arrays.stream(Material.values())
            .filter(mat -> mat.name().contains("LEGACY"))
            .toArray(Material[]::new);

    public static Map<EntityType, Vector> entityDimensions = new HashMap<>();;

    public static boolean containsIgnoreCase(String toCheck, String contains) {
        return toCheck.toLowerCase().contains(contains.toLowerCase());
    }

    public static int getOridinal(Material material) {
        int i = 0;
        for (Material mat : array) {
            if(mat.getId() == material.getId()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static BaseComponent[] toComponentArray(TextComponent message) {
        List<BaseComponent> components = new ArrayList<>();

        components.add(message);
        components.addAll(message.getExtra());
        return components.toArray(new BaseComponent[0]);
    }

    public static String injectColor(String string, String color) {
        String[] split = string.split("");

        return Arrays.stream(split).map(s -> color + s).collect(Collectors.joining());
    }

    public static Material getById(int id) {
        return Arrays.stream(Material.values()).filter(mat -> mat.getId() == id).findFirst()
                .orElse(Material.getMaterial("AIR"));
    }

    public static String line(String color) {
        return color + Color.Strikethrough + "-----------------------------------------------------";
    }

    public static String line() {
        return Color.Strikethrough + "-----------------------------------------------------";
    }

    public static String lineNoStrike(String color) {
        return color + "-----------------------------------------------------";
    }

    public static long copy(InputStream from, OutputStream to) throws IOException {
        if(from == null || to == null) return 0;

        byte[] buf = new byte[4096];
        long total = 0L;

        int r ;
        while((r = from.read(buf)) != -1) {
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    private static WrappedClass materialClass = new WrappedClass(Material.class);
    public static Material match(String material) {
        if(ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_13)) {
            return materialClass
                    .getMethod("matchMaterial", String.class, boolean.class)
                    .invoke(null, material, material.contains("LEGACY_"));
        } return Material.getMaterial(material.replace("LEGACY_", ""));
    }

    public static <T> List<T> combine(List<T> one, List<T> two) {
        one.addAll(two);

        return one;
    }

    public static String trimEnd(String string) {
        if(string.length() <= 1) {
            return string;
        }
        return string.substring(0, string.length() - 1);
    }

    public static String[] splitIntoLine(String input, int maxCharInLine) {

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while (word.length() > maxCharInLine) {
                output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
                word = word.substring(maxCharInLine - lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append("&f" + word + " ");

            lineLen += word.length() + 1;
        }
        // output.split();
        // return output.toString();
        return output.toString().split("\n");
    }

    public static <T> T getResult(Supplier<T> consumer) {
        return consumer.get();
    }

    public static String lineNoStrike() {
        return "-----------------------------------------------------";
    }

    public static String unloadPlugin(String pl) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        SimplePluginManager spm = (SimplePluginManager)pm;
        SimpleCommandMap cmdMap = null;
        List plugins = null;
        Map names = null;
        Map commands = null;
        Map listeners = null;
        boolean reloadlisteners = true;
        if(spm != null) {
            try {
                Field tp = spm.getClass().getDeclaredField("plugins");
                tp.setAccessible(true);
                plugins = (List)tp.get(spm);
                Field arr$ = spm.getClass().getDeclaredField("lookupNames");
                arr$.setAccessible(true);
                names = (Map)arr$.get(spm);

                Field len$;
                try {
                    len$ = spm.getClass().getDeclaredField("listeners");
                    len$.setAccessible(true);
                    listeners = (Map)len$.get(spm);
                } catch (Exception var19) {
                    reloadlisteners = false;
                }

                len$ = spm.getClass().getDeclaredField("commandMap");
                len$.setAccessible(true);
                cmdMap = (SimpleCommandMap)len$.get(spm);
                Field i$ = cmdMap.getClass().getDeclaredField("knownCommands");
                i$.setAccessible(true);
                commands = (Map)i$.get(cmdMap);
            } catch (IllegalAccessException | NoSuchFieldException var20) {
                return "Failed to unload plugin!";
            }
        }

        String var21 = "";
        Plugin[] var22 = Bukkit.getServer().getPluginManager().getPlugins();
        int var23 = var22.length;

        for(int var24 = 0; var24 < var23; ++var24) {
            Plugin p = var22[var24];
            if(p.getDescription().getName().equalsIgnoreCase(pl)) {
                pm.disablePlugin(p);
                var21 = var21 + p.getName() + " ";
                if(plugins != null && plugins.contains(p)) {
                    plugins.remove(p);
                }

                if(names != null && names.containsKey(pl)) {
                    names.remove(pl);
                }

                Iterator it;
                if(listeners != null && reloadlisteners) {
                    it = listeners.values().iterator();

                    while(it.hasNext()) {
                        SortedSet entry = (SortedSet)it.next();
                        Iterator c = entry.iterator();

                        while(c.hasNext()) {
                            RegisteredListener value = (RegisteredListener)c.next();
                            if(value.getPlugin() == p) {
                                c.remove();
                            }
                        }
                    }
                }

                if(cmdMap != null) {
                    it = commands.entrySet().iterator();

                    while(it.hasNext()) {
                        Map.Entry var25 = (Map.Entry) it.next();
                        if(var25.getValue() instanceof PluginCommand) {
                            PluginCommand var26 = (PluginCommand)var25.getValue();
                            if(var26.getPlugin() == p) {
                                var26.unregister(cmdMap);
                                it.remove();
                            }
                        }
                    }
                }
            }
        }

        return var21 + "has been unloaded and disabled!";
    }

    //Stolen from Luke
    public static boolean contains(Object[] array, Object obj) {
        for (Object object : array) if (object != null && object.equals(obj)) return true;
        return false;
    }

    public static <T> T parseObjectFromString(String s, Class<T> clazz) throws Exception {
        return clazz.getConstructor(new Class[] {String.class}).newInstance(s);
    }

    public static BoundingBox getEntityBoundingBox(LivingEntity entity) {
        if (entityDimensions.containsKey(entity.getType())) {
            Vector entityVector = entityDimensions.get(entity.getType());

            float minX = (float) Math.min(-entityVector.getX() + entity.getLocation().getX(), entityVector.getX() + entity.getLocation().getX());
            float minY = (float) Math.min(entity.getLocation().getY(), entityVector.getY() + entity.getLocation().getY());
            float minZ = (float) Math.min(-entityVector.getZ() + entity.getLocation().getZ(), entityVector.getZ() + entity.getLocation().getZ());
            float maxX = (float) Math.max(-entityVector.getX() + entity.getLocation().getX(), entityVector.getX() + entity.getLocation().getX());
            float maxY = (float) Math.max(entity.getLocation().getY(), entityVector.getY() + entity.getLocation().getY());
            float maxZ = (float) Math.max(-entityVector.getZ() + entity.getLocation().getZ(), entityVector.getZ() + entity.getLocation().getZ());
            return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        }
        return ReflectionsUtil.toBoundingBox(ReflectionsUtil.getBoundingBox(entity));
    }

    /* MAKE SURE TO ONLY RUN THIS METHOD IN onLoad() AND NO WHERE ELSE */
    public static void registerCommand(String name, JavaPlugin plugin) {
        plugin.getDescription().getCommands().put(name, new HashMap<>());
    }

    public static ItemStack createItem(Material material, int amount, String name, String... lore) {
        ItemStack thing = new ItemStack(material, amount);
        ItemMeta thingm = thing.getItemMeta();
        thingm.setDisplayName(Color.translate(name));
        ArrayList<String> loreList = new ArrayList<>();
        for (String string : lore) {
            loreList.add(Color.translate(string));
        }
        thingm.setLore(loreList);
        thing.setItemMeta(thingm);
        return thing;
    }

    public static boolean arraysSimilar(String[] one, String[] two) {
        if(one.length != two.length) return false;

        for (int i = 0; i < one.length; i++) {
            String a1 = one[i], a2 = two[i];

            if(!a1.equalsIgnoreCase(a2)) {
                return false;
            }
        }
        return true;
    }

    public static void loadPlugin(final String pl) {
        Plugin targetPlugin = null;
        String msg = "";
        final File pluginDir = new File("plugins");
        if (!pluginDir.isDirectory()) {
            return;
        }
        File pluginFile = new File(pluginDir, pl + ".jar");
        if (!pluginFile.isFile()) {
            for (final File f : pluginDir.listFiles()) {
                try {
                    if (f.getName().endsWith(".jar")) {
                        final PluginDescriptionFile pdf = Lex.getInstance().getPluginLoader().getPluginDescription(f);
                        if (pdf.getName().equalsIgnoreCase(pl)) {
                            pluginFile = f;
                            msg = "(via search) ";
                            break;
                        }
                    }
                }
                catch (InvalidDescriptionException e2) {
                    return;
                }
            }
        }
        try {
            Lex.getInstance().getServer().getPluginManager().loadPlugin(pluginFile);
            targetPlugin = getPlugin(pl);
            Lex.getInstance().getServer().getPluginManager().enablePlugin(targetPlugin);
        }
        catch (UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e3) {
            e3.printStackTrace();
        }
    }


    private static Plugin getPlugin(final String p) {
        for (final Plugin pl : Lex.getInstance().getServer().getPluginManager().getPlugins()) {
            if (pl.getDescription().getName().equalsIgnoreCase(p)) {
                return pl;
            }
        }
        return null;
    }

    public static <T> T getArgOrNull(T[] array, int index) {
        if(array.length > index) {
            return array[index];
        }
        return null;
    }

    public static void printToConsole(String string, Object... objects) {
        Lex.c(String.format(string, objects));
    }

    public static void printToConsole(String string) {
        Lex.c(string);
    }

    static {
        entityDimensions.put(EntityType.WOLF, new Vector(0.31, 0.8, 0.31));
        entityDimensions.put(EntityType.SHEEP, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.COW, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.PIG, new Vector(0.45, 0.9, 0.45));
        entityDimensions.put(EntityType.MUSHROOM_COW, new Vector(0.45, 1.3, 0.45));
        entityDimensions.put(EntityType.WITCH, new Vector(0.31, 1.95, 0.31));
        entityDimensions.put(EntityType.BLAZE, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.PLAYER, new Vector(0.3, 1.8, 0.3));
        entityDimensions.put(EntityType.VILLAGER, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.CREEPER, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.GIANT, new Vector(1.8, 10.8, 1.8));
        entityDimensions.put(EntityType.SKELETON, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.ZOMBIE, new Vector(0.31, 1.8, 0.31));
        entityDimensions.put(EntityType.SNOWMAN, new Vector(0.35, 1.9, 0.35));
        entityDimensions.put(EntityType.HORSE, new Vector(0.7, 1.6, 0.7));
        entityDimensions.put(EntityType.ENDER_DRAGON, new Vector(1.5, 1.5, 1.5));
        entityDimensions.put(EntityType.ENDERMAN, new Vector(0.31, 2.9, 0.31));
        entityDimensions.put(EntityType.CHICKEN, new Vector(0.2, 0.7, 0.2));
        entityDimensions.put(EntityType.OCELOT, new Vector(0.31, 0.7, 0.31));
        entityDimensions.put(EntityType.SPIDER, new Vector(0.7, 0.9, 0.7));
        entityDimensions.put(EntityType.WITHER, new Vector(0.45, 3.5, 0.45));
        entityDimensions.put(EntityType.IRON_GOLEM, new Vector(0.7, 2.9, 0.7));
        entityDimensions.put(EntityType.GHAST, new Vector(2, 4, 2));
    }
}

