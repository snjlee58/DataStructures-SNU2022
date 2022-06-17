import java.util.*;

public class Graph {
    public Set<Station> stations;
    public HashMap<String, Station> codeToStation;
    public HashMap<String, LinkedList<Station>> sameNameStations; // Key: stationName (ex. 강남), Value: List of Stations with the same name

    Graph() {
        this.stations = new HashSet<>();
        this.codeToStation = new HashMap<>();
        this.sameNameStations = new HashMap<>();
    }

    public void addStationName(String stationName){
        sameNameStations.put(stationName, new LinkedList<>());
    }

    public void addStation(String stationName, String stationCode, Station stationNode) {
        codeToStation.put(stationCode, stationNode);
        sameNameStations.get(stationName).add(stationNode);
        stations.add(stationNode);
    }

    public void addEdge(Station source, Station destination, long distance, String subwayLine) {
        Edge newEdge = new Edge(source, destination, distance, subwayLine);
        source.addAdjacentEdge(newEdge);
    }

    public boolean hasStation(String stationName){
        if (sameNameStations.containsKey(stationName)) return true;
        else return false;
    }

    public DijkstraSearchResult DijkstraSearch(Station source, Station destination) {
        DijkstraSearchResult dijkstraSearchResult = new DijkstraSearchResult();
        dijkstraSearchResult.destRelaxedBy.put(source, null);
        dijkstraSearchResult.subwayLineAtDest.put(source, null);

        // Dijkstra algorithm applied
        HashSet<Station> visitedStations = new HashSet<>();
        HashSet<Station> notVisitedStations = new HashSet<>();
        for (Station node : stations) {
            dijkstraSearchResult.shortestPathToDest.put(node, Long.MAX_VALUE);
        }
        dijkstraSearchResult.shortestPathToDest.put(source, 0L);
        visitedStations.add(source);

        for (Edge edge : source.getAdjacentEdges()) {
            dijkstraSearchResult.shortestPathToDest.put(edge.destination, edge.distance);
            dijkstraSearchResult.destRelaxedBy.put(edge.destination, source);
            dijkstraSearchResult.subwayLineAtDest.put(edge.destination, edge.subwayLine);
            notVisitedStations.add(edge.destination);
        }

        while (!notVisitedStations.isEmpty()){
            Station closestStation = findClosestStation(dijkstraSearchResult.shortestPathToDest, notVisitedStations);

            if (closestStation == destination){
                return dijkstraSearchResult;
            }
            visitedStations.add(closestStation);
            notVisitedStations.remove(closestStation);

            // Relaxation
            for (Edge edge : closestStation.getAdjacentEdges()) {
                if (visitedStations.contains(edge.destination))
                    continue;
                notVisitedStations.add(edge.destination);

                if (dijkstraSearchResult.shortestPathToDest.get(closestStation) + edge.distance < dijkstraSearchResult.shortestPathToDest.get(edge.destination)) {
                    dijkstraSearchResult.shortestPathToDest.put(edge.destination, dijkstraSearchResult.shortestPathToDest.get(closestStation) + edge.distance);
                    dijkstraSearchResult.destRelaxedBy.put(edge.destination, closestStation);
                    dijkstraSearchResult.subwayLineAtDest.put(edge.destination, edge.subwayLine);
                }
            }
        }
        return null;
    }

    private Station findClosestStation(HashMap<Station, Long> shortestDistanceMap, HashSet<Station> notVisitedStations) {
        long minDistance = Long.MAX_VALUE;
        Station closestStation = null;

        for (Station station: notVisitedStations){
            long currDistance = shortestDistanceMap.get(station);
            if (currDistance < minDistance) {
                minDistance = currDistance;
                closestStation = station;
            }
        }
        return closestStation;
    }
}
