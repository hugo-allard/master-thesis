package be.ac.umons.thesis.io;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import be.ac.umons.thesis.transducer.TState;
import be.ac.umons.thesis.transducer.TTransition;
import be.ac.umons.thesis.transducer.Transducer;

/**
 * 
 * @author Hugo Allard
 *
 */
public class Reader implements ContentHandler{
  
  private Transducer input;
  
  public Reader(){
    
    super();
  }
  
  public Transducer getInput(){
    
    return input;
  }
  
  @Override
  public void startDocument() throws SAXException{
  }
  
  @Override
  public void endDocument() throws SAXException{
  }
  
  @Override
  public void startElement(String nameSpaceURI, String localName, 
      String rawName, Attributes attributes) throws SAXException{
    
    if(localName.equalsIgnoreCase("transducer")){
      
      int size = 0;
      
      for (int i = 0; i < attributes.getLength(); i++)
        if(attributes.getLocalName(i).equalsIgnoreCase("size")){
          size = Integer.parseInt(attributes.getValue(i));
          break;
        }
      
      input = new Transducer(size);
    }
    
    else if(localName.equalsIgnoreCase("node")){
      
      int id = -1;
      String output = "";
      boolean accepting = false;
      boolean initial = false;
      
      for (int i = 0; i < attributes.getLength(); i++){
        
        if(attributes.getLocalName(i).equalsIgnoreCase("id"))
          id = Integer.parseInt(attributes.getValue(i));
        
        else if(attributes.getLocalName(i).equalsIgnoreCase("output"))
          output = attributes.getValue(i);
        
        else if(attributes.getLocalName(i).equalsIgnoreCase("initial")){
          if(attributes.getValue(i).equalsIgnoreCase("true"))
            initial = true;
        }
        else if(attributes.getLocalName(i).equalsIgnoreCase("accepting")){
          if(attributes.getValue(i).equalsIgnoreCase("true"))
            accepting = true;
        }
      }
      
      TState state = new TState(id, output);
      
      if(initial)
        state.setInitial(true);
      if(accepting)
        state.setAccepting(true);
      
      input.addState(state);
    }
    
    else if(localName.equalsIgnoreCase("transition")){
      
      int from = -1;
      int to = -1;
      String in = "";
      String out = "";
      
      for (int i = 0; i < attributes.getLength(); i++){
        
        if(attributes.getLocalName(i).equalsIgnoreCase("from"))
          from = Integer.parseInt(attributes.getValue(i));
        
        else if(attributes.getLocalName(i).equalsIgnoreCase("to"))
          to = Integer.parseInt(attributes.getValue(i));
        
        else if(attributes.getLocalName(i).equalsIgnoreCase("input"))
          in = attributes.getValue(i);
        
        else if(attributes.getLocalName(i).equalsIgnoreCase("output"))
          out = attributes.getValue(i);
      }
      
      TTransition transition = 
          new TTransition(input.getState(from), input.getState(to), in, out);
      input.getState(from).addOutgoing(transition);
      input.getState(to).addIngoing(transition);
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException{
    
  }

  @Override
  public void endElement(String uri, String localName, String qName) 
      throws SAXException{
    
  }

  @Override
  public void endPrefixMapping(String prefix) throws SAXException{
    
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) 
      throws SAXException{
    
  }

  @Override
  public void processingInstruction(String target, String data) 
      throws SAXException{
    
  }

  @Override
  public void skippedEntity(String name) throws SAXException{
    
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException{
    
  }

  @Override
  public void setDocumentLocator(Locator locator){
    
  }
}