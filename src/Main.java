import graphvisualizer.containers.MenuPane;
import graphvisualizer.graph.Vertex;
import graphvisualizer.graphview.*;
import graphvisualizer.graph.Graph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import graphvisualizer.containers.SmartGraphDemoContainer;
import graphvisualizer.graph.AdjacencyMapDigraph;
import usecase.strongConnectedComponent.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jgrapht.alg.connectivity.VisualizationStep;

public class Main extends Application {
    @Override
    public void start(Stage ignored) {
        final AdjacencyMapDigraph<String, Integer> defaultDigraph = new AdjacencyMapDigraph<>();
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        randomGraph(defaultDigraph);
        SmartGraphPanel<String, Integer> graphView = new SmartGraphPanel<>(defaultDigraph, strategy);
        SmartGraphDemoContainer smartGraphDemoContainer = new SmartGraphDemoContainer(graphView);
        MenuPane menu = smartGraphDemoContainer.getMenu();
        menu.getStatusBox().setText("[Graph Strong Connected Component Algorithms Visualization]\n" + defaultDigraph);

        Scene scene = new Scene(smartGraphDemoContainer, 1024, 768);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setOpacity(0.0);
        stage.setTitle("Graph Strong Connected Component Algorithms Visualization");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        stage.setOpacity(1.0);
        graphView.init();
        graphView.setAutomaticLayout(true);

        menu.setRandomGraphButtonAction(event -> {
            resetDefaultDigraph(defaultDigraph, graphView);
            ((AdjacencyMapDigraph<String, Integer>) defaultDigraph).clear();
            generateGraph(defaultDigraph, graphView);
            menu.getStatusBox().appendText("[Graph]\n" + defaultDigraph);
            graphView.update();
        });

        menu.setRunKosarajuButtonAction(event -> {
            Map<String, Vertex<String>> mapV = new HashMap<>();
            for (Vertex<String> vertex : defaultDigraph.vertices()) {
                mapV.put(vertex.element(), vertex);
            }
            Visualization v = new Visualization("kosaraju", defaultDigraph);
            List<VisualizationStep> vr = v.visualize();
            ScreenRerender sr = new ScreenRerender(vr, graphView, mapV, menu, defaultDigraph);
            sr.start();
        });

        menu.setRunTarjanButtonAction(event -> {
            Map<String, Vertex<String>> mapV = new HashMap<>();
            for (Vertex<String> vertex : defaultDigraph.vertices()) {
                mapV.put(vertex.element(), vertex);
            }
            Visualization v = new Visualization("tarjan", defaultDigraph);
            List<VisualizationStep> vr = v.visualize();
            ScreenRerender sr = new ScreenRerender(vr, graphView, mapV, menu, defaultDigraph);
            sr.start();
        });

        menu.setResetButtonAction(event -> {
            resetDefaultDigraph(defaultDigraph, graphView);
            menu.getStatusBox().setText("Graph has been reset.\n\n" + defaultDigraph);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    private void randomGraph(Graph<String, Integer> defaultDigraph)
    {
        Random rn = new Random();
        int numberOfVertices = rn.nextInt(12-7+1)+7;
        double p = 1.5/numberOfVertices;
        String Arr[] = {"a","b","c","d","e","f","g","h","i","k","l","m","n","o","p","q"};
        for(int i =0; i<numberOfVertices;i++){
            defaultDigraph.insertVertex(Arr[i]);
        }
        for(int i =0; i<numberOfVertices-1;i++){
            for(int j =i+1; j< numberOfVertices; j++) {
                double x = Math.random();
                double y = Math.random();
                if(x<p){defaultDigraph.insertEdge(Arr[i], Arr[j], 1);}
                if(y<p){defaultDigraph.insertEdge(Arr[j], Arr[i], 1);}
            }
        }
    }
    private void generateGraph(Graph<String, Integer> defaultDigraph, SmartGraphPanel<String, Integer> graphView) {
        randomGraph(defaultDigraph);
    }

    private void resetDefaultDigraph(Graph<String, Integer> defaultDigraph,
                                     SmartGraphPanel<String, Integer> graphView) {

        for (Vertex<String> vertex : defaultDigraph.vertices()) {
            graphView.getStylableVertex(vertex).setStyleClass("vertex");
        }
        graphView.update();
    }

}