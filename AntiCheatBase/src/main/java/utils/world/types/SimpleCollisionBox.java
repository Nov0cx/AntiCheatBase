package utils.world.types;

import cc.funkemunky.api.utils.BoundingBox;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.api.utils.ReflectionsUtil;
import cc.funkemunky.api.utils.types.WrappedClass;
import cc.funkemunky.api.utils.world.CollisionBox;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class SimpleCollisionBox implements CollisionBox {
    public double xMin, yMin, zMin, xMax, yMax, zMax;

    public SimpleCollisionBox(Object o) {
        WrappedClass wrappedClass = new WrappedClass(o.getClass());
        xMin = wrappedClass.getFieldByName("a").get(o);
        yMin = wrappedClass.getFieldByName("b").get(o);
        zMin = wrappedClass.getFieldByName("c").get(o);
        xMax = wrappedClass.getFieldByName("d").get(o);
        yMax = wrappedClass.getFieldByName("e").get(o);
        zMax = wrappedClass.getFieldByName("f").get(o);
    }

    public SimpleCollisionBox() {
        this(0, 0, 0, 0, 0, 0);
    }

    public SimpleCollisionBox(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        if (xMin < xMax) {
            this.xMin = xMin;
            this.xMax = xMax;
        } else {
            this.xMin = xMax;
            this.xMax = xMin;
        }
        if (yMin < yMax) {
            this.yMin = yMin;
            this.yMax = yMax;
        } else {
            this.yMin = yMax;
            this.yMax = yMin;
        }
        if (zMin < zMax) {
            this.zMin = zMin;
            this.zMax = zMax;
        } else {
            this.zMin = zMax;
            this.zMax = zMin;
        }
    }

    public SimpleCollisionBox(Vector min, Vector max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }
    
    public SimpleCollisionBox(Location loc, double width, double height) {
        this(loc.toVector(), width, height);
    }

    public SimpleCollisionBox(Vector vec, double width, double height) {
        this(vec.getX(), vec.getY(), vec.getZ(), vec.getX(), vec.getY(), vec.getZ());

        expand(width / 2, 0, width / 2);
        yMax+= height;
    }

    public SimpleCollisionBox add(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new SimpleCollisionBox(this.xMin + minX, this.yMin + minY, this.zMin + minZ, this.xMax + maxX, this.yMax + maxY, this.zMax + maxZ);
    }

    public SimpleCollisionBox(BoundingBox box) {
        this(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public void sort() {
        double temp = 0;
        if (xMin >= xMax) {
            temp = xMin;
            this.xMin = xMax;
            this.xMax = temp;
        }
        if (yMin >= yMax) {
            temp = yMin;
            this.yMin = yMax;
            this.yMax = temp;
        }
        if (zMin >= zMax) {
            temp = zMin;
            this.zMin = zMax;
            this.zMax = temp;
        }
    }

    public SimpleCollisionBox copy() {
        return new SimpleCollisionBox(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public SimpleCollisionBox offset(double x, double y, double z) {
        this.xMin += x;
        this.yMin += y;
        this.zMin += z;
        this.xMax += x;
        this.yMax += y;
        this.zMax += z;
        return this;
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {
        list.add(this);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    public SimpleCollisionBox expandMin(double x, double y, double z) {
        this.xMin += x;
        this.yMin += y;
        this.zMin += z;
        return this;
    }

    public SimpleCollisionBox expandMax(double x, double y, double z) {
        this.xMax += x;
        this.yMax += y;
        this.zMax += z;
        return this;
    }

    public SimpleCollisionBox expand(double x, double y, double z) {
        this.xMin -= x;
        this.yMin -= y;
        this.zMin -= z;
        this.xMax += x;
        this.yMax += y;
        this.zMax += z;
        return this;
    }

    public SimpleCollisionBox expand(double value) {
        this.xMin -= value;
        this.yMin -= value;
        this.zMin -= value;
        this.xMax += value;
        this.yMax += value;
        this.zMax += value;
        return this;
    }

    public Vector[] corners() {
        sort();
        Vector[] vectors = new Vector[8];
        vectors[0] = new Vector(xMin,yMin,zMin);
        vectors[1] = new Vector(xMin,yMin,zMax);
        vectors[2] = new Vector(xMax,yMin,zMin);
        vectors[3] = new Vector(xMax,yMin,zMax);
        vectors[4] = new Vector(xMin,yMax,zMin);
        vectors[5] = new Vector(xMin,yMax,zMax);
        vectors[6] = new Vector(xMax,yMax,zMin);
        vectors[7] = new Vector(xMax,yMax,zMax);
        return vectors;
    }

    public Vector min() {
        return new Vector(xMin, yMin, zMin);
    }

    public Vector max() {
        return new Vector(xMax, yMax, zMax);
    }

    public SimpleCollisionBox addCoord(double x, double y, double z) {
        double d0 = this.xMin;
        double d1 = this.yMin;
        double d2 = this.zMin;
        double d3 = this.xMax;
        double d4 = this.yMax;
        double d5 = this.zMax;

        if (x < 0.0D) {
            d0 += x;
        } else if (x > 0.0D) {
            d3 += x;
        }

        if (y < 0.0D) {
            d1 += y;
        } else if (y > 0.0D) {
            d4 += y;
        }

        if (z < 0.0D) {
            d2 += z;
        } else if (z > 0.0D) {
            d5 += z;
        }

        return this;
    }

    @Override
    public boolean isCollided(CollisionBox other) {
        if (other instanceof SimpleCollisionBox) {
            SimpleCollisionBox box = ((SimpleCollisionBox) other);
            box.sort();
            sort();
            return box.xMax >= this.xMin && box.xMin <= this.xMax
                    && box.yMax >= this.yMin && box.yMin <= this.yMax
                    && box.zMax >= this.zMin && box.zMin <= this.zMax;
        } else {
            return other.isCollided(this);
            // throw new IllegalStateException("Attempted to check collision with " + other.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isIntersected(CollisionBox other) {
        if(other instanceof SimpleCollisionBox) {
            SimpleCollisionBox box = (SimpleCollisionBox) other;
            box.sort();
            sort();
            return box.xMax > this.xMin && box.xMin < this.xMax
                    && box.yMax > this.yMin && box.yMin < this.yMax
                    && box.zMax > this.zMin && box.zMin < this.zMax;
        } else {
            return other.isIntersected(this);
        }
    }

    public boolean isInBoundingBox(Vector point) {
        return point.getX() >= xMin && point.getX() <= xMax && point.getY() >= yMin && point.getY() <= yMax && point.getZ() >= zMin && point.getZ() <= zMax;
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateXOffset(SimpleCollisionBox other, double offsetX) {
        if (other.yMax > this.yMin && other.yMin < this.yMax && other.zMax > this.zMin && other.zMin < this.zMax) {
            if (offsetX > 0.0D && other.xMax <= this.xMin) {
                double d1 = this.xMin - other.xMax;

                if (d1 < offsetX) {
                    offsetX = d1;
                }
            } else if (offsetX < 0.0D && other.xMin >= this.xMax) {
                double d0 = this.xMax - other.xMin;

                if (d0 > offsetX) {
                    offsetX = d0;
                }
            }

            return offsetX;
        } else {
            return offsetX;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateYOffset(SimpleCollisionBox other, double offsetY) {
        if (other.xMax > this.xMin && other.xMin < this.xMax && other.zMax > this.zMin && other.zMin < this.zMax) {
            if (offsetY > 0.0D && other.yMax <= this.yMin) {
                double d1 = this.yMin - other.yMax;

                if (d1 < offsetY) {
                    offsetY = d1;
                }
            } else if (offsetY < 0.0D && other.yMin >= this.yMax) {
                double d0 = this.yMax - other.yMin;

                if (d0 > offsetY) {
                    offsetY = d0;
                }
            }

            return offsetY;
        } else {
            return offsetY;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateZOffset(SimpleCollisionBox other, double offsetZ) {
        if (other.xMax > this.xMin && other.xMin < this.xMax && other.yMax > this.yMin && other.yMin < this.yMax) {
            if (offsetZ > 0.0D && other.zMax <= this.zMin) {
                double d1 = this.zMin - other.zMax;

                if (d1 < offsetZ) {
                    offsetZ = d1;
                }
            } else if (offsetZ < 0.0D && other.zMin >= this.zMax) {
                double d0 = this.zMax - other.zMin;

                if (d0 > offsetZ) {
                    offsetZ = d0;
                }
            }

            return offsetZ;
        } else {
            return offsetZ;
        }
    }

    public BoundingBox toBoundingBox() {
        return new BoundingBox(new Vector(xMin, yMin, zMin), new Vector(xMax, yMax, zMax));
    }

    //TODO Make this perform much better with an updated util.
    public <T> T toAxisAlignedBB() {
        return (T) ReflectionsUtil.newAxisAlignedBB(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public double distance(SimpleCollisionBox box) {
        double xwidth = (xMax - xMin) / 2, zwidth = (zMax - zMin) / 2;
        double bxwidth = (box.xMax - box.xMin) / 2, bzwidth = (box.zMax - box.zMin) / 2;
        double hxz = Math.hypot(xMin - box.xMin, zMin - box.zMin);

        return hxz - (xwidth + zwidth + bxwidth + bzwidth) / 4;
    }

    public double distance(Vector vector) {
        double xwidth = (xMax - xMin) / 2, zwidth = (zMax - zMin) / 2;
        double bxwidth = (vector.getX()) / 2, bzwidth = (vector.getZ()) / 2;
        double hxz = Math.hypot(xMin - vector.getX(), zMin - vector.getZ());

        return hxz - (xwidth + zwidth + bxwidth + bzwidth) / 4;
    }

    public double distanceY(Player player, SimpleCollisionBox box) {
        double y = yMin + player.getEyeHeight();
        double deltaY = MathUtils.getDelta(y, box.yMin), deltaY2 = MathUtils.getDelta(y, box.yMax);

        return Math.min(deltaY, deltaY2);
    }
}