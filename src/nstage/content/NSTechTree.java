package nstage.content;

import arc.struct.Seq;
import mindustry.content.*;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;
import mindustry.content.TechTree;
import nstage.content.blocks.NSTurrets;

import static mindustry.content.TechTree.*;

public class NSTechTree {
    public static TechNode context;

    public static void load() {
        margeNode(Blocks.lancer, () -> {
            node(NSTurrets.serpent, () -> {
                node(NSTurrets.punisher, () -> {
                    node(NSTurrets.storm, () -> {
                        node(NSTurrets.eradication);
                    });
                });
            });
        });
        margeNode(Blocks.scorch, () -> {
            node(NSTurrets.squall);
        });
        margeNode(UnitTypes.flare, () -> {
            node(NSUnits.flicker);
        });
        margeNode(Blocks.waterExtractor, () -> {
            node(NSBlocks.mistGatherer);
        });
        margeNode(Blocks.siliconCrucible, () -> {
            node(NSBlocks.carborundumCrucible);
        });
        margeNode(Items.titanium, () -> {
            node(NSItems.carborundum);
        });
    }

    private static void margeNode(UnlockableContent parent, Runnable children) {
        context = TechTree.all.find(t -> t.content == parent);
        children.run();
    }

    private static void node(UnlockableContent content, ItemStack[] requirements, Seq<Objectives.Objective> objectives, Runnable children) {
        TechNode node = new TechNode(context, content, requirements);
        if (objectives != null) node.objectives = objectives;

        TechNode prev = context;
        context = node;
        children.run();
        context = prev;
    }

    private static void node(UnlockableContent content, ItemStack[] requirements, Runnable children) {
        node(content, requirements, null, children);
    }

    private static void node(UnlockableContent content, Runnable children) {
        node(content, content.researchRequirements(), children);
    }

    private static void node(UnlockableContent block) {
        node(block, () -> {
        });
    }
}
