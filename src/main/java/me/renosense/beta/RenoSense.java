package me.renosense.beta;

import me.renosense.beta.manager.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = RenoSense.MODID, name = RenoSense.MODNAME, version = RenoSense.MODVER)
public class RenoSense {

    public static final String MODID = "renosense";
    public static final String MODNAME = "RenoSense";
    public static final String MODVER = "0.6.8";
    public static final Logger LOGGER = LogManager.getLogger("RenoSense");

    public static TotemPopManager totemPopManager;
    public static TimerManager timerManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;

    @Mod.Instance
    public static RenoSense INSTANCE;
    private static boolean unloaded;

    static {
        unloaded = false;
    }

    public static void load() {
        LOGGER.info("\n\nLoading RenoSense by Sjnez");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        totemPopManager = new TotemPopManager();
        timerManager = new TimerManager();
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("RenoSense successfully loaded!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading RenoSense by Sjnez");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        RenoSense.onUnload();
        timerManager = null;
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        LOGGER.info("RenoSense unloaded!\n");
    }

    public static void reload() {
        RenoSense.unload(false);
        RenoSense.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(RenoSense.configManager.config.replaceFirst("renosense/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("smoking on this piggot pack");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle("RenoSense " + MODVER);
        RenoSense.load();
    }

}