package nstage;

import arc.func.Func;
import mindustry.mod.*;
import nstage.content.*;

import java.util.*;

import static arc.Core.bundle;
import static mindustry.Vars.*;

public class NewStage extends Mod{

    @Override
    public void init() {
        super.init();
        Mods.LoadedMod mod = mods.locateMod("nstage");
        if (!headless) {
            //from Omaloon by xStaBUx
            Func<String, String> stringf = value -> bundle.get("mod." + value);

            mod.meta.displayName = stringf.get(mod.meta.name + ".name");
            String[] r = {
                    bundle.get("mod.nstage.subtitle1"),
                    bundle.get("mod.nstage.subtitle2")
            };
            Random rand = new Random();
            String f = String.valueOf(
                    r[rand.nextInt(r.length)]
            );
            mod.meta.subtitle = f + "\n\n" + "[#87ceeb]" + mod.meta.version + "[]";
        }
    }

    @Override
    public void loadContent(){

        NSSounds.load();
        NSItems.load();
        NSUnits.load();
        NSBlocks.load();
        NSTechTree.load();
    }
}
/*
 * Initialization
 * - Teams
 * - Status effects
 * - Weather
 * - Items
 * - Liquids
 * - Bullet
 * - Units
 * - Blocks
 * - Planets(Sectors)
 * - Tech tree
 */