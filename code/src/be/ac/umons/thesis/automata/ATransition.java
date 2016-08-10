package be.ac.umons.thesis.automata;

import be.ac.umons.thesis.graph.State;
import be.ac.umons.thesis.graph.Transition;

/**
 * 
 * @author Hugo Allard
 *
 */
public class ATransition extends Transition{

  protected String input;
  
  public ATransition(State origin, State destination, String input){
    
    super(origin, destination);
    this.input = input;
  }
  
  public String getInput(){
    
    return input;
  }
  
  public void setInput(String input){
    
    this.input = input;
  }
}
