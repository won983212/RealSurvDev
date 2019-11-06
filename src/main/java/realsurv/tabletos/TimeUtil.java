package realsurv.tabletos;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TimeUtil {
	private static double rotation;
	private static double rota;
	private static long lastUpdateTick;

	public static float getTimeAngle(World worldIn) {
		if (worldIn == null) {
			return 0.0F;
		} else {
			double d0;
			if (worldIn.provider.isSurfaceWorld()) {
				d0 = (double) worldIn.getCelestialAngle(1.0F);
			} else {
				d0 = Math.random();
			}
			if (worldIn.getTotalWorldTime() != lastUpdateTick) {
				lastUpdateTick = worldIn.getTotalWorldTime();
				d0 = MathHelper.positiveModulo(d0 - rotation + 0.5D, 1.0D) - 0.5D;
				rota += d0 * 0.1D;
				rota *= 0.9D;
				rotation = MathHelper.positiveModulo(rotation + rota, 1.0D);
			}
			return (float) rotation;
		}
	}
}
