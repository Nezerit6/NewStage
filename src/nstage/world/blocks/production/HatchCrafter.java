package nstage.world.blocks.production;

import arc.Core;
import arc.graphics.g2d.*;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.effect.RadialEffect;
import mindustry.graphics.Layer;
import mindustry.world.blocks.production.GenericCrafter;
import nstage.content.NSFx;

public class HatchCrafter extends GenericCrafter {
    public TextureRegion hatchRegion;
    public Effect steamEffect = Fx.none;

    public HatchCrafter(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        hatchRegion = Core.atlas.find(name + "-hatch");
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, hatchRegion};
    }

    public class HatchCrafterBuild extends GenericCrafterBuild {

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            Draw.z(Layer.blockOver);

            if (efficiency == 0f || progress < 0.90f) {
                Draw.rect(hatchRegion, x, y);
            }

            if (progress > 0.95f && progress < 0.96f && wasVisible) {
                steamEffect.at(x, y);
            }

            Draw.z(Layer.block);
            drawTeamTop();
        }
    }
}