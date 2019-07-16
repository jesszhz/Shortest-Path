
//Program to demonstrate Dijkstra's Shortest Path Algorithm

package shortestpath;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;


public class ShortestPath extends JFrame{
    static int height = 1000;
    static int width = 1500;
    
    static ArrayList<Point> points;
    static ArrayList<Edge> edges;

    static ArrayList<Boolean> inSet; // whether a point in in the set or not
    static ArrayList<Boolean> edgeInSet; // whether the edge is in the set or not
    static ArrayList<Double> dist;
    
    static int source, target; // index of source and target points
    
    public ShortestPath(){
        points = new ArrayList<>();
        edges = new ArrayList<>();
        inSet = new ArrayList<>();
        edgeInSet = new ArrayList<>();
        dist = new ArrayList<>();
        
        source = 0;
        target = 0;
    }
    
    // called in main
    public void findShortestPath(){
        while (!inSet.get(target)){ // while we have not yet reached the target node
            chooseNextPath();
            drawNextPath();
            sleep(4000);
            repaint();
        }   
        System.out.println("MINIMUM DISTANCE FROM SOURCE TO TARGET IS " + dist.get(target));
    }
    
    public void addPoint( Point p ){
            points.add(p);        
    }
    
    public void addEdge( Point a, Point b, String l){
        Edge e = new Edge(a, b, l);
        
        if ( !a.equals(b) || !edges.contains(e) ){
            edges.add(e);
        }        
    }
    
    public void paint( Graphics g ){
        Image img = drawNextPath();
        g.drawImage(img, 8, 30, this); 
    }
    
    public Image drawNextPath(){ // draws the path to the screen 
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();

        // drawing the points
        for (int i = 0; i < points.size(); i++){
            g.setColor(Color.red);
            drawPoint(g, points.get(i), 10);
            g.drawString(points.get(i).label, points.get(i).x+5, points.get(i).y); // labelling point
            
            // labelling distance to point
            g.setColor(Color.white);
            String d = "DISTANCE: ";
            if (dist.get(i)== Double.MAX_VALUE){
                d += "Infinity";
            } else {
                d += Double.toString(Math.round(dist.get(i)));
            }
            g.drawString(d, points.get(i).x + 20, points.get(i).y+20); // labelling dist
            
            // labelling source and target point
            g.setColor(Color.CYAN);
            if (i == source){
                g.drawString("STARTING POINT", points.get(i).x-20, points.get(i).y-20);
            } else if (i == target){
                g.drawString("TARGET POINT", points.get(i).x-20, points.get(i).y - 20);
            }
        }
        
        // drawing and labelling the edges
        g.setColor(Color.blue);
        for (int i = 0; i < edges.size(); i++){
            Edge e = edges.get(i);
            drawEdge(g, e);
            g.drawString("WEIGHT: " + Double.toString(Math.round(e.weight)), (int)(e.A.x + e.B.x)/2 , (int)(e.A.y + e.B.y)/2);
        }
        
        // drawing the shortest path 
        g.setColor(Color.white);
        for (int i = 0; i < points.size(); i++){
            if (inSet.get(i)){
                Point p = points.get(i);
                drawPoint(g, p, 10);
            }
        }
        for (int i = 0; i < edges.size(); i++){
            if (checkInSet(edges.get(i)) && checkInSet(edges.get(i).A) && checkInSet(edges.get(i).B)){
                drawEdge(g, edges.get(i));                
            }
        }        

        return bufferedImage;
    }
    
    public void drawEdge(Graphics g, Edge e){ // draws Edge
        g.drawLine(e.A.x, e.A.y, e.B.x, e.B.y);
    }
    
    public void drawPoint(Graphics g, Point p, int d){ // draws Point
        int r = d/2;
        g.fillOval(p.x-r, p.y-r, d, d);
    }
    
    public int minIndex(){    // finds index of point with minimum distance value not already in the set
        double minValue = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i<points.size(); i++){
            if (dist.get(i) < minValue && !inSet.get(i)){
                minValue = dist.get(i);
                minIndex = i;
            }
        } 
        return minIndex;        
    }  
    
    public void inSet(Point p){   // sets point p's inSet value to true
        inSet.set(points.indexOf(p), true);
    }   
    
    public void inSet(Edge e){ // sets Edge e's edgeInSet value to true
        edgeInSet.set(edges.indexOf(e), true);
    }
    
    public boolean checkInSet(Point p){ // checks if point is in set
        return inSet.get(points.indexOf(p));
    }
    
    public boolean checkInSet(Edge e){ // checks if edge is in set
        return edgeInSet.get(edges.indexOf(e));
    }
    
    public void setDist(int i, double d){ // sets the relative distance of a point from source 
        dist.set(i, d);
    }
    public void chooseNextPath(){ // chooses next path
        int pIndex = minIndex(); // choose the point with the minimum distance value
        inSet.set(pIndex, true);
        
        Point p = points.get(pIndex);
        
        for (int i = 0; i<points.get(pIndex).neighbours.size(); i++){ // for every neighbour of the point   
            int otherIndex = points.indexOf( p.neighbours.get(i).getOtherPoint(p));
            
            if (dist.get(pIndex) + p.neighbours.get(i).weight < dist.get(otherIndex) // if the updated distance would be greater than the current distance
                    && !inSet.get(otherIndex)){ // and the point is not already in the set
                setDist(otherIndex, dist.get(pIndex) + p.neighbours.get(i).weight); // update distance
                inSet(p.neighbours.get(i)); // include the point in the set
            }
        }        
        
    }
    
    public void setInitialValues() throws IOException{
        loadGraphFromFile("graph.txt");       

        for (int i = 0; i < points.size(); i++){        
            //chooses source point as left most point
            if (points.get(i).x < points.get(source).x){
                source = i;
            }
            
            //chooses target point as right most point
            if (points.get(i).x > points.get(target).x){
                target = i;
            }
        } 
        // sets distance to source point as 0
        dist.set(source, 0.0);
        
    }
    
    public void loadGraphFromFile(String filename) throws IOException{
        FileReader f = new FileReader(filename);
        Scanner s = new Scanner(f);
        
        // loading points
        int numPoints = s.nextInt();
        Point p;
        int x, y;
        String l;
        for (int i = 0; i < numPoints; i++){
            x = s.nextInt();
            y = s.nextInt();
            l = Integer.toString( s.nextInt() );
            p = new Point(x, y, l);
            points.add(p);
            inSet.add(false);
            dist.add(Double.MAX_VALUE);
        }
        
        
        // loading edges
        int numEdges = s.nextInt();
        int a, b;
        String n;
        Edge e;
        for (int i = 0; i < numEdges; i++){
            a = s.nextInt();
            b = s.nextInt();
            n = Integer.toString(s.nextInt());
            e = new Edge(points.get(a), points.get(b), n);
            edges.add(e);
            points.get(a).addNeighbour(e);
            points.get(b).addNeighbour(e);
            edgeInSet.add(false);
        }
    }
    
    public static void sleep(int duration){
        try {
            Thread.sleep(duration);
        }
        catch (Exception e) {}
    }
    
    public void initializeWindow(){
        setTitle("Dijkstra's Shortest Path Finder");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.black);
        setVisible(true);
    }
    
    public static void main(String[] args) throws IOException {
        ShortestPath sp = new ShortestPath();
        sp.initializeWindow();
        sp.setInitialValues();
        
        sp.findShortestPath();
                
    }
}
