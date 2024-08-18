package nstage.world.blocks;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.*;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;

public class AdditionCore extends CoreBlock {
    public TextureRegion additionRegion;
    public float additionActivation = 0.8f, spawnTime = 60f * 5f;

    public AdditionCore(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        additionRegion = Core.atlas.find(name + "-addition");
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region};
    }

    @Override
    public boolean canBreak(Tile tile) {
        return true;
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        return true;
    }

    public class AdditionCoreBuild extends CoreBuild {
        public float timer = 0f;
        public boolean isActive = false, attempt = false;

        @Override
        public void requestSpawn(Player player) {
            if (!attempt) {
                timer = spawnTime;
                attempt = true;
                Time.run(timer, () -> {
                    if (player.dead()) {
                        super.requestSpawn(player);
                        Call.soundAt(Sounds.respawn, x, y, 1, 1);
                    }
                    attempt = false;
                });
            }
        }

        @Override
        public void updateTile() {
            super.updateTile();
            isActive = health() < maxHealth * additionActivation;
            if (timer > 0) timer -= Time.delta;
        }
    }
}