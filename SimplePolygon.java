import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SimplePolygon {
    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;
    boolean clockwise;
    int[][] coordsArray;
    public static Color[] colors = {new Color(170,170,170),new Color(200,100,100),new Color(100,200,100),new Color(100,100,200)};

    /**
     * Constructs a SimplePolygon from an array of vertices
     * @param vertices the vertices of the polygon
     */
    public SimplePolygon(Vertex[] vertices)
    {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        Collections.addAll(this.vertices, vertices);
        if (vertices.length==0)
        {
            return;
        }
        for (int i = 0; i < this.vertices.size(); i++)
        {
            edges.add(Edge.polygonalEdge(this.vertices.get(i),this.vertices.get((i+1)%this.vertices.size())));
        }
        Vertex lowest = this.vertices.get(0);
        for (Vertex vertex:this.vertices)
        {
            if (vertex.getY()<lowest.getY()||(vertex.getY()==lowest.getY()&&vertex.getX()<lowest.getX()))
                lowest = vertex;
            vertex.setColor(colors[0]);
        }
        clockwise = left(lowest.getCoordsArr(),lowest.getPrev().getCoordsArr(),lowest.getNext().getCoordsArr());
    }
    /**
     * Constructs a SimplePolygon from a list of vertices
     * @param vertices the vertices of the polygon
     */
    public SimplePolygon(ArrayList<Vertex> vertices)
    {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>();
        for (int i = 0; i < this.vertices.size(); i++)
        {
            edges.add(Edge.polygonalEdge(this.vertices.get(i),this.vertices.get((i+1)%this.vertices.size())));
        }
        Vertex lowest = this.vertices.get(0);
        for (Vertex vertex:this.vertices)
        {
            if (vertex.getX()<lowest.getX())
                lowest = vertex;
            vertex.setColor(colors[0]);
        }
        clockwise = left(lowest.getCoordsArr(),lowest.getPrev().getCoordsArr(),lowest.getNext().getCoordsArr());
    }

    /**
     * Used to make a triangle specifically
     * @param triangleVertex the vertices of the triangle
     * @param b whether the polygon is counterclockwise or not
     */
    public SimplePolygon(Vertex[] triangleVertex, boolean b) {
        this.vertices = new ArrayList<>(Arrays.asList(triangleVertex));
        this.edges = new ArrayList<>();
        if (!left(triangleVertex[0].getCoordsArr(),triangleVertex[1].getCoordsArr(),triangleVertex[2].getCoordsArr()))
            Collections.reverse(this.vertices);
        for (int i = 0; i < this.vertices.size(); i++)
        {
            edges.add(new Edge(this.vertices.get(i),this.vertices.get((i+1)%this.vertices.size())));
        }
        clockwise = !b;
    }

    /**
     * Counts the number of grey colored vertices in the given polygon
     * @return the number of grey colored vertices in the given polygon
     */
    public int greyCount()
    {
        int i = 0;
        for (Vertex vertex: vertices)
        {
            if (vertex.getColor().equals(colors[0]))
                i++;
        }
        return i;
    }

    /**
     * Colors in the vertices of a triangulation such that the result is a 3-coloring of the PSLG
     * @param triangles the triangles in the triangulation (as a list)
     * @param length the number of vertices in the triangulation
     * @return an integer array with the number of red, green, and blue vertices
     */
    public static int[] fiskBound(ArrayList<SimplePolygon> triangles, int length)
    {
        int[] redgreenblue = {1,1,1};
        Main.gpanel.triangles = triangles;
        triangles.get(0).vertices.get(0).setColor(colors[1]);
        triangles.get(0).vertices.get(1).setColor(colors[2]);
        triangles.get(0).vertices.get(2).setColor(colors[3]);
        int count = 3;
        int i = 0;
        while (count < length&&i< Short.MAX_VALUE)
        {
            int newColor = triangles.get(i).fillIn();
            if (newColor>0) {
                count++;
                redgreenblue[newColor - 1]++;
            }
            i++;
            if (i >= triangles.size())
                i %= triangles.size();
        }
        return redgreenblue;
    }

    /**
     * Fills in the remaining vertex of a triangle with 2 vertices colored in
     * @return the color of the newly colored vertex as an integer, either 1,2,3
     */
    public int fillIn()
    {
        int greyCount = this.greyCount();
        if (greyCount!=1)
            return 0;
        int grey = 0;
        int newColor = 0;
        int i = 0;
        for (Vertex vertex: vertices)
        {
            if (vertex.getColor().equals(colors[0]))
                grey = i;
            if (vertex.getColor().equals(colors[2]))
                newColor+=1;
            if (vertex.getColor().equals(colors[3]))
                newColor+=2;
            i++;
        }
        newColor = 4 - newColor;
        vertices.get(grey).setColor(colors[newColor]);
        return newColor;
    }

    /**
     * Draws the polygon
     * @param g the graphics object being used to draw
     */
    public void paint(Graphics g)
    {
        g.setColor(new Color(150,160,250));
        g.fillPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
        g.setColor(Color.BLACK);
        g.drawPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
        int i = 0;
        for (Vertex vertex : vertices)
        {
            g.setColor(vertex.getColor());
            g.fillOval(vertex.getX()-5,vertex.getY()-5,10,10);
            g.drawString(""+i++, vertex.getX()+5, vertex.getY()+5);
        }
    }
    /**
     * Draws the polygon without vertex labels
     * @param g the graphics object being used to draw
     */
    public void paintsub(Graphics g)
    {
        g.setColor(new Color(150,160,250));
        g.fillPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
        g.setColor(Color.BLACK);
        g.drawPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
    }

    /**
     * Generates a coordinate array of the vertices compliant with g.drawPolygon()'s specifications
     * @return coordinate array
     */
    public int[][] getCoordsArray() {
        if (coordsArray != null&&coordsArray.length== vertices.size())
            return coordsArray;
        coordsArray = new int[2][vertices.size()];
        int i = 0;
        for (Vertex vertex : vertices)
        {
            coordsArray[0][i] = vertex.getX();
            coordsArray[1][i++] = vertex.getY();
        }
        return coordsArray;
    }

    /**
     * Clips off a vertex if it's an ear vertex
     * @param v0 the vertex in question
     * @return the clipped triangle
     */
    public SimplePolygon clipEar(Vertex v0)
    {
        if (!diagonal(v0.getPrev(),v0.getNext()))
        {
            return null;
        }
        Vertex[] triangleVertex = {v0.getPrev(),v0,v0.getNext()};
        SimplePolygon newPolygon = new SimplePolygon(triangleVertex,true);
        Edge newEdge = Edge.polygonalEdge(triangleVertex[0],triangleVertex[2]);
        edges.remove(vertices.indexOf(v0));
        this.vertices.remove(v0);
        edges.remove(vertices.indexOf(triangleVertex[0]));
        edges.add((vertices.indexOf(triangleVertex[0])-1+ edges.size())%edges.size(),newEdge);
        return newPolygon;
    }

    /**
     * The Ear-Clipping algorithm for triangulating a polygon
     * @return the triangles in the triangulation
     */
    public ArrayList<SimplePolygon> triangulate()
    {
        if (clockwise)
        {
            for (Edge edge: edges) {
                edge.invert();
            }
            for (Vertex vertex: vertices)
            {
                vertex.invert();
            }
            Collections.reverse(this.vertices);
            Collections.reverse(edges);
            clockwise = false;
        }
        ArrayList<SimplePolygon> triangles = new ArrayList<>();
        triangles.add(this);
        int i = 0;
        while (vertices.size()>3&&i< Short.MAX_VALUE)
        {
            SimplePolygon triangle = clipEar(vertices.get(i%vertices.size()));
            if (triangle != null)
            {
                triangles.add(triangle);
            }
            i++;
        }
        return triangles;
    }
    /**
     * Determines if two vertices have a diagonal
     * @param v0 first vertex
     * @param v1 second vertex
     * @return true if they have a diagonal
     */
    public boolean diagonal(Vertex v0, Vertex v1)
    {
        return inCone(v0,v1)&&inCone(v1,v0)&&diagonalie(v0,v1);
    }

    /**
     * Determines if two vertices can see each other
     * @param v0 first vertex
     * @param v1 second vertex
     * @return true if no edges overlap with line between vertices, false otherwise.
     */
    public boolean diagonalie(Vertex v0, Vertex v1)
    {
        for (Edge edge : edges)
        {
            if (edge.contains(v0)||edge.contains(v1))
            {
                continue;
            }
            if (intersectsProp(edge.getStart().getCoordsArr(),edge.getEnd().getCoordsArr(),v0.getCoordsArr(), v1.getCoordsArr()))
            {
                return false;
            }
        }
        return true;
    }
    /**
     * Determines whether a vertex is in the "open cone" of another vertex.
     * @param v0 vertex who's in the cone is analyzed
     * @param v1 vertex who may or may not be in the cone
     * @return true if vertex is in the cone, false otherwise
     */
    public boolean inCone(Vertex v0, Vertex v1)
    {
        Vertex next = vertices.get((vertices.indexOf(v0)+1)% vertices.size());
        Vertex prev = vertices.get((vertices.indexOf(v0)-1+vertices.size())% vertices.size());
        if (leftOn(v0.getCoordsArr(),next.getCoordsArr(),prev.getCoordsArr()))
        {
            return left(v0.getCoordsArr(),v1.getCoordsArr(),prev.getCoordsArr())&&left(v1.getCoordsArr(),v0.getCoordsArr(),next.getCoordsArr());
        }
        return !(leftOn(v0.getCoordsArr(),v1.getCoordsArr(),next.getCoordsArr())&&leftOn(v1.getCoordsArr(),v0.getCoordsArr(),prev.getCoordsArr()));
    }
    
    /**
     * Determines if two line segments intersect
     * @param a first endpoint of first line segment
     * @param b second endpoint of first line segment
     * @param c first endpoint of second line segment
     * @param d first endpoint of second line segment
     * @return true if they intersect, false otherwise
     */
    public static boolean intersectsProp(int[] a, int[] b, int[] c, int[] d)
    {
        if (collinear(a,b,c) && collinear(a,b,d))
        {
            return (between(a,b,c)||between(a,b,d));
        }
        if ((between(a,b,c)||between(a,b,d)||between(c,d,a)||between(c,d,b)))
        {
            return true;
        }
        if (collinear(a,b,c) || collinear(a,b,d) || collinear (c, d, a) || collinear(c,d,b))
        {
            return false;
        }
        return (left(a,b,c) != left(a,b,d)) && (left(c,d,a) != left(c,d,b));
    }
    /**
     * Determines if a point is between two other points (all collinear)
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point between two other points
     * @return true if c is between a and b, false elsewise.
     */
    public static boolean between(int[]a, int[] b, int[] c)
    {
        if (!collinear(a,b,c))
            return false;
        if (a[0] != b[0])
            return ((a[0] <= c[0]) && (c[0] <=b[0])) ||
                    ((a[0] >= c[0]) && (c[0] >= b[0]));
        return ((a[1] <= c[1]) && (c[1] <= b[1])) ||
                ((a[1] >= c[1]) && (c[1] >= b[1]));
    }
    /**
     * Determines if a point is to the left of two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be to the left
     * @return true if c is to the left of a and b, false elsewise.
     */
    public static boolean left(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) > 0;
    }
    /**
     * Determines if a point is to the left of or inline with two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be to the left
     * @return true if c is to the left of or collinear with a and b, false elsewise.
     */
    public static boolean leftOn(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) >= 0;
    }
    /**
     * Determines if a point is to the collinear with two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be collinear.
     * @return true if c is to the left of a and b, false elsewise.
     */
    public static boolean collinear(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) == 0;
    }
    /**
     * Determines signed area of the parallelogram with points a,b,c,d
     * @param a coordinates of point a
     * @param b coordinates of point b
     * @param c coordinates of point c
     * @param d coordinates of point d
     * @return signed area of the parallelogram with points a,b,c,d
     */
    public static int crossProduct(int[] a, int[] b, int[] c, int[]d)
    {
        return (b[0] - a[0]) * (d[1] - c[1]) - (b[1] - a[1]) * (d[0] - c[0]);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("SimplePolygon{" +
                "vertices=");
        for (Vertex vertex: vertices)
        {
            str.append(Main.gpanel.vertices.indexOf(vertex)).append(",");
        }
        return str+"}";
    }
}
