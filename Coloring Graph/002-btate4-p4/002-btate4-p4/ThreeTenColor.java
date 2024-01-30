import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.awt.Color;

import javax.swing.JPanel;

import java.util.Collection;
import java.util.NoSuchElementException;

import java.util.LinkedList;

/**
 * Simulation of our coloring algorithm.
 * 
 * @author btate4/GTAs/professors
 */
class ThreeTenColor implements ThreeTenAlg {
    /**
     * The graph the algorithm will run on.
     */
    Graph<GraphNode, GraphEdge> graph;

    /**
     * The priority queue of nodes for the algorithm.
     */
    WeissPriorityQueue<GraphNode> queue;

    /**
     * The stack of nodes for the algorithm.
     */
    LinkedList<GraphNode> stack;

    /**
     * Whether or not the algorithm has been started.
     */
    private boolean started = false;

    /**
     * Whether or not the algorithm is in the coloring stage or not.
     */
    private boolean coloring = false;

    /**
     * The color when a node has "no color".
     */
    public static final Color COLOR_NONE_NODE = Color.WHITE;

    /**
     * The color when an edge has "no color".
     */
    public static final Color COLOR_NONE_EDGE = Color.BLACK;

    /**
     * The color when a node is inactive.
     */
    public static final Color COLOR_INACTIVE_NODE = Color.LIGHT_GRAY;

    /**
     * The color when an edge is inactive.
     */
    public static final Color COLOR_INACTIVE_EDGE = Color.LIGHT_GRAY;

    /**
     * The color when a node is highlighted.
     */
    public static final Color COLOR_HIGHLIGHT = new Color(255, 204, 51);

    /**
     * The color when a node is in warning.
     */
    public static final Color COLOR_WARNING = new Color(255, 51, 51);

    /**
     * The colors used to assign to nodes.
     */
    public static final Color[] COLORS = { Color.PINK, Color.GREEN, Color.CYAN, Color.ORANGE, Color.MAGENTA, Color.YELLOW, Color.DARK_GRAY, Color.BLUE };

    /**
     * {@inheritDoc}
     */
    public EdgeType graphEdgeType() {
        return EdgeType.UNDIRECTED;
    }

    /**
     * {@inheritDoc}
     */
    public void reset(Graph<GraphNode, GraphEdge> graph) {
        this.graph = graph;
        started = false;
        coloring = false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * {@inheritDoc}
     */
    public void start() {
        this.started = true;

        // create an empty stack
        stack = new LinkedList<>();

        // create an empty priority queue
        queue = new WeissPriorityQueue<>();

        for (GraphNode v : graph.getVertices()) {

            // Set the cost of each node to be its degree
            v.setCost(graph.degree(v));

            // Set each node to be active
            // This enables the display of cost for the node
            v.setActive();

            // add node into queue
            queue.add(v);
        }

        // highlight the current node with max priority
        highlightNextMax();

    }

    /**
     * {@inheritDoc}
     */
    public void finish() {

        // Coloring completed. Set all edges back to "no color".
        for (GraphEdge e : graph.getEdges()) {
            e.setColor(COLOR_NONE_EDGE);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void cleanUpLastStep() {
        // Unused. Required by the interface.
    }

    /**
     * {@inheritDoc}
     */
    public boolean setupNextStep() {

        // Whole algorithm done.
        if (coloring && stack.size() == 0)
            return false;

        // First stage done when all nodes are pushed into stack.
        // Change the flag to start the coloring stage.
        if (!coloring && graph.getVertexCount() == stack.size()) {
            coloring = true;
        }

        // Return true to indicate more steps to continue.
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void doNextStep() {

        if (!coloring) {
            // Stage 1: pushing nodes into stack one by one & update record

            // maxNode is the active node with the highest priority
            // Remove the maxNode from priority queue and push it into stack
            GraphNode maxNode = findMax();

            // Update the cost of all nodes that is a neighbor of the maxNode
            updateNeighborCost(maxNode);

            // Identify and highlight the next max node in the updated priority queue
            highlightNextMax();

        } else {
            // Stage 2: pop nodes from stack one by one and choose a color for each

            // Pop off stack top
            GraphNode node = stack.pop();

            // For the node popped off, pick a color that is different from all
            // neighbors who has got assigned a color so far
            Color newColor = chooseColor(node);

            // Inform all neighbors of this node the selected color
            updateColor(node, newColor);

        }

    }

    /**
     * Change the the color of max node in queue to be COLOR_HIGHLIGHT.
     * 
     * @throws NoSuchElementException if queue is empty
     **/
    public void highlightNextMax() {

        // check if queue size is 0
        if (queue.size() == 0)
            return;

        queue.element().setColor(COLOR_HIGHLIGHT); /* highlight highest priority item in queue */

    }

    /**
     * Sets highest priority node and all its edges to be inactive color.
     * O(1)
     * 
     * @return node with highest cost in the priority queue (tiebreaker is lower
     *         node id) or null if node is not in heap.
     **/
    public GraphNode findMax() {

        if (queue.isEmpty())
            return null; /* return null if queue is empty */

        // 1. Remove the node with the max priority from the priority queue;
        /* remove node with highest cost */
        GraphNode maxItem = queue.remove();

        // 2. Push the max node into stack
        // Note: Take a look at the JavaDoc of LinkedList to determine which method
        // to use, especially the interface Deque.
        stack.push(maxItem);

        // 3. Set max node to be inactive and change its color to be
        // COLOR_INACTIVE_NODE;
        stack.peek().unsetActive();
        stack.peek().setColor(COLOR_INACTIVE_NODE);

        // 4. Set the color of all incident edges of max node to be COLOR_INACTIVE_EDGE.
        for (GraphEdge edge : graph.getIncidentEdges(stack.peek()))
            edge.setColor(COLOR_INACTIVE_EDGE);

        /* return the item that was removed */
        return maxItem;
    }

    /**
     * Note that cost is the number of neighbors for a given node.
     * Updates the cost maxNode's neighbors to be one less
     * O(N) where n is number of neighboring nodes for maxNode
     * 
     * @param maxNode The highest priority node in the graph (node being removed)
     **/
    public void updateNeighborCost(GraphNode maxNode) {
        // Update the cost for all active neighbors of maxNode.
        // Note that the cost of a node is equal to the number of its *active*
        // neighbors.

        /*
         * if max node is not in the graph or active -> don't try to update neighbor
         * costs
         */
        if (!graph.containsVertex(maxNode) || maxNode.isActive())
            return;

        /* for each of maxNode's neighbors */
        for (GraphNode neighbor : graph.getNeighbors(maxNode)) {

            /*
             * decrement each neighbor's cost to account for maxNode being
             * disconnected from them
             */
            neighbor.setCost(neighbor.getCost() - 1);
            queue.update(neighbor); // This is nlogn
        }
        return;

    }

    /**
     * Assigns the color with the highest priority (lowest array index) to the
     * provided node
     * while ensuring that the selected color does not match any colors of the
     * provided node's neighbors.
     * Then informs node's neighbors that the selected color is in use.
     * 
     * @param node The node for which a color is being selected.
     * @return The selected color for node, warning color if no unique color could
     *         be found,
     *         or null if node
     **/
    public Color chooseColor(GraphNode node) {

        /* If node is null --> return null. */
        if (node == null)
            return null;

        int colorIndex = -1; /* index of the optimal color in COLORS array */

        // Pick a color for node based on the following criteria:
        // 1. The color is one of the listed colors in ThreeTenColor.COLORS;
        // 2. The color has not been assigned to any of its neighbors, and
        // 3. The color has the lowest index in array ThreeTenColor.COLORS in all colors
        // that satisfy condition 2.
        for (int i = 0; i < COLORS.length; i++) {

            // if 0 neighbors have a color that is in COLORS array
            if (!node.nbrHasColor(i)) {
                colorIndex = i; /* record lowest index of a color that is not shared with a neighbor */
                break;
            }
        }

        // if valid color found
        if (colorIndex > -1) {

            /* inform node's neighbors they have a neighbor with the selected color */
            for (GraphNode neighbor : graph.getNeighbors(node)) {

                neighbor.setNbrColor(colorIndex); // notify neighbor that one of its neighbors has a new color
                queue.update(neighbor);
            }

            /* return unique color at lowest array index in COLORS */
            return COLORS[colorIndex];
        }

        /* otherwise return COLOR_WARNING */
        return COLOR_WARNING;
    }

    /**
     * Changes the color for a node to be newColor.
     * 
     * @param node     the node that is having its color changed
     * @param newColor the color that node is having its color set to
     **/
    public void updateColor(GraphNode node, Color newColor) {

        // Do not update color if either node or newColor is null
        if (!graph.containsVertex(node) || newColor == null)
            return;

        /* set color for node to be newColor */
        node.setColor(newColor);
        queue.update(node);

        /* go through edges in graph */
        for (GraphEdge edge : graph.getIncidentEdges(node)) {

            /*
             * for each of the node's incident edges,
             * if edge color is inactive -> update edge color to be newColor
             */
            if (edge.getColor().equals(COLOR_INACTIVE_EDGE)) {
                edge.setColor(newColor);

            }
        }
    }

}
