package stevekung.mods.moreplanets.module.planets.diona.potion;

import net.minecraft.entity.EntityLivingBase;
import stevekung.mods.moreplanets.init.MPPotions;
import stevekung.mods.moreplanets.util.DamageSourceMP;
import stevekung.mods.moreplanets.util.PotionMP;
import stevekung.mods.moreplanets.util.helper.ColorHelper;

public class DarkEnergyEffect extends PotionMP
{
    public DarkEnergyEffect()
    {
        super("dark_energy", true, ColorHelper.rgbToDecimal(20, 20, 20), 2);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        if (this == MPPotions.DARK_ENERGY)
        {
            int k = 20 >> amplifier;
            return k > 0 ? duration % k == 0 : true;
        }
        return false;
    }

    @Override
    public void performEffect(EntityLivingBase living, int food)
    {
        if (this == MPPotions.DARK_ENERGY)
        {
            living.attackEntityFrom(DamageSourceMP.DARK_ENERGY, 1.0F);
        }
    }
}