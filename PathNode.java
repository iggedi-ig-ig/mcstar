package org.gauss.util.pathfinding;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

@Getter
@Setter
public class PathNode {
    public static final int[][] NEIGHBOR_OFFSETS = {
            {-1, -1, -1, 3},
            {-1, -1, 0, 2},
            {-1, -1, 1, 3},
            {-1, 0, -1, 2},
            {-1, 0, 0, 1},
            {-1, 0, 1, 2},
            {-1, 1, -1, 3},
            {-1, 1, 0, 2},
            {-1, 1, 1, 3},
            {0, -1, -1, 2},
            {0, -1, 1, 2},
            {0, 0, -1, 1},
            {0, 0, 1, 1},
            {0, 1, -1, 2},
            {0, 1, 1, 2},
            {1, -1, -1, 3},
            {1, -1, 0, 2},
            {1, -1, 1, 3},
            {1, 0, -1, 2},
            {1, 0, 0, 1},
            {1, 0, 1, 2},
            {1, 1, -1, 3},
            {1, 1, 0, 2},
            {1, 1, 1, 3},};

    private final int hashCode;

    private final int posX;
    private final int posY;
    private final int posZ;

    private float gScore = Float.POSITIVE_INFINITY;
    private float hScore = Float.POSITIVE_INFINITY;

    public boolean isAccessible(WorldClient theWorld) {
        final BlockPos lower = new BlockPos(this.posX, this.posY, this.posZ);
        final BlockPos upper = lower.add(0, 1, 0);
        final BlockPos below = lower.add(0, -1, 0);

        return theWorld.isBlockPassable(lower) && theWorld.isBlockPassable(upper) && !theWorld.isBlockPassable(below);
    }

    public PathNode(int x, int y, int z) {
        this.hashCode = positionHash(x, y, z);

        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PathNode) {
            final PathNode other = (PathNode) o;

            return this.hashCode == other.hashCode;
        }

        return o.equals(this);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Custom bijective hash function for blocks in a 2^12 by 2^12 block square
     */
    public static int positionHash(int x, int y, int z) {
        int xComp = x & 0b111111111111;
        int yComp = (y & 0b11111111) << 12;
        int zComp = (z & 0b111111111111) << 20;

        return xComp | yComp | zComp;
    }

    public float getFScore() {
        return this.gScore + this.hScore;
    }

    public Vec3 getCenter() {
        return new Vec3((float) this.posX + 0.5F, (float) this.posY + 0.5F, (float) this.posZ + 0.5F);
    }

    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY, this.posZ);
    }
}
