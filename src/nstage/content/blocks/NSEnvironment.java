package nstage.content.blocks;

import mindustry.world.Block;
import mindustry.world.blocks.environment.*;

public class NSEnvironment {
    public static Block
            darkgrass, darkmossWall, rockySoil, dehydrate, dunecliffWall, lightmossWall, pinkgrass, rockySoilWall;
    public static void load(){
        darkgrass = new Floor("darkgrass") {{
            variants = 4;
        }};

        darkmossWall = new StaticWall("darkmossWall") {{
            variants = 2;
        }};

        dehydrate = new OverlayFloor("dehydrate") {{
            variants = 3;
        }};

        dunecliffWall = new StaticWall("dunecliffWall") {{
            variants = 2;
        }};

        lightmossWall = new StaticWall("lightmossWall") {{
            variants = 2;
        }};

        pinkgrass = new Floor("pinkgrass") {{
            variants = 4;
        }};

        rockySoilWall = new StaticWall("rockySoilWall") {{
            variants = 2;
        }};

        rockySoil = new Floor("rockySoil") {{
            variants = 3;
        }};
    }
}
