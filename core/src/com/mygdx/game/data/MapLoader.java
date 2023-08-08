package com.mygdx.game.data;

import com.mygdx.game.model.HitCircle;
import com.mygdx.game.model.HitObject;
import com.mygdx.game.model.Map;

import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    public static Map Load() {

        // test data

        HitCircle hitCircle1 = new HitCircle(201, 138, 705);
        HitCircle hitCircle2 = new HitCircle(400, 123, 1200);
        HitCircle hitCircle3 = new HitCircle(288, 155, 1781);
        HitCircle hitCircle4 = new HitCircle(195, 267, 2336);
        HitCircle hitCircle5 = new HitCircle(299, 127, 2989);

        List<HitObject> testData = new ArrayList<>(List.of(hitCircle1, hitCircle2, hitCircle3, hitCircle4, hitCircle5));




        return new Map(testData);

    }
}
