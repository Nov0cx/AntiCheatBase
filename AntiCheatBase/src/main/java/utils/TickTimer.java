package utils;


import me.novocx.lex.Lex;

public class TickTimer {
    protected int ticks = Lex.getInstance().getTickManager().getTicks(), defaultPassed;

    public TickTimer(int defaultPassed) {
        this.defaultPassed = defaultPassed;
    }

    public void reset() {
        ticks = Lex.getInstance().getTickManager().getTicks();
    }

    public boolean hasPassed() {
        return Lex.getInstance().getTickManager().getTicks() - ticks > defaultPassed;
    }

    public boolean hasPassed(int amount) {
        return Lex.getInstance().getTickManager().getTicks() - ticks > amount;
    }

    public boolean hasNotPassed() {
        return Lex.getInstance().getTickManager().getTicks() - ticks <= defaultPassed;
    }

    public boolean hasNotPassed(int amount) {
        return Lex.getInstance().getTickManager().getTicks() - ticks <= amount;
    }

    public int getPassed() {
        return Lex.getInstance().getTickManager().getTicks() - ticks;
    }
}
