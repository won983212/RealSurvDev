package realsurv;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import realsurv.models.TabletModel;

public class ClientEventHandler {
	public static final ClientEventHandler instance = new ClientEventHandler();
	
	private ClientEventHandler() {}
	
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent e) {
		Object obj = e.getModelRegistry().getObject(TabletModel.baseModelLoc);
		if(obj instanceof IBakedModel)
			e.getModelRegistry().putObject(TabletModel.baseModelLoc, new TabletModel((IBakedModel)obj));
	}
}
