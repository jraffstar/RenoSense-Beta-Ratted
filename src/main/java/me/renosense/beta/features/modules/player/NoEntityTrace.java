package me.renosense.beta.features.modules.player;

import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;

public class NoEntityTrace extends Module {

    public static NoEntityTrace INSTANCE;
    public Setting<Boolean> pickaxe = this.register(new Setting("Pickaxe", true));

    public NoEntityTrace() {
        super("NoEntityTrace", "No trace", Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static NoEntityTrace getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoEntityTrace();
        }
        return INSTANCE;
    }


}
