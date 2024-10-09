package PIMC;
import java.lang.Math;

/**
 *  Simulation of Feynman's Quantum Path Integral with various potentials by Monte Carlo method 
 *  for simulating the quantum behavior of particles under various potential functions.
 *
 *  @author tsotchke
 *  @version 0.1
 */
public class PIMC {  
  double[] x;                   // displacement of particle
  double xt;                    // random change in x[j]
  
  int N;                        // total segments
  int M = N/5;                  // total walkers
  int M0;                       // desired number of walkers
  
  double p;                     // probability of x[j]

  double δ;                     // maximum change in trial for displacement of atom
  int mcs;                      // total number of monte carlo steps per particle
  
  int s = 1;                    // potential selection
  
  double τ;                     // total time
  double Δτ;                    // change in time
  double ΔE;                    // change in energy
  double E0;                    // ground state energy
  
  double r1;                    // random number 1 on unit interval
  double r2;                    // random number 2 on unit interval
  
  double[] pdist;               // estimate of ground state wave function
  double[] pdistNorm;           // normalised ground state wave function
  double psum;                  // sum of potential
  double pave = 0;              // mean potential
  double pref = 0;              // reference potential
  double[] xv;                  // x values for computing pdist
  
  double[] Ψ0;                  // estimate of ground state wave function
  
  double Δx = 0.1;              // bin width
  double dt;                    // probability time interval
  double xmin;                  // minimum x
 
  /**
   * Initializes  arrays
   */
  public void initialize() {
    M0 = M;
       
    Ψ0 = new double[N+1];
    pdist = new double[N+1];
    pdistNorm = new double[N+1];

    x = new double[N+1];
    xv = new double[N+1];
    xmin = -Δx * (N+1)/2.0; // minimum location for computing Ψ0
    
    double binEdge = xmin;
    for(int i = 0;i<N+1;i++) {
      xv[i] = binEdge;
      binEdge += Δx;
    }
    
    // generate initial x[i] positions with potential randomly on unit interval with random sign
    for(int i = 0; i<N; i++) {
      x[i] = Math.pow(-1, (int) Math.round((10 * Math.random()))) * Math.random() + V(i, s);
      x[N] = x[0];
    }
    
    for(int i = 0; i<M; i++) {
      pref += V(s, i);
    }
    Δτ = τ/N;
    
    pave = 0;
    pref = 0;
    mcs = 0;
    dt = Δx*Δx;   
  }
  
  /**
   *  Potential functions 
   */
  public double V(int s, int j) {
    if (s == 1) {
      return Math.pow(x[j], 2)/2.0;                              // Harmonic potential
    }
    if (s == 2) {
      return (2 * Math.pow(1 - Math.exp(-x[j]), 2))/2.0;         // Morse potential
    }
    if (s == 3) {
      return Math.pow(x[j], 4)/4.0 - Math.pow(x[j], 2)/2.0;      // Double-well potential
    }
    if (s == 4) {
      return Math.pow(x[j], 4) + Math.pow(x[j], 2)/2.0;          // Anharmonic potential
    }
    else{
      return 0;  
    }
  }
  
  public double VTrial(int s, double xt) {
    if (s == 1) { 
      return Math.pow(xt, 2)/2.0;                                // Harmonic potential
    }
    if (s == 2) {
      return (2 * Math.pow(1 - Math.exp(-xt), 2))/2.0;           // Morse potential
    }
    if (s == 3) {
      return Math.pow(xt, 4)/4.0 - Math.pow(xt, 2)/2.0;          // Double-well potential
    }
    if (s == 4) {
      return Math.pow(xt, 4) + Math.pow(xt, 2)/2.0;              // Anharmonic potential
    }
    else{
      return 0;  
    }
  }
  
  /**
   *  Kinetic functions
   */
  public double T(int s, int j) {
    if (s == 1) {
      return (1/2.0) * x[j] * x[j];                                               // Harmonic kinetic
    }
    if (s == 2) {
      return (1/2.0) * x[j] * (4 * Math.exp(-x[j]) * (1 - Math.exp(-x[j])));      // Morse kinetic
    }
    if (s == 3) {
      return (1/2.0) * x[j] * (Math.pow(x[j], 3) - x[j]);                         // Double-well kinetic
    }
    if (s == 4) {
      return (1/2.0) * x[j] * (4 * Math.pow(x[j], 3) - x[j]);                     // Double-well kinetic
    }
    else{
      return 0;  
    }
  }
  
  /**
   * Generate probability distribution
   */
  void walk() {
    // Metropolis algorithm
    for(int i = M-1;i>=0;i--) {
      double pot = V(s, i);
      double dp = pot-pref;
      psum += pot;
      if(dp<0) {              // decide to add or delete walker
        if(M==0||(Math.random()<-dp*dt)&&(M<x.length)) {
          psum += pot;        // add potential probability of new walker
          M++;
        }
      } else {
        if((Math.random()<dp*dt)&&(M>0)) {
          M--;
          psum -= pot;        // subtract probability of deleted walker
        }
      }
    }
    pave = (M==0) ? 0         // if no walkers, probability = 0
                  : psum/M;
    if (M0 > 0) {
      pref = pave-(M-M0)/M0/dt;
    }
  }  
  
  /**
   *  Random change in path
   */
  public void changePath() {
    int end = N-1;
    
    // Metropolis-Hastings algorithm
    for(int i = 0; i<=N; i++){
      int j = (int) Math.round((Math.random() * (N-1)));
      
      r1 = Math.random();
      xt = x[j] + δ * (2 * r1 - 1);
      
      if (j == end) {
        ΔE = (1/2.0) * 
            (Math.pow(((x[N] - xt)/Δτ), 2) + Math.pow(((xt - x[j-1])/Δτ), 2) + VTrial(s, xt)
           - Math.pow(((x[N] - x[j])/Δτ), 2) - Math.pow(((x[j] - x[j-1])/Δτ), 2) - V(s, j));
        x[0] = x[N]; // ring constraint x[0] == x[N]
      }
      if (j == 0) {
        ΔE = (1/2.0) * 
            (Math.pow(((x[j+1] - xt)/Δτ), 2) + Math.pow(((xt - x[N-1])/Δτ), 2) + VTrial(s, xt)
           - Math.pow(((x[j+1] - x[j])/Δτ), 2) - Math.pow(((x[j] - x[N-1])/Δτ), 2) - V(s, j));
        x[N] = x[0]; // ring constraint x[N] == x[0]
      }
      else {
        ΔE = (1/2.0) * 
            (Math.pow(((x[j+1] - xt)/Δτ), 2) + Math.pow(((xt - x[j-1])/Δτ), 2) + VTrial(s, xt)
           - Math.pow(((x[j+1] - x[j])/Δτ), 2) - Math.pow(((x[j] - x[j-1])/Δτ), 2) - V(s, j));
        x[0] = x[N]; // ring constraint x[0] == x[N]
      }
      
      x[N] = x[0];   // ring constraint x[0] == x[N]
      
      p = Math.exp(-(Δτ * ΔE));
      r2 = Math.random();
      
      if (ΔE < 0 || r2<=p) {
        x[j] = xt;
      }
      
      walk();

      if (mcs > 0) {
        int bin = (int) Math.floor((x[j]-xmin)/Δx); // calculate bin index
        if(bin>=0&&bin<N+1) {
          pdist[bin]++;
          pdistNorm[bin] = pdist[bin] * (1.0/(N * mcs * 0.10));
          Ψ0[bin] =  Math.sqrt(pdistNorm[bin] * (1.0/Δx) * (1.0/(2*Math.PI)));
          E0 = (pdistNorm[bin] * T(s, bin) * V(s, bin));
        }
      }
    }
    mcs++;
  }

  /**
   * Resets all data to 0
   */
  void resetData() {
    for(int i = 0;i<N;i++) {
      x[i] = 0;
      Ψ0[i] = 0;
      pdist[i] = 0;
      pdistNorm[i] = 0;
    }
    E0 = 0;
    mcs = 0;
  }
}