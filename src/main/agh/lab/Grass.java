package agh.lab;

public class Grass {
    private final Vector2d position;    // tego pola się nie da odczytać; czy ono w ogóle jest potrzebne?

    public Grass(Vector2d position) {
        this.position = position;
    }

    public String toString() {
        return " \u2698 ";
    }
}
