package me.novocx.anticheat.api.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.novocx.anticheat.api.check.ICheck;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class AntiCheatFlagEvent extends Event implements Cancellable {

    private final Player player;
    private final ICheck check;
    @Getter(AccessLevel.NONE)
    private boolean cancelled;
    @Getter(AccessLevel.NONE)
    private static final HandlerList handlers = new HandlerList();

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
