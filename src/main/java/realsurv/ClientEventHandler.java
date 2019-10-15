package realsurv;

import java.util.List;

import com.google.common.eventbus.Subscribe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import realsurv.gui.GuiScreenTablet;
import realsurv.models.TabletModel;
import realsurv.tabletos.TabletOS;
import won983212.guitoolkit.font.FontFactory;

public class ClientEventHandler {
	public static final ClientEventHandler instance = new ClientEventHandler();
	private TabletOS ctx;
	private IBakedModel tablet_model = null;
	
	private ClientEventHandler() {}
	
	public static void initTabletOS() {
		ClientEventHandler.instance.ctx = new TabletOS();
	}
	
	public void bindTabletScreenTexture() {
		ctx.bindFrameBufferTexture();
	}
	
	//TODO To debug - displyscreen on guimainmenu
	@SubscribeEvent
	public void onMainScreenEvent(GuiScreenEvent.InitGuiEvent e) {
		/*if(e.getGui() instanceof GuiMainMenu)
			Minecraft.getMinecraft().displayGuiScreen(new GuiScreenTablet());*/
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
		}
	}

	public TabletOS getTabletContext() {
		return ctx;
	}
}
