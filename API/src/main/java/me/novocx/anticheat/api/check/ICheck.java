package me.novocx.anticheat.api.check;

import me.novocx.anticheat.api.check.annotation.CheckInfo;
import org.bukkit.entity.Player;

public interface ICheck {
    Player getPlayer();
    double getVl();
    boolean doBan();
    CheckInfo getCheckInfo();
}
