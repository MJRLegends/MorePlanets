package stevekung.mods.moreplanets.module.planets.diona.world.gen.dungeon;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import stevekung.mods.moreplanets.util.world.gen.dungeon.DungeonConfigurationMP;
import stevekung.mods.moreplanets.util.world.gen.dungeon.RoomEmptyMP;

public class RoomSpawnerDiona extends RoomEmptyMP
{
    public RoomSpawnerDiona() {}

    public RoomSpawnerDiona(DungeonConfigurationMP configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    public RoomSpawnerDiona(DungeonConfigurationMP configuration, Random rand, int blockPosX, int blockPosZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, rand.nextInt(4) + 6, configuration.getRoomHeight(), rand.nextInt(4) + 6, entranceDir);
    }

    @Override
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox boundingBox)
    {
        if (super.addComponentParts(world, rand, boundingBox))
        {
            for (int i = 1; i <= this.sizeX - 1; ++i)
            {
                for (int j = 1; j <= this.sizeY - 1; ++j)
                {
                    for (int k = 1; k <= this.sizeZ - 1; ++k)
                    {
                        if (rand.nextFloat() < 0.025F)
                        {
                            this.setBlockState(world, this.configuration.getWebBlock(), i, j, k, boundingBox);
                        }
                    }
                }
            }

            this.setBlockState(world, Blocks.mob_spawner.getDefaultState(), 1, 0, 1, boundingBox);
            this.setBlockState(world, Blocks.mob_spawner.getDefaultState(), this.sizeX - 1, 0, this.sizeZ - 1, boundingBox);
            BlockPos blockpos = new BlockPos(this.getXWithOffset(1, 1), this.getYWithOffset(0), this.getZWithOffset(1, 1));
            TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(blockpos);

            if (spawner != null)
            {
                spawner.getSpawnerBaseLogic().setEntityName(this.getRandomMob(rand));
            }

            blockpos = new BlockPos(this.getXWithOffset(this.sizeX - 1, this.sizeZ - 1), this.getYWithOffset(0), this.getZWithOffset(this.sizeX - 1, this.sizeZ - 1));
            spawner = (TileEntityMobSpawner) world.getTileEntity(blockpos);

            if (spawner != null)
            {
                spawner.getSpawnerBaseLogic().setEntityName(this.getRandomMob(rand));
            }
            return true;
        }
        return false;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound)
    {
        super.readStructureFromNBT(tagCompound);
    }

    protected String getRandomMob(Random rand)
    {
        switch (rand.nextInt(6))
        {
        case 0:
        default:
            return "moreplanets.zelius_zombie";
        case 1:
            return "moreplanets.zelius_creeper";
        case 2:
            return "moreplanets.zelius_skeleton";
        case 3:
            return "GalacticraftCore.evolved_spider";
        case 4:
            return "GalacticraftCore.evolved_creeper";
        case 5:
            return "GalacticraftCore.evolved_skeleton";
        case 6:
            return "GalacticraftCore.evolved_zombie";
        }
    }
}