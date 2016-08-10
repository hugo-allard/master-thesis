package be.ac.umons.thesis.graph;

/**
 * @author Hugo Allard
 *
 */
public class Graph{

  protected State[] states;
  protected int size;
  
  public Graph(int size){
    
    this.size = size;
    this.states = new State[size];
  }
  
  public void addState(State state){
    
    states[state.getId()] = state;
  }
  
  public State getState(int id){
    
    return states[id];
  }

  public State[] getStates(){
  
    return states;
  }

  public void setStates(State[] states){
  
    this.states = states;
    this.size = states.length;
  }

  public int getSize(){
  
    return size;
  }

  public void setSize(int size){
  
    this.size = size;
  }
}
