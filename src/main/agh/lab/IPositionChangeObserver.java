package agh.lab;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal, Vector2d newPosition);
}
