import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jgrapht.alg.connectivity.VisualizationStep;

import graphvisualizer.containers.MenuPane;
import graphvisualizer.graph.AdjacencyMapDigraph;
import graphvisualizer.graph.Vertex;
import graphvisualizer.graphview.SmartGraphPanel;

public class ScreenRerender extends Thread {
  List<VisualizationStep> vr;
  SmartGraphPanel<String, Integer> graphView;
  MenuPane menu;
  Map<String, Vertex<String>> mapV;
  AdjacencyMapDigraph<String, Integer> graph;

  public ScreenRerender(List<VisualizationStep> vr, SmartGraphPanel<String, Integer> graphView,
      Map<String, Vertex<String>> mapV, MenuPane menu, AdjacencyMapDigraph<String, Integer> graph) {
    this.vr = vr;
    this.graphView = graphView;
    this.mapV = mapV;
    this.menu = menu;
    this.graph = graph;
  }

  public void run() {
    System.out.println("Screen update");
    for (int i = 0; i < vr.size(); i++) {
      VisualizationStep action = vr.get(i);
      menu.getStatusBox().appendText(action.toString() + "\n");
      if (action.phase == 1 && action.action.equals("visit")) {
        if (mapV.containsKey((action.vertex))) {
          graphView.getStylableVertex(mapV.get(action.vertex)).setStyleClass("visited-vertex" + action.group);
        }
      }
      if (action.phase == 1 && action.action.equals("push2Stack")) {
        if (mapV.containsKey((action.vertex))) {
          graphView.getStylableVertex(mapV.get(action.vertex)).setStyleClass("stacked-vertex" + action.group);
        }
      }
      if (action.phase == 2 && action.action.equals("visit")) {
        if (mapV.containsKey((action.vertex))) {
          graphView.getStylableVertex(mapV.get(action.vertex)).setStyleClass("connected-vertex" + action.group);
        }
      }
      if (action.action.equals("inverseGraph")) {
        graph.inverseGraph();
        graphView.autosize();
        graphView.setAutomaticLayout(true);
        try {
          TimeUnit.MILLISECONDS.sleep(800);
        } catch (Exception e) {
          // TODO: handle exception
        }
      }

      graphView.update();

      try {
        TimeUnit.MILLISECONDS.sleep(600);
      } catch (Exception e) {
        // TODO: handle exception
      }
    }
    menu.getStatusBox().appendText("\n=== done ===\n");
    graphView.autosize();
    graphView.setAutomaticLayout(true);
    graphView.update();
  }
}
