import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Subway {
    public static void main(String[] args) throws UnsupportedEncodingException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        System.setOut(out);

        String dataPath = args[0];

        // Read data from file
        File file = new File(dataPath);
        Graph subwayGraph = new Graph();
        readData(file, subwayGraph);

        while (true){
            try{
                String inputLine = br.readLine();

                if (inputLine.compareTo("QUIT") == 0)
                    break;

                String[] inputData = inputLine.split("\\s");
                String source = inputData[0];
                String destination = inputData[1];

                LinkedList<Station> possibleSourceStations = subwayGraph.sameNameStations.get(source);
                LinkedList<Station> possibleDestStations = subwayGraph.sameNameStations.get(destination);

                DijkstraSearchResult bestDijkstraSearchResult = null;
                Station finalDestStation = null;

                long shortestTotalDistance = Long.MAX_VALUE;
                for (Station sourceStation: possibleSourceStations){
                    for (Station destinationStation: possibleDestStations){
                        DijkstraSearchResult dijkstraSearchResult =  subwayGraph.DijkstraSearch(sourceStation, destinationStation);

                        if (dijkstraSearchResult.shortestPathToDest.get(destinationStation) < shortestTotalDistance){
                            shortestTotalDistance = dijkstraSearchResult.shortestPathToDest.get(destinationStation);
                            bestDijkstraSearchResult = dijkstraSearchResult;
                            finalDestStation = destinationStation;

                        }
                    }
                }

                // Print results
                Station toStation = finalDestStation;

                StringBuffer sb = new StringBuffer(finalDestStation.name);
                while (true) {
                    Station fromStation = bestDijkstraSearchResult.destRelaxedBy.get(toStation);

                    if (fromStation == null) {
                        break;
                    }

                    if (!fromStation.name.equals(toStation.name)){
                        if (bestDijkstraSearchResult.subwayLineAtDest.get(fromStation) != null && !bestDijkstraSearchResult.subwayLineAtDest.get(fromStation).equals(bestDijkstraSearchResult.subwayLineAtDest.get(toStation))){
                            sb.insert(0, "[" + fromStation.name + "]" + " ");
                        } else{
                            sb.insert(0, fromStation.name + " ");
                        }
                    }
                    toStation = fromStation;
                }
                System.out.println(sb);

                long timeTaken = bestDijkstraSearchResult.shortestPathToDest.get(finalDestStation);
                System.out.println(timeTaken);

            } catch (IOException e)
            {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void readData(File file, Graph subwayGraph){
        HashMap<String, String> subwayLines = new HashMap<>();

        try{
            Scanner fileText = new Scanner(file, "UTF-8");
            String inputLine = null;

            // Create station nodes
            while (fileText.hasNext()){
                inputLine = fileText.nextLine();
                if (inputLine.isEmpty()) break;

                String[] inputData = inputLine.split("\\s+");
                String stationCode = inputData[0]; // ex. 810
                String stationName = inputData[1]; // ex. 강남
                String subwayLine = inputData[2]; // ex. 8, K2
                Station newStation = new Station(stationName);


                // Add stationName to graph if it doesn't already exist
                if (!subwayGraph.hasStation(stationName)){
                    subwayGraph.addStationName(stationName);
                }

                // Create transfer edges between stations with the same name (distance = 5)
                LinkedList<Station> sameNameStations = subwayGraph.sameNameStations.get(stationName);
                for (Station sameNameStation : sameNameStations){
                    // "TRANSFER LINE" is used as placeholder and will never overlap with a subway line code
                    // because all given codes will not contain whitespace
                    subwayGraph.addEdge(newStation, sameNameStation, 5, "TRANSFER LINE");
                    subwayGraph.addEdge(sameNameStation, newStation, 5, "TRANSFER LINE");
                }

                // Add station to subwayGraph
                subwayGraph.addStation(stationName, stationCode, newStation);

                // Add stationCode to subway line hashmap (Key: stationCode 810, Value: subwayLine 8)
                subwayLines.put(stationCode, subwayLine);
            }

            // Create subway edges (path between 2 stations)
            while (fileText.hasNext()){
                inputLine = fileText.nextLine();
                String[] inputData = inputLine.split("\\s+");
                Station source =  subwayGraph.codeToStation.get(inputData[0]);
                Station destination = subwayGraph.codeToStation.get(inputData[1]);
                long distance = Long.valueOf(inputData[2]);
                String subwayLine = subwayLines.get(inputData[0]);

                subwayGraph.addEdge(source, destination, distance, subwayLine);
            }
        } catch (IOException e)
        {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }
}

