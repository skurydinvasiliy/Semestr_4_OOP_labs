import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Этот класс представляет собой пользовательский компонент Swing для представления отдельной ячейки карты на 2D-карте.
 * Ячейка имеет несколько различных типов состояний, но самое основное состояние состоит в том, является ли ячейка проходимой или нет.
 */
public class JMapCell extends JComponent
{
    private static final Dimension CELL_SIZE = new Dimension(12, 12);

    /** Истина означает, что ячейка является конечной точкой, начала или окончания. **/
    boolean endpoint = false;

    /** Истина означает, что ячейка проходима; ложь означает, что это не так. **/
    boolean passable = true;

    /** Истина означает, что эта ячейка является частью пути между началом и концом. **/
    boolean path = false;

    /** Построить новую ячейку карты с указанной «проходимостью». Ввод истины означает, что ячейка проходима. **/
    public JMapCell(boolean pass)
    {
        /** Установите предпочтительный размер ячейки, чтобы управлять начальным размером окна. **/
        setPreferredSize(CELL_SIZE);

        setPassable(pass);
    }

    /** Создайте новую ячейку карты, которая будет доступна по умолчанию. **/
    public JMapCell()
    {
        /** Вызовите другой конструктор, указав true для «passable». **/
        this(true);
    }

    /** Помечает эту ячейку как начальную или конечную. **/
    public void setEndpoint(boolean end)
    {
        endpoint = end;
        updateAppearance();
    }

    /**
     * Устанавливает эту ячейку как проходимую или не проходимую.
     * Ввод истины помечает ячейку как проходимую; ввод ложных пометок это как не пройденный.
     **/
    public void setPassable(boolean pass)
    {
        passable = pass;
        updateAppearance();
    }

    /** Возвращает true, если эта ячейка проходима, или false в противном случае. **/
    public boolean isPassable()
    {
        return passable;
    }

    /** Переключает текущее «проходимое» состояние ячейки карты. **/
    public void togglePassable()
    {
        setPassable(!isPassable());
    }

    /** Помечает эту ячейку как часть пути, обнаруженного алгоритмом A *. **/
    public void setPath(boolean path)
    {
        this.path = path;
        updateAppearance();
    }

    /**
     * Этот вспомогательный метод обновляет цвет фона в соответствии с текущим внутренним состоянием ячейки.
     **/
    private void updateAppearance()
    {
        if (passable)
        {
            /** Сносная клетка. Укажите его состояние с помощью границы. **/
            setBackground(Color.WHITE);

            if (endpoint)
                setBackground(Color.CYAN);
            else if (path)
                setBackground(Color.GREEN);
        }
        else
        {
            /** Непроходимая клетка. Сделай все это красным. **/
            setBackground(Color.RED);
        }
    }

    /** Реализация метода рисования для рисования цвета фона в ячейке карты. **/
    protected void paintComponent(Graphics g)
    {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}