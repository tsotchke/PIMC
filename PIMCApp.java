package PIMC;
import java.awt.Color;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.PlotFrame;

/**
 *  Application for simulating Feynman's Quantum Path Integral with various potentials by Monte Carlo method
 *
 *  @author tsotchke
 *  @version 0.1
 */
public class PIMCApp extends AbstractSimulation {
  PIMC action = new PIMC();
  PlotFrame path = new PlotFrame("Time (ω⁻¹)", "Atomic Displacement ((ħ/mω)¹ᐟ²)", "Ring of Virtual Atoms");
  PlotFrame probability = new PlotFrame("x", "P(x)", "Ground State Probability Distribution");
  PlotFrame wave = new PlotFrame("x", "|Ψ₀(x)|²", "Ground State Wave Function Probability Density");
  PlotFrame energy = new PlotFrame("Time", "E₀", "Ground State Energy");

  /**
   * Sets path frame properties
   */
  public PIMCApp() {
    // path graph
    path.setAutoscaleX(true);
    path.setAutoscaleY(true);
    path.setMarkerShape(0, 0);
    path.setConnected(true); // draw lines between points
    
    // probability distribution graph
    probability.setAutoscaleY(true);
    probability.setPreferredMinMaxX(-4.0, 4.01);
    probability.setMarkerShape(0, 1);
    probability.setMarkerColor(0, Color.black);
    
    // wave function graph
    wave.setAutoscaleY(true);
    wave.setPreferredMinMaxX(-4.0, 4.01);
    wave.setMarkerShape(0, 1);
    wave.setMarkerColor(0, Color.blue);
    
    // energy graph
    energy.setAutoscaleY(true);
    energy.setMarkerShape(0, 1);
    energy.setMarkerColor(0, Color.green);
  }

  /**
   * Gets parameters and initializes medium
   */
  public void initialize() {
    action.N = control.getInt("Total number of segments");
    action.τ = control.getDouble("Total time (ω⁻¹)");
    action.δ = control.getDouble("Maximum change in atomic displacement ((ħ/mω)¹ᐟ²)");
    action.s = control.getInt("Potential Function (1: Harmonic, 2: Morse, 3: Double-well; 4: Anharmonic)");
    action.initialize();
    path.clearData();
    drawPath();
  }
  
  /**
   * Draw a path
   */
  public void drawPath() {
    for(int i = 0;i<action.N;i++) {
      path.append(0, i*action.Δτ, action.x[i]);
    }
  }
  
  /**
   * Makes one change in path at a time
   */
  public void doStep() {
    action.changePath();
    path.clearData();
    drawPath();
    path.setMessage(action.mcs + " Monte Carlo steps");
    probability.append(0, action.xv, action.pdistNorm);
    wave.append(0, action.xv, action.Ψ0);
    energy.append(0, action.mcs, action.E0);
    energy.setMessage("E₀ = " + action.E0);
  }

  /**
   * Resets to default values
   */
  public void reset() {
    control.setValue("Total number of segments", 200);
    control.setValue("Total time (ω⁻¹)", 10.0);
    control.setValue("Maximum change in atomic displacement ((ħ/mω)¹ᐟ²)", 1.0);
    control.setValue("Potential Function (1: Harmonic, 2: Morse, 3: Double-well; 4: Anharmonic)", 1);
    path.clearData();
    enableStepsPerDisplay(true);
    setStepsPerDisplay(100);
  }
  
  /**
   * Resets the accumulated data.
   */
  public void resetData() {
    action.resetData();
    path.clearData();
    path.repaint();
    probability.clearData();
    probability.repaint();
    wave.clearData();
    wave.repaint();
    energy.clearData();
    energy.repaint();
  }

  /**
   * Starts the Java application.
   * @param args  command line parameters
   */
  public static void main(String[] args) {
    SimulationControl control = SimulationControl.createApp(new PIMCApp());
    control.addButton("resetData", "Reset Data");
  }
}