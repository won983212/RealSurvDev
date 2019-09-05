package realsurv;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = RealSurvMod.MODID, name = RealSurvMod.NAME, version = RealSurvMod.VERSION)
public class RealSurvMod
{
    public static final String MODID = "realsurvmod";
    public static final String NAME = "realsurvmod";
    public static final String VERSION = "0.1";
    
    private static Logger logger;

    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
