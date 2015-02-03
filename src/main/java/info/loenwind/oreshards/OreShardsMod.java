package info.loenwind.oreshards;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = OreShardsMod.MODID, version = OreShardsMod.VERSION, dependencies = "after:*")
public class OreShardsMod {
	
	public static final String MODID = "oreshards";
	public static final String VERSION = "1.0.2";
	public static Logger logger;

	private File config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		logger.info(MODID + " version " + VERSION + " loading...");
		config = event.getSuggestedConfigurationFile();
		OreShardsConfig.instance.loadConfig(config);
		OreShardsRegistry.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		OreShardsRegistry.init();
	}
}
