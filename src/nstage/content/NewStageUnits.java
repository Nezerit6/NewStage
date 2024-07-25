package nstage.content;

import nstage.ai.types.*;
import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.blocks.units.*;

public class NewStageUnits {
    public static UnitType
            navicula, frontis, lenta, serpen, serene/*riangulus*/, werycringe;

    public static void load() {
        navicula = new UnitType("navicula") {{
            aiController = KamikazeAI::new;
            controller = u -> new KamikazeAI();
            constructor = UnitEntity::create;

            flying = true;
            drag = 0.05f;
            speed = 3.2f;
            rotateSpeed = 7f;
            accel = 0.1f;
            health = 305;
            engineOffset = 5.5f;
            hitSize = 8f;
            itemCapacity = 5;
            range = 240;

            weapons.add(new Weapon("nstage-w2") {{
                top = true;
                mirror = false;
                y = -2f;
                x = 0f;
                reload = 45f;
                recoil = 1f;
                shootSound = Sounds.missile;
                //velocityRnd = 0.5f;
                rotate = true;
                rotationLimit = 80;

                bullet = new BasicBulletType(6.5f, 9) {{
                    lifetime = 35f;
                    trailLength = 2;
                    trailWidth = 1.8f;
                    //trailSinScl = 2f;
                    //trailSinMag = 0.5f;
                    trailEffect = Fx.none;
                    trailColor = backColor = Color.valueOf("FF6F89FF");
                    //frontColor = Color.white;
                    width = 5;
                    height = 6;
                    despawnEffect = Fx.fuelburn;
                }};
            }});
        }};

/*        frontis = new UnitType("frontis") {{
            constructor = UnitEntity::create;
            flying = true;
            drag = 0.05f;
            speed = 2.1f;
            rotateSpeed = 9.4f;
            accel = 0.1f;
            range = 130f;
            health = 600;
            engineOffset = 8.5f;
            hitSize = 12f;
            faceTarget = false;

            weapons.add(new Weapon("nstage-w1") {{
                top = true;
                mirror = true;
                y = 5.7f;
                x = 4f;
                reload = 30f;
                recoil = 1f;
                shootSound = Sounds.missile;
                velocityRnd = 0.5f;
                inaccuracy = 15f;
                rotationLimit = 160f;
                rotateSpeed = 3.5f;
                rotate = true;

                bullet = new BasicBulletType(6f, 7) {{
                    lifetime = 60f;
                    trailLength = 15;
                    trailWidth = 1.6f;
                    trailSinScl = 2f;
                    trailSinMag = 0.5f;
                    trailEffect = Fx.none;
                    trailColor = Color.valueOf("FF6F89FF");
                    frontColor = Color.white;
                    backColor = Color.valueOf("D85876FF");
                    width = 5;
                    height = 6;
                }};
            }});

            weapons.add(new Weapon("nstage-w2") {{
                top = true;
                mirror = true;
                y = -1.6f;
                x = -4.5f;
                reload = 30f;
                recoil = 1f;
                shootSound = Sounds.missile;
                velocityRnd = 0.5f;
                inaccuracy = 15f;
                rotationLimit = 70f;
                rotateSpeed = 2f;
                rotate = true;

                bullet = new BasicBulletType(2.5f, 18) {{
                    lifetime = 60f;
                    trailLength = 15;
                    trailWidth = 1.6f;
                    trailSinScl = 2f;
                    trailSinMag = 0.5f;
                    trailEffect = Fx.none;
                    trailColor = Color.valueOf("FF6F89FF");
                    frontColor = Color.white;
                    backColor = Color.valueOf("D85876FF");
                    width = 7;
                    height = 8;
                }};
            }});
        }};

        lenta = new UnitType("lenta") {{
            health = 500;
            speed = 2.5f;
            flying = true;
            constructor = UnitEntity::create;
        }};

        serpen = new UnitType("serpen") {{
            health = 500;
            speed = 2.5f;
            flying = true;
            constructor = UnitEntity::create;
        }};

        serene = new UnitType("serene") {{
            health = 500;
            speed = 2.5f;
            flying = true;
            constructor = UnitEntity::create;
        }};*/

        //T1
        UnitFactory airFactory = (UnitFactory) Blocks.airFactory;
        airFactory.plans.add(new UnitFactory.UnitPlan(navicula, 160f * 5, ItemStack.with(Items.silicon, 40, Items.lead, 30)));
        //T2
        /*Reconstructor additiveReconstructor = (Reconstructor) Blocks.additiveReconstructor;
        additiveReconstructor.upgrades.add(new UnitType[]{NewStageUnits.navicula, frontis});*/
    }
}