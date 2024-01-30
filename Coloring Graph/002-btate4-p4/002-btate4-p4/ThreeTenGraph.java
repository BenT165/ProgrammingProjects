
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;

import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.EdgeType;

import org.apache.commons.collections15.Factory;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class that represents a Graph of vertices and edges.
 * Neighbors and edges of each vertice are represented in an adjacency list.
 * Vertices in the graph are represented by a linked list of GraphNodes.
 * 
 * 
 * @author btate4/GTAs/professors
 **/
class ThreeTenGraph implements Graph<GraphNode, GraphEdge>, UndirectedGraph<GraphNode, GraphEdge> {

    /**
     * Maximum number of nodes that this graph can contain.
     */
    private static final int MAX_NUMBER_OF_NODES = 200;

    /**
     * A linked list containing each of the GraphNodes in this graph.
     */
    private LinkedList<GraphNode> nodeList = null;

    /**
     * An adjacency list of Destinations.
     * Each Destination represents a neighbor/edge pairing
     * for a given node in nodeList.
     */
    private LinkedList<Destination>[] adjList = null;

    /**
     * A class that represents a neighbor/edge pair for each
     * node in the nodeList. The node is the neighbor GraphNode in nodeList
     * connects to. The edge is the edge that connects GraphNode in nodeList
     * to its neighbor.
     */
    private class Destination {

        /**
         * The current GraphNode's neighbor.
         */
        GraphNode node;

        /**
         * The edge connecting the current Graph to its neighbor.
         */
        GraphEdge edge;

        /**
         * Class constructor that initializes this classes' fields.
         * 
         * @param n The neighboring node/
         * @param e The edge connecting this node to its neighbor.
         */
        Destination(GraphNode n, GraphEdge e) {
            this.node = n;
            this.edge = e;
        }

    }

    /**
     * Class constructor that initializes empty node and adjacency lists.
     **/
    public ThreeTenGraph() {

        this.nodeList = new LinkedList<GraphNode>();
        this.adjList = (LinkedList<ThreeTenGraph.Destination>[]) new LinkedList[MAX_NUMBER_OF_NODES];
    }

    /**
     * Assembles a linked list of all of the edges in this graph (non-repeating).
     * O(n+e) where e is the number of edges in the graph and n is the number
     * of nodes in the graph (NOT the max number of nodes in the graph)
     * 
     * @return a Collection of all edges in this graph
     */
    public Collection<GraphEdge> getEdges() {

        // a linked list to keep track of the edges in the adjList
        LinkedList<GraphEdge> edgeList = new LinkedList<>();

        /* go through list of nodes */
        for (GraphNode vertice : nodeList) {
            for (Destination dest : adjList[vertice.getId()]) {

                // if neighbor's id > current vertice's id (neighbor's edge not seen yet) -> add
                // it to the edgeList
                if (dest.node.getId() > vertice.getId())
                    edgeList.add(dest.edge);
            }
        }
        return edgeList;
    }

    /**
     * Assembles a linked list of all of the GraphNodes(vertices) in this graph.
     * O(n) where n is the number of nodes in the graph.
     * 
     * @return a Collection of all the vertices in this graph
     */
    public Collection<GraphNode> getVertices() {

        /* create a linked list of vertices to be returned */
        LinkedList<GraphNode> verticeList = new LinkedList<>();

        /* go through node list and add all of its elements to return list */
        for (GraphNode element : nodeList)
            verticeList.add(element);

        // return a collection representing all of the vertices in this graph
        return verticeList;
    }

    /**
     * Returns the number of edges in this graph.
     * O(n) where n is the number of nodes in this grpah.
     * 
     * @return the number of edges in this graph
     */
    public int getEdgeCount() {

        /* number of edges found so far */
        int edgeCount = 0;

        // go through nodes in the gaph
        for (GraphNode node : nodeList)

            // add the number of neighbors for each node to a count
            edgeCount += getNeighborCount(node);

        /* return edgecount divided by 2 (so we don't accidentally count edges twice) */
        return edgeCount / 2;
    }

    /**
     * Returns the number of vertices in this graph.
     * O(1)
     * 
     * @return the number of vertices in this graph
     **/
    public int getVertexCount() {

        /* return the number of vertices in nodeList */
        return nodeList.size();
    }

    /**
     * Returns true if the provided vertex is in the graph.
     * O(1)
     * 
     * @param vertex the vertex that is being search
     * @return true iff this graph contains the given vertex
     */
    public boolean containsVertex(GraphNode vertex) {

        // list can't contain null vertices
        if (vertex == null)
            return false;

        /*
         * check the adjacency list index corresponding to the
         * vertex id to see if vertex is in the adjacency list
         * (it will be null if vertex is not in the graph)
         */
        return adjList[vertex.getId()] != null;
    }

    /**
     * Returns a linked list of the different GraphNodes that are
     * connected to the given vertex.
     * O(n) where n is the number of nodes in the graph.
     * 
     * @param vertex the vertex whose neighbors are being searched for.
     * @return the collection of vertices which are connected to vertex,
     *         or null if vertex is not present
     */
    public Collection<GraphNode> getNeighbors(GraphNode vertex) {

        /* if vertex is not in list -> return null */
        if (!containsVertex(vertex))
            return null;

        /* create a linked list of neighbors to be returned */
        LinkedList<GraphNode> neighbors = new LinkedList<>();

        for (Destination element : adjList[vertex.getId()]) {

            // add GraphNode components at the corresponding index of the adjacency list to
            // the list of neighbors
            neighbors.add(element.node);
        }

        return neighbors;
    }

    /**
     * Returns the number of vertices that are adjacent to vertex.
     * O(1)
     * 
     * @param vertex The vertex whose neighbors are being counted.
     * @return the number of neighboring vertices.
     */
    public int getNeighborCount(GraphNode vertex) {

        /* if vertex is not in list --> return 0 */
        if (!containsVertex(vertex))
            return 0;

        // return size of the list of destinations for the given vertex (use adjList)
        return adjList[vertex.getId()].size();
    }

    /**
     * Returns a linked list represenation of all the edges that
     * are directly connected to the given vertex.
     * O(n) where n is the number of nodes in the graph
     * 
     * @param vertex the vertex whose incident edges are to be returned.
     * @return the collection of edges which are connected to vertex or null if
     *         vertex is not present
     */
    public Collection<GraphEdge> getIncidentEdges(GraphNode vertex) {

        /* return null if vertex is not in this graph */
        if (!containsVertex(vertex))
            return null;

        // create a linked list of incident edges to be returned
        LinkedList<GraphEdge> incidentEdges = new LinkedList<>();

        // get provided vertex's list of Destination and add correponding edges to a
        // list of incidentEdges
        for (Destination dest : adjList[vertex.getId()])
            incidentEdges.add(dest.edge);

        // return list of incident edges
        return incidentEdges;
    }

    /**
     * Returns the Pair of vertices that this edge is connecting.
     * O(n+e) where e is the number of edges in the graph and n is the number
     * of nodes in the graph (NOT the max number of nodes in the graph)
     * 
     * @param edge the edge whose endpoints are to be returned.
     * @return The endpoints (incident vertices) of the given edge or null if the
     *         edge is not in the graph
     * 
     */
    public Pair<GraphNode> getEndpoints(GraphEdge edge) {

        /* return null if edge is null */
        if (edge == null)
            return null;

        /* for each node in the nodeList */
        for (GraphNode vertex : nodeList) {

            /* search that nodes' list of Destinations for the provided edge */
            for (Destination dest : adjList[vertex.getId()]) {

                /* if the current Destination has the same edge as provided edge */
                if (dest.edge.equals(edge)) {

                    /*
                     * return node pair containing current element
                     * and Destination node associated with edge
                     */
                    return (Pair<GraphNode>) new Pair(vertex, dest.node);
                }
            }
        }

        /* return null if edge not found in graph */
        return null;
    }

    /**
     * Returns an edge that connects v1 to v2.
     * Returns null if any of the following is true:
     * -v1 is not in the graph
     * -v2 is not in the graph
     * -there is not an edge in the graph connecting v1 and v2
     * O(n) where n is the number of nodes in the graph.
     * 
     * @param v1 first vertice being checked to see if it has an edge
     * @param v2 second vertice being checked to see if it has an edge connecting to
     *           v1
     * @return an edge that connects v1 to v2, or null if no such edge exists (or
     *         either vertex is not present)
     * @see Hypergraph#findEdgeSet(Object, Object)
     */
    public GraphEdge findEdge(GraphNode v1, GraphNode v2) {

        /* return null if either vertex is not in this graph */
        if (!containsVertex(v1) || !containsVertex(v2))
            return null;

        // go through destinations for v1 --> O(n) where n is number of Nodes for graph
        for (Destination dest : adjList[v1.getId()]) {

            /* if v1 connects to v2 --> return the edge connecting them */
            if (dest.node.equals(v2))
                return dest.edge;
        }

        /* otherwise, return null for no edge found */
        return null;
    }

    /**
     * Returns true if provided edge is connected to the
     * provided vertex.
     * O(n) where n is the number of nodes in the graph
     * 
     * @param vertex The vertex for which edge is being checked
     * @param edge   The edge being checked to see if it is connected to the given
     *               vertex.
     * @return true if vertex and edge are incident to each other.
     */
    public boolean isIncident(GraphNode vertex, GraphEdge edge) {

        return getIncidentEdges(vertex).contains(edge);

    }

    /**
     * Adds an edge e to this graph that connects
     * vertices v1 and v2.
     * If this graph does not contain v1, v2,
     * or both, throw an IllegalArgumentException.
     * Returns false if any of the following is true:
     * -edge is null
     * -edge is already in graph
     * -vertex 1 and vertex 2 are the same vertex
     * -there is already a different edge connecting v1 and v2
     * 
     * <p>
     * O(n+e) where e is the number of edges in the graph and n is the number
     * of nodes in the graph (NOT the max number of nodes in the graph)
     * 
     * @param e  the edge to be added
     * @param v1 the first vertex to be connected
     * @param v2 the second vertex to be connected
     * @return true if the add is successful, false otherwise
     * @see Hypergraph#addEdge(Object, Collection)
     * @see #addEdge(Object, Object, Object, EdgeType)
     * @throws IllegalArgumentException If at least one of the vertices is not in
     *                                  the graph
     */
    public boolean addEdge(GraphEdge e, GraphNode v1, GraphNode v2) {

        /*
         * if either vertex is already in the graph --> throw IllegalArgumentException
         */
        if (!containsVertex(v1) || !containsVertex(v2))
            throw new IllegalArgumentException(
                    "Error adding edge, At least one of those vertices is not in the graph!");

        // return false if:
        // -edge is null
        // -edge is already in graph
        // -edge would connect vertice to itself
        // -there is already a different edge connecting vertex 1 to vertex 2
        if (e == null || getEdges().contains(e) || v1.equals(v2) || findEdge(v1, v2) != null)
            return false;

        // add a Destination to adjList index corresponding to v1 with that contains the
        // provided graphNode and edge
        return (adjList[v1.getId()].add(new Destination(v2, e)) && adjList[v2.getId()].add(new Destination(v1, e)));

    }

    /**
     * Adds vertex to this graph.
     * Fails if vertex is null or already in the graph.
     * O(1)
     * 
     * @param vertex the vertex to add
     * @return true if the add is successful, and false otherwise
     * @throws IllegalArgumentException if vertex is null
     */
    public boolean addVertex(GraphNode vertex) {

        // throw exception for null argument
        if (vertex == null)
            throw new IllegalArgumentException("Cannot add null vertex");

        // return false if vertex is already in the adjacency list
        else if (containsVertex(vertex))
            return false;

        // append vertex to this classes' nodeList
        nodeList.add(vertex);

        // create empty linked list of Destinations corresponding to vertex in this
        // classes' adjacency list
        adjList[vertex.getId()] = new LinkedList<Destination>();

        return true;
    }

    /**
     * Removes edge from this graph.
     * Fails if edge is null, or is otherwise not an element of this graph.
     * O(n+e) where e is the number of edges in the graph and n is the number
     * of nodes in the graph (NOT the max number of nodes in the graph)
     * 
     * @param edge the edge to remove
     * @return true if the removal is successful, false otherwise
     */
    public boolean removeEdge(GraphEdge edge) {

        // if edge is not in the graph or null return false
        if (edge == null || !getEdges().contains(edge))
            return false;

        // list iterator for Destination lists
        Iterator<Destination> it;

        for (GraphNode node : nodeList) {

            // set up iterator for current Destination list
            it = adjList[node.getId()].iterator();
            while (it.hasNext()) {

                if (it.next().edge.equals(edge)) {

                    // remove any Destinations containing the given edge
                    it.remove();
                    break;
                }
            }
        }

        return true;
    }

    /**
     * Removes a vertex from this graph and any edges that are connected to that
     * vertex.
     *
     * <p>
     * Fails under the following circumstances:
     * <ul>
     * <li/>vertex is not an element of this graph
     * vertex is null
     * </ul>
     * /O(n+e) where e is the number of edges in the graph and n is the number of
     * nodes in the graph
     * 
     * @param vertex the vertex to remove
     * @return true if the removal is successful, false otherwise
     */
    public boolean removeVertex(GraphNode vertex) {

        // return false if vertex is null or not in list
        if (vertex == null || !getVertices().contains(vertex))
            return false;

        // iterator for node list
        Iterator<GraphNode> nodeItr;

        // iterator for Destination list
        Iterator<Destination> destItr;

        // current Node in nodeList
        GraphNode currentNode;

        // current Destination in Destination list
        Destination currentDestination;

        // create iterator for the nodeList
        nodeItr = nodeList.iterator();

        // for each node in nodeList
        while (nodeItr.hasNext()) {

            // store current node into variable
            currentNode = nodeItr.next();

            // if the current node is the GraphNode we are removing --> set Destination list
            // to null and remove it from the nodeList
            if (currentNode.equals(vertex)) {
                adjList[currentNode.getId()] = null;
                nodeItr.remove();
                break;
            }

            // if currentNode is NOT the GraphNode we are removing -> go through each of its
            // Destination
            else {

                // set destItr to iterate over current Destination list
                destItr = adjList[currentNode.getId()].iterator();

                while (destItr.hasNext()) {

                    // store current node into a variable
                    currentDestination = destItr.next();

                    // if current Destination list contains vertex we are removing, remove current
                    // Destination
                    if (currentDestination.node.equals(vertex)) {

                        destItr.remove();
                        break;
                    }
                }
            }
        }

        // then remove the given vertex from this classes' nodeList
        nodeList.remove(vertex);

        // if removal was successful
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return super.toString();
    }

    /**
     * Returns true if v1 and v2 share an incident edge.
     * Equivalent to getNeighbors(v1).contains(v2).
     * 
     * @param v1 the first vertex to test
     * @param v2 the second vertex to test
     * @return true if v1 and v2 share an incident edge
     */
    public boolean isNeighbor(GraphNode v1, GraphNode v2) {
        return (findEdge(v1, v2) != null);
    }

    /**
     * Returns true if this graph's edge collection contains edge.
     * Equivalent to getEdges().contains(edge).
     * 
     * @param edge the edge whose presence is being queried
     * @return true iff this graph contains an edge edge
     */
    public boolean containsEdge(GraphEdge edge) {
        return (getEndpoints(edge) != null);
    }

    /**
     * Returns the collection of edges in this graph which are of type edge_type.
     * 
     * @param edgeType the type of edges to be returned
     * @return the collection of edges which are of type edge_type, or
     *         null if the graph does not accept edges of this type
     * @see EdgeType
     */
    public Collection<GraphEdge> getEdges(EdgeType edgeType) {
        if (edgeType == EdgeType.UNDIRECTED) {
            return getEdges();
        }
        return null;
    }

    /**
     * Returns the number of edges of type edge_type in this graph.
     * 
     * @param edgeType the type of edge for which the count is to be returned
     * @return the number of edges of type edge_type in this graph
     */
    public int getEdgeCount(EdgeType edgeType) {
        if (edgeType == EdgeType.UNDIRECTED) {
            return getEdgeCount();
        }
        return 0;
    }

    /**
     * Returns the number of edges incident to vertex.
     * Special cases of interest:
     * <ul>
     * <li/>Incident self-loops are counted once.
     * <li>If there is only one edge that connects this vertex to
     * each of its neighbors (and vice versa), then the value returned
     * will also be equal to the number of neighbors that this vertex has
     * (that is, the output of getNeighborCount).
     * <li>If the graph is directed, then the value returned will be
     * the sum of this vertex's indegree (the number of edges whose
     * destination is this vertex) and its outdegree (the number
     * of edges whose source is this vertex), minus the number of
     * incident self-loops (to avoid double-counting).
     * </ul>
     * 
     * <p>
     * Equivalent to getIncidentEdges(vertex).size().
     * 
     * @param vertex the vertex whose degree is to be returned
     * @return the degree of this node
     * @see Hypergraph#getNeighborCount(Object)
     */
    public int degree(GraphNode vertex) {
        return getNeighborCount(vertex);
    }

    /**
     * Returns a Collection view of the predecessors of vertex
     * in this graph. A predecessor of vertex is defined as a vertex v
     * which is connected to
     * vertex by an edge e, where e is an outgoing edge of
     * v and an incoming edge of vertex.
     * 
     * @param vertex the vertex whose predecessors are to be returned
     * @return a Collection view of the predecessors of
     *         vertex in this graph
     */
    public Collection<GraphNode> getPredecessors(GraphNode vertex) {
        return getNeighbors(vertex);
    }

    /**
     * Returns a Collection view of the successors of vertex
     * in this graph. A successor of vertex is defined as a vertex v
     * which is connected to
     * vertex by an edge e, where e is an incoming edge of
     * v and an outgoing edge of vertex.
     * 
     * @param vertex the vertex whose predecessors are to be returned
     * @return a Collection view of the successors of
     *         vertex in this graph
     */
    public Collection<GraphNode> getSuccessors(GraphNode vertex) {
        return getNeighbors(vertex);
    }

    /**
     * Returns true if v1 is a predecessor of v2 in this graph.
     * Equivalent to v1.getPredecessors().contains(v2).
     * 
     * @param v1 the first vertex to be queried
     * @param v2 the second vertex to be queried
     * @return true if v1 is a predecessor of v2, and false otherwise.
     */
    public boolean isPredecessor(GraphNode v1, GraphNode v2) {
        return isNeighbor(v1, v2);
    }

    /**
     * Returns true if v1 is a successor of v2 in this graph.
     * Equivalent to v1.getSuccessors().contains(v2).
     * 
     * @param v1 the first vertex to be queried
     * @param v2 the second vertex to be queried
     * @return true if v1 is a successor of v2, and false otherwise.
     */
    public boolean isSuccessor(GraphNode v1, GraphNode v2) {
        return isNeighbor(v1, v2);
    }

    /**
     * If directed_edge is a directed edge in this graph, returns the source;
     * otherwise returns null.
     * The source of a directed edge d is defined to be the vertex for which
     * d is an outgoing edge.
     * directed_edge is guaranteed to be a directed edge if
     * its EdgeType is DIRECTED.
     * 
     * @param directedEdge The edge for which a source node is being found
     * @return the source of directed_edge if it is a directed edge in this graph,
     *         or null otherwise
     */
    public GraphNode getSource(GraphEdge directedEdge) {
        return null;
    }

    /**
     * If directed_edge is a directed edge in this graph, returns the destination;
     * otherwise returns null.
     * The destination of a directed edge d is defined to be the vertex
     * incident to d for which
     * d is an incoming edge.
     * directed_edge is guaranteed to be a directed edge if
     * its EdgeType is DIRECTED.
     * 
     * @param directedEdge The edge for which a Destination node is being found
     * @return the destination of directed_edge if it is a directed edge in this
     *         graph, or null otherwise
     */
    public GraphNode getDest(GraphEdge directedEdge) {
        return null;
    }

    /**
     * Returns a Collection view of the incoming edges incident to vertex
     * in this graph.
     * 
     * @param vertex the vertex whose incoming edges are to be returned
     * @return a Collection view of the incoming edges incident
     *         to vertex in this graph
     */
    public Collection<GraphEdge> getInEdges(GraphNode vertex) {
        return getIncidentEdges(vertex);
    }

    /**
     * Returns the collection of vertices in this graph which are connected to edge.
     * Note that for some graph types there are guarantees about the size of this
     * collection
     * (i.e., some graphs contain edges that have exactly two endpoints, which may
     * or may
     * not be distinct). Implementations for those graph types may provide alternate
     * methods
     * that provide more convenient access to the vertices.
     * 
     * @param edge the edge whose incident vertices are to be returned
     * @return the collection of vertices which are connected to edge,
     *         or null if edge is not present
     */
    public Collection<GraphNode> getIncidentVertices(GraphEdge edge) {

        Pair<GraphNode> p = getEndpoints(edge);
        if (p == null)
            return null;

        LinkedList<GraphNode> ret = new LinkedList<>();
        ret.add(p.getFirst());
        ret.add(p.getSecond());
        return ret;
    }

    /**
     * Returns a Collection view of the outgoing edges incident to vertex
     * in this graph.
     * 
     * @param vertex the vertex whose outgoing edges are to be returned
     * @return a Collection view of the outgoing edges incident
     *         to vertex in this graph
     */
    public Collection<GraphEdge> getOutEdges(GraphNode vertex) {
        return getIncidentEdges(vertex);
    }

    /**
     * Returns the number of incoming edges incident to vertex.
     * Equivalent to getInEdges(vertex).size().
     * 
     * @param vertex the vertex whose indegree is to be calculated
     * @return the number of incoming edges incident to vertex
     */
    public int inDegree(GraphNode vertex) {
        return degree(vertex);
    }

    /**
     * Returns the number of outgoing edges incident to vertex.
     * Equivalent to getOutEdges(vertex).size().
     * 
     * @param vertex the vertex whose outdegree is to be calculated
     * @return the number of outgoing edges incident to vertex
     */
    public int outDegree(GraphNode vertex) {
        return degree(vertex);
    }

    /**
     * Returns the number of predecessors that vertex has in this graph.
     * Equivalent to vertex.getPredecessors().size().
     * 
     * @param vertex the vertex whose predecessor count is to be returned
     * @return the number of predecessors that vertex has in this graph
     */
    public int getPredecessorCount(GraphNode vertex) {
        return degree(vertex);
    }

    /**
     * Returns the number of successors that vertex has in this graph.
     * Equivalent to vertex.getSuccessors().size().
     * 
     * @param vertex the vertex whose successor count is to be returned
     * @return the number of successors that vertex has in this graph
     */
    public int getSuccessorCount(GraphNode vertex) {
        return degree(vertex);
    }

    /**
     * Returns the vertex at the other end of edge from vertex.
     * (That is, returns the vertex incident to edge which is not vertex.)
     * 
     * @param vertex the vertex to be queried
     * @param edge   the edge to be queried
     * @return the vertex at the other end of edge from vertex
     */
    public GraphNode getOpposite(GraphNode vertex, GraphEdge edge) {
        Pair<GraphNode> p = getEndpoints(edge);
        if (p.getFirst().equals(vertex)) {
            return p.getSecond();
        } else {
            return p.getFirst();
        }
    }

    /**
     * Returns all edges that connects v1 to v2.
     * If this edge is not uniquely
     * defined (that is, if the graph contains more than one edge connecting
     * v1 to v2), any of these edges
     * may be returned. findEdgeSet(v1, v2) may be
     * used to return all such edges.
     * Returns null if v1 is not connected to v2.
     * <br/>
     * Returns an empty collection if either v1 or v2 are not present in this graph.
     * 
     * <p>
     * <b>Note</b>: for purposes of this method, v1 is only considered to be
     * connected to
     * v2 via a given <i>directed</i> edge d if
     * v1 == d.getSource() && v2 == d.getDest() evaluates to true.
     * (v1 and v2 are connected by an undirected edge u if
     * u is incident to both v1 and v2.)
     * 
     * @param v1 The first vertex
     * @param v2 The second vertex
     * @return a collection containing all edges that connect v1 to v2,
     *         or null if either vertex is not present
     * @see Hypergraph#findEdge(Object, Object)
     */
    public Collection<GraphEdge> findEdgeSet(GraphNode v1, GraphNode v2) {
        GraphEdge edge = findEdge(v1, v2);
        if (edge == null) {
            return null;
        }

        LinkedList<GraphEdge> ret = new LinkedList<>();
        ret.add(edge);
        return ret;

    }

    /**
     * Returns true if vertex is the source of edge.
     * Equivalent to getSource(edge).equals(vertex).
     * 
     * @param vertex the vertex to be queried
     * @param edge   the edge to be queried
     * @return true iff vertex is the source of edge
     */
    public boolean isSource(GraphNode vertex, GraphEdge edge) {
        return getSource(edge).equals(vertex);
    }

    /**
     * Returns true if vertex is the destination of edge.
     * Equivalent to getDest(edge).equals(vertex).
     * 
     * @param vertex the vertex to be queried
     * @param edge   the edge to be queried
     * @return true iff vertex is the destination of edge
     */
    public boolean isDest(GraphNode vertex, GraphEdge edge) {
        return getDest(edge).equals(vertex);
    }

    /**
     * Adds edge e to this graph such that it connects
     * vertex v1 to v2.
     * Equivalent to addEdge(e, new Pair GraphNode(v1, v2)).
     * If this graph does not contain v1, v2,
     * or both, implementations may choose to either silently add
     * the vertices to the graph or throw an IllegalArgumentException.
     * If edgeType is not legal for this graph, this method will
     * throw IllegalArgumentException.
     * See Hypergraph.addEdge() for a listing of possible reasons
     * for failure.
     * 
     * @param e        the edge to be added
     * @param v1       the first vertex to be connected
     * @param v2       the second vertex to be connected
     * @param edgeType the type to be assigned to the edge
     * @return true if the add is successful, false otherwise
     * @see Hypergraph#addEdge(Object, Collection)
     * @see #addEdge(Object, Object, Object)
     */
    public boolean addEdge(GraphEdge e, GraphNode v1, GraphNode v2, EdgeType edgeType) {
        // NOTE: Only directed edges allowed

        if (edgeType == EdgeType.DIRECTED) {
            throw new IllegalArgumentException();
        }

        return addEdge(e, v1, v2);
    }

    /**
     * Adds edge to this graph.
     * Fails under the following circumstances:
     * <ul>
     * <li/>edge is already an element of the graph
     * <li/>either edge or vertices is null
     * <li/>vertices has the wrong number of vertices for the graph type
     * <li/>vertices are already connected by another edge in this graph,
     * and this graph does not accept parallel edges
     * </ul>
     * 
     * @param edge     The edge being added
     * @param vertices A list of this Graph's vertices
     * @return true if the add is successful, and false otherwise
     */
    @SuppressWarnings("unchecked")
    public boolean addEdge(GraphEdge edge, Collection<? extends GraphNode> vertices) {
        if (edge == null || vertices == null || vertices.size() != 2) {
            return false;
        }

        GraphNode[] vs = (GraphNode[]) vertices.toArray();
        return addEdge(edge, vs[0], vs[1]);
    }

    /**
     * Adds edge to this graph with type edge_type.
     * Fails under the following circumstances:
     * <ul>
     * <li/>edge is already an element of the graph
     * <li/>either edge or vertices is null
     * <li/>vertices has the wrong number of vertices for the graph type
     * <li/>vertices are already connected by another edge in this graph,
     * and this graph does not accept parallel edges
     * <li/>edge_type is not legal for this graph
     * </ul>
     * 
     * @param edge     The edge being added
     * @param vertices A list of this Graph's vertices
     * @param edgeType The specified type of the edge being added
     * @return true if the add is successful, and false otherwise
     */
    @SuppressWarnings("unchecked")
    public boolean addEdge(GraphEdge edge, Collection<? extends GraphNode> vertices, EdgeType edgeType) {
        if (edge == null || vertices == null || vertices.size() != 2) {
            return false;
        }

        GraphNode[] vs = (GraphNode[]) vertices.toArray();
        return addEdge(edge, vs[0], vs[1], edgeType);
    }

    /**
     * Returns a {@code Factory} that creates an instance of this graph type.
     * 
     * @return A new instance of this graph with a unique id.
     */
    public static Factory<UndirectedGraph<GraphNode, GraphEdge>> getFactory() {
        return new Factory<UndirectedGraph<GraphNode, GraphEdge>>() {
            @SuppressWarnings("unchecked")
            public UndirectedGraph<GraphNode, GraphEdge> create() {
                return (UndirectedGraph<GraphNode, GraphEdge>) new ThreeTenGraph();
            }
        };
    }

    /**
     * Returns the edge type of edge in this graph.
     * 
     * @param edge The edge who's type is being queried.
     * @return the EdgeType of edge, or null if edge has no defined type
     */

    public EdgeType getEdgeType(GraphEdge edge) {
        return EdgeType.UNDIRECTED;
    }

    /**
     * Returns the default edge type for this graph.
     * 
     * @return the default edge type for this graph
     */
    public EdgeType getDefaultEdgeType() {
        return EdgeType.UNDIRECTED;
    }

    /**
     * Returns the number of vertices that are incident to edge.
     * For hyperedges, this can be any nonnegative integer; for edges this
     * must be 2 (or 1 if self-loops are permitted).
     * 
     * <p>
     * Equivalent to getIncidentVertices(edge).size().
     * 
     * @param edge the edge whose incident vertex count is to be returned
     * @return the number of vertices that are incident to edge.
     */
    public int getIncidentCount(GraphEdge edge) {
        return 2;
    }

}