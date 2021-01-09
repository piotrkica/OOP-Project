package agh.lab;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public static MapDirection[] MAP_DIRS_INDEXED = new MapDirection[]{NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST};
    // vide: Enum.values()
    public String toString() {
        switch (this) {
            case NORTH:
                return "Północ";
            case NORTHEAST:
                return "Północny wschód";
            case EAST:
                return "Wschód";
            case SOUTHEAST:
                return "Południowy wschód";
            case SOUTH:
                return "Południe";
            case SOUTHWEST:
                return "Południowy zachód";
            case WEST:
                return "Zachód";
            case NORTHWEST:
                return "Północny zachód";
            default:
                return null; // Nie zdarzy sie
        }
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);  // nowy wektor co wywołanie
            case NORTHEAST:
                return new Vector2d(1, 1);
            case EAST:
                return new Vector2d(1, 0);
            case SOUTHEAST:
                return new Vector2d(1, -1);
            case SOUTH:
                return new Vector2d(0, -1);
            case SOUTHWEST:
                return new Vector2d(-1, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case NORTHWEST:
                return new Vector2d(-1, 1);
            default:
                return null;
        }
    }

    public static int getIndex(MapDirection animalDirection) {  // skoro ta metoda wymaga kierunku, to dlaczego jest statyczna?
        int index = 0;
        for (MapDirection direction : MAP_DIRS_INDEXED) {   // kosztowne
            if (animalDirection.equals(direction)) {
                break;
            }
            index++;
        }
        return index;   // Enum.ordinal()
    }
}
