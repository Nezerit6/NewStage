package nstage.world.blocks;

import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.draw.*;


import static mindustry.Vars.tilesize;

public class AdditionCore extends CoreBlock {
    public float additionActivation = 0.8f, spawnTime = 60f * 5f;

    public DrawBlock drawer = new DrawDefault();

    public AdditionCore(String name) {
        super(name);
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
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
        public float warmup;

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
            if (timer > 0) {
                timer -= Time.delta;
                warmup = Mathf.approach(warmup, 1f, Time.delta / spawnTime);
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, 0.3f);
            }
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            drawer.draw(this);
            drawCracks();

            if (attempt && timer > 0) {
                drawProgressBar();
                drawConstructionEffect();
            }
        }

        @Override
        public void drawLight() {
            super.drawLight();
            drawer.drawLight(this);
        }

        private void drawProgressBar() {
            float progress = 1 - timer / spawnTime;
            float width = size * tilesize * 0.8f;
            float height = 2f;
            float x = this.x;
            float y = this.y + size * tilesize / 2 + 3;

            Draw.color(Pal.darkishGray);
            Lines.stroke(1f);
            Lines.rect(x - width / 2 - 1, y - height / 2 - 1, width + 2, height + 2);

            Draw.color(Pal.gray);
            Fill.rect(x, y, width, height);

            Draw.color(Pal.accent);
            Fill.rect(x - width / 2 + progress * width / 2, y, progress * width, height);

            Draw.reset();
        }

        private void drawConstructionEffect() {
            float progress = 1 - timer / spawnTime;
            Draw.z(Layer.blockOver);
            Draw.draw(Layer.blockOver, () -> Drawf.construct(this, unitType, 0f, progress, 1f, progress * 300f));
            Drawf.square(x, y, 6f);
        }
    }
}