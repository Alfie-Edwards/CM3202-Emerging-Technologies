package Framework;

public enum ShapeTypes{
    Ellipse,
    Triangle,
    Rectangle,
    Pentagon,
    Hexagon,
    Heptagon,
    Octagon;

    public int numSides() {
        switch (this) {
            case Ellipse:
                return 0;
            case Triangle:
                return 3;
            case Rectangle:
                return 4;
            case Pentagon:
                return 5;
            case Hexagon:
                return 6;
            case Heptagon:
                return 7;
            case Octagon:
                return 8;
            default:
                throw new IllegalArgumentException("Unhandled shape type in ShapeTypes.numSides: " + this.toString());
        }
    }
}
