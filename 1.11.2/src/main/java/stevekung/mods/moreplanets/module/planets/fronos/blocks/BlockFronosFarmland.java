package stevekung.mods.moreplanets.module.planets.fronos.blocks;

import net.minecraft.block.Block;
import stevekung.mods.moreplanets.util.blocks.BlockFarmlandMP;
import stevekung.mods.moreplanets.util.helper.BlockStateHelper;

public class BlockFronosFarmland extends BlockFarmlandMP
{
    public BlockFronosFarmland(String name)
    {
        super();
        this.name = name;
        this.setDefaultState(this.getDefaultState().withProperty(BlockStateHelper.MOISTURE, Integer.valueOf(0)));
        this.setUnlocalizedName(name);
    }

    @Override
    protected Block getDirtBlock()
    {
        return FronosBlocks.FRONOS_DIRT;
    }

    @Override
    protected Block getSourceBlock()
    {
        return null;
    }
}