package me.renosense.beta.features.modules.movement;

import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;
import me.renosense.beta.util.EntityUtil;
import me.renosense.beta.util.Util;

//made by sjnez

public class AntiWeb extends Module {

    public Setting<Boolean> HoleOnly = register(new Setting("HoleOnly", true));
    public Setting<Float> timerSpeed = register(new Setting("Speed", 4.0f, 0.1f, 50.0f));

    public float speed = 1.0f;

    public AntiWeb() {
        super("AntiWeb", "Turns on timer when in a web", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        this.speed = timerSpeed.getValue();
    }


    @Override
    public void onUpdate() {
        if (HoleOnly.getValue()) {
            if (mc.player.isInWeb && EntityUtil.isInHole(Util.mc.player)) {
                mc.timer.tickLength = 50.0f / ((this.timerSpeed.getValue() == 0.0f) ? 0.1f : this.timerSpeed.getValue());
            } else {
                mc.timer.tickLength = 50.0f;
            }
            if (mc.player.onGround && EntityUtil.isInHole(Util.mc.player)) {
                mc.timer.tickLength = 50.0f;
            }
        }
        if (!HoleOnly.getValue()) {
            if (mc.player.isInWeb) {
                mc.timer.tickLength = 50.0f / ((this.timerSpeed.getValue() == 0.0f) ? 0.1f : this.timerSpeed.getValue());

            } else {
                mc.timer.tickLength = 50.0f;

            }
            if (mc.player.onGround) {
                mc.timer.tickLength = 50.0f;
            }
        }
    }
}