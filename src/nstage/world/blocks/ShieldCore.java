package nstage.world.blocks;

import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.*;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.Bar;
import mindustry.world.meta.*;

import static mindustry.Vars.renderer;

public class ShieldCore extends AdditionCore {
    public float radius = 0f;
    public float regen = 0f;
    public float shieldHealth = 0f;
    public float cooldownTime = 60f * 1;
    public float angle = 0f;
    public float angleOffset = 0f;
    public float width = 0f;
    public float rotationSpeed = 0f;
    public int shields = 0;
    public Effect deflectEffect = Fx.none;

    public ShieldCore(String name) {
        super(name);
        update = true;
        solid = true;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.shieldHealth, shieldHealth);
        //stats.add(Stat.regen, regen * 60f, StatUnit.perSecond);
        stats.add(Stat.cooldownTime, cooldownTime / 60f, StatUnit.seconds);
    }

    public class ShieldCoreBuild extends AdditionCoreBuild {
        public float shield;
        float alpha, widthScale;
        float rotation = 0f;
        Seq<Float> sectorSizes;
        float cooldown = 0f;

        @Override
        public void created() {
            super.created();
            sectorSizes = new Seq<>(shields);
            for (int i = 0; i < shields; i++) {
                sectorSizes.add(0f);
            }
            shield = shieldHealth;
        }

        @Override
        public void updateTile() {
            super.updateTile();

            boolean shouldBeActive = isActive && cooldown <= 0 && shield > 0;

            if (shouldBeActive) {
                if (shield < shieldHealth) {
                    shield += Time.delta * regen;
                    shield = Math.min(shield, shieldHealth);
                }

                alpha = Math.max(alpha - Time.delta / 10f, 0f);
                widthScale = Mathf.lerpDelta(widthScale, 1f, 0.06f);

                rotation += rotationSpeed * Time.delta;
                rotation %= 360f;

                float reach = radius + width / 2f;
                Groups.bullet.intersect(x - reach, y - reach, reach * 2f, reach * 2f, this::deflectBullet);
            }

            shieldAnimation(shouldBeActive);

            if (cooldown > 0) {
                cooldown -= Time.delta;
                if (cooldown <= 0) {
                    cooldown = 0;
                    shield = shieldHealth;
                }
            }
        }

        private void shieldAnimation(boolean isActive) {
            float animationSpeed = isActive ? 0.03f : 0.05f;
            float targetSize = isActive ? 1f : 0f;

            for (int i = 0; i < shields; i++) {
                sectorSizes.set(i, Mathf.approach(sectorSizes.get(i), targetSize, animationSpeed * Time.delta));
            }
        }

        public void deflectBullet(Bullet b) {
            if (b.team != team && b.type.reflectable && shield > 0 &&
                    !b.within(this, radius - width / 2f) &&
                    Tmp.v1.set(b).add(b.vel).within(this, radius + width / 2f)) {

                for (int i = 0; i < shields; i++) {
                    float sectorRotation = rotation + i * 360f / shields;
                    if (Angles.within(angleTo(b), angleOffset + sectorRotation, angle / 2f)) {

                        float penX = Math.abs(x - b.x), penY = Math.abs(y - b.y);
                        if (penX > penY) {
                            b.vel.x *= -1;
                        } else {
                            b.vel.y *= -1;
                        }

                        b.owner = this;
                        b.team = team;
                        b.time += 1f;

                        //b.absorb();
                        deflectEffect.at(b);

                        shield -= b.damage();
                        alpha = 1f;

                        if (shield <= 0) {
                            shield = 0;
                            cooldown = cooldownTime;
                            Fx.shieldBreak.at(this);
                        }
                        break;
                    }
                }
            }
        }

        @Override
        public void draw() {
            super.draw();

            Draw.z(Layer.shields);
            Draw.color(team.color, Color.white, Mathf.clamp(alpha));

            if (!renderer.animateShields) {
                Draw.alpha(0.4f);
            }

            Lines.stroke(width);
            for (int i = 0; i < shields; i++) {
                float sectorRotation = rotation + i * 360f / shields;
                float sectorSize = sectorSizes.get(i);
                if (sectorSize > 0) {
                    Lines.arc(x, y, radius * sectorSize, angle / 360f * sectorSize, sectorRotation - angle / 2f);
                }
            }
            Draw.reset();
        }

        @Override
        public void displayBars(Table table) {
            super.displayBars(table);

            table.add(new Bar("shields", Pal.accent, () -> shield / shieldHealth))
                    .growX()
                    .height(24f)
                    .pad(4);

        }

        @Override
        public void drawSelect() {
            Drawf.dashCircle(x, y, radius, team.color);
        }
    }
}