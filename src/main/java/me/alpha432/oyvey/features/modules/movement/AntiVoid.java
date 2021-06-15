package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AntiVoid
        extends Module {
    public Setting<Float> yLevel = this.register(new Setting<Float>("YLevel", 0.5F, 0.1F, 10.0F));


    public AntiVoid() {
        super("AntiVoid", "Glitches you up from void.", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (AntiVoid.fullNullCheck()) {
            return;
        }
        if (!AntiVoid.mc.player.noClip && AntiVoid.mc.player.posY <= yLevel.getValue()) {
            RayTraceResult trace = AntiVoid.mc.world.rayTraceBlocks(AntiVoid.mc.player.getPositionVector(), new Vec3d(AntiVoid.mc.player.posX, 0.0, AntiVoid.mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                return;
            }
            AntiVoid.mc.player.motionY = 0.41;
            if (AntiVoid.mc.player.getRidingEntity() != null) {
                AntiVoid.mc.player.getRidingEntity().setVelocity(0.0, 0.41, 0.0);
            }
        }
    }
}

