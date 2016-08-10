package be.ac.umons.thesis.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Hugo Allard
 *
 */
public class State{
  
  public static final short UNEXPLORED = 0;
  public static final short EXPLORED = 1;
  public static final short CURRENTLY_EXPLORED = 2;

  protected int id;
  protected int mark;
  protected List<Transition> ingoing;
  protected List<Transition> outgoing;

  public State(int id){
    
    this.id = id;
    this.mark = 0;
    this.ingoing = new ArrayList<Transition>();
    this.outgoing = new ArrayList<Transition>();
  }
  
  public void addIngoing(Transition in){
    
    ingoing.add(in);
  }
  
  public void addOutgoing(Transition out){
    
    outgoing.add(out);
  }
  
  public int getId(){
    
    return id;
  }

  public void setId(int id){
  
    this.id = id;
  }

  public int getMark(){
  
    return mark;
  }

  public void setMark(int marked){
  
    this.mark = marked;
  }

  public List<Transition> getIngoing(){
  
    return ingoing;
  }

  public void setIngoing(List<Transition> ingoing){
  
    this.ingoing = ingoing;
  }

  public List<Transition> getOutgoing(){
  
    return outgoing;
  }

  public void setOutgoing(List<Transition> outgoing){
  
    this.outgoing = outgoing;
  }
}
