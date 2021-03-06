package stevekung.mods.moreplanets.module.planets.chalos.client.renderer.entity;

import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.module.planets.chalos.client.renderer.entity.layer.LayerCheeseSlimeGel;
import stevekung.mods.moreplanets.module.planets.chalos.entity.EntityCheeseSlime;

@SideOnly(Side.CLIENT)
public class RenderCheeseSlime extends RenderLiving<EntityCheeseSlime>
{
    public RenderCheeseSlime(RenderManager manager)
    {
        super(manager, new ModelSlime(16), 0.25F);
        this.addLayer(new LayerCheeseSlimeGel(this));
    }

    @Override
    public void doRender(EntityCheeseSlime entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.shadowSize = 0.25F * entity.getSlimeSize();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntityCheeseSlime entity, float partialTicks)
    {
        float f = entity.getSlimeSize();
        float f1 = (entity.prevSquishFactor + (entity.squishFactor - entity.prevSquishFactor) * partialTicks) / (f * 0.5F + 1.0F);
        float f2 = 1.0F / (f1 + 1.0F);
        GlStateManager.scale(f2 * f, 1.0F / f2 * f, f2 * f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCheeseSlime entity)
    {
        return new ResourceLocation("moreplanets:textures/entity/cheese_slime.png");
    }
}