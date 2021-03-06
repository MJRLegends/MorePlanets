package stevekung.mods.moreplanets.module.planets.nibiru.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.core.MorePlanetsCore;
import stevekung.mods.moreplanets.util.EnumParticleTypesMP;
import stevekung.mods.moreplanets.util.blocks.BlockGrassMP;

public class BlockInfectedGrass extends BlockGrassMP implements IGrowable
{
    public static PropertyBool SNOWY = PropertyBool.create("snowy");

    public BlockInfectedGrass(String name)
    {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)));
        this.setUnlocalizedName(name);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        Block block = world.getBlockState(pos.up()).getBlock();
        return state.withProperty(SNOWY, Boolean.valueOf(block == NibiruBlocks.INFECTED_SNOW || block == NibiruBlocks.INFECTED_SNOW_LAYER));
    }

    @Override
    public void onPlantGrow(World world, BlockPos pos, BlockPos source)
    {
        if (world.getBlockState(pos).getBlock() == NibiruBlocks.INFECTED_GRASS)
        {
            world.setBlockState(pos, NibiruBlocks.INFECTED_DIRT.getDefaultState(), 2);
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (!world.isRemote)
        {
            if (world.getLightFromNeighbors(pos.up()) < 4 && world.getBlockLightOpacity(pos.up()) > 2)
            {
                world.setBlockState(pos, NibiruBlocks.INFECTED_DIRT.getDefaultState());
            }
            else if (world.getLightFromNeighbors(pos.up()) >= 9)
            {
                for (int i = 0; i < 4; ++i)
                {
                    BlockPos pos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                    if (world.getBlockState(pos1) == NibiruBlocks.INFECTED_DIRT.getDefaultState())
                    {
                        if (world.getLightFromNeighbors(pos1.up()) >= 4 && world.getBlockState(pos1.up()).getBlock().getLightOpacity() <= 2)
                        {
                            world.setBlockState(pos1, this.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(NibiruBlocks.INFECTED_DIRT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (rand.nextInt(8) == 0)
        {
            if (!World.doesBlockHaveSolidTopSurface(world, pos.up()))
            {
                MorePlanetsCore.PROXY.spawnParticle(EnumParticleTypesMP.INFECTED_SPORE, pos.getX() + rand.nextFloat(), pos.getY() + 1.1F, pos.getZ() + rand.nextFloat());
            }
        }
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state)
    {
        BlockPos blockpos = pos.up();

        for (int i = 0; i < 128; ++i)
        {
            BlockPos blockpos1 = blockpos;
            int j = 0;

            while (true)
            {
                if (j >= i / 16)
                {
                    if (world.isAirBlock(blockpos1))
                    {
                        if (rand.nextInt(8) == 0)
                        {
                            if (NibiruBlocks.NIBIRU_FLOWER.canBlockStay(world, blockpos1, NibiruBlocks.NIBIRU_FLOWER.getDefaultState()))
                            {
                                world.setBlockState(blockpos1, NibiruBlocks.NIBIRU_FLOWER.getDefaultState(), 3);
                            }
                        }
                        else
                        {
                            IBlockState iblockstate1 = NibiruBlocks.NIBIRU_TALL_GRASS.getDefaultState();

                            if (NibiruBlocks.NIBIRU_TALL_GRASS.canBlockStay(world, blockpos1, iblockstate1))
                            {
                                world.setBlockState(blockpos1, iblockstate1, 3);
                            }
                        }
                    }
                    break;
                }

                blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (world.getBlockState(blockpos1.down()).getBlock() != NibiruBlocks.INFECTED_GRASS || world.getBlockState(blockpos1).getBlock().isNormalCube())
                {
                    break;
                }
                ++j;
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {SNOWY});
    }

    @Override
    public String getName()
    {
        return "infected_grass";
    }
}