package be.ac.umons.thesis.automata;

import be.ac.umons.thesis.graph.State;

/**
 * 
 * @author Hugo Allard
 *
 */
public class AState extends State{

  protected boolean initial;
  protected boolean accepting;
  protected boolean accessible;
  protected boolean coaccessible;
  
  public AState(int id){
    
    super(id);
  }
  
  public AState(int id, boolean initial, boolean accepting){
    
    super(id);
    this.initial = initial;
    this.accepting = accepting;
  }

  public boolean isInitial(){
  
    return initial;
  }

  public void setInitial(boolean initial){
  
    this.initial = initial;
  }

  public boolean isAccepting(){
  
    return accepting;
  }

  public void setAccepting(boolean accepting){
  
    this.accepting = accepting;
  }

	public boolean isAccessible(){
		
		return accessible;
	}
	
	public void setAccessible(boolean accessible){
		
		this.accessible = accessible;
	}
	
	public boolean isCoaccessible(){
		
		return coaccessible;
	}
	
	public void setCoaccessible(boolean coaccessible){
		
		this.coaccessible = coaccessible;
	}
}
