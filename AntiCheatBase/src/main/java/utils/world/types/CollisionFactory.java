package utils.world.types;


import cc.funkemunky.api.utils.ProtocolVersion;
import cc.funkemunky.api.utils.world.CollisionBox;
import org.bukkit.block.Block;

public interface CollisionFactory {
    CollisionBox fetch(ProtocolVersion version, Block block);
}