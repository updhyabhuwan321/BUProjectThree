
/** The CellTower class represents a cell tower with a name, coordinates, and range.
 *
 * @author acer Bhuwan Upadhyaya
 * Date: 5/5/2024
 */
public class CellTower {

    private final String name;
    private final double x;
    private final double y;
    private final double range;

    /**
     * Constructor to initialize a CellTower object.
     *
     * @param name The name of the cell tower.
     * @param x The x-coordinate of the cell tower.
     * @param y The y-coordinate of the cell tower.
     * @param range The range of the cell tower.
     */
    public CellTower(String name, double x, double y, double range) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.range = range;
    }

    /**
     *
     * @return name of cell tower
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The x-coordinate of the cell tower.
     */
    public double getX() {
        return x;
    }

    /**
     *
     * @return The y-coordinate of the cell tower.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the range of the cell tower.
     *
     * @return The range of the cell tower.
     */
    public double getRange() {
        return range;
    }

    /**
     * Returns a string representation of the cell tower.
     *
     * @return A string representation of the cell tower.
     */
    @Override
    public String toString() {
        return "CellTower{"
                + "name='" + name + '\''
                + ", x=" + x
                + ", y=" + y
                + ", range=" + range
                + '}';
    }
}
