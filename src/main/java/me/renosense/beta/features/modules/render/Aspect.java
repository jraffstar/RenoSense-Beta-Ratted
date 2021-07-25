package me.renosense.beta.features.modules.render;

import me.renosense.beta.event.events.PerspectiveEvent;
import me.renosense.beta.features.modules.Module;
import me.renosense.beta.features.setting.Setting;
import me.renosense.beta.util.Util;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Aspect extends Module {

    public Setting<Double> aspect = this.register(new Setting<Double>("aspect", Util.mc.displayWidth / Util.mc.displayHeight + 0.0, 0.0, 3.0));

    public Aspect() {
        super("AspectRatio", "Stretched res like fortnite", Category.RENDER, true, false, false);

    }

    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent event) {
        event.setAspect(aspect.getValue().floatValue());
    }
}