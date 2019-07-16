
package shortestpath;


public class Edge {
    Point A, B;
    double weight;
    String label;
    
    public Edge(Point A, Point B, String l){
        this.A = A;
        this.B = B;
        setWeightAsDist();
    }
    
    public Point getA(){
        return A;
    }
    
    public Point getB(){
        return B;
    }
    
    public Point getOtherPoint(Point p){
        if (p.equals(A)){
            return B;
        } else {
            return A;
        }
    }
    
    public double getWeight(){
        return weight;
    }
    
    public void setWeightAsDist(){
        this.weight = Math.sqrt(Math.pow(A.x - B.x, 2) + Math.pow(A.y - B.y, 2));
    }
    
    public void printEdge(){
        System.out.println("Points: " + A.getCoordinates() + B.getCoordinates());
        System.out.println("Weight: " + weight);
    }
    
}
