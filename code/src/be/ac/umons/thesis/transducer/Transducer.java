package be.ac.umons.thesis.transducer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import be.ac.umons.thesis.Pair;
import be.ac.umons.thesis.automata.Automaton;
import be.ac.umons.thesis.graph.State;
import be.ac.umons.thesis.graph.Transition;
import be.ac.umons.thesis.producttransducer.ProductTState;
import be.ac.umons.thesis.producttransducer.ProductTransducer;
import be.ac.umons.thesis.producttransducer.ProductTransition;

/**
 * 
 * @author Hugo Allard
 *
 */
public class Transducer extends Automaton{

  private int K;
  
  public Transducer(int size){
    
    super(size);
  }
  
  /**
   * Returns the cartesian square of this <i>Transducer</i>
   * 
   * @return the cartesian square of this <i>Transducer</i>
   */
  public ProductTransducer square(){
    
    ProductTransducer output = new ProductTransducer(size*size);
    
    // Computes QxQ
    for(int i = 0; i < size; i++)
      for(int j = 0; j < size; j++){
        int id = hash(states[i].getId(), states[j].getId());
        output.addState(new ProductTState(id, states[i], states[j]));
      }
    
    // Computes the transitions
    for(int i = 0; i < size*size; i++){
      ProductTState origin = (ProductTState) output.getStates()[i];
      
      for(Transition ft : origin.getSubstates().first.getOutgoing())
        for(Transition st : origin.getSubstates().second.getOutgoing())
          if(((TTransition)ft).getInput().equals(((TTransition)st).getInput())){
            
            int id = hash(((TTransition)ft).getDestination().getId(), 
                ((TTransition)st).getDestination().getId());
            
            ProductTState dest = 
                (ProductTState) output.getStates()[id];
            
            ProductTransition transition = 
                new ProductTransition(origin, dest, 
                    ((TTransition)ft).getInput(), 
                        new Pair<String, String>(((TTransition)ft).getOutput(), 
                                                ((TTransition)st).getOutput()));
            
            origin.addOutgoing(transition);
            dest.addIngoing(transition);
          }
    }
    
    return output;
  }
  
  /**
   * Checks whether this <i>Transducer</i> is functionnal.
   * 
   * @return <i>true</i> if this <i>Transducer</i> is functionnal.
   */
  public boolean isFunctionnal(){
    
    Transducer u = this.square();
    u.trim();
    
    Stack<ProductTState> stack = new Stack<ProductTState>();
    
    for(int i = 0; i < u.getSize(); i++){
      
      ProductTState s = (ProductTState) u.getStates()[i];
      if(s.isInitial()){
        
        s.setMark(State.EXPLORED);
        s.setValue("", "");
        stack.push(s);
      }
    }
    
    while(!stack.isEmpty()){
      
      ProductTState current = stack.pop();
      
      for(Transition transition : current.getOutgoing()){
        
        ProductTransition t = (ProductTransition) transition;
        ProductTState next = (ProductTState) t.getDestination();
        
        Pair<String, String> delay = 
            computeDelay(current.getDelay(), t.getOutput());
        
        if(next.getMark() == State.UNEXPLORED){
          
          next.setMark(State.EXPLORED);
          next.setValue(delay);
          stack.push(next);
        }
        else if(!next.getDelay().equals(delay) || 
            (next.isAccepting() && 
                !next.getDelay().equals(new Pair<String, String>("", "")))){
          
          return false;
        }
      }
    }
      
    return true;
  }
  
  /**
   * Checks whether this <i>Transducer</i> is subsequential. <br /> 
   * This <i>Transducer</i> should be functionnal as this procedure doesn't 
   * check functionnality.
   * 
   * @return <i>true</i> if this <i>Transducer</i> is subsequential.
   */
  public boolean isSubsequential(){
    
    computeK();
    ProductTransducer t2 = this.square();
    t2.computeV();
    
    String[] tab1 = new String[size*size];
    String[] tab2 = new String[size*size];
    State[] t2States = t2.getStates();
    Stack<State> stack = new Stack<State>();
    
    for(int i = 0; i < t2States.length; i++){
      
      tab1[i] = "";
      tab2[i] = "";
      if(((ProductTState)t2States[i]).isInitial())
        stack.push(t2States[i]);
    }
    
    while(!stack.isEmpty()){
      
      ProductTState current = (ProductTState) stack.pop();
      
      current.setMark(State.EXPLORED);
      
      for(Transition tr : current.getOutgoing()){
        
        ProductTransition t = (ProductTransition) tr;
        ProductTState dest = (ProductTState) t.getDestination();
        
        // Only consider states in V'
        if(dest.isCoaccessible()){
          
          Pair<String, String> delay = computeDelay(new Pair<String, String>("",
              tab1[current.getId()]), t.getOutput());
                 
          if(delay == null)
            return false;
          else if(delay.first.length() > K*size*size || 
              delay.second.length() > K*size*size)
            return false;
          else if(dest.getMark() != State.EXPLORED){
            
            if(delay.first.equals(""))
              tab1[dest.getId()] = delay.second;
            else
              tab2[dest.getId()] = delay.first;
            
            stack.push(dest);
          }
          else if(dest.getMark() == State.EXPLORED){
            
            Pair<String, String> d;
            
            if(delay.first.equals(""))
              d = computeDelay(
                  new Pair<String, String>("", tab1[dest.getId()]), delay);
            else
              d = computeDelay(
                  new Pair<String, String>(tab2[dest.getId()], ""), delay);
            
            if(d == null)
              return false;

            if(!delay.equals(new Pair<String, String>("",tab1[dest.getId()]))){
              
              if(delay.second.length() > tab1[dest.getId()].length())
                tab1[dest.getId()] = delay.second;
              
              stack.push(dest);
            }
          }
          
          delay = computeDelay(new Pair<String, String>(tab2[current.getId()], 
              ""), t.getOutput());
          
          if(delay == null)
            return false;
          else if(delay.first.length() > K*size*size || 
              delay.second.length() > K*size*size)
            return false;
          else if(dest.getMark() != State.EXPLORED){
            
            if(delay.first.equals(""))
              tab1[dest.getId()] = delay.second;
            else
              tab2[dest.getId()] = delay.first;
            
            stack.push(dest);
          }
          else if(dest.getMark() == State.EXPLORED){
            
            Pair<String, String> d;
            
            if(delay.first.equals(""))
              d = computeDelay(
                  new Pair<String, String>("", tab1[dest.getId()]), delay);
            else
              d = computeDelay(
                  new Pair<String, String>(tab2[dest.getId()], ""), delay);
            
            if(d == null)
              return false;

            // If the cycle increased the value, continue the dfs
            if(!delay.equals(new Pair<String, String>(tab2[dest.getId()],""))){
              
              if(delay.first.length() > tab2[dest.getId()].length())
                tab2[dest.getId()] = delay.first;
              
              stack.push(dest);
            }    
          }
        }
      }
    } 
    
    return true;
  }
  
  public Transducer determinize() throws Exception{
    
    computeK();
    
    // Create the initial state
    List<MetaState> newStates = new ArrayList<MetaState>();
    MetaState initial = new MetaState(0);
    
    for(int i = 0; i < size; i++)
      if(((TState) states[i]).isInitial())
        initial.addSubstate((TState) states[i], "");
    
    initial.setInitial(true);
    newStates.add(initial);
    
    computeNextState(newStates, initial);
    
    // Create the output transducer
    Transducer output = new Transducer(newStates.size());
    
    for(int i = 0; i < newStates.size(); i++){
      
      MetaState s = newStates.get(i);
      
      for(Pair<TState, String> p : s.getSubstates())
        if(p.first.isAccepting()){
          s.setAccepting(true);
          if(p.second != null)
            s.setOutput(p.second);
        }
      
      output.addState(s);
    }
    
    return output;
  }
  
  private void computeNextState(List<MetaState> newStates, MetaState current) 
      throws Exception{
    
    List<TTransition> outTransitions = new ArrayList<TTransition>();
    
    for(Pair<TState, String> p : current.getSubstates())
      for(Transition t : p.first.getOutgoing())
        outTransitions.add((TTransition) t);
    
    while(outTransitions.size() > 0){
      
      String input = outTransitions.get(0).getInput();
      MetaState next = new MetaState(newStates.size());
      
      Iterator<TTransition> it = outTransitions.iterator();
      
      List<Pair<TState, String>> r = new ArrayList<Pair<TState, String>>();
      
      while(it.hasNext()){
        
        TTransition p = it.next();
        
        if(p.getInput().equals(input)){
          
          for(Pair<TState, String> s : current.getSubstates()){
            
            if(s.first.equals(p.getOrigin())){
              
              boolean dup = false;
              
              for(Pair<TState, String> temp : r)
                if(temp.first.equals(p.getDestination())){
                  dup = true;
                  if(temp.second.length() < s.second.length() + p.getOutput().length())
                    temp.second = s.second + p.getOutput();
                }
              if(!dup)
                r.add(new Pair<TState, String>((TState) p.getDestination(), 
                  s.second + p.getOutput()));
              
            }
          }
          
          it.remove(); // Remove used transitions from the pool
        }
      }
      
      // Compute longest common prefix amongst the yet-to-write outputs...
      String[] outputs = new String[r.size()];
      for(int i = 0; i < r.size(); i++)
        outputs[i] = r.get(i).second;
      
      // ...and remove it, the lcp will be the output of the outgoing transition
      String lcp = lcp(outputs);
      for(Pair<TState, String> p : r){
        
        p.second = p.second.replaceFirst(lcp, "");
        
        if(p.second.length() > K * size * size)
          throw new Exception("This transducer isn't subsequential");
      }
      // The next state receive R as substates
      next.setSubstates(r);
      boolean duplicate = false;
      
      // If next already exists, then add a transition from current to it
      for(State s : newStates)
        if(next.equals((MetaState) s)){
          duplicate = true;
          current.addOutgoing(new TTransition(current, s, input, lcp));
          s.addIngoing(new TTransition(current, next, input, lcp));
          break;
        }
      
      if(!duplicate){
        
        newStates.add(next);
        current.addOutgoing(new TTransition(current, next, input, lcp));
        next.addIngoing(new TTransition(current, next, input, lcp));
        computeNextState(newStates, next);
      }
    }
  }
  
  private void computeK(){
    
    int newK;
    
    for(int i = 0; i < size; i++)
      for(Transition t : states[i].getOutgoing())
        if((newK = ((TTransition) t).getOutput().length()) > K)
          K = newK;
  }
  
  // Computes the value between to pairs of words
  // Return null if the pairs are not comparable
  private static Pair<String, String> computeDelay(Pair<String, String> p1, 
                                                   Pair<String, String> p2){
    
    if(p1 == null || p2 == null)
      return null;
    
    Pair<String, String> delay = 
        new Pair<String, String>(p1.first + p2.first, p1.second + p2.second);
    
    String lcp = lcp(delay);
    delay = new Pair<String, String>(delay.first.replaceFirst(lcp, ""), 
                                     delay.second.replaceFirst(lcp, ""));
    
    // If not comparable
    if(!delay.first.equals("") && !delay.second.equals(""))
      return null;
    
    return delay;
  }
  
  // Computes longest common prefix
  private String lcp(String[] words){
    
    String lcp = "";
    int size = Integer.MAX_VALUE;
    int i;
      
    for(i = 0; i < words.length; i++)
      if(words[i].length() < size)
        size = words[i].length();
    
    for(i = 0; i < size; i++){
      
      boolean stop = false;
      
      for(int j = 0; j < words.length; j++){
        if(words[j].charAt(i) != words[0].charAt(i)){
          
          stop = true;
          break;
        }
      }
      
      if(stop)
        break;
    }
    lcp = words[0].substring(0, i);
    
    return lcp;
  }
  
  // Computes longest common prefix
  private static String lcp(Pair<String, String> pair){
    
    String lcp = "";
    int size = Math.min(pair.first.length(), pair.second.length());
    int i;
    
    for(i = 0; i < size; i++)
      if(pair.first.charAt(i) != pair.second.charAt(i))
        break; 
    
    lcp = pair.first.substring(0, i);
    return lcp;
  }
  
  // Computes the index of the state (a,b)
  private int hash(int a, int b){
    
    return a * size + b;
  }
}

class MetaState extends TState{
  
  private List<Pair<TState, String>> substateSet;
  
  public MetaState(int id){
    
    super(id);
    
    substateSet = new ArrayList<Pair<TState, String>>();
  }
  
  public void addSubstate(TState state, String out){
    
    substateSet.add(new Pair<TState, String>(state, out));
  }
  
  public void setSubstates(List<Pair<TState, String>> substates){
    
    substateSet = substates;
  }

  public List<Pair<TState, String>> getSubstates(){
    
    return substateSet;
  }

  public boolean equals(MetaState other){
    
    boolean equal = true;
    
    if(substateSet.size() != other.getSubstates().size())
      return false;
    
    // For every substate in this, check if it's in other
    for(Pair<TState, String> p : substateSet){
      
      equal = false;
      for(Pair<TState, String> s : other.getSubstates()){

        if(p.first.getId() == s.first.getId() && p.second.equals(s.second)){
          equal = true;
          break;
        }
      }
      if(!equal)
        break;
      }
    
    return equal;
  }
}
