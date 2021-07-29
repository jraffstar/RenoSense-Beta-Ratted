package me.renosense.beta.features.modules.player;

import com.mojang.authlib.GameProfile;
import me.renosense.beta.features.command.Command;
import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;
import me.renosense.beta.util.Util;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module {

    private EntityOtherPlayerMP _fakePlayer;

    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing", Module.Category.PLAYER, false, false, false);
    }

    public Setting<String> playername = register(new Setting<String>("PlayerName", "Scott"));

    @Override
    public void onEnable() {
        if (FakePlayer.fullNullCheck()) {
            disable();
            return;
        }
        _fakePlayer = null;
        if (FakePlayer.mc.player != null) {
            _fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.randomUUID(), playername.getValue()));
            Command.sendMessage(String.format("%s has been spawned.", playername.getValue()));
            _fakePlayer.copyLocationAndAnglesFrom(FakePlayer.mc.player);
            _fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
            FakePlayer.mc.world.addEntityToWorld(-100, _fakePlayer);
        }
    }

    @Override
    public void onDisable() {
        if (_fakePlayer != null) {
            Util.mc.world.removeEntity(_fakePlayer);
            _fakePlayer = null;
        }

    }
}