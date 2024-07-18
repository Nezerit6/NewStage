package nstage;

import arc.util.*;
import mindustry.mod.*;
import nstage.content.NewStageBlocks;
import nstage.content.NewStageItems;
import nstage.content.NewStageSounds;
import nstage.content.NewStageUnits;

public class NewStage extends Mod{

    public NewStage(){
        Log.info("Loaded NewStage constructor.");

        /**Events.on(ClientLoadEvent.class, e -> {
            Time.runTask(10f, () -> {
                BaseDialog frogDialog = new BaseDialog("frog");
                frogDialog.cont.add("behold").row();
                frogDialog.cont.image(Core.atlas.find("nstage-frog")).pad(50f).row();
                frogDialog.cont.button("I see", frogDialog::hide).size(100f, 50f);
                frogDialog.show();

                frogDialog.hidden(()-> {
                    Time.runTask(1f, () -> {
                        BaseDialog secondDialog = new BaseDialog("Second Dialog");
                        secondDialog.cont.add("This dialog appears after closing the frog dialog.").row();
                        secondDialog.cont.button("Close", secondDialog::hide).size(100f, 50f);
                        secondDialog.show();
                    });
                });
            });
        });*/
    }

    @Override
    public void loadContent(){
        Log.info("Loading some nstage content.");

        NewStageSounds.load();
        NewStageItems.load();
        NewStageUnits.load();
        NewStageBlocks.load();

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