package be.ac.umons.thesis;

/**
 * Represents a generic pair.
 * 
 * @author Hugo Allard
 *
 */
public class Pair<T, S>{

  public T first;
  public S second;
  
  public Pair(T first, S second){
    
    this.first = first;
    this.second = second;
  }
  
  public boolean equals(Pair<T, S> other){
    
    return this.first.equals(other.first) && this.second.equals(other.second);
  }
}
