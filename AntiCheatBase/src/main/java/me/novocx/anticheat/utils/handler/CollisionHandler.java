package me.novocx.anticheat.utils.handler;

import cc.funkemunky.api.utils.BlockUtils;
import cc.funkemunky.api.utils.BoundingBox;
import cc.funkemunky.api.utils.MinecraftReflection;
import me.novocx.lex.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CollisionHandler {
    private final PlayerData data;
    private final List<Block> below = new ArrayList<>();
    private final List<Block> feet = new ArrayList<>();
    private final List<Block> all = new ArrayList<>();
    private final List<Block> above = new ArrayList<>();
    
    public CollisionHandler(PlayerData data) {
        this.data = data;
    }

    public void handle(BoundingBox box, World world) {
        clear();

        double minX = box.minX;
        double minY = box.minY;
        double minZ = box.minZ;
        double maxX = box.maxX;
        double maxY = box.maxY;
        double maxZ = box.maxZ;

        for (double x = minX; x <= maxX; x += maxX - minX) {
            for (double z = minZ; z <= maxZ; z += maxZ - minZ) {
                this.feet.add(BlockUtils.getBlock(new Location(world, x, minY - 0.1D, z)));
                this.below.add(BlockUtils.getBlock(new Location(world, x, minY - 1.5D, z)));
                this.above.add(BlockUtils.getBlock(new Location(world, x, maxY + 0.5D, z)));

                for (double y = minY; y <= maxY; y += 0.1D) {
                    this.all.add(BlockUtils.getBlock(new Location(world, x, y, z)));
                }
            }
        }
    }

    private void clear() {
        below.clear();
        feet.clear();
        all.clear();
        above.clear();
    }

    public boolean checkFeet(Predicate<Block> func) {
        return feet.stream().anyMatch(func);
    }

    public boolean checkBelow(Predicate<Block> func) {
        return below.stream().anyMatch(func);
    }

    public boolean check(Predicate<Block> func) {
        return all.stream().anyMatch(func);
    }

    public boolean checkAbove(Predicate<Block> func) {
        return above.stream().anyMatch(func);
    }

    public List<BoundingBox> getCollidingBoxes() {
        return MinecraftReflection.getCollidingBoxes(data.getPlayer(), data.getPlayer().getWorld(), data.getMovementInformation().box);
    }
}
