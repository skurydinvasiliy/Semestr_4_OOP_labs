import java.util.HashMap;
import java.util.HashSet;

/** Этот класс содержит реализацию алгоритма поиска пути A *. Алгоритм реализован как
 статический метод, поскольку алгоритм поиска пути действительно не должен
 поддерживать никакого состояния между вызовами алгоритма. **/
public class AStarPathfinder
{
     /** Эта константа содержит максимальный предел отсечения для стоимости
     путей. Если конкретная путевая точка превышает этот предел стоимости, путевая
     точка отбрасывается. **/
    public static final float COST_LIMIT = 1e6f;

    /**Попытка вычисления пути, который проводит путь между начальной и
     конечной точками указанной карты. Если путь может быть найден, путевая точка
     финального шага путивозвращается; эта точка может быть использована
     для возврата назад к начальной точке. Если путь не может быть найден,
     возвращается <code> null </ code>.
     **/
    public static Waypoint computePath(Map2D map)
    {
        /** Переменные, необходимые для поиска поалгоритму А* **/
        AStarState state = new AStarState(map);
        Location finishLoc = map.getFinish();

        /** Установка стартовой путевой точки для запуска поиска А* **/
        Waypoint start = new Waypoint(map.getStart(), null);
        start.setCosts(0, estimateTravelCost(start.getLocation(), finishLoc));
        state.addOpenWaypoint(start);

        Waypoint finalWaypoint = null;
        boolean foundPath = false;

        while (!foundPath && state.numOpenWaypoints() > 0)
        {

            /** Найдите «лучшую» (т.е. самую дешевую) путевую точку на данный момент. **/
            Waypoint best = state.getMinOpenWaypoint();


            /** Если лучшее местоположение - финишн, то мы закончили! **/
            if (best.getLocation().equals(finishLoc))
            {
                finalWaypoint = best;
                foundPath = true;
            }

            /** Добавить / обновить всех соседей текущего лучшего местоположения.
             * Это равносильно попытке выполнения всех «следующих шагов» из этого
             * местоположения. **/
            takeNextStep(best, state);

            /** Наконец, переместите это местоположение из «открытого» списка
             * в «закрытый» список. **/
            state.closeWaypoint(best.getLocation());
        }

        return finalWaypoint;
    }

    /** Этот статический вспомогательный метод берет путевую точку и генерирует
     все подходящие «последующие шаги» из этой путевой точки. Новые путевые точки добавляются в
     коллекцию «открытых путевых точек» переданного объекта состояния A *. **/
    private static void takeNextStep(Waypoint currWP, AStarState state)
    {
        Location loc = currWP.getLocation();
        Map2D map = state.getMap();

        for (int y = loc.yCoord - 1; y <= loc.yCoord + 1; y++)
        {
            for (int x = loc.xCoord - 1; x <= loc.xCoord + 1; x++)
            {
                Location nextLoc = new Location(x, y);

                /** Если следующее местоположение за пределами карты - пропускайте его **/
                if (!map.contains(nextLoc))
                    continue;

                /** Если следующее местоположение является этим (текущим?)
                 * местоположением - пропускайте его **/
                if (nextLoc == loc)
                    continue;

                /** Если это местоположение уже находится в «закрытом» наборе,
                 * тогда переходите к следующему местоположению. **/
                if (state.isLocationClosed(nextLoc))
                    continue;

                /** Сделайте путевую точку для этого «следующего местоположения». **/
                Waypoint nextWP = new Waypoint(nextLoc, currWP);

                /** ОК, мы обманываем и используем оценку стоимости, чтобы вычислить
                 * фактическую стоимость из предыдущей ячейки. Затем мы добавляем стоимость
                 * из ячейки карты, на которую мы вступаем, для включения барьеров и т. Д. **/
                float prevCost = currWP.getPreviousCost() +
                        estimateTravelCost(currWP.getLocation(),
                                nextWP.getLocation());

                prevCost += map.getCellValue(nextLoc);

                /** Пропустите это «следующее место», если это слишком дорого. **/
                if (prevCost >= COST_LIMIT)
                    continue;

                nextWP.setCosts(prevCost,
                        estimateTravelCost(nextLoc, map.getFinish()));

                /** Добавьте путевую точку в набор открытых путевых точек. Если
                 *  для этого местоположения уже есть путевая точка, новая
                 *  путевая точка заменяет только старую путевую точку, если она
                 *  дешевле, чем старая. **/
                state.addOpenWaypoint(nextWP);
            }
        }
    }

    /** Оценивает стоимость перехо между двумя указанными местами.
     Вычисляемая фактическая стоимость - это просто прямая дистанция между двумя точками (положениями).
     **/
    private static float estimateTravelCost(Location currLoc, Location destLoc)
    {
        int dx = destLoc.xCoord - currLoc.xCoord;
        int dy = destLoc.yCoord - currLoc.yCoord;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}