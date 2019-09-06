package realsurv.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTablet extends Item {
	public ItemTablet() {
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.MISC);
	}

	//TODO Debug: 아이템 사용시 아침으로
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		worldIn.setWorldTime(1000);
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
