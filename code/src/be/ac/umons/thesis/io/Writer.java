package be.ac.umons.thesis.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import be.ac.umons.thesis.automata.AState;
import be.ac.umons.thesis.automata.ATransition;
import be.ac.umons.thesis.graph.Graph;
import be.ac.umons.thesis.graph.State;
import be.ac.umons.thesis.graph.Transition;
import be.ac.umons.thesis.transducer.TState;
import be.ac.umons.thesis.transducer.TTransition;

/**
 * 
 * @author Hugo Allard
 *
 */
public enum Writer{
  
  GRAPH("graph", true, false, false, false, true, true, false, false),
  AUTOMATON("automaton", true, true, true, false, true, true, true, false),
  TRANSDUCER("transducer", true, true, true, true, true, true, true, true);
  
  public String type;
  public boolean nId, nInitial, nAccepting, nOutput; // Nodes spec
  public boolean tFrom, tTo, tInput, tOutput; // Transitions spec
   
  Writer(String type, boolean nId, boolean nInitial, boolean nAccepting, 
      boolean nOutput, boolean tFrom, boolean tTo, boolean tInput, 
      boolean tOutput){
    
    this.type = type;
    this.nId = nId;
    this.nInitial = nInitial;
    this.nAccepting = nAccepting;
    this.nOutput = nOutput;
    this.tFrom = tFrom;
    this.tTo = tTo;
    this.tInput = tInput;
    this.tOutput = tOutput;
  }

  public static void write(Graph output, String file, Writer o) 
      throws XMLStreamException, UnsupportedEncodingException, 
      FactoryConfigurationError, FileNotFoundException{
    
    OutputStream outputStream = new FileOutputStream(new File(file));
    XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
    
    out.writeStartDocument();
    out.writeCharacters(System.getProperty("line.separator"));
    
    out.writeStartElement(o.type);
    out.writeAttribute("", "", "size", ""+output.getSize());
    out.writeAttribute("", "", "xmlns:xsi", 
        "http://www.w3.org/2001/XMLSchema-instance");
    out.writeAttribute("", "", "xsi:noNamespaceSchemaLocation", "schema.xsd");
    
    out.writeCharacters(System.getProperty("line.separator"));
    out.writeCharacters(System.getProperty("line.separator"));
    out.writeCharacters("  ");
    out.writeStartElement("nodes");
    out.writeCharacters(System.getProperty("line.separator"));
    
    for(int i = 0; i < output.getSize(); i++){
      
      State current = output.getState(i);
      
      out.writeCharacters("    ");
      out.writeEmptyElement("node");
      
      if(o.nId)
        out.writeAttribute("", "", "id", ""+current.getId());
      
      if(o.nInitial)
        if(((AState) current).isInitial())
          out.writeAttribute("", "", "initial", "true");
      
      if(o.nAccepting)
        if(((AState) current).isAccepting())
          out.writeAttribute("", "", "accepting", "true");

      if(o.nOutput)
        if(!((TState) current).getOutput().equals(""))
          out.writeAttribute("", "", "output", ((TState) current).getOutput());
      
      out.writeCharacters(System.getProperty("line.separator"));
    }
    
    out.writeCharacters("  ");
    out.writeEndElement();
    out.writeCharacters(System.getProperty("line.separator"));
    out.writeCharacters(System.getProperty("line.separator"));
    out.writeCharacters("  ");
    out.writeStartElement("transitions");
    out.writeCharacters(System.getProperty("line.separator"));
    
    for(int i = 0; i < output.getSize(); i++){
      
      for(Transition t : output.getState(i).getOutgoing()){
        
        out.writeCharacters("    ");
        out.writeEmptyElement("transition");
        
        if(o.tFrom)
          out.writeAttribute("", "", "from", ""+t.getOrigin().getId());
        
        if(o.tTo)
          out.writeAttribute("", "", "to", ""+t.getDestination().getId());
        
        if(o.tInput)
          out.writeAttribute("", "", "input", ((ATransition) t).getInput());
          
        if(o.tOutput)
          out.writeAttribute("", "", "output", ((TTransition) t).getOutput());
        
        out.writeCharacters(System.getProperty("line.separator"));
      }
    }
    
    out.writeCharacters("  ");
    out.writeEndElement();
    out.writeCharacters(System.getProperty("line.separator"));
    out.writeEndElement();
    out.writeCharacters(System.getProperty("line.separator"));
    out.writeEndDocument();

    out.close();
  }
}
