package be.ac.umons.thesis.transducer;

import be.ac.umons.thesis.automata.AState;

/**
 * 
 * @author Hugo Allard
 *
 */
public class TState extends AState{

  protected String output; // Subsequential output
  
  public TState(int id){
    
    super(id);
    this.output = "";
  }
  
  public TState(int id, String output){
    
    super(id);
    this.output = output;
  }

  public String getOutput(){
  
    return output;
  }

  public void setOutput(String output){
  
    this.output = output;
  }
}
