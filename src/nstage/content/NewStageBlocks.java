package nstage.content;

import arc.graphics.Color;
import arc.math.Interp;
import mindustry.content.*;
import mindustry.entities.UnitSorts;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Sounds;
import mindustry.graphics.*;
import mindustry.type.Category;
import mindustry.type.Weapon;
import mindustry.type.unit.MissileUnitType;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.*;
import nstage.entities.bullet.*;
import nstage.world.blocks.power.*;

import static mindustry.type.ItemStack.with;

public class NewStageBlocks {

    public static Block plasmaPunisher, stormBringer, mistGatherer, fireSquall, meteorite, serpent;

    public static void load() {

        plasmaPunisher = new LiquidTurret("PlasmaPunisher") {{
            requirements(Category.turret, with(Items.copper, 110, Items.lead, 65, Items.titanium, 35, Items.silicon, 20));
            health = 430;
            rotateSpeed = 4.2f;
            recoil = 3f;
            size = 2;
            range = 205;
            reload = 120f;
            shootY = 6.5f;
            heatColor = Color.valueOf("AFEEEE");
            inaccuracy = 5;
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
                    Liquids.cryofluid, new BasicBulletType(12.6f, 36) {{
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
            reload = 550f;
            health = 530;
            inaccuracy = 19;
            rotateSpeed = 5;

            shoot.shots = 32;
            shoot.shotDelay = 4;
            shoot.firstShotDelay = 15f;

            shootEffect = Fx.sparkShoot;
            shootSound = NewStageSounds.smallLaserShoot;

            consumePower(1.4f);
            coolant = consumeCoolant(0.2f);

            shootType = new LaserBoltBulletType(6, 8) {{
                knockback = 0.3f;
                lifetime = 20f;
                backColor = Pal.heal;
                frontColor = Color.white;
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

        //TODO balance
        fireSquall = new ItemTurret("FireSquall"){{
            requirements(Category.turret, with(Items.copper, 90, Items.lead, 55, Items.titanium, 40, Items.silicon, 30));
            ammo(
                    Items.graphite, new BasicBulletType(5f, 9){{
                        width = 10f;
                        height = 16f;
                        shootEffect = Fx.shootBig;
                        reloadMultiplier = 0.9f;
                        ammoMultiplier = 2;

                        incendChance = 0.01f;
                        incendSpread = 0.2f;
                        incendAmount = 1;
                        collideTerrain = true;
                    }},
                    Items.silicon, new BasicBulletType(5.5f, 13){{
                        width = 10f;
                        height = 16f;
                        shootEffect = Fx.shootBig;
                        reloadMultiplier = 1.25f;
                        ammoMultiplier = 1;
                        lifetime = 32f;

                        incendChance = 0.03f;
                        incendSpread = 0.4f;
                        incendAmount = 1;
                        collideTerrain = true;
                        collidesAir = false;
                    }}
            );
            targetAir = false;
            reload = 80;
            recoilTime = reload * 2f;
            coolantMultiplier = 0.5f;
            ammoUseEffect = Fx.casing4;
            range = 200f;
            inaccuracy = 2.5f;
            recoil = 3f;
            shoot = new ShootAlternate(7f);
            size = 2;
            shootCone = 24f;
            shootSound = Sounds.shootBig;
            shoot.shots = 2;
            ammoPerShot = 2;
            rotateSpeed = 4.3f;

            health = 460;
            coolant = consumeCoolant(0.2f);

            drawer = new DrawTurret("based-");
        }};

        // WIP
        mistGatherer = new MistGatherer("based-block-2") {{
            requirements(Category.production, BuildVisibility.sandboxOnly, with(Items.copper, 40, Items.lead, 30, Items.metaglass, 30, Items.silicon, 25));
            health = 150;
            squareSprite = true;
            pumpAmount = 0.11f;
            size = 2;
            liquidCapacity = 30;
            hasLiquids = true;
            collectEffect = NewStageFx.steamAbsorption;
            effectChance = 0.1f;

            consumePower(2.4f);
        }};

        meteorite = new ItemTurret("Meteorite") {{
            requirements(Category.turret, with(Items.copper, 490, Items.titanium, 370, Items.silicon, 285, Items.thorium, 150));
            recoil = 0.5f;

            fogRadiusMultiplier = 0.4f;
            coolantMultiplier = 6f;
            shootSound = Sounds.missileLaunch;
            squareSprite = true;
            minWarmup = 0.94f;
            targetInterval = 40f;
            unitSort = UnitSorts.strongest;
            shootWarmupSpeed = 0.03f;
            targetAir = false;
            targetUnderBlocks = false;

            shake = 6f;
            ammoPerShot = 15;
            maxAmmo = 30;
            outlineColor = Pal.darkOutline;
            size = 3;
            envEnabled |= Env.space;
            reload = 1250f;
            range = 950;
            shootCone = 1f;
            scaledHealth = 220;
            rotateSpeed = 0.9f;
            shootY = 9.5f;

            consumePower(4.4f);
            coolant = consume(new ConsumeLiquid(Liquids.cryofluid, 0.2f));
            limitRange();

            ammo(
                    Items.titanium, new BasicBulletType(0, 0) {{
                        shootEffect = Fx.shootBig;
                        smokeEffect = Fx.shootSmallSmoke;
                        ammoMultiplier = 1f;

                        spawnUnit = new MissileUnitType("Meteorite-missile"){{
                            speed = 4.15f;
                            maxRange = 6f;
                            lifetime = 60f * 5.5f;
                            outlineColor = Pal.darkOutline;
                            engineColor = trailColor = NewStagePal.pink;
                            engineLayer = Layer.effect;
                            engineSize = 2.9f;
                            engineOffset = 8f;
                            rotateSpeed = 0.75f;
                            trailLength = 3;
                            trailEffect = Fx.greenBomb;
                            trailRotation = true;
                            trailInterval = 3f;
                            missileAccelTime = 50f;
                            lowAltitude = true;
                            loopSound = Sounds.missileTrail;
                            loopSoundVolume = 0.6f;
                            deathSound = Sounds.largeExplosion;
                            targetAir = true;
                            targetUnderBlocks = false;

                            fogRadius = 6f;

                            health = 210;

                            weapons.add(new Weapon(){{
                                shootCone = 360f;
                                mirror = false;
                                reload = 1f;
                                deathExplosionEffect = Fx.massiveExplosion;
                                shootOnDeath = true;
                                shake = 10f;
                                bullet = new ExplosionBulletType(650f, 45f){{
                                    hitColor = NewStagePal.pink;
                                    shootEffect = new MultiEffect(Fx.massiveExplosion, Fx.scatheExplosion, Fx.scatheLight, new WaveEffect(){{
                                        lifetime = 10f;
                                        strokeFrom = 4f;
                                        sizeTo = 130f;
                                    }});

                                    collidesAir = true;
                                    buildingDamageMultiplier = 1f;
                                    ammoMultiplier = 1f;

                                }};
                            }});

                            abilities.add(new MoveEffectAbility(){{
                                //effect = Fx.missileTrailSmoke;
                                effect = Fx.artilleryTrailSmoke;
                                rotation = 180f;
                                y = -12f;
                                color = NewStagePal.pink;
                                //interval = 10.5f;
                            }});
                        }};
                    }}
            );

            drawer = new DrawTurret("based-") {{
                parts.add(new RegionPart("-missile") {{
                    progress = PartProgress.reload.curve(Interp.pow2In);

                    colorTo = new Color(1f, 1f, 1f, 0f);
                    color = Color.white;
                    mixColorTo = Pal.accent;
                    mixColor = new Color(1f, 1f, 1f, 0f);
                    outline = false;
                    under = true;

                    layerOffset = -0.01f;

                    moves.add(new PartMove(PartProgress.reload.inv(), 0f, 2.5f, 0f));
                    moves.add(new PartMove(PartProgress.warmup, 0f, 3f, 0f));
                }});
            }};
        }};

        serpent = new PowerTurret("Serpent"){{
            requirements(Category.turret, with(Items.copper, 75, Items.lead, 90, Items.titanium, 40, Items.silicon, 55));
            size = 2;
            range = 140;
            recoil = 2f;
            reload = 120f;
            health = 530;
            rotateSpeed = 3;
            //shootEffect = smokeEffect = destroyEffect = placeEffect = Fx.none;

            shoot = new ShootAlternate(10){{
                shots = 2;
            }};

            shootSound = Sounds.laser;

            consumePower(1.4f);
            coolant = consumeCoolant(0.2f);

            shootType = new LaserBulletType(60) {{

                lifetime = 20f;
                //smokeEffect = hitEffect = shootEffect = Fx.none;

                //hitColor = trailColor = Pal.heal;
                trailLength = 2;
                trailWidth = 1.8f;
                lightningLength = 16;
                lightningColor = Color.valueOf("feb380");
                sideAngle = 45f;
                sideWidth = 1f;
                sideLength = 15f;
                colors = new Color[]{Color.valueOf("feb380").cpy().a(0.4f), Color.valueOf("feb380"), Color.white};
            }};

            drawer = new DrawTurret("based-");
            }};
    }
}