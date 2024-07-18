package nstage.content;

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
            constructor = UnitEntity::create;
            flying = true;
            drag = 0.05f;
            speed = 2.6f;
            rotateSpeed = 15f;
            accel = 0.1f;
            range = 130f;
            health = 400;
            engineOffset = 6.5f;
            hitSize = 9f;

            weapons.add(new Weapon("nstage-w2") {{
                top = true;
                mirror = false;
                y = 0f;
                x = 0f;
                reload = 30f;
                recoil = 1f;
                shootSound = Sounds.missile;
                velocityRnd = 0.5f;
                inaccuracy = 15f;

                bullet = new BasicBulletType(3f, 20) {{
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

        frontis = new UnitType("frontis") {{
            constructor = UnitEntity::create;
            health = 50;
            hitSize = 11f;
            speed = 2.4f;
            rotateSpeed = 5.4f;
            drag = 0.03f;
            accel = 0.1f;
            flying = true;
            targetAir = true;
            targetGround = false;
            engineOffset = 5.7f;
            lowAltitude = true;
            itemCapacity = 5;
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
        }};

        //T1
        UnitFactory airFactory = (UnitFactory) Blocks.airFactory;
        airFactory.plans.add(new UnitFactory.UnitPlan(navicula, 60f * 5, ItemStack.with(Items.silicon, 30, Items.lead, 20)));
        //T2
        Reconstructor additiveReconstructor = (Reconstructor) Blocks.additiveReconstructor;
        additiveReconstructor.upgrades.add(new UnitType[]{NewStageUnits.navicula, frontis});
    }
}