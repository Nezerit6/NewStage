package nstage.content.blocks;

import arc.graphics.Color;
import arc.math.Interp;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.abilities.MoveEffectAbility;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.type.unit.MissileUnitType;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.*;
import nstage.content.*;
import nstage.entities.bullet.*;

import static mindustry.type.ItemStack.with;

public class NSTurrets {
    public static Block
            punisher, storm, squall, serpent, eradication, radiant, singularity;
    public static void load(){
        punisher = new LiquidTurret("punisher") {{
            requirements(Category.turret, with(Items.copper, 110, Items.lead, 65, Items.titanium, 35, Items.silicon, 20));
            health = 430;
            rotateSpeed = 4.2f;
            recoil = 3f;
            size = 2;
            range = 205;
            reload = 120f;
            shootY = 6.5f;
            heatColor = Color.valueOf("afeeee");
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

        storm = new PowerTurret("storm") {{
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

            drawer = new DrawTurret("based-");
        }};

        squall = new ItemTurret("squall"){{
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

        eradication = new PowerTurret("eradication") {{
            requirements(Category.turret, with(Items.titanium, 120, Items.plastanium, 40, NSItems.carborundum, 30));

            size = 3;
            range = 240;
            recoil = 2f;
            shoot.shots = 3;
            shoot.shotDelay = 7.8f;
            shootSound = Sounds.release;
            reload = 140;
            health = 1670;
            rotateSpeed = 2.3f;
            consumePower(1.4f);
            coolant = consumeCoolant(0.2f);

            outlineColor = Pal.darkOutline;

            shootType = new BasicBulletType() {{
                hitColor = Pal.lancerLaser;
                despawnSound = Sounds.none;
                sprite = "large-orb";
                pierceCap = 3;
                trailEffect = Fx.missileTrail;
                trailInterval = 3f;
                trailParam = 4f;
                speed = 12f;
                damage = 46f;
                lifetime = 20f;
                width = height = 10f;
                backColor = NSPal.darkPink;
                frontColor = NSPal.pink;
                shrinkX = shrinkY = 0f;
                trailColor = NSPal.darkPink;
                trailLength = 3;
                trailWidth = 2.2f;
                despawnEffect = hitEffect = Fx.none;
                lightningColor = NSPal.pink;
                lightningDamage = 12;
                lightning = 3;
                lightningLength = 2;
                lightningLengthRand = 8;
            }};

            drawer = new DrawTurret("based-");
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

        singularity = new LaserTurret("singularity"){{
            requirements(Category.turret, BuildVisibility.hidden, with(Items.copper, 1));

            size = 1;
            shootEffect = Fx.none;
            recoil = 0f;
            //shake = 0f;
            range = 1000f;
            reload = 900f;
            health = 1;
            firingMoveFract = 0.5f;
            shootDuration = 230f;
            shootSound = Sounds.none;
            loopSound = Sounds.flux;
            loopSoundVolume = 1.5f;
            envEnabled |= Env.space;
            shootY = 10;

            shootType = new ContinuousLaserBulletType(500){{
                shake = 0;
                length = 1000f;
                colors = new Color[]{Color.valueOf("ffffff").cpy().a(0.4f), Color.valueOf("ffffff"), Color.white};
            }};

            //scaledHealth = 200;
            consumePower(100f);

            drawer = new DrawTurret("based-");
        }};
    }
}