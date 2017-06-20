package stevekung.mods.moreplanets.util.entity.ai;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidBase;
import stevekung.mods.moreplanets.util.blocks.BlockWallMP;

public class WalkNodeProcessorMP extends NodeProcessor
{
    private boolean canEnterDoors;
    private boolean canBreakDoors;
    private boolean avoidsWater;
    private boolean canSwim;
    private boolean shouldAvoidWater;

    @Override
    public void initProcessor(IBlockAccess world, Entity entity)
    {
        super.initProcessor(world, entity);
        this.shouldAvoidWater = this.avoidsWater;
    }

    @Override
    public void postProcess()
    {
        super.postProcess();
        this.avoidsWater = this.shouldAvoidWater;
    }

    @Override
    public PathPoint getPathPointTo(Entity entityIn)
    {
        int i;

        if (this.canSwim && entityIn.isInWater())
        {
            i = (int)entityIn.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor_double(entityIn.posX), i, MathHelper.floor_double(entityIn.posZ));
            Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock();

            for (; block == Blocks.flowing_water || block == Blocks.water || block instanceof BlockFluidBase; block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock())
            {
                ++i;
                blockpos$mutableblockpos.set(MathHelper.floor_double(entityIn.posX), i, MathHelper.floor_double(entityIn.posZ));
            }
            this.avoidsWater = false;
        }
        else
        {
            i = MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5D);
        }
        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), i, MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
    }

    @Override
    public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double target)
    {
        return this.openPoint(MathHelper.floor_double(x - entityIn.width / 2.0F), MathHelper.floor_double(y), MathHelper.floor_double(target - entityIn.width / 2.0F));
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
    {
        int i = 0;
        int j = 0;

        if (this.getVerticalOffset(entityIn, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord) == 1)
        {
            j = 1;
        }

        PathPoint pathpoint = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, j);
        PathPoint pathpoint1 = this.getSafePoint(entityIn, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, j);
        PathPoint pathpoint2 = this.getSafePoint(entityIn, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, j);
        PathPoint pathpoint3 = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, j);

        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint;
        }
        if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint1;
        }
        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint2;
        }
        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint3;
        }
        return i;
    }

    private PathPoint getSafePoint(Entity entityIn, int x, int y, int z, int p_176171_5_)
    {
        PathPoint pathpoint = null;
        int i = this.getVerticalOffset(entityIn, x, y, z);

        if (i == 2)
        {
            return this.openPoint(x, y, z);
        }
        else
        {
            if (i == 1)
            {
                pathpoint = this.openPoint(x, y, z);
            }
            if (pathpoint == null && p_176171_5_ > 0 && i != -3 && i != -4 && this.getVerticalOffset(entityIn, x, y + p_176171_5_, z) == 1)
            {
                pathpoint = this.openPoint(x, y + p_176171_5_, z);
                y += p_176171_5_;
            }
            if (pathpoint != null)
            {
                int j = 0;
                int k;

                for (k = 0; y > 0; pathpoint = this.openPoint(x, y, z))
                {
                    k = this.getVerticalOffset(entityIn, x, y - 1, z);

                    if (this.avoidsWater && k == -1)
                    {
                        return null;
                    }
                    if (k != 1)
                    {
                        break;
                    }
                    if (j++ >= entityIn.getMaxFallHeight())
                    {
                        return null;
                    }

                    --y;

                    if (y <= 0)
                    {
                        return null;
                    }
                }
                if (k == -2)
                {
                    return null;
                }
            }
            return pathpoint;
        }
    }

    private int getVerticalOffset(Entity entityIn, int x, int y, int z)
    {
        return this.func_176170_a(this.blockaccess, entityIn, x, y, z, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
    }

    private int func_176170_a(IBlockAccess blockaccessIn, Entity entityIn, int x, int y, int z, int sizeX, int sizeY, int sizeZ, boolean avoidWater, boolean breakDoors, boolean enterDoors)
    {
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entityIn);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int i = x; i < x + sizeX; ++i)
        {
            for (int j = y; j < y + sizeY; ++j)
            {
                for (int k = z; k < z + sizeZ; ++k)
                {
                    blockpos$mutableblockpos.set(i, j, k);
                    Block block = blockaccessIn.getBlockState(blockpos$mutableblockpos).getBlock();

                    if (block.getMaterial() != Material.air)
                    {
                        if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor)
                        {
                            if (block != Blocks.flowing_water && block != Blocks.water && !(block instanceof BlockFluidBase))
                            {
                                if (!enterDoors && block instanceof BlockDoor && block.getMaterial() == Material.wood)
                                {
                                    return 0;
                                }
                            }
                            else
                            {
                                if (avoidWater)
                                {
                                    return -1;
                                }

                                flag = true;
                            }
                        }
                        else
                        {
                            flag = true;
                        }

                        if (entityIn.worldObj.getBlockState(blockpos$mutableblockpos).getBlock() instanceof BlockRailBase)
                        {
                            if (!(entityIn.worldObj.getBlockState(blockpos).getBlock() instanceof BlockRailBase) && !(entityIn.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase))
                            {
                                return -3;
                            }
                        }
                        else if (!block.isPassable(blockaccessIn, blockpos$mutableblockpos) && (!breakDoors || !(block instanceof BlockDoor) || block.getMaterial() != Material.wood))
                        {
                            if (block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockWall || block instanceof BlockWallMP)
                            {
                                return -3;
                            }
                            if (block == Blocks.trapdoor || block == Blocks.iron_trapdoor)
                            {
                                return -4;
                            }

                            Material material = block.getMaterial();

                            if (material != Material.lava)
                            {
                                return 0;
                            }
                            if (!entityIn.isInLava())
                            {
                                return -2;
                            }
                        }
                    }
                }
            }
        }
        return flag ? 2 : 1;
    }

    public void setEnterDoors(boolean canEnterDoorsIn)
    {
        this.canEnterDoors = canEnterDoorsIn;
    }

    public void setBreakDoors(boolean canBreakDoorsIn)
    {
        this.canBreakDoors = canBreakDoorsIn;
    }

    public void setAvoidsWater(boolean avoidsWaterIn)
    {
        this.avoidsWater = avoidsWaterIn;
    }

    public void setCanSwim(boolean canSwimIn)
    {
        this.canSwim = canSwimIn;
    }

    public boolean getEnterDoors()
    {
        return this.canEnterDoors;
    }

    public boolean getCanSwim()
    {
        return this.canSwim;
    }

    public boolean getAvoidsWater()
    {
        return this.avoidsWater;
    }
}