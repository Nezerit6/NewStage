package nstage.content;

import arc.graphics.Color;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.*;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.draw.DrawTurret;
import nstage.entities.bullet.*;
import nstage.world.blocks.power.*;

import static mindustry.type.ItemStack.with;

public class NewStageBlocks {

    public static Block plasmaPunisher, stormBringer, mistGatherer;

    public static void load() {

        plasmaPunisher = new LiquidTurret("PlasmaPunisher") {{
            requirements(Category.turret, with(Items.copper, 110, Items.lead, 65, Items.titanium, 35, Items.silicon, 20));
            health = 430;
            rotateSpeed = 4.2f;
            recoil = 3f;
            size = 2;
            range = 205;
            reload = 90f;
            shootY = 6.5f;
            heatColor = Color.valueOf("AFEEEE");
            inaccuracy = 3;
            liquidCapacity = 60;
            squareSprite = false;
            extinguish = false;
            targetAir = false;
            shootSound = NewStageSounds.bigLaserShoot;
            loopSound = Sounds.none;
            shootEffect = Fx.none;
            smokeEffect = NewStageFx.laserSparks;
            moveWhileCharging = false;
            accurateDelay = false;
            shoot.firstShotDelay = 60f;
            consumePower(3.6f);

            ammo(
                    Liquids.cryofluid, new BasicBulletType(12.6f, 45) {{
                        //despawnEffect = Fx.colorSpark;
                        hitEffect = Fx.none;
                        lifetime = 17.5f;
                        width = 4;
                        height = 28;
                        //splashDamageRadius = 32f * 0.50f;
                        //splashDamage = 20f;
                        chargeEffect = new MultiEffect(Fx.lancerLaserCharge, Fx.lancerLaserChargeBegin);
                        //statusDuration = 240f;
                        hitColor = backColor = trailColor = Color.valueOf("afeeee");
                        trailLength = 3;
                        trailWidth = 1.9f;
                        homingPower = 0.03f;
                        homingDelay = 2f;
                        homingRange = 60f;
                        ammoMultiplier = 2f / 10f;
                        collidesAir = false;
                        fragBullets = 1;

                        fragBullet = new AftershockBulletType(20f, 20) {{
                            splashAmount = 2;
                            splashDelay = 70f;
                            hitEffect = Fx.none;
                            collidesAir = false;
                            splashDamageRadius = 20f * 0.50f;
                            //chargeEffect = new MultiEffect(Fx.lancerLaserCharge, Fx.lancerLaserChargeBegin);
                            status = StatusEffects.freezing;
                            statusDuration = 160f;
                            frontColor = hitColor = backColor = trailColor = Color.valueOf("afeeee");
                        }};
                    }}
            );

            drawer = new DrawTurret("based-") {{
                parts.addAll(
                        new RegionPart("-nozzle") {{
                            progress = DrawPart.PartProgress.warmup;
                            heatProgress = DrawPart.PartProgress.charge;
                            mirror = true;
                            under = false;
                            moveRot = 7f;
                            moves.add(new DrawPart.PartMove(DrawPart.PartProgress.recoil, 0f, 0f, -30f));
                            heatColor = Color.valueOf("afeeee");
                        }}
                );
            }};
        }};

        stormBringer = new PowerTurret("StormBringer") {{
            requirements(Category.turret, with(Items.copper, 210, Items.lead, 90, Items.titanium, 50, Items.silicon, 40));
            size = 2;
            range = 130;
            recoil = 2f;
            reload = 420f;
            health = 600;
            inaccuracy = 14;
            rotateSpeed = 7;

            shoot.shots = 32;
            shoot.shotDelay = 4;
            shoot.firstShotDelay = 15f;

            shootEffect = Fx.sparkShoot;
            shootSound = NewStageSounds.smallLaserShoot;

            consumePower(1.4f);
            coolant = consumeCoolant(0.2f);

            shootType = new LaserBoltBulletType(6, 13) {{
                knockback = 0.3f;
                lifetime = 20f;
                backColor = Pal.heal;
                frontColor = Color.white;
                status = StatusEffects.corroded;
                statusDuration = 240f;
                smokeEffect = Fx.none;
                hitEffect = despawnEffect = Fx.hitLaser;
                hitColor = trailColor = Pal.heal;
                trailLength = 2;
                trailWidth = 1.8f;
            }};

            drawer = new DrawTurret("based-") {{
                parts.addAll(
                        new RegionPart("-side") {{
                            progress = PartProgress.warmup;
                            mirror = true;
                            under = false;
                            moveY = 4.5f;
                            moves.add(new PartMove(PartProgress.heat, 0f, -4.5f, 0f));
                        }}
                );
            }};
        }};

        // WIP
        mistGatherer = new MistGatherer("based-block-2") {{
            requirements(Category.power, with(Items.copper, 40, Items.lead, 30, Items.metaglass, 30, Items.silicon, 25));
            health = 150;
            squareSprite = true;
            size = 2;
            liquidCapacity = 30;
            hasLiquids = true;
            collectEffect = NewStageFx.steamAbsorption;
        }};
    }
}