package me.renosense.beta.features.modules.render;

import me.renosense.beta.event.events.PacketEvent;
import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fullbright extends Module {

    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.GAMMA));
    public Setting<Boolean> effects = this.register(new Setting<Boolean>("Effects", false));
    private float previousSetting = 1.0f;

    public Fullbright() {
        super("Fullbright", "Makes your game brighter.", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        this.previousSetting = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.GAMMA) {
            mc.gameSettings.gammaSetting = 1000.0f;
        }
        if (this.mode.getValue() == Mode.POTION) {
            mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5210));
        }
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.POTION) {
            mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        }
        mc.gameSettings.gammaSetting = this.previousSetting;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0 && event.getPacket() instanceof SPacketEntityEffect && this.effects.getValue().booleanValue()) {
            SPacketEntityEffect packet = (SPacketEntityEffect) event.getPacket();
            if (mc.player != null && packet.getEntityId() == mc.player.getEntityId() && (packet.getEffectId() == 9 || packet.getEffectId() == 15)) {
                event.setCanceled(true);
            }
        }
    }

    public static enum Mode {
        GAMMA,
        POTION;

    }
}