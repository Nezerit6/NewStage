package nstage.world.blocks.power;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Point2;
import arc.struct.EnumSet;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Liquid;
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
    public float waterProduction = 0.1f;
    public float productionEfficiency = 0.5f;
    public Effect collectEffect = Fx.none;

    public MistGatherer(String name) {
        super(name);
        flags = EnumSet.of();
        size = 2;
        outputLiquid = new LiquidStack(Liquids.water, waterProduction);
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
        Lines.stroke(1f);
        Lines.square(x * tilesize + offset, y * tilesize + offset, tilesize * 3);
        Draw.reset();
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.output, waterProduction * 60f, StatUnit.liquidSecond);
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, rotatorRegion, topRegion};
    }

    public class MistGathererBuild extends GenericCrafterBuild {
        private float time = 0f;
        private int count = 0;
        private float effectTime = 0f;

        @Override
        public void updateTile() {
            count = (count + 1) % 60;
            if (count == 1) {
                updateEfficiency();
            }
            if (efficiency > 0 && productionEfficiency > 0) {
                float producedWater = waterProduction * delta() * productionEfficiency;
                if (producedWater > 0) {
                    handleLiquid(this, Liquids.water, producedWater);
                }
                effectTime += delta() * productionEfficiency;
                if (effectTime >= 1f) {
                    collectEffect.at(x, y);
                    effectTime = 0f;
                }
            }
            transferWater();
            if (efficiency > 0) {
                time += delta() * productionEfficiency;
            }
        }

        private void updateEfficiency() {
            int bot = (int) (-((size - 1) / 2f)) - 1;
            int top = (int) ((size - 1) / 2f + 0.5f) + 1;
            Point2[] edges = concatenateArrays(
                    Edges.getEdges(size),
                    Edges.getEdges(size + 2),
                    new Point2[]{new Point2(bot, bot), new Point2(bot, top), new Point2(top, top), new Point2(top, bot)},
                    new Point2[]{new Point2(bot - 1, bot - 1), new Point2(bot - 1, top + 1), new Point2(top + 1, top + 1), new Point2(top + 1, bot - 1)}
            );
            int base = edges.length;
            int occupied = 0;
            for (Point2 edge : edges) {
                Tile adjacentTile = Vars.world.tile(tile.x + edge.x, tile.y + edge.y);
                if (adjacentTile != null && adjacentTile.solid()) {
                    occupied++;
                }
            }
            productionEfficiency = enabled && efficiency > 0 ? Math.max(0f, 1f - (float) occupied / base) : 0f;
        }

        private void transferWater() {
            float maxTransferAmount = 5f;
            for (int i = 0; i < proximity.size; i++) {
                Building other = proximity.get(i);
                if (other != null && other.acceptLiquid(this, Liquids.water) && liquids.get(Liquids.water) > 0) {
                    float transferAmount = Math.min(liquids.get(Liquids.water), maxTransferAmount);
                    other.handleLiquid(this, Liquids.water, transferAmount);
                    liquids.remove(Liquids.water, transferAmount);
                }
            }
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            super.drawCracks();
            Draw.rect(rotatorRegion, x, y, time);
            Draw.rect(topRegion, x, y);
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
        public void handleLiquid(Building source, Liquid liquid, float amount) {
            super.handleLiquid(source, liquid, amount);
            if (liquid == Liquids.water && amount > 0) {
                moveLiquid(source, liquid, amount);
            }
        }

        public void moveLiquid(Building source, Liquid liquid, float amount) {
            for (int i = 0; i < proximity.size; i++) {
                Building other = proximity.get(i);
                if (other != null && other.acceptLiquid(this, liquid)) {
                    other.handleLiquid(this, liquid, amount / proximity.size);
                }
            }
        }
    }
}