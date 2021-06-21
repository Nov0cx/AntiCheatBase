package utils.world.types;

import cc.funkemunky.api.utils.world.CollisionBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComplexCollisionBox implements CollisionBox {
    @Getter
    private List<CollisionBox> boxes = new ArrayList<>();

    public ComplexCollisionBox(CollisionBox... boxes) {
        Collections.addAll(this.boxes, boxes);
    }

    public boolean add(CollisionBox collisionBox) {
        return boxes.add(collisionBox);
    }

    @Override
    public boolean isCollided(CollisionBox other) {
        return boxes.stream().anyMatch(box -> box.isCollided(other));
    }

    @Override
    public boolean isIntersected(CollisionBox other) {
        return boxes.stream().anyMatch(box -> box.isIntersected(other));
    }

    @Override
    public CollisionBox copy() {
        ComplexCollisionBox cc = new ComplexCollisionBox();
        for (CollisionBox b : boxes)
            cc.boxes.add(b.copy());
        return cc;
    }

    @Override
    public CollisionBox offset(double x, double y, double z) {
        for (CollisionBox b : boxes)
            b.offset(x,y,z);
        return this;
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {
        for (CollisionBox box : boxes)
            box.downCast(list);
    }

    @Override
    public boolean isNull() {
        for(CollisionBox box: boxes)
            if (!box.isNull())
                return false;
        return true;
    }

}