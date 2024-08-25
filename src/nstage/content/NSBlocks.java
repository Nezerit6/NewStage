package nstage.content;

import mindustry.content.*;
import mindustry.entities.effect.*;
import mindustry.gen.Sounds;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import nstage.content.blocks.*;
import nstage.world.blocks.*;
import nstage.world.blocks.power.*;
import nstage.world.blocks.production.*;

import static mindustry.type.ItemStack.with;

public class NSBlocks {

    public static Block
     mistGatherer, carborundumCrucible, testCore;

    public static void load() {

        NSTurrets.load();
        NSEnvironment.load();

        // WIP
        mistGatherer = new MistGatherer("mistGatherer") {{
            requirements(Category.production, BuildVisibility.sandboxOnly, with(Items.copper, 40, Items.lead, 30, Items.metaglass, 30, Items.silicon, 25));
            health = 150;
            squareSprite = true;
            pumpAmount = 0.11f;
            size = 3;
            liquidCapacity = 30;
            hasLiquids = true;
            collectEffect = NSFx.steamAbsorption;
            effectChance = 0.1f;

            drawer = new DrawMulti(
                     new DrawRegion("-bottom"),
                     new DrawLiquidTile(Liquids.water),
                     new DrawBlurSpin("-rotator", 6f),
                     new DrawDefault());

            consumePower(2.4f);
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

            craftEffect = Fx.none;

            steamEffect = new RadialEffect(NSFx.steamSplash, 4, 90f, 7f){{
                rotationOffset = 45;
            }};

            consumeItems(with(Items.silicon, 2, Items.graphite, 2, Items.sand, 1));
            consumeLiquid(Liquids.water, 20f / 60f);
            consumePower(3.2f);
        }};
    }
}