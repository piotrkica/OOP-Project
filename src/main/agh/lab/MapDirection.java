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

    public static MapDirection[] MAP_DIRS_INDEXED = new MapDirection[] {NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST};

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

    public MapDirection next() {
        switch (this) {
            case NORTH:
                return NORTHEAST;
            case NORTHEAST:
                return EAST;
            case EAST:
                return SOUTHEAST;
            case SOUTHEAST:
                return SOUTH;
            case SOUTH:
                return SOUTHWEST;
            case SOUTHWEST:
                return WEST;
            case WEST:
                return NORTHWEST;
            case NORTHWEST:
                return NORTH;
            default:
                return null;
        }
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH:
                return NORTHWEST;
            case NORTHEAST:
                return NORTH;
            case EAST:
                return NORTHEAST;
            case SOUTHEAST:
                return EAST;
            case SOUTH:
                return SOUTHEAST;
            case SOUTHWEST:
                return SOUTH;
            case WEST:
                return SOUTHWEST;
            case NORTHWEST:
                return WEST;
            default:
                return null;
        }
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);
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
}
