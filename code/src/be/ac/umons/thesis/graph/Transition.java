package be.ac.umons.thesis.graph;

/**
 * 
 * @author Hugo Allard
 *
 */
public class Transition{

  protected State origin;
  protected State destination;
  
  public Transition(State origin, State destination){
    
    this.origin = origin;
    this.destination = destination;
  }

  public State getOrigin(){
  
    return origin;
  }

  public void setOrigin(State origin){
  
    this.origin = origin;
  }

  public State getDestination(){
  
    return destination;
  }

  public void setDestination(State destination){
  
    this.destination = destination;
  }
}
