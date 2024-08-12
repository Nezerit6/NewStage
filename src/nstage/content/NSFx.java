package nstage.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.entities.*;
import mindustry.graphics.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.randLenVectors;

public class NSFx {
    public static final Rand rand = new Rand();
    public static final Vec2 v = new Vec2();

    public static final Effect none = new Effect(0, 0f, e -> {
    }),

    laserSparks = new Effect(11f, e -> {
        color(Color.white, e.color, e.fin());
        stroke(e.fout() * 1.1f + 0.5f);
        randLenVectors(e.id, 5, 15f * e.fin(), e.rotation, 4f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 2f + 0.5f);
        });
    }),

    steamAbsorption = new Effect(72f, e -> {
        color(Pal.coalBlack, 0.1f + 0.9f * e.fin());

        randLenVectors(e.id, 12, 0.4f + 37f * e.fout(), e.rotation, 360f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fslope() * 0.05f + 0.87f);
        });
    }),

    steamSplash = new Effect(220f, e -> {
        color(Color.white);
        alpha(0.3f);

        rand.setSeed(e.id);
        for(int i = 0; i < 3; i++){
            float len = rand.random(8f), rot = rand.range(40f) + e.rotation;

            e.scaled(e.lifetime * rand.random(0.3f, 1f), b -> {
                v.trns(rot, len * b.finpow());
                Fill.circle(e.x + v.x, e.y + v.y, 2f * b.fslope() + 0.2f);
            });
        }
    });
}