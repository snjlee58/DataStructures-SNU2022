import java.util.HashMap;

/* A class that stores information about a dijkstra search */
public class DijkstraSearchResult {
    HashMap<Station, Station> destRelaxedBy;
    HashMap<Station, String> subwayLineAtDest;
    HashMap<Station, Long> shortestPathToDest;

    public DijkstraSearchResult(){
        this.destRelaxedBy = new HashMap<>();
        this.subwayLineAtDest = new HashMap<>();
        this.shortestPathToDest = new HashMap<>();

    }
}
