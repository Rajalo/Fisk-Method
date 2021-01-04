public class Edge {
    private Vertex start,end;

    /**
     * Constructs an edge from two vertices
     * @param start the first vertex
     * @param end the second vertex
     */
    public Edge(Vertex start, Vertex end)
    {
        this.start = start;
        this.end=end;
    }

    /**
     * Constructs an Edge for use in a Simple polygon, basically sets them as next to each other
     * @param start start vertex
     * @param end end vertex
     * @return the edge from start to end
     */
    public static Edge polygonalEdge(Vertex start, Vertex end)
    {
        Edge edge = new Edge(start,end);
        start.setNext(end);
        end.setPrev(start);
        return edge;
    }

    /**
     * Checks if an edge has a vertex as its start or end
     * @param vertex the vertex in question
     * @return true if an edge has a vertex as its start or end, false otherwise
     */
    public boolean contains(Vertex vertex)
    {
        return start.equals(vertex) || end.equals(vertex);
    }

    public Vertex getEnd() {
        return end;
    }

    public Vertex getStart() {
        return start;
    }

    /**
     * Inverts which is the start and end vertices
     */
    public void invert()
    {
        Vertex temp = start;
        start = end;
        end =temp;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
