package realsurv.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TabletModel implements IBakedModel {
	public static final ModelResourceLocation baseModelLoc = new ModelResourceLocation("realsurv:tablet", "inventory");

	private IBakedModel baseModel;
	private TabletItemOverrideList overrideList;

	public TabletModel(IBakedModel base) {
		baseModel = base;
		overrideList = new TabletItemOverrideList(Collections.EMPTY_LIST);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		/*
		 * List<BakedQuad> col = new ArrayList<>(baseModel.getQuads(state, side, rand));
		 * TextureAtlasSprite chessPieceTexture =
		 * Minecraft.getMinecraft().getTextureMapBlocks()
		 * .getAtlasSprite("minecraft:blocks/diamond_block");
		 * col.add(createBakedQuadForFace(0.5f, 0.0625f*15, 0.0625f*4.5f, 0.0625f*7,
		 * -1+0.07f, 0, chessPieceTexture, EnumFacing.UP));
		 */
		if(side == null) renderModel(baseModel);
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return baseModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return baseModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return baseModel.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrideList;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseModel.getItemCameraTransforms();
	}

	private void renderModel(IBakedModel model) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		for (EnumFacing enumfacing : EnumFacing.values()) {
			this.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L));
		}
		this.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L));
	}

	private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads) {
		int i = 0;
		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, 0xffffffff);
		}
	}

	private class TabletItemOverrideList extends ItemOverrideList {
		public TabletItemOverrideList(List<ItemOverride> overridesIn) {
			super(overridesIn);
		}

		@Override
		public IBakedModel handleItemState(IBakedModel model, ItemStack stack, World world, EntityLivingBase ent) {
			return super.handleItemState(model, stack, world, ent);
		}
	}
}
