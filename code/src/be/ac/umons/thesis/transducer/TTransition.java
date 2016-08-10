package be.ac.umons.thesis.transducer;

import be.ac.umons.thesis.automata.ATransition;
import be.ac.umons.thesis.graph.State;

/**
 * 
 * @author Hugo Allard
 *
 */
public class TTransition extends ATransition{

  protected String output;
  
  public TTransition(State origin, State destination, 
      String input, String output){
    
    super(origin, destination, input);
    this.output = output;
  }
  
  public String getOutput(){
  
    return output;
  }

  public void setOutput(String output){
  
    this.output = output;
  }
}
