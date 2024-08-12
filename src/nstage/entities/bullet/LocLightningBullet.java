package nstage.entities.bullet;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.content.Fx;
import mindustry.entities.*;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.*;
import arc.graphics.*;

public class LocLightningBullet extends BulletType {
    public Color lightningColor = Color.white;
    public float maxRange = 0;

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
            LocLightning.createLightning(b, b.team, b.x, b.y, endPoint.x, endPoint.y, lightningColor, true, 0, 0, LocLightning.width, 1, p -> {
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
}