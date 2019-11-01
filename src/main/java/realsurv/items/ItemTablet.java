package realsurv.items;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import realsurv.gui.GuiScreenTablet;

public class ItemTablet extends Item {
	public ItemTablet() {
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenTablet());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
