import java.util.LinkedList;

public class Station {
    String name;
    private LinkedList<Edge> adjacentEdges;

    Station(String name) {
        this.name = name;
        adjacentEdges = new LinkedList<>();
    }

    public void addAdjacentEdge(Edge newEdge){
        this.adjacentEdges.add(newEdge);
    }

    public LinkedList<Edge> getAdjacentEdges(){
        return adjacentEdges;
    }
}
