package me.renosense.beta.features.gui.components.items.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.renosense.beta.RenoSense;
import me.renosense.beta.features.gui.OyVeyGui;
import me.renosense.beta.features.modules.client.ClickGui;
import me.renosense.beta.features.setting.Setting;
import me.renosense.beta.util.RenderUtil;
import me.renosense.beta.util.Util;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class EnumButton
        extends Button {
    public Setting setting;

    public EnumButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? RenoSense.colorManager.getColorWithAlpha(RenoSense.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : RenoSense.colorManager.getColorWithAlpha(RenoSense.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        RenoSense.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.RED + (this.setting.currentEnumName().equalsIgnoreCase("ABC") ? "ABC" : this.setting.currentEnumName()), this.x + 2.3f, this.y - 1.7f - (float) OyVeyGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            Util.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.setting.increaseEnum();
    }

    @Override
    public boolean getState() {
        return true;
    }
}
