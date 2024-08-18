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
import mindustry.type.ItemStack;
import mindustry.type.Weapon;
import mindustry.type.unit.MissileUnitType;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import nstage.entities.bullet.*;
import nstage.world.blocks.ShieldCore;
import nstage.world.blocks.power.*;
import nstage.world.blocks.production.HatchCrafter;

import static mindustry.type.ItemStack.with;

public class NSBlocks {

    public static Block plasmaPunisher, stormBringer, mistGatherer,
            fireSquall, meteorite, serpent,
            carborundumCrucible, radiant, testCore,
            fst;

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
            shootSound = NSSounds.bigLaserShoot;
            loopSound = Sounds.none;
            shootEffect = Fx.none;
            smokeEffect = NSFx.laserSparks;
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
            shootSound = NSSounds.smallLaserShoot;

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
                    Items.silicon, new BasicBulletType(3.1f, 13){{
                        width = 10f;
                        height = 16f;
                        shootEffect = Fx.shootBig;
                        reloadMultiplier = 1.25f;
                        ammoMultiplier = 1;
                        lifetime = 90f;

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
        mistGatherer = new MistGatherer("mistGatherer") {{
            requirements(Category.production, BuildVisibility.sandboxOnly, with(Items.copper, 40, Items.lead, 30, Items.metaglass, 30, Items.silicon, 25));
            health = 150;
            squareSprite = true;
            pumpAmount = 0.11f;
            size = 2;
            liquidCapacity = 30;
            hasLiquids = true;
            collectEffect = NSFx.steamAbsorption;
            effectChance = 0.1f;

            drawer = new DrawMulti(
                    new DrawDefault(),
                    new DrawLiquidRegion()/*,
                    new DrawRegion("-rotator"){{
                        rotateSpeed = 3;
                    }}*/
            );

            consumePower(2.4f);
        }};

        meteorite = new ItemTurret("Meteorite") {{
            requirements(Category.turret, with(Items.titanium, 120, Items.plastanium, 40, NSItems.carborundum, 30));
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
            ammoPerShot = 10;
            maxAmmo = 20;
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
                    NSItems.carborundum, new BasicBulletType(0, 0) {{
                        shootEffect = Fx.shootBig;
                        smokeEffect = Fx.shootSmallSmoke;
                        ammoMultiplier = 1f;

                        spawnUnit = new MissileUnitType("Meteorite-missile"){{
                            speed = 4.15f;
                            maxRange = 6f;
                            lifetime = 60f * 5.5f;
                            outlineColor = Pal.darkOutline;
                            engineColor = trailColor = NSPal.pink;
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
                                    hitColor = NSPal.pink;
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
                                color = NSPal.pink;
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

        //WIIIIIIIIIIIIIP
        serpent = new PowerTurret("serpent"){{
            requirements(Category.turret, with(Items.copper, 75, Items.lead, 90, Items.titanium, 40, Items.silicon, 55));
            size = 2;
            range = 140;
            recoil = 1f;
            targetAir = false;

            reload = 55f;
            health = 980;
            rotateSpeed = 3;
            //shootEffect = smokeEffect = destroyEffect = placeEffect = Fx.none;

            shootSound = Sounds.laser;

            consumePower(1.4f);
            coolant = consumeCoolant(0.2f);
            shootType = new LaserBulletType(50) {{
                buildingDamageMultiplier = 0.25f;
                lifetime = 25f;
                collidesAir = false;
                trailLength = 2;
                trailWidth = 1.8f;
                lightningLength = 16;
                lightningColor = Color.valueOf("feb380");
                sideAngle = 45f;
                sideWidth = 1f;
                recoil = 45;
                recoilTime = 30;
                sideLength = 15f;
                colors = new Color[]{Color.valueOf("feb380").cpy().a(0.4f), Color.valueOf("feb380"), Color.white};
            }};

            shoot = new ShootAlternate(10);

            recoils = 2;
            drawer = new DrawTurret("based-"){{
                for(int i = 0; i < 2; i ++){
                    int f = i;
                    parts.add(new RegionPart("-barrel-" + (i == 0 ? "l" : "r")){{
                        progress = PartProgress.recoil;
                        recoilIndex = f;
                        under = true;
                        moveY = -3.5f;
                    }});
                }
            }};
        }};

        testCore = new ShieldCore("regen-core"){{
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(Items.copper, 1));
            size = 2;
            /*regenRange = 30f;
            reloadTime = 60f;
            healAmount = 24f;
            sectors = 4;*/
            additionActivation = 0.8f;

            spawnTime = 60f * 3f;

            deflectEffect = Fx.hitBulletSmall;
            radius = 30f;
            regen = 0.1f;
            shieldHealth = 100f;
            cooldownTime = 60f * 7;
            angle = 80f;
            width = 6f;
            shields = 2;
            rotationSpeed = 1.2f;

            unitType = UnitTypes.alpha;
            health = 1100;
            itemCapacity = 4000;

            unitCapModifier = 8;
        }};

        carborundumCrucible = new HatchCrafter("carborundum-crucible"){{
            requirements(Category.crafting, with(Items.titanium, 120, Items.silicon, 80, Items.graphite, 65, Items.plastanium, 50));
            size = 3;

            itemCapacity = 20;
            craftTime = 60f * 6f;
            liquidCapacity = 80f * 3;

            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.9f;
            hasLiquids = true;

            outputItem = new ItemStack(NSItems.carborundum, 2);

            craftEffect = new RadialEffect(NSFx.steamSplash, 4, 90f, 7f){{
                rotationOffset = 45;
            }};

            consumeItems(with(Items.silicon, 2, Items.graphite, 2, Items.sand, 1));
            consumeLiquid(Liquids.water, 20f / 60f);
            consumePower(3.2f);
        }};

        radiant = new PowerTurret("radiant"){{
           requirements(Category.turret, BuildVisibility.sandboxOnly, with(Items.copper, 1));

            shootType = new LocLightningBullet(){{
                damage = 2f;
                //effectLifetime = 4;
                maxRange = 20f / 2f;
                lightningColor = Color.valueOf("e8d174");
                collidesAir = false;
                lifetime = Fx.lightning.lifetime;
                shootEffect = smokeEffect = hitEffect = Fx.none;
                status = StatusEffects.shocked;
                statusDuration = 30f;
                hittable = false;
                buildingDamageMultiplier = 0.25f;

                shoot = new ShootAlternate(11){{
                    shots = 2;
                }};
            }};

            shoot.shots = 150;
            shoot.shotDelay = 1.5f;
            shoot.firstShotDelay = 35;
            reload = 280f;
            shootEffect = smokeEffect = Fx.none;
            coolEffect = Fx.none;
            rotateSpeed = 3f;
            targetAir = false;
            predictTarget = false;
            range = 230f;
            recoil = 1f;
            size = 3;
            health = 260;
            playerControllable = false;
            shootSound = Sounds.bolt;

            consumePower(4.3f);
            outlineColor = Pal.darkOutline;

            drawer = new DrawTurret("based-");
        }};

        fst = new PowerTurret("fst") {{
            requirements(Category.turret, BuildVisibility.sandboxOnly, with(Items.copper, 1));
            outlineColor = Color.valueOf("2d2630");
            targetAir = true;
            targetGround = true;
            rotateSpeed = 2f;
            size = 2;
            health = 2120;
            range = 120f;
            reload = 750;
            recoil = 12;
            shootSound = Sounds.plasmaboom;
            consumePower(4.3f);

            shootType = new BasicBulletType() {{
                /*chargeEffect = Fx.diluvioCharge;
                despawnEffect = unitFx.blueHitBig;*/
                hittable = false;
                width = 23;
                height = 23;
                backColor = Color.valueOf("d8f3f4");
                frontColor = Color.white;
                shrinkX = 0;
                shrinkY = 0;
                lifetime = 80;
                damage = 350;
                lightRadius = 70f;
                lightColor = Color.valueOf("d8f3f4");
                clipSize = 250f;
                sprite = "large-orb";
                speed = 1.5f;
                splashDamage = 90f;
                splashDamageRadius = 50;
                hitShake = 4f;
                soundPitchMin = 1;
                soundPitchMax = 1.2f;
                hitSound = Sounds.plasmaboom;
                //hitEffect = unitFx.diluvioHit;
            }};

            drawer = new DrawTurret("") {{
                parts.add(new RegionPart("-ala") {{
                    moves.add(new PartMove(PartProgress.charge.curve(Interp.circleIn), -3, 3, 35));
                    moves.add(new PartMove(PartProgress.recoil.curve(Interp.pow2In), -3, 3, 35));
                    mirror = true;
                    under = true;
                    layerOffset = -0.0001f;
                    heatProgress = PartProgress.charge.curve(Interp.circleIn);
                }});
            }};
        }};

        /*
        fst = new PowerTurret("fst"){{
            requirements(Category.turret, BuildVisibility.sandboxOnly, with(Items.copper, 1));

            size = 2;
            recoil = 2f;
            reload = 100f;
            health = 530;
            inaccuracy = 6;
            rotateSpeed = 4;
            consumePower(1.4f);

            shootType = new  BasicBulletType(){{

                /*shootEffect = new MultiEffect(Fx.shootTitan, new WaveEffect(){{
                    colorTo = Pal.sapBulletBack;
                    sizeTo = 26f;
                    lifetime = 14f;
                    strokeFrom = 4f;
                }});*
                smokeEffect = Fx.shootSmokeTitan;
                hitColor = Pal.lancerLaser;
                despawnSound = Sounds.spark;

                sprite = "large-orb";
                pierceCap = 4;
                //trailEffect = Fx.missileTrail;
                trailInterval = 3f;
                trailParam = 4f;
                speed = 3f;
                damage = 13f;
                lifetime = 60f;
                width = height = 15f;
                backColor = NSPal.darkPink;
                frontColor = NSPal.pink;
                shrinkX = shrinkY = 0f;
                trailColor = NSPal.darkPink;
                trailLength = 12;
                trailWidth = 2.2f;
                despawnEffect = hitEffect = new ExplosionEffect(){{
                    frontColor = NSPal.pink;
                    smokeColor = Color.gray;
                    sparkColor = Pal.sap;
                    waveStroke = 4f;
                    waveRad = 40f;
                }};

                intervalBullet = new LightningBulletType(){{
                    damage = 16;
                    collidesAir = false;
                    ammoMultiplier = 1f;
                    frontColor = NSPal.pink;
                    lightningLength = 3;
                    lightningLengthRand = 6;

                    buildingDamageMultiplier = 0.25f;

                    lightningType = new BulletType(0.0001f, 0f){{
                        lifetime = Fx.lightning.lifetime;
                        hitEffect = Fx.hitLancer;
                        despawnEffect = Fx.none;
                        status = StatusEffects.shocked;
                        statusDuration = 10f;
                        hittable = false;
                        lightColor = Color.white;
                        buildingDamageMultiplier = 0.25f;
                    }};
                }};

                bulletInterval = 4f;

                frontColor = NSPal.pink;
                lightningDamage = 17;
                lightning = 8;
                lightningLength = 2;
                lightningLengthRand = 8;
            }};
        }};*/
    }
}