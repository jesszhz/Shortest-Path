package shortestpath;

import java.util.ArrayList;


public class Point {
    int x, y;
    String label;
    ArrayList<Edge> neighbours;

    
    public Point(int x, int y, String l){
        this.x = x;
        this.y = y;
        this.label = l;
        neighbours = new ArrayList<>();
    }
    
    public void addNeighbour(Edge e){
        if (!neighbours.contains(e)){
            neighbours.add(e);
        }
    }
    
    public boolean containsNeighbour( Edge e){
        return neighbours.contains(e);
    }
    
    public void printPoint(){
        System.out.println(getCoordinates());
    }
    
    public String getCoordinates(){
        return "(" + x + "," + y + ")";
    }
}
