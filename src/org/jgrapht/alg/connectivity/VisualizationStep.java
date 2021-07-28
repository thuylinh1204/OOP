package org.jgrapht.alg.connectivity;

public class VisualizationStep {
  public int phase;
  public int group;
  public String action;
  public String vertex;

  public VisualizationStep(int phase, int group, String action, String vertex) {
    this.phase = phase;
    this.action = action;
    this.vertex = vertex;
    this.group = group;
  }

  public String toString() {
    if (action.equals("inverseGraph")) {
      return "phase " + this.phase + ": action: inverse graph";
    }

    if (action.equals("kosarajuAlgStack")) {
      return "phase " + this.phase + ": current stack: " + this.vertex;
    }

    return "phase " + this.phase + ": action: " + this.action + " vertex " + this.vertex;
  }
}
