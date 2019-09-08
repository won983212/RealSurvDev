package realsurv.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
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
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import realsurv.ClientEventHandler;
import realsurv.tabletos.TabletOS;

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
		if(type == TransformType.FIRST_PERSON_RIGHT_HAND) {
			if(side == null) {
				renderModel(baseModel);
				RenderHelper.disableStandardItemLighting();
				GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
				ClientEventHandler.instance.bindTabletScreenTexture();
				float bx = OpenGlHelper.lastBrightnessX;
				float by = OpenGlHelper.lastBrightnessY;
				final float zlevel = 0.065f;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
				GlStateManager.glBegin(7);
				GlStateManager.glTexCoord2f(0, 0);
				GlStateManager.glVertex3f(0.0625f, zlevel, 0.0625f*8);
				GlStateManager.glTexCoord2f(1, 0);
				GlStateManager.glVertex3f(0.0625f*15, zlevel, 0.0625f*8);
				GlStateManager.glTexCoord2f(1, 1);
				GlStateManager.glVertex3f(0.0625f*15, zlevel, 0.0625f);
				GlStateManager.glTexCoord2f(0, 1);
				GlStateManager.glVertex3f(0.0625f, zlevel, 0.0625f);
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
