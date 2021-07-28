package usecase.strongConnectedComponent;

import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.graph.*;

import graphvisualizer.graph.AdjacencyMapDigraph;
import graphvisualizer.graph.Edge;
import graphvisualizer.graph.Vertex;

import java.util.*;

public class Visualization {
  private String alg;
  private AdjacencyMapDigraph<String, Integer> graph;

  public Visualization(String alg, AdjacencyMapDigraph<String, Integer> graph) {
    this.alg = alg;
    this.graph = graph;
  }

  public List<VisualizationStep> visualize() {
    Graph<String, DefaultEdge> directedGraph = transformGraphToJGraph();

    // computes all the strongly connected components of the directed graph
    StrongConnectivityAlgorithm<String, DefaultEdge> scAlg = new CustomKosaraju<>(directedGraph);
    ;

    if (alg.equals("tarjan")) {

      scAlg = new CustomTarjan<>(directedGraph);
    }

    List<Graph<String, DefaultEdge>> stronglyConnectedSubgraphs = scAlg.getStronglyConnectedComponents();

    // prints the strongly connected components
    System.out.println("Strongly connected components:");
    for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
      System.out.println(stronglyConnectedSubgraphs.get(i));
    }

    return ((StrongConnectivityVisualization) scAlg).getVis();
  }

  private Graph<String, DefaultEdge> transformGraphToJGraph() {
    Graph<String, DefaultEdge> directedGraph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
    for (Vertex<String> vertex : graph.vertices()) {
      directedGraph.addVertex(vertex.element());
    }

    for (Edge<Integer, String> edge : graph.edges()) {
      Vertex<String>[] verties = edge.vertices();
      directedGraph.addEdge(verties[0].element(), verties[1].element());

      System.out.println(verties[0].element() + "->" + verties[1].element());
    }

    return directedGraph;
  }
}
