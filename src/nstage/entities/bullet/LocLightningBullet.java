package nstage.entities.bullet;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.content.Fx;
import mindustry.entities.*;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.*;

public class LocLightningBullet extends BulletType {
    public Color lightningColor = Color.white;
    public float maxRange = 0;
    public static float width = 2.5f;
    public static float range = 6f;
    public static float effectLifetime = 60f;
    public static float effectRange = 300f;

    private static final Rand rand = new Rand();

    public LocLightningBullet(){
        super();
        scaleLife = true;
        hitShake = 0f;
        hitSound = Sounds.spark;
        absorbable = keepVelocity = false;
        instantDisappear = true;
        collides = false;
        collidesAir = collidesGround = true;
        lightning = 1;
        lightningDamage = damage;
        lightningLength = lightningLengthRand = 6;
        despawnEffect = Fx.none;
    }

    public float range(){
        return maxRange;
    }

    @Override
    public void init(Bullet b){
        float length = b.lifetime * range() / lifetime;

        float endX = b.x + Mathf.cosDeg(b.rotation()) * length;
        float endY = b.y + Mathf.sinDeg(b.rotation()) * length;
        Healthc target = Damage.linecast(b, b.x, b.y, b.rotation(), length + 4f);
        b.data = target;

        if(target instanceof Hitboxc){
            Hitboxc hit = (Hitboxc)target;
            hit.collision(b, hit.x(), hit.y());
            b.collision(hit, hit.x(), hit.y());
            endX = hit.x();
            endY = hit.y();
        }else if(target instanceof Building){
            Building tile = (Building)target;
            if(tile.collide(b)){
                tile.collision(b);
                hit(b, tile.x, tile.y);
                endX = tile.x;
                endY = tile.y;
            }
        }

        Vec2 endPoint = new Vec2(endX, endY);

        for (int i = 0; i < lightning; i++) {
            createLightning(b, b.team, b.x, b.y, endPoint.x, endPoint.y, lightningColor, true, 0, 0, width, 1, p -> {
                hitEffect.at(p.x, p.y, b.rotation(), lightningColor);
                Effect.shake(hitShake, hitShake, p.x, p.y);
            });
        }

        super.init(b);
    }

    @Override
    public void despawned(Bullet b) {
        despawnEffect.at(b.x, b.y, b.rotation(), lightningColor);
    }

    @Override
    public void hit(Bullet b) {}

    @Override
    public void hit(Bullet b, float x, float y) {}

    @Override
    public void draw(Bullet b) {}

    @Override
    public void drawLight(Bullet b) {}

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