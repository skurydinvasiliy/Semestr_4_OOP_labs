/** двумерный класс точки. **/
public class Point3d {

    /** координата X **/
    private double xCoord;

    /** координата Y **/
    private double yCoord;

    /** координата Z **/
    private double zCoord;

    /** Конструктор инициализации **/
    public Point3d (double x, double y, double z) {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    /** Конструктор по умолчанию. **/
    public Point3d () {
        this(0, 0, 0);  // Вызовите конструктор с двумя параметрами и определите источник.
    }

    /** Возвращение координаты X **/
    public double getX () {
        return xCoord;
    }

    /** Возвращение координаты Y **/
    public double getY () {
        return yCoord;
    }

    /** Возвращение координаты Z **/
    public double getZ () {
        return zCoord;
    }

    /** Установка значения координаты X. **/
    public void setX (double val) {
        xCoord = val;
    }

    /** Установка значения координаты Y. **/
    public void setY (double val) {
        yCoord = val;
    }

    /** Установка значения координаты Z. **/
    public void setZ (double val) {
        zCoord = val;
    }

    /** Метод для сравнения двух объектов Point3d **/
    public boolean IsTheSameEquals(Point3d obj) {
        return this.xCoord == obj.xCoord && this.yCoord == obj.yCoord && this.zCoord == obj.zCoord;
    }

    /** Метод для нахождения расстояния между двумя точками **/
    public double distanceTo(Point3d obj) {
        double x1 = this.xCoord;
        double y1 = this.yCoord;
        double z1 = this.zCoord;
        double x2 = obj.xCoord;
        double y2 = obj.yCoord;
        double z2 = obj.zCoord;

        /* По формуле вычисляем расстояние между двумя точками в пространстве
           Умножаем на 100, чтобы сделть дальнейшее округление до 2х знаков после запятой */
        double L = Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - z1), 2))) * 100;

        /* Переводим число из double в integer */
        int result = (int) Math.round(L);

        /* Делим на 100, чтобы округлить до 2х знаков */
        return (double) result / 100;
    }
}
