package realsurv;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import realsurv.gui.GuiScreenTablet;
import realsurv.models.TabletModel;
import realsurv.tabletos.TabletOS;
import won983212.simpleui.font.FontFactory;

public class ClientEventHandler {
	public static final ClientEventHandler instance = new ClientEventHandler();
	private Minecraft mc = Minecraft.getMinecraft();
	private TabletOS ctx;
	private IBakedModel tablet_model = null;
	private int prevFactor = -1;
	
	private ClientEventHandler() {}
	
	public static void initTabletOS() {
		ClientEventHandler.instance.ctx = new TabletOS();
	}
	
	public void bindTabletScreenTexture() {
		ctx.bindFrameBufferTexture();
	}
	
	//TODO To debug - displyscreen on guimainmenu / guiscreentablet
	@SubscribeEvent
	public void onMainScreenEvent(GuiScreenEvent.InitGuiEvent e) {
		if(e.getGui() instanceof GuiMainMenu)
			Minecraft.getMinecraft().displayGuiScreen(new GuiScreenAddServer(e.getGui(), new ServerData("Minecraft server", "", false)));
	}
	
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent e) {
		Object obj = e.getModelRegistry().getObject(TabletModel.baseModelLoc);
		if(obj instanceof IBakedModel) {
			tablet_model = (IBakedModel) obj;
			e.getModelRegistry().putObject(TabletModel.baseModelLoc, new TabletModel(tablet_model));
		}
	}
	
	@SubscribeEvent
	public void onRender(RenderTickEvent e) {
		if(e.phase == Phase.START) {
			ctx.updateScreen();
			
			ScaledResolution res = new ScaledResolution(mc);
			int factor = res.getScaleFactor();
			if(prevFactor != factor) {
				mc.fontRenderer = FontFactory.makeMinecraftFont("맑은 고딕", 7 * factor, factor);
				if(mc.currentScreen != null)
		            mc.currentScreen.setWorldAndResolution(mc, res.getScaledWidth(), res.getScaledHeight());
				prevFactor = factor;
			}
		}
	}

	public TabletOS getTabletContext() {
		return ctx;
	}
}
