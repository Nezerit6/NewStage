package nstage.entities.bullet;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.entities.Effect;
import mindustry.game.Team;
import mindustry.gen.Bullet;

public class LocLightning {
    public static float width = 2.5f;
    public static float range = 6f;
    public static float effectLifetime = 60f;
    public static float effectRange = 300f;

    private static final Rand rand = new Rand();

    public static void createLightning(Bullet b, Team team, float startX, float startY, float endX, float endY, Color color, boolean accurate, float offsetX, float offsetY, float strokeWidth, int boltNum, LightningHandler handler) {
        for (int i = 0; i < boltNum; i++) {
            new Effect(effectLifetime, effectRange, e -> {
                float tx = endX, ty = endY;
                float dst = Mathf.dst(e.x, e.y, tx, ty);
                Tmp.v1.set(tx - e.x, ty - e.y).nor();
                float normx = Tmp.v1.x, normy = Tmp.v1.y;
                int links = Mathf.ceil(dst / range);
                float spacing = dst / links;

                Lines.stroke(strokeWidth * e.fout());
                Draw.color(Color.white, color, e.fin());
                Lines.beginLine();
                Lines.linePoint(e.x, e.y);

                rand.setSeed(e.id);
                for (int j = 0; j < links; j++) {
                    float nx, ny;
                    if (j == links - 1) {
                        nx = tx;
                        ny = ty;
                    } else {
                        float len = (j + 1) * spacing;
                        Tmp.v1.setToRandomDirection(rand).scl(range / 2f);
                        nx = e.x + normx * len + Tmp.v1.x;
                        ny = e.y + normy * len + Tmp.v1.y;
                    }
                    Lines.linePoint(nx, ny);
                }
                Lines.endLine();
            }).at(startX + offsetX, startY + offsetY, 0, new Vec2(endX, endY));
        }

        if (handler != null) {
            handler.handle(new Vec2(endX, endY));
        }
    }

    public interface LightningHandler {
        void handle(Vec2 point);
    }
}