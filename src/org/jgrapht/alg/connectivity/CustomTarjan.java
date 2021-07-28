package org.jgrapht.alg.connectivity;

import org.jgrapht.*;
import org.jgrapht.alg.interfaces.StrongConnectivityVisualization;
import org.jgrapht.util.*;

import java.util.*;

/**
 * Computes strongly connected components of a directed graph. The algorithm is
 * implemented after "Cormen et al: Introduction to algorithms", Chapter 22.5.
 * It has a running time of $O(V + E)$.
 *
 * <p>
 * Unlike {@link ConnectivityInspector}, this class does not implement
 * incremental inspection. The full algorithm is executed at the first call of
 * {@link CustomTarjan#stronglyConnectedSets()} or
 * {@link CustomTarjan#isStronglyConnected()}.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @author Christian Soltenborn
 * @author Christian Hammer
 */
public class CustomTarjan<V, E> extends AbstractStrongConnectivityInspector<V, E>
    implements StrongConnectivityVisualization {
  // stores the vertices, ordered by their finishing time in first dfs
  private LinkedList<VertexData<V>> orderedVertices;

  // maps vertices to their VertexData object
  private Map<V, VertexData<V>> vertexToVertexData;
  private Map<V, Integer> vertexToLowLink;

  private List<VisualizationStep> vis;

  private int groupCount;

  private int sccIndex;

  private int index;

  public List<VisualizationStep> getVis() {
    return this.vis;
  }

  /**
   * Constructor
   *
   * @param graph the input graph
   * @throws NullPointerException if the input graph is null
   */
  public CustomTarjan(Graph<V, E> graph) {
    super(graph);
  }

  @Override
  public List<Set<V>> stronglyConnectedSets() {
    if (stronglyConnectedSets == null) {
      orderedVertices = new LinkedList<>();
      stronglyConnectedSets = new ArrayList<>();
      vis = new ArrayList<>();
      groupCount = 0;
      index = 0;
      sccIndex = 0;

      // create VertexData objects for all vertices, store them
      createVertexData();

      // perform the first round of DFS, result is an ordering
      // of the vertices by decreasing finishing time
      for (VertexData<V> data : vertexToVertexData.values()) {
        if (!data.isDiscovered()) {
          System.out.println("step0/dfs: " + data.getVertex());
          dfsVisit(graph, data);
          groupCount += 1;
        }
      }

      // clean up for garbage collection
      orderedVertices = null;
      vertexToVertexData = null;
    }

    return stronglyConnectedSets;
  }

  /*
   * Creates a VertexData object for every vertex in the graph and stores them in
   * a HashMap.
   */
  private void createVertexData() {
    vertexToVertexData = CollectionUtil.newHashMapWithExpectedSize(graph.vertexSet().size());
    vertexToLowLink = CollectionUtil.newHashMapWithExpectedSize(graph.vertexSet().size());
    for (V vertex : graph.vertexSet()) {
      vertexToVertexData.put(vertex, new VertexData2<>(vertex, false, false, 0, 0));
    }
  }

  /*
   * The subroutine of DFS. NOTE: the set is used to distinguish between 1st and
   * 2nd round of DFS. set == null: finished vertices are stored (1st round). set
   * != null: all vertices found will be saved in the set (2nd round)
   */
  private void dfsVisit(Graph<V, E> visitedGraph, VertexData<V> vertexData) {
    vertexData.setIndex(index);
    vertexData.setLowLink(index);
    this.index += 1;
    vertexData.setDiscovered(true);
    orderedVertices.addFirst(vertexData);
    
    this.vis.add(new VisualizationStepTarjan(1, groupCount, "visit", vertexData.getVertex().toString(), vertexData.getIndex(), vertexData.lowLink));
    vertexToLowLink.put(vertexData.getVertex(), vertexData.getLowLink());

    // follow all edges
    for (E edge : visitedGraph.outgoingEdgesOf(vertexData.getVertex())) {
      VertexData<V> targetData = vertexToVertexData.get(visitedGraph.getEdgeTarget(edge));

      if (!targetData.isDiscovered()) {
        // the "recursion"
        this.dfsVisit(visitedGraph, targetData);
        int lowLink = Math.min(vertexData.lowLink, targetData.lowLink);
        vertexData.setLowLink(lowLink);
      } else if(orderedVertices.contains(targetData))
      {
        int lowLink = Math.min(vertexData.lowLink, targetData.index);
        vertexData.setLowLink(lowLink);
      }
      System.out.println("update low link: vertex " + targetData.getVertex() + " low link: " + targetData.lowLink);
      this.vis.add(new VisualizationStepTarjan(1, groupCount, "updateLowLink", vertexData.getVertex().toString(), vertexData.getIndex(), vertexData.getLowLink()));
      vertexToLowLink.put(vertexData.getVertex(), vertexData.getLowLink());
    }

    if (vertexData.lowLink == vertexData.index) {
      this.vis.add(new VisualizationStepTarjan(2, sccIndex, "takeSCC", vertexData.getVertex().toString(), vertexData.getIndex(), vertexData.getLowLink()));

      // print current stack
      String stack = "";
      for (VertexData<V> data : orderedVertices) {
        stack = data.getVertex() +"(" + vertexToLowLink.get(data.getVertex()) + ")" + " " + stack;
      }
      this.vis.add(new VisualizationStepTarjan(2, sccIndex, "tarjanStack", stack, 0, 0));
      Set<V> set = new HashSet<>();
      stronglyConnectedSets.add(set);
      VertexData<V> w;
      do {
        w = orderedVertices.removeFirst();
        set.add(w.getVertex());
        this.vis.add(new VisualizationStepTarjan(2, sccIndex, "visit", w.getVertex().toString(), w.getIndex(), w.getLowLink()));
      } while (!orderedVertices.isEmpty() && orderedVertices.getFirst().getLowLink() == vertexData.getLowLink());
      sccIndex += 1;
    }
  }

  /*
   * Lightweight class storing some data for every vertex.
   */
  private abstract static class VertexData<V> {
    private byte bitfield;
    private int index;
    private int lowLink;

    private VertexData(boolean discovered, boolean finished, int index) {
      this.bitfield = 0;
      this.index = index;
      this.lowLink = index;
      setDiscovered(discovered);
      setFinished(finished);
    }

    private boolean isDiscovered() {
      return (bitfield & 1) == 1;
    }

    private void setDiscovered(boolean discovered) {
      if (discovered) {
        bitfield |= 1;
      } else {
        bitfield &= ~1;
      }
    }

    private void setFinished(boolean finished) {
      if (finished) {
        bitfield |= 2;
      } else {
        bitfield &= ~2;
      }
    }

    public int getIndex() {
      return this.index;
    }

    public int getLowLink() {
      return this.lowLink;
    }

    public void setLowLink(int lowLink) {
      this.lowLink = lowLink;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    abstract VertexData<V> getFinishedData();

    abstract V getVertex();
  }

  private static final class VertexData2<V> extends VertexData<V> {
    private final V vertex;

    private VertexData2(V vertex, boolean discovered, boolean finished, int index, int lowLink) {
      super(discovered, finished, index);
      this.setLowLink(lowLink);
      this.vertex = vertex;
    }

    @Override
    VertexData<V> getFinishedData() {
      return null;
    }

    @Override
    V getVertex() {
      return vertex;
    }
  }
}
