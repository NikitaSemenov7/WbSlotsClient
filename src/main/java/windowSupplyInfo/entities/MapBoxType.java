package windowSupplyInfo.entities;

import java.util.Map;

public class MapBoxType {
    public static Map<String, Integer> mapBoxType = Map.ofEntries(
            Map.entry("Короба", 2),
            Map.entry("Монопаллеты", 5),
            Map.entry("Суперсейф", 6)
    );
}
