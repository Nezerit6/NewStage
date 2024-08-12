package nstage.world.blocks.power;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.EnumSet;
import arc.util.io.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

import java.util.*;

import static mindustry.Vars.*;

public class MistGatherer extends GenericCrafter {
    private TextureRegion rotatorRegion;
    public float pumpAmount = 0f;
    public Effect collectEffect = Fx.none;
    public float effectChance = 0f;

    public MistGatherer(String name) {
        super(name);
        flags = EnumSet.of();
        size = 2;

        pumpAmount = 0.11f;
        outputLiquid = new LiquidStack(Liquids.water, pumpAmount);
        config(Float.class, MistGathererBuild::setPumpAmount);
        consumePower(2.4f);

    }

    @Override
    public void load() {
        super.load();
        rotatorRegion = Core.atlas.find(name + "-rotator");
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Draw.color(Pal.placing);
        Lines.stroke(2f);
        Lines.square(x * tilesize + offset, y * tilesize + offset, tilesize * 3);
        Draw.reset();
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.remove(Stat.productionTime);
        stats.remove(Stat.output);
        stats.add(Stat.output, StatValues.liquid(outputLiquid.liquid, pumpAmount * 60f, true));
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, rotatorRegion};
    }

    public class MistGathererBuild extends GenericCrafterBuild {
        private float time = 0f;
        private Point2[] edges;
        private float efficiency = 1f;
        private boolean isFull = false;

        @Override
        public void updateTile() {

            if (isFull && liquids.get(outputLiquid.liquid) < liquidCapacity * 0.99f) {
                isFull = false;
            }

            if (efficiency > 0 && power.status > 0 && !isFull) {
                float producedWater = pumpAmount * efficiency * delta();
                if (producedWater > 0) {
                    liquids.add(outputLiquid.liquid, producedWater);
                }

                if (Mathf.chance(effectChance * delta() * efficiency)) {
                    collectEffect.at(x, y);
                }

                time += delta() * efficiency;


                isFull = liquids.get(outputLiquid.liquid) >= liquidCapacity;
            }

            dumpLiquid(outputLiquid.liquid);


            if (Mathf.chanceDelta(0.1f)) {
                updateEfficiency();
            }
        }

        private void updateEfficiency() {
            if (edges == null) {
                int bot = (int) (-((size - 1) / 2f)) - 1;
                int top = (int) ((size - 1) / 2f + 0.5f) + 1;
                edges = concatenateArrays(
                        Edges.getEdges(size),
                        Edges.getEdges(size + 2),
                        new Point2[]{new Point2(bot, bot), new Point2(bot, top), new Point2(top, top), new Point2(top, bot)},
                        new Point2[]{new Point2(bot - 1, bot - 1), new Point2(bot - 1, top + 1), new Point2(top + 1, top + 1), new Point2(top + 1, bot - 1)}
                );
            }
            int base = edges.length;
            int occupied = 0;
            for (Point2 edge : edges) {
                Tile adjacentTile = Vars.world.tile(tile.x + edge.x, tile.y + edge.y);
                if (adjacentTile != null && adjacentTile.solid()) {
                    occupied++;
                }
            }
            efficiency = enabled ? Math.max(0f, 1f - (float) occupied / base) : 0f;
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            Draw.z(Layer.blockOver);

            if (!isFull) {
                Draw.rect(rotatorRegion, x, y, time);
            }

            Draw.z(Layer.blockOver + 0.1f);
            Draw.z(Layer.block);
            drawTeamTop();
        }

        @Override
        public void drawSelect() {
            drawPlace(tileX(), tileY(), 0, true);
        }

        @SafeVarargs
        private <T> T[] concatenateArrays(T[]... arrays) {
            List<T> resultList = new ArrayList<>();
            for (T[] array : arrays) {
                resultList.addAll(Arrays.asList(array));
            }
            return resultList.toArray(arrays[0].clone());
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            pumpAmount = read.f();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(pumpAmount);
        }

        public void setPumpAmount(float amount) {
            pumpAmount = amount;
        }
    }
}