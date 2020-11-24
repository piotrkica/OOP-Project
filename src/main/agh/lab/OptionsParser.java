package agh.lab;

public class OptionsParser {
    public MoveDirection[] parse(String[] args) {
        MoveDirection[] directions = new MoveDirection[args.length];
        for (int i = 0; i < directions.length; i++) {
            switch (args[i]) {
                case "f":
                case "forward":
                    directions[i] = MoveDirection.FORWARD;
                    break;
                case "b":
                case "backward":
                    directions[i] = MoveDirection.BACKWARD;
                    break;
                case "r":
                case "right":
                    directions[i] = MoveDirection.RIGHT;
                    break;
                case "l":
                case "left":
                    directions[i] = MoveDirection.LEFT;
                    break;
                default:
                    throw new IllegalArgumentException(args[i] + " is not legal move specification");
            }
        }
        return directions;
    }
}
