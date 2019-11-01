package realsurv.models;

import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import realsurv.ClientEventHandler;

public class TabletModel implements IBakedModel {
	public static final ModelResourceLocation baseModelLoc = new ModelResourceLocation("realsurv:tablet", "inventory");

	private TransformType type;
	private IBakedModel baseModel;
	private TabletItemOverrideList overrideList;

	public TabletModel(IBakedModel base) {
		baseModel = base;
		overrideList = new TabletItemOverrideList(Collections.EMPTY_LIST);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if (type == TransformType.FIRST_PERSON_RIGHT_HAND) {
			if (side == null) {
				renderModel(baseModel);
				RenderHelper.disableStandardItemLighting();
				GL11.glPushAttrib(GL11.GL_TEXTURE_BIT | GL11.GL_ENABLE_BIT);
				GlStateManager.disableBlend();
				ClientEventHandler.instance.bindTabletScreenTexture();
				float bx = OpenGlHelper.lastBrightnessX;
				float by = OpenGlHelper.lastBrightnessY;
				final float unit = 0.0625f;
				final float zlevel = (1 - 0.2402f) * unit;
				final float xMin = 0.455f * unit;
				final float xMax = xMin + 16f * unit;
				final float zMin = 2.39f * unit;
				final float zMax = zMin + 10.75f * unit;

				GlStateManager.disableTexture2D();
				GlStateManager.color(1, 1, 1, 1);
				GlStateManager.glBegin(7);
				GlStateManager.glVertex3f(xMin, zlevel, zMax);
				GlStateManager.glVertex3f(xMax, zlevel, zMax);
				GlStateManager.glVertex3f(xMax, zlevel, zMin);
				GlStateManager.glVertex3f(xMin, zlevel, zMin);
				GlStateManager.glEnd();

				GlStateManager.enableTexture2D();
				GlStateManager.color(1, 1, 1, 1);
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
				GlStateManager.glBegin(7);
				GlStateManager.glTexCoord2f(0, 0);
				GlStateManager.glVertex3f(xMin, zlevel, zMax);
				GlStateManager.glTexCoord2f(1, 0);
				GlStateManager.glVertex3f(xMax, zlevel, zMax);
				GlStateManager.glTexCoord2f(1, 1);
				GlStateManager.glVertex3f(xMax, zlevel, zMin);
				GlStateManager.glTexCoord2f(0, 1);
				GlStateManager.glVertex3f(xMin, zlevel, zMin);
				GlStateManager.glEnd();
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, bx, by);
				RenderHelper.enableStandardItemLighting();
				GL11.glPopAttrib();
			}
			return Collections.emptyList();
		}
		return baseModel.getQuads(state, side, rand);
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

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		type = cameraTransformType;
		return IBakedModel.super.handlePerspective(cameraTransformType);
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
