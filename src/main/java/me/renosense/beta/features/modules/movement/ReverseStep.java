package me.renosense.beta.features.modules.movement;

import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;

public class ReverseStep extends Module {

    private final Setting<Integer> speed = this.register(new Setting<>("Speed", 0, 0, 20));

    public ReverseStep() {
        super("ReverseStep", "Speeds up downwards motion", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater()) {
            return;
        }
        if (mc.player.onGround) {
            ReverseStep.mc.player.motionY -= this.speed.getValue() / 10;
        }
    }
}