package be.ac.umons.thesis.producttransducer;

import be.ac.umons.thesis.Pair;
import be.ac.umons.thesis.automata.ATransition;
import be.ac.umons.thesis.graph.State;

/**
 * 
 * @author Hugo Allard
 *
 */
public class ProductTransition extends ATransition{

  protected Pair<String, String> output;
  
  public ProductTransition(State origin, State destination, 
      String input, Pair<String, String> output){
    
    super(origin, destination, input);
    this.output = output;
  }

  public Pair<String, String> getOutput(){
  
    return output;
  }

  public void setOutput(Pair<String, String> output){
  
    this.output = output;
  }
}
