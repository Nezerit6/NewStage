package nstage.world.blocks.power;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.EnumSet;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.LiquidStack;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mindustry.Vars.tilesize;

public class MistGatherer extends GenericCrafter {
    private TextureRegion rotatorRegion;
    private TextureRegion topRegion;
    public float pumpAmount = 0f;
    public Effect collectEffect = Fx.none;
    public float effectChance = 0f;

    public MistGatherer(String name) {
        super(name);
        flags = EnumSet.of();
        size = 2;
        outputLiquid = new LiquidStack(Liquids.water, pumpAmount / 60f);
        config(Float.class, (MistGathererBuild tile, Float f) -> tile.pumpAmount = f);
        consumePower(2.4f);
        craftTime = 0;
    }

    @Override
    public void load() {
        super.load();
        rotatorRegion = Core.atlas.find(name + "-rotator");
        topRegion = Core.atlas.find(name + "-top");
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
        stats.add(Stat.output, pumpAmount * 60f, StatUnit.liquidSecond);
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, rotatorRegion, topRegion};
    }

    public class MistGathererBuild extends GenericCrafterBuild {
        private float time = 0f;
        private int count = 0;
        private Point2[] edges;
        private float pumpAmount = MistGatherer.this.pumpAmount;
        private float efficiency = 1f;

        @Override
        public void updateTile() {
            count = (count + 1) % 60;
            if (count == 1) {
                updateEfficiency();
            }

            if (efficiency > 0 && power.status > 0) {
                float producedWater = pumpAmount * efficiency * delta();
                if (producedWater > 0) {
                    liquids.add(outputLiquid.liquid, producedWater);
                }

                if (Mathf.chance(effectChance * delta() * efficiency)) {
                    collectEffect.at(x, y);
                }

                time += delta() * efficiency;
            }

            dumpLiquid(outputLiquid.liquid);
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

        private void transferWater() {
            if (liquids.get(Liquids.water) > 0.01f) {
                for (int i = 0; i < proximity.size; i++) {
                    Building other = proximity.get(i);
                    if (other != null && other.acceptLiquid(this, Liquids.water)) {
                        moveLiquid(other, Liquids.water);
                    }
                }
            }
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            Draw.z(Layer.blockOver);
            Draw.rect(rotatorRegion, x, y, time);
            Draw.z(Layer.blockOver + 0.1f);
            Draw.rect(topRegion, x, y);
            Draw.z(Layer.block);
            drawTeamTop();
        }

        @Override
        public void drawSelect() {
            drawPlace(tileX(), tileY(), 0, true);
        }

        @SafeVarargs
        private final <T> T[] concatenateArrays(T[]... arrays) {
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
    }
}