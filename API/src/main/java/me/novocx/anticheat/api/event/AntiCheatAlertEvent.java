package me.novocx.anticheat.api.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.novocx.anticheat.api.check.ICheck;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class AntiCheatAlertEvent extends Event implements Cancellable {
    private final Player player;
    private final ICheck check;
    @Setter
    private TextComponent textComponent;
    @Getter(AccessLevel.NONE)
    private boolean cancelled;
    @Getter(AccessLevel.NONE)
    private static final HandlerList handlers = new HandlerList();

    public AntiCheatAlertEvent(Player player, ICheck check, TextComponent component) {
        this.player = player;
        this.check = check;
        this.textComponent = component;
    }

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
