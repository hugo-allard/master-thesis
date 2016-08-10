package be.ac.umons.thesis.producttransducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import be.ac.umons.thesis.Pair;
import be.ac.umons.thesis.graph.State;
import be.ac.umons.thesis.graph.Transition;
import be.ac.umons.thesis.transducer.Transducer;

/**
 * 
 * @author Hugo Allard
 *
 */
public class ProductTransducer extends Transducer{

  public ProductTransducer(int size){
    
    super(size);
  }

  /**
   * Compute the V' sub-automaton by marking each of its nodes as coaccessible.
   */
  public void computeV(){
    
    // For each state we store the total output built so far
    List<Pair<String,String>> outputs = new ArrayList<Pair<String,String>>();
    
    for(int i = 0; i < this.size; i++)
      outputs.add(new Pair<String,String>("",""));
    
   for(int i = 0; i < this.size; i++)
     if(((ProductTState)states[i]).isInitial())
       dfs(states[i], outputs);
   
   readBack();
   
   for(int i = 0; i < size; i++)
     states[i].setMark(State.UNEXPLORED);
  }
  
  private void dfs(State state, List<Pair<String,String>> outputs){
    
    ProductTState q = (ProductTState) state;
    q.setMark(State.CURRENTLY_EXPLORED);
    q.setAccessible(true);
    
    for(Transition tr : q.getOutgoing()){
      
      ProductTransition t = (ProductTransition) tr;
      ProductTState p = (ProductTState) t.getDestination();
      
      // Non explored => just dfs
      if(p.getMark() == State.UNEXPLORED){
        
        // Update output
        Pair<String, String> out = outputs.get(q.getId());
        Pair<String, String> newOutput = 
            new Pair<String, String> (out.first + t.getOutput().first, 
                                      out.second + t.getOutput().second);
        
        outputs.get(p.getId()).first = newOutput.first;
        outputs.get(p.getId()).second = newOutput.second;
        
        dfs(p, outputs);
      }
      
      // Currently explored => cycle. Check if the cycle is not empty, i.e.,
      // the  output stored for the destination is different from the new one.
      // If that's the case, mark the predecessor as coaccessible
      else if(t.getDestination().getMark() == State.CURRENTLY_EXPLORED){
        
        Pair<String, String> out = outputs.get(q.getId());
        Pair<String, String> newOutput = 
            new Pair<String, String> (out.first + 
                t.getOutput().first, out.second + t.getOutput().second);
        
        if(!newOutput.equals(outputs.get(p.getId())))
          q.setCoaccessible(true);

      }
    }
    q.setMark(State.EXPLORED);
  }
  
  private void readBack(){
    
    Stack<State> stack = new Stack<State>();
    
    for(int i = 0; i < states.length; i++)
      if(((ProductTState)states[i]).isCoaccessible())
        stack.push(states[i]);
    
    while(!stack.isEmpty()){
      
      ProductTState current = (ProductTState) stack.pop();
      
      current.setCoaccessible(true);
      
      for(Transition t : current.getIngoing())
        if(!((ProductTState)t.getOrigin()).isCoaccessible() && 
            ((ProductTState)t.getOrigin()).isAccessible())
          stack.push(t.getOrigin());
    } 
  }
}
