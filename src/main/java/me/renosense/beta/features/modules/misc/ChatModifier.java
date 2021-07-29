package me.renosense.beta.features.modules.misc;

import me.renosense.beta.event.events.PacketEvent;
import me.renosense.beta.features.command.Command;
import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;
import me.renosense.beta.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatModifier extends Module {

    public Setting<Suffix> suffix = register(new Setting<Suffix>("Suffix", Suffix.NONE, "Your Suffix."));
    public Setting<Boolean> clean = register(new Setting<Boolean>("CleanChat", false, "Cleans your chat"));
    public Setting<Boolean> infinite = register(new Setting<Boolean>("Infinite", false, "Makes your chat infinite."));
    public Setting<Boolean> autoQMain = register(new Setting<Boolean>("AutoQMain", false, "Spams AutoQMain"));
    public Setting<Boolean> qNotification = register(new Setting<Object>("QNotification", false, v -> autoQMain.getValue()));
    public Setting<Integer> qDelay = register(new Setting<Object>("QDelay", Integer.valueOf(9), Integer.valueOf(1), Integer.valueOf(90), v -> autoQMain.getValue()));
    private final Timer timer = new Timer();
    private static ChatModifier INSTANCE = new ChatModifier();

    public ChatModifier() {
        super("ChatModifier", "Modifies your chat", Module.Category.MISC, true, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ChatModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatModifier();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (autoQMain.getValue()) {
            if (!shouldSendMessage((EntityPlayer) ChatModifier.mc.player)) {
                return;
            }
            if (qNotification.getValue()) {
                Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
            }
            ChatModifier.mc.player.sendChatMessage("/queue main");
            timer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/")) {
                return;
            }
            switch (suffix.getValue()) {
                case RENOSENSE: {
                    s = s + " ||| \u13D2\u13AC\u13C1\u13BE\u13D5\u13AC\u13C1\u13D5\u13AC";
                    break;
                }
                case DOTGOD: {
                    s = s + " \u1D05\u1D0F\u1D1B\u0262\u1D0F\u1D05.\u1D04\u1D04";
                    break;
                }
                case SN0W: {
                    s = s + " \u2744";
                    break;
                }
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            packet.message = s;
        }
    }


    private boolean shouldSendMessage(EntityPlayer player) {
        if (player.dimension != 1) {
            return false;
        }
        if (!timer.passedS(qDelay.getValue().intValue())) {
            return false;
        }
        return player.getPosition().equals((Object) new Vec3i(0, 240, 0));
    }

    public enum Suffix {
        NONE,
        RENOSENSE,
        DOTGOD,
        SN0W

    }
}