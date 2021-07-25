package me.renosense.beta.features.modules.render;

import me.renosense.beta.event.events.PacketEvent;
import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;
import me.renosense.beta.util.Util;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Swing extends Module {

    public Swing() {
        super("Swing", "si", Category.PLAYER, true, false, false);
    }

    public Setting<mode> SwingMode = register(new Setting("Mode", mode.Offhand));
    public Setting<Boolean> NoMotion = register(new Setting("NoMotion", true));
    public Setting<Boolean> Fatigue = register(new Setting("Fatigue", false));

    public enum mode {
        Mainhand,
        Offhand,
        Disable
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send packet) {
        Object object = packet.getPacket();
        if (object instanceof CPacketAnimation) {
            if (this.SwingMode.getValue() == mode.Disable) {
                packet.setCanceled(true);
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.Fatigue.getValue()) {
            Util.mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 69420));
        }
        if (this.Fatigue.getValue().equals(false)) {
            Util.mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
        }
        if (SwingMode.getValue().equals(mode.Offhand)) {
            Util.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (SwingMode.getValue().equals(mode.Mainhand)) {
            Util.mc.player.swingingHand = EnumHand.MAIN_HAND;
        }

        if (NoMotion.getValue()) {
            if (Util.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
                Util.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                Util.mc.entityRenderer.itemRenderer.itemStackMainHand = Util.mc.player.getHeldItemMainhand();
            }
        }
    }
}