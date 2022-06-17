public class Edge{
    Station source;
    Station destination;
    long distance;
    String subwayLine;

    Edge(Station source, Station destination, long distance, String subwayLine) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.subwayLine = subwayLine;
    }

}
