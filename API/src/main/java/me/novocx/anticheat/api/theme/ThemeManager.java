package me.novocx.anticheat.api.theme;

import lombok.Getter;
import lombok.Setter;
import me.novocx.anticheat.api.theme.impl.DefaultTheme;

import java.util.ArrayList;

public class ThemeManager {
    private final ArrayList<Theme> themes = new ArrayList<>();
    @Getter
    @Setter
    private Theme currentTheme;

    public ThemeManager() {
        themes.add(new DefaultTheme());
        currentTheme = getThemeByName("Lex");
    }

    public void registerTheme(Theme theme) {
        if(getThemeByName(theme.getThemeName()) == null)
            themes.add(theme);
    }

    public Theme getThemeByName(String name) {
        return themes.stream().filter(t -> t.getThemeName().equalsIgnoreCase(name)).findFirst().orElse(new DefaultTheme());
    }
}
