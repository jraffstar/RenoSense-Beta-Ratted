package me.sjnez.renosense.features.modules.render;

import me.sjnez.renosense.features.modules.Module;
import me.sjnez.renosense.features.setting.Setting;
import net.minecraft.client.settings.GameSettings;

public class FOV extends Module {

    public Setting<Float> fov = this.register(new Setting<>("Fov", 150.0f, 0.0f, 180.0f));

    public FOV() {
        super("Fov", "Changes ur FOV", Category.RENDER, true, false, false);
    }

    @Override
    public void onUpdate() {
        mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue());
    }
}