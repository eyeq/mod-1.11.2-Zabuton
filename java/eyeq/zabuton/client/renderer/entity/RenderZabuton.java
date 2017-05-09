package eyeq.zabuton.client.renderer.entity;

import eyeq.util.client.renderer.EntityRenderResourceLocation;
import eyeq.zabuton.client.model.ModelZabuton;
import eyeq.zabuton.entity.projectile.EntityZabuton;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import static eyeq.zabuton.Zabuton.MOD_ID;

public class RenderZabuton extends Render {
    protected static final ResourceLocation textures = new EntityRenderResourceLocation(MOD_ID, "zabuton");

    protected ModelBase model;

    public RenderZabuton(RenderManager renderManager) {
        super(renderManager);
        model = new ModelZabuton();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float rotationYaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glRotatef(180F - rotationYaw, 0.0F, 1.0F, 0.0F);

        float[] afloat = EntitySheep.getDyeRgb(((EntityZabuton) entity).getColor());
        GlStateManager.color(afloat[0], afloat[1], afloat[2]);
        this.bindEntityTexture(entity);
        GL11.glScalef(-1F, -1F, 1.0F);
        this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return textures;
    }
}
