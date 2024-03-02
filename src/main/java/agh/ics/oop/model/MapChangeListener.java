package agh.ics.oop.model;

import java.io.IOException;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap) throws IOException;
}
