package be.ac.umons.thesis.producttransducer;

import be.ac.umons.thesis.Pair;
import be.ac.umons.thesis.graph.State;
import be.ac.umons.thesis.transducer.TState;

/**
 * 
 * @author Hugo Allard
 *
 */
public class ProductTState extends TState{

  protected Pair<TState, TState> substates;
  protected Pair<String, String> value;
  
  public ProductTState(int id, State s, State t){
    
    super(id);
    this.substates = new Pair<TState, TState>((TState) s, (TState) t);
    // Initial/Accepting iff both substates are.
    if(((TState) s).isInitial() && ((TState) t).isInitial())
      this.setInitial(true);
    if(((TState) s).isAccepting() && ((TState) t).isAccepting())
      this.setAccepting(true);
  }

  public Pair<TState, TState> getSubstates(){
  
    return substates;
  }

  public void setSubstates(Pair<TState, TState> substates){
  
    this.substates = substates;
  }

  public Pair<String, String> getDelay(){
  
    return value;
  }

  public void setValue(Pair<String, String> delay){
  
    this.value = delay;
  }

  public void setValue(String str, String str2){
    
    this.value = new Pair<String, String>(str, str2);
  }
}
