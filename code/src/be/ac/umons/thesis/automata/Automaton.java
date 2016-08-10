package be.ac.umons.thesis.automata;

import java.util.Iterator;
import java.util.Stack;

import be.ac.umons.thesis.graph.Graph;
import be.ac.umons.thesis.graph.State;
import be.ac.umons.thesis.graph.Transition;

/**
 * 
 * @author Hugo Allard
 *
 */
public class Automaton extends Graph{

  public Automaton(int size){
    
    super(size);
  }
  
  public void trim(){

    Stack<State> stack = new Stack<State>();
    
    // Read forward
    for(int i = 0; i < states.length; i++)
      if(((AState)states[i]).isInitial())
        stack.push(states[i]);
    
    while(!stack.isEmpty()){
      
      AState current = (AState)stack.pop();
      current.setAccessible(true);
      
      for(Transition t : current.getOutgoing()){
        if(!((AState)t.getDestination()).isAccessible())
          stack.push(t.getDestination());
      }
    }
    
    int useful = 0;
    
    // Read backward
    for(int i = 0; i < states.length; i++)
      if(((AState)states[i]).isAccepting())
        stack.push(states[i]);
    
    while(!stack.isEmpty()){
      
      AState current = (AState)stack.pop();
      
      if(!current.isCoaccessible())
        if(current.isAccessible())
          useful++;
      
      current.setCoaccessible(true);
      
      for(Transition t : current.getIngoing()){
        if(!((AState)t.getOrigin()).isCoaccessible())
          stack.push(t.getOrigin());
      }
    } 
      
    State[] usefulStates = new State[useful];
    int j = 0;
    
    // Remove the useless states
    for(int i = 0; i < states.length; i++){
      if(((AState)states[i]).isAccessible() 
          && ((AState)states[i]).isCoaccessible()){
        
          usefulStates[j] = states[i];
          usefulStates[j].setId(j);
          j++;
      }
    }
    
    // Remove the useless transitions
    for(int i = 0; i < usefulStates.length; i++){
      
      Iterator<Transition> iter = usefulStates[i].getOutgoing().iterator();
      while(iter.hasNext()){
        Transition t = iter.next();
        if(!(((AState) t.getDestination()).isAccessible() && 
            ((AState) t.getDestination()).isCoaccessible()))
          iter.remove();
      }
      
      iter = usefulStates[i].getIngoing().iterator();
      while(iter.hasNext()){
        Transition t = (Transition) iter.next();
        if(!(((AState) t.getOrigin()).isAccessible() && 
            ((AState) t.getOrigin()).isCoaccessible()))
          iter.remove();
      }
    }
    
    // Back to initial values
    for(int i = 0; i < usefulStates.length; i++){
      
      ((AState) usefulStates[i]).setAccessible(false);
      ((AState) usefulStates[i]).setCoaccessible(false);
    }
    
    setStates(usefulStates);
  }
}
