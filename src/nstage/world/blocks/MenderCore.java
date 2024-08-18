package nstage.world.blocks;

import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.gen.Sounds;
import mindustry.graphics.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class MenderCore extends AdditionCore {
    public Color baseColor = Color.valueOf("84f491");
    public float reloadTime = 0f;
    public float regenRange = 0f;
    public float healAmount = 0f;
    public float layer = Layer.bullet - 0.001f;
    public float sectorRad = 0f;
    public int sectors = 0;
    public Sound shootSound = Sounds.lasershoot;

    public MenderCore(String name) {
        super(name);
        solid = true;
        update = true;
        hasItems = true;
        emitLight = true;
        lightRadius = 50f;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.repairTime, healAmount, StatUnit.seconds);
        stats.add(Stat.range, regenRange / tilesize, StatUnit.blocks);
    }

    public class MenderCoreBuild extends AdditionCoreBuild {
        public float heat, charge = Mathf.random(reloadTime), smoothEfficiency;
        private float activationProgress = 0f;

        @Override
        public void updateTile() {
            super.updateTile();

            activationProgress = Mathf.approachDelta(activationProgress, isActive ? 1f : 0f, 0.02f);

            smoothEfficiency = Mathf.lerpDelta(smoothEfficiency, efficiency, 0.08f);
            heat = Mathf.lerpDelta(heat, efficiency > 0 ? 1f : 0f, 0.08f);

            if (isActive) {
                charge += heat * Time.delta;

                if (charge >= reloadTime) {
                    charge = 0f;

                    indexer.eachBlock(this, regenRange, b -> b.damaged() && !b.isHealSuppressed(), other -> {
                        other.heal(healAmount * efficiency);
                        other.recentlyHealed();
                        shootSound.at(x, y);
                        Fx.chainLightning.at(x, y, 0f, baseColor, other);
                        Fx.healBlockFull.at(other.x, other.y, other.block.size, baseColor, other.block);
                    });
                    Units.nearby(this.team, x, y, regenRange, otherUnit -> {
                        if (otherUnit.damaged()) {
                            shootSound.at(x, y);
                            Fx.chainLightning.at(x, y, 0f, baseColor, otherUnit);
                            otherUnit.heal(healAmount * efficiency);
                        }
                    });
                }
            }
        }

        @Override
        public void draw() {
            super.draw();

            if (activationProgress > 0) {
                Draw.alpha(activationProgress);
                Draw.rect(additionRegion, x, y);
                Draw.alpha(1f);

                Draw.z(layer);
                Draw.color(baseColor);
                Draw.alpha(heat * Mathf.absin(Time.time, 50f / Mathf.PI2, 1f) * 0.6f * activationProgress);

                Lines.stroke((0.7f + Mathf.absin(20f, 0.7f)) * activationProgress, baseColor);

                for (int i = 0; i < sectors; i++) {
                    float rot = i * 360f / sectors - Time.time * 2;
                    float radius = regenRange;
                    Lines.arc(x, y, radius, sectorRad * activationProgress, rot);
                }

                Drawf.light(x, y, regenRange * 1.5f, baseColor, heat * 0.8f * activationProgress);
            }

            Draw.reset();
        }

        @Override
        public void drawLight() {
            Drawf.light(x, y, lightRadius * smoothEfficiency * activationProgress, baseColor, 0.7f * smoothEfficiency * activationProgress);
        }
    }
}