package nstage.world.blocks.production;

import arc.Core;
import arc.graphics.g2d.*;
import mindustry.graphics.Layer;
import mindustry.world.blocks.production.GenericCrafter;

public class HatchCrafter extends GenericCrafter {
    public TextureRegion hatchRegion;

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

            if (efficiency == 0f || progress < 0.75f) {
                Draw.rect(hatchRegion, x, y);
            }

            Draw.z(Layer.block);
            drawTeamTop();
        }
    }
}