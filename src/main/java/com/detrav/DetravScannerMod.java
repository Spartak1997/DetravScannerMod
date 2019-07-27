package com.detrav;

import com.detrav.net.DetravProPickPacket00;
import org.apache.logging.log4j.LogManager;

import com.detrav.net.DetravNetwork;
import com.detrav.proxies.CommonProxy;
import com.detrav.utils.DetravCreativeTab;
import com.detrav.utils.GTppHelper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.GregTech_API;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = DetravScannerMod.MODID, version = DetravScannerMod.VERSION,dependencies = "required-after:IC2;required-after:gregtech;after:miscutils;after:bartworks")
public class DetravScannerMod
{
    public static final String MODID = "detravscannermod";
    public static final String VERSION = "1.5.4";
    public static final String DEBUGOVERRIDE = "@false";
    public static final boolean DEBUGBUILD = Boolean.parseBoolean(DEBUGOVERRIDE.substring(1));
    public static final CreativeTabs TAB_DETRAV = new DetravCreativeTab();

    public static final org.apache.logging.log4j.Logger Logger = LogManager.getLogger("GT Scanner Mod");
    
    @SidedProxy(clientSide = "com.detrav.proxies.ClientProxy", serverSide = "com.detrav.proxies.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(DetravScannerMod.MODID)
    public static DetravScannerMod instance;

    public  DetravScannerMod()
    {
        GregTech_API.sAfterGTPreload.add(new Detrav_AfterGTPreload_Loader());
        new DetravNetwork();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration Config = new Configuration(event.getSuggestedConfigurationFile());
        Config.load();


        if (Config.hasChanged()){
            Config.save();
        }

        proxy.onPreInit();
    }

        @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        //System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy );
    }

    @EventHandler
    public void onLoad(FMLInitializationEvent aEvent)
    {
        proxy.onLoad();
    }

    @EventHandler
    public void onPostLoad(FMLPostInitializationEvent aEvent) {
        proxy.onPostLoad();
        if (Loader.isModLoaded("miscutils"))
        	GTppHelper.generate_OreIDs();

        DetravProPickPacket00.reFillFluidColors();
    }

}
