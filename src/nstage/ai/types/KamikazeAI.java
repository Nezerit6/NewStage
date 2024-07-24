package nstage.ai.types;

import mindustry.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.liquid.*;
import nstage.content.NewStageSounds;

import static mindustry.Vars.*;

public class KamikazeAI extends FlyingAI {
    private boolean playedSound = false;

    @Override
    public void updateUnit() {
        if (unit.healthf() > 0.35f) {
            super.updateUnit();
            playedSound = false;
        } else {

            if (Units.invalidateTarget(target, unit.team, unit.x, unit.y, Float.MAX_VALUE)) {
                target = null;
            }

            if (retarget()) {
                target = target(unit.x, unit.y, unit.range(), unit.type.targetAir, unit.type.targetGround);
            }

            Building core = unit.closestEnemyCore();

            if (target == null) {
                target = core;
            }

            boolean rotate = false, shoot = false, moveToTarget = false;

            if (!Units.invalidateTarget(target, unit, unit.range()) && unit.hasWeapons()) {
                rotate = true;
                shoot = unit.within(target, unit.type.weapons.first().bullet.range +
                        (target instanceof Building b ? b.block.size * Vars.tilesize / 2f : ((Hitboxc) target).hitSize() / 2f));

                if (target != null) {
                    moveToTarget = true;
                    unit.movePref(vec.set(target).sub(unit).limit(unit.speed()));
                }

                if (unit.within(target, unit.type.weapons.first().bullet.range / 2f)) {
                    explode();
                }
            }

            if (!moveToTarget) {
                boolean move = true;

                if (core == null && state.rules.waves && unit.team == state.rules.defaultTeam) {
                    Tile spawner = getClosestSpawner();
                    if (spawner != null && unit.within(spawner, state.rules.dropZoneRadius + 120f)) {
                        move = false;
                    }
                }

                if (move) {
                    pathfind(Pathfinder.fieldCore);
                }
            }

            unit.controlWeapons(rotate, shoot);
            faceTarget();

            if (!playedSound && unit.healthf() <= 0.35f) {
                NewStageSounds.kamikaze.at(unit.x, unit.y);
                playedSound = true;
            }
        }
    }

    private void explode() {
        Fx.smokeCloud.at(unit.x, unit.y);

        Damage.damage(unit.x, unit.y, unit.hitSize() * 2f, unit.maxHealth() * 2f);

        unit.kill();
    }

    @Override
    public Teamc target(float x, float y, float range, boolean air, boolean ground) {
        return Units.closestTarget(unit.team, x, y, range, u -> u.checkTarget(air, ground), t -> ground &&
                !(t.block instanceof Conveyor || t.block instanceof Conduit));
    }
}