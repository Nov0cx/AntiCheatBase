package me.novocx.anticheat.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.novocx.lex.Lex;

@Getter
@RequiredArgsConstructor
public class ConfigValue<T> {
    private final String path;
    private final String name;
    private final String comment;

    public void load(T defaultValue) {
        if (!Lex.getInstance().getConfig().contains(path + "." + name)) {
            set(defaultValue);
        }
    }

    public void set(T value) {
        if (!comment.isEmpty())
            Lex.getInstance().getConfig().set(path + ".comments." + name, comment);
        Lex.getInstance().getConfig().set(path + "." + name, value);
        Lex.getInstance().saveConfig();
    }

    public T getValue() {
        return (T) Lex.getInstance().getConfig().get(path + "." + name);
    }
}
