package stevekung.mods.moreplanets.module.planets.nibiru.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import stevekung.mods.moreplanets.module.planets.nibiru.blocks.NibiruBlocks;
import stevekung.mods.moreplanets.util.blocks.BlockVinesMP;

public class WorldGenInfectedMegaJungle extends WorldGenHugeTreesMP
{
    public WorldGenInfectedMegaJungle(boolean genLeaves, int baseHeight, int extraRandomHeight)
    {
        super(genLeaves, baseHeight, extraRandomHeight);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        int i = this.func_150533_a(rand);

        if (!this.func_175929_a(world, rand, pos, i))
        {
            return false;
        }
        else
        {
            this.func_175930_c(world, pos.up(i), 2);

            for (int j = pos.getY() + i - 2 - rand.nextInt(4); j > pos.getY() + i / 2; j -= 2 + rand.nextInt(4))
            {
                float f = rand.nextFloat() * (float)Math.PI * 2.0F;
                int k = pos.getX() + (int)(0.5F + MathHelper.cos(f) * 4.0F);
                int l = pos.getZ() + (int)(0.5F + MathHelper.sin(f) * 4.0F);

                for (int i1 = 0; i1 < 5; ++i1)
                {
                    k = pos.getX() + (int)(1.5F + MathHelper.cos(f) * i1);
                    l = pos.getZ() + (int)(1.5F + MathHelper.sin(f) * i1);
                    this.setBlockAndNotifyAdequately(world, new BlockPos(k, j - 3 + i1 / 2, l), NibiruBlocks.NIBIRU_LOG.getStateFromMeta(2));
                }

                int j2 = 1 + rand.nextInt(2);
                int j1 = j;

                for (int k1 = j - j2; k1 <= j1; ++k1)
                {
                    int l1 = k1 - j1;
                    this.func_175928_b(world, new BlockPos(k, k1, l), 1 - l1);
                }
            }

            for (int i2 = 0; i2 < i; ++i2)
            {
                BlockPos blockpos = pos.up(i2);

                if (this.isAirLeaves(world, blockpos))
                {
                    this.setBlockAndNotifyAdequately(world, blockpos, NibiruBlocks.NIBIRU_LOG.getStateFromMeta(2));

                    if (i2 > 0)
                    {
                        this.func_181632_a(world, rand, blockpos.west(), BlockVinesMP.EAST);
                        this.func_181632_a(world, rand, blockpos.north(), BlockVinesMP.SOUTH);
                    }
                }

                if (i2 < i - 1)
                {
                    BlockPos blockpos1 = blockpos.east();

                    if (this.isAirLeaves(world,blockpos1))
                    {
                        this.setBlockAndNotifyAdequately(world, blockpos1, NibiruBlocks.NIBIRU_LOG.getStateFromMeta(2));

                        if (i2 > 0)
                        {
                            this.func_181632_a(world, rand, blockpos1.east(), BlockVinesMP.WEST);
                            this.func_181632_a(world, rand, blockpos1.north(), BlockVinesMP.SOUTH);
                        }
                    }

                    BlockPos blockpos2 = blockpos.south().east();

                    if (this.isAirLeaves(world,blockpos2))
                    {
                        this.setBlockAndNotifyAdequately(world, blockpos2, NibiruBlocks.NIBIRU_LOG.getStateFromMeta(2));

                        if (i2 > 0)
                        {
                            this.func_181632_a(world, rand, blockpos2.east(), BlockVinesMP.WEST);
                            this.func_181632_a(world, rand, blockpos2.south(), BlockVinesMP.NORTH);
                        }
                    }

                    BlockPos blockpos3 = blockpos.south();

                    if (this.isAirLeaves(world,blockpos3))
                    {
                        this.setBlockAndNotifyAdequately(world, blockpos3, NibiruBlocks.NIBIRU_LOG.getStateFromMeta(2));

                        if (i2 > 0)
                        {
                            this.func_181632_a(world, rand, blockpos3.west(), BlockVinesMP.EAST);
                            this.func_181632_a(world, rand, blockpos3.south(), BlockVinesMP.NORTH);
                        }
                    }
                }
            }
            return true;
        }
    }

    private void func_181632_a(World p_181632_1_, Random p_181632_2_, BlockPos p_181632_3_, PropertyBool p_181632_4_)
    {
        if (p_181632_2_.nextInt(3) > 0 && p_181632_1_.isAirBlock(p_181632_3_))
        {
            this.setBlockAndNotifyAdequately(p_181632_1_, p_181632_3_, NibiruBlocks.INFECTED_VINES.getDefaultState().withProperty(p_181632_4_, Boolean.valueOf(true)));
        }
    }

    private void func_175930_c(World world, BlockPos p_175930_2_, int p_175930_3_)
    {
        int i = 2;

        for (int j = -i; j <= 0; ++j)
        {
            this.func_175925_a(world, p_175930_2_.up(j), p_175930_3_ + 1 - j);
        }
    }

    private boolean isAirLeaves(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.isAir(world, pos) || block.isLeaves(world, pos);
    }
}