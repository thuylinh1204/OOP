package org.jgrapht.alg.connectivity;

public class VisualizationStepTarjan extends VisualizationStep {
  public int lowLink;
  public int index;

  public VisualizationStepTarjan(int step, int group, String action, String vertex, int index, int lowLink) {
    super(step, group, action, vertex);
    this.index = index;
    this.lowLink = lowLink;
  }

  public String toString() {
    if (action.equals("tarjanStack")) {
      return "phase " + this.phase + ": current stack [vertex(low link)]: " + this.vertex;
    }

    if (action.equals("takeSCC")) {
      return "phase " + this.phase + ": action: take strong connected component - no." + this.group + ". Root vertex: " + this.vertex + "(index: " + this.index + ", low link: " + this.lowLink + ")";
    }

    if (action.equals("visit") && this.phase == 2) {
      return "phase " + this.phase + ": action: put vertex " + this.vertex + "(index: " + this.index + ", low link: " + this.lowLink + ")" + " to SCC no." + this.group;
    }

    if (action.equals("updateLowLink")) {
      return "phase " + this.phase + ": action: update low link " + this.vertex + "(index: " + this.index + ", low link: " + this.lowLink + ")";
    }

    return "phase " + this.phase + ": action: " + this.action + " vertex " + this.vertex + "(index: " + this.index + ", low link: " + this.lowLink + ")";
  }
}
