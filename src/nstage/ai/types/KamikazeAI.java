package nstage.ai.types;

import mindustry.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.liquid.*;
import nstage.content.NewStageSounds;

public class KamikazeAI extends FlyingAI {
    private static final float HEALTH_THRESHOLD = 0.35f;
    private boolean playedSound = false;

    @Override
    public void updateUnit() {
        if (unit.healthf() > HEALTH_THRESHOLD) {
            super.updateUnit();
            playedSound = false;
        } else {
            updateKamikazeMode();
        }
    }

    private void updateKamikazeMode() {
        updateTarget();
        updateMovementAndAttack();
        checkForExplosion();
        Fx.incinerateSlag.at(unit.x, unit.y);
        unit.apply(StatusEffects.burning);
    }

    private void updateTarget() {
        if (Units.invalidateTarget(target, unit.team, unit.x, unit.y)) {
            target = null;
        }

        if (retarget()) {
            target = target(unit.x, unit.y, unit.range(), unit.type.targetAir, unit.type.targetGround);
        }

        if (target == null) {
            target = unit.closestEnemyCore();
        }
    }

    private void updateMovementAndAttack() {
        boolean rotate = false, shoot = false;

        if (!Units.invalidateTarget(target, unit, unit.range()) && unit.hasWeapons()) {
            rotate = true;
            shoot = isTargetInRange();

            if (target != null) {
                moveTowardsTarget();
            }
        }

        unit.controlWeapons(rotate, shoot);
        faceTarget();
    }

    private boolean isTargetInRange() {
        float range = unit.type.weapons.first().bullet.range;
        float targetSize = (target instanceof Building b) ? b.block.size * Vars.tilesize / 2f : ((Hitboxc) target).hitSize() / 2f;
        return unit.within(target, range + targetSize);
    }

    private void moveTowardsTarget() {
        unit.movePref(vec.set(target).sub(unit).limit(unit.speed() * 1.5f));
    }

    private void checkForExplosion() {
        if (!playedSound && unit.healthf() <= HEALTH_THRESHOLD) {
            NewStageSounds.kamikaze.at(unit.x, unit.y);
            playedSound = true;
        }

        if (target != null && unit.within(target, unit.hitSize() + 5f)) {
            explode();
        }
    }

    private void explode() {
        Fx.explosion.at(unit.x, unit.y);
        Damage.damage(unit.x, unit.y, unit.hitSize() * 2f, unit.maxHealth() * 2f);
        unit.kill();
    }

    @Override
    public Teamc target(float x, float y, float range, boolean air, boolean ground) {
        return Units.closestTarget(unit.team, x, y, range, u -> u.checkTarget(air, ground), t -> ground &&
                !(t.block instanceof Conveyor || t.block instanceof Conduit));
    }
}