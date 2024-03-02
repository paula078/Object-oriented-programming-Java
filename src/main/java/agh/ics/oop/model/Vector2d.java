package agh.ics.oop.model;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vector2d {
    private final int x;
    private final int y;
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    @Override
    public String toString(){
        return "("+x+","+y+")";
    }
    public boolean precedes(Vector2d other){
        return x <= other.x && y <= other.y;
    }
    public boolean follows(Vector2d other){
        return x >= other.x && y >= other.y;
    }
    public Vector2d add(Vector2d other){
        return new Vector2d(x+other.x, y+other.y);
    }
    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        if(!(other instanceof Vector2d)) return false;
        Vector2d that = (Vector2d) other;
        return x == that.getX() && y == that.getY();
    }
    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }
}
