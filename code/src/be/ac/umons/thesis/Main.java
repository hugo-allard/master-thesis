package be.ac.umons.thesis;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import be.ac.umons.thesis.io.Reader;
import be.ac.umons.thesis.io.Writer;

import be.ac.umons.thesis.transducer.Transducer;

/**
 * 
 * @author Hugo Allard
 *
 */
public class Main{
  
  public static void main(String[] args){

    String input = null;
    String output = null;
    
    if(args.length > 2){
      System.out.println("Too many arguments");
      System.exit(1);
    }
  
    if(args.length == 0){

      System.out.print("Enter a filename: ");
      Scanner keyboard = new Scanner(System.in);
      input = keyboard.nextLine();
      output = "doc/outputs/out.xml";
      keyboard.close();
    }
    else{
      
      input = args[0];
      
      if(args.length == 2)
        output = args[1];
      else
        output = "doc/outputs/out.xml";
    }


    Transducer transducer = null;
    
    try{
      
      XMLReader saxReader = XMLReaderFactory.
          createXMLReader("org.apache.xerces.parsers.SAXParser");
      saxReader.setContentHandler(new Reader());
      saxReader.parse(input);
      transducer = ((Reader) saxReader.getContentHandler()).getInput();
    }
    catch(FileNotFoundException e){
      
      System.out.println("File \"" + input + "\" not found");
    }
    catch(Exception e){
      
      System.out.println(e);
    }
    
    System.out.println("Checking functionnality");
    
    if(transducer.isFunctionnal())
      System.out.println("  Input transducer is functionnal");
    else{
      System.out.println("  Input transducer is not functionnal");
      System.exit(0);
    }
    System.out.println("Checking subsequentiality. " );
    
    if(transducer.isSubsequential())
      System.out.println("  Input transducer is subsequential");
    else{
      System.out.println("  Input transducer is not subsequential");
      System.exit(0);
    }
    
    Transducer ss = null;
    
    try{
      System.out.println("Determinizing transducer");
      ss = transducer.determinize();
    } catch(Exception e){
      System.out.println(e);
      System.exit(0);
    }
    
    try{
      System.out.println("  Writing output in " + output);
      Writer.write(ss, output, Writer.TRANSDUCER);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (FactoryConfigurationError e) {
      e.printStackTrace();
    }
  }
}