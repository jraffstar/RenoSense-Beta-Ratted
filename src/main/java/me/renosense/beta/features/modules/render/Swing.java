package me.renosense.beta.features.modules.render;

import me.renosense.beta.event.events.PacketEvent;
import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Swing extends Module {

    public Swing() {
        super("Swing", "si", Category.PLAYER, true, false, false);
    }

    public Setting<mode> SwingMode = register(new Setting("Mode", mode.Offhand));
    public Setting<Boolean> NoMotion = register(new Setting("NoMotion", true));

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

        if (SwingMode.getValue().equals(mode.Offhand)) {
            mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (SwingMode.getValue().equals(mode.Mainhand)) {
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        }

        if (NoMotion.getValue()) {
            if (mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
                mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
            }
        }
    }
}