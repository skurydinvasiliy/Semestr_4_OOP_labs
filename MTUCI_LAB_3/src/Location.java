/** Этот класс представляет конкретное местоположение на 2D-карте. Координаты - целочисленные значения. **/
public class Location {
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;

    /** Позволяет создать точку с координатами, которые мы передаём в качестве аргументов. **/
    public Location(int x, int y) {
        xCoord = x;
        yCoord = y;
    }

    /** Если не передаём аргументы, то создаётся точка с координатами (0, 0) **/
    public Location() {
        this(0, 0);
    }

    /** equals
     * проверка на совпадение для двух точек (по обоим значениям X и Y)
     **/
    @Override  // Переопределение для метода equals (т.к. есть встроенный метод equal в java)
    public boolean equals(Object obj) {
        Location other = (Location) obj;
        return this.xCoord == other.xCoord &&
                this.yCoord == other.yCoord;
    }

    /**
     * Метод hashCode. В будущем, позволяет проверить, что 2 объекта примерно (не равны), дабы усткорить работу программы
     * в качестве реализации взят код, который много где описан для реализации этого метода
     **/
    @Override // Переопределение для метода equals (т.к. есть встроенный метод equal в java)
    public int hashCode() {
        int result = 18;
        result = result + (xCoord * 8);
        result = result + (yCoord * 17);
        return result;
    }
}
