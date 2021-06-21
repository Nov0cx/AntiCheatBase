package utils;

import cc.funkemunky.api.utils.types.Reflections;
import cc.funkemunky.api.utils.types.WrappedClass;
import cc.funkemunky.api.utils.types.WrappedMethod;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CraftReflection {
    public static WrappedClass craftHumanEntity = Reflections.getCBClass("entity.CraftHumanEntity"); //1.7-1.14
    public static WrappedClass craftEntity = Reflections.getCBClass("entity.CraftEntity"); //1.7-1.14
    public static WrappedClass craftItemStack = Reflections.getCBClass("inventory.CraftItemStack"); //1.7-1.14
    public static WrappedClass craftBlock = Reflections.getCBClass("block.CraftBlock"); //1.7-1.14
    public static WrappedClass craftPlayer = Reflections.getCBClass("entity.CraftPlayer");
    public static WrappedClass craftWorld = Reflections.getCBClass("CraftWorld"); //1.7-1.14
    public static WrappedClass craftInventoryPlayer = Reflections.getCBClass("inventory.CraftInventoryPlayer"); //1.7-1.14
    public static WrappedClass craftServer = Reflections.getCBClass("CraftServer"); //1.7-1.14\
    public static WrappedClass craftChunk = Reflections.getCBClass("CraftChunk");
    public static WrappedClass craftMagicNumbers = Reflections.getCBClass("util.CraftMagicNumbers");

    //Vanilla Instances
    private static WrappedMethod itemStackInstance = craftItemStack.getMethod("asNMSCopy", ItemStack.class); //1.7-1.14
    private static WrappedMethod humanEntityInstance = craftHumanEntity.getMethod("getHandle"); //1.7-1.14
    private static WrappedMethod entityInstance = craftEntity.getMethod("getHandle"); //1.7-1.14
    private static WrappedMethod blockInstance = craftBlock.getMethod("getNMSBlock"); //1.7-1.14
    private static WrappedMethod worldInstance = craftWorld.getMethod("getHandle"); //1.7-1.14
    private static WrappedMethod bukkitEntity = MinecraftReflection.entity.getMethod("getBukkitEntity"); //1.7-1.14
    private static WrappedMethod getInventory = craftInventoryPlayer.getMethod("getInventory"); //1.7-1.14
    private static WrappedMethod mcServerInstance = craftServer.getMethod("getServer"); //1.7-1.14
    private static WrappedMethod entityPlayerInstance = craftPlayer.getMethod("getHandle");
    private static WrappedMethod chunkInstance = craftChunk.getMethod("getHandle");
    private static WrappedMethod methodGetBlockFromMaterial = ProtocolVersion.getGameVersion()
            .isOrAbove(ProtocolVersion.V1_13) ? craftMagicNumbers.getMethod("getBlock", Material.class)
            : craftMagicNumbers.getMethod("getBlock", int.class);

    public static <T> T getVanillaItemStack(ItemStack stack) {
        return itemStackInstance.invoke(null, stack);
    }

    public static <T> T getEntityHuman(HumanEntity entity) {
        return humanEntityInstance.invoke(entity);
    }

    public static <T> T getEntity(Entity entity) {
        return entityInstance.invoke(entity);
    }

    public static <T> T getEntityPlayer(Player player) {
        return entityPlayerInstance.invoke(player);
    }

    public static <T> T getVanillaBlock(Block block) {
        return blockInstance.invoke(block);
    }

    public static <T> T getVanillaWorld(World world) {
        return worldInstance.invoke(world);
    }

    public static Entity getBukkitEntity(Object vanillaEntity) {
        return bukkitEntity.invoke(vanillaEntity);
    }

    public static <T> T getVanillaInventory(Player player) {
        return getInventory.invoke(player.getInventory());
    }

    public static <T> T getMinecraftServer() {
        return mcServerInstance.invoke(Bukkit.getServer());
    }

    public static <T> T getVanillaChunk(Chunk chunk) {
        return chunkInstance.invoke(chunk);
    }

    public static <T> T getVanillaBlock(Material material) {
        if(ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_13)) {
            return methodGetBlockFromMaterial.invoke(null, material);
        } else {
            return methodGetBlockFromMaterial.invoke(null, material.getId());
        }
    }
}
