package me.renosense.beta.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.renosense.beta.RenoSense;
import me.renosense.beta.features.command.Command;
import me.renosense.beta.features.modules.misc.FriendSettings;
import me.renosense.beta.manager.FriendManager;
import me.renosense.beta.util.Util;
import net.minecraft.network.play.client.CPacketChatMessage;

public class FriendCommand
        extends Command {
    public FriendCommand() {
        super("friend", new String[]{"<add/del/name/clear>", "<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            if (RenoSense.friendManager.getFriends().isEmpty()) {
                FriendCommand.sendMessage("Friend list empty D:.");
            } else {
                String f = "Friends: ";
                for (FriendManager.Friend friend : RenoSense.friendManager.getFriends()) {
                    try {
                        f = f + friend.getUsername() + ", ";
                    } catch (Exception exception) {
                    }
                }
                FriendCommand.sendMessage(f);
            }
            return;
        }
        if (commands.length == 2) {
            switch (commands[0]) {
                case "reset": {
                    RenoSense.friendManager.onLoad();
                    FriendCommand.sendMessage("Friends got reset.");
                    return;
                }
            }
            FriendCommand.sendMessage(commands[0] + (RenoSense.friendManager.isFriend(commands[0]) ? " is friended." : " isn't friended."));
            return;
        }
        if (commands.length >= 2) {
            switch (commands[0]) {
                case "add": {
                    RenoSense.friendManager.addFriend(commands[1]);
                    FriendCommand.sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                    if (FriendSettings.getInstance().notify.getValue()) {
                        Util.mc.player.connection.sendPacket(new CPacketChatMessage("/w " + commands[1] + " I just added you to my friends list on RenoSense!"));
                    }
                    return;
                }
                case "del": {
                    RenoSense.friendManager.removeFriend(commands[1]);
                    if (FriendSettings.getInstance().notify.getValue()) {
                        Util.mc.player.connection.sendPacket(new CPacketChatMessage("/w " + commands[1] + " I just removed you from my friends list on RenoSense!"));
                    }
                    FriendCommand.sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                    return;
                }
            }
            FriendCommand.sendMessage("Unknown Command, try friend add/del (name)");
        }
    }
}

