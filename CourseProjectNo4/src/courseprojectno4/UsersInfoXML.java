package courseprojectno4;

import courseprojectno4.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


/**
 * CourseProjectNo4: UsersInfoXML.java
 * Write the information for the users(user name, password and permissions)
 * in a XML file
 */
public class UsersInfoXML {
    private Formatter out;
    
    /**
     * Create a XML content with the information of the users (user name, 
     * password and permissions), create a XML file and write the content there
     * @param users 
     */
    public void writeXML(ArrayList<User> users){        
        try{
            String xmlString;
            StringWriter stringWriter = new StringWriter();
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlStreamWriter = 
                    outputFactory.createXMLStreamWriter(stringWriter);
            
            xmlStreamWriter.writeStartDocument();
            xmlStreamWriter.writeStartElement("users");
            
            for (User user : users) {
                createSimpleElement(xmlStreamWriter, "userName", 
                        user.getUserName());
                createSimpleElement(xmlStreamWriter, "password", 
                        user.getPassword());
                createSimpleElement(xmlStreamWriter, "permissions", 
                        user.getUserLevel().toString());
            }
            
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
            
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
            
            xmlString = stringWriter.getBuffer().toString(); 
            createXMLDocument(xmlString);
            
            stringWriter.close();
        } catch(XMLStreamException xmlStreamException){
            System.err.println("Problem with the XML Stream occured.");
        } catch(IOException ioException){
            System.err.println("I/O problem occured.");
        }
    }
    
    /**
     * Create the XML file and write the XML content there
     * @param content 
     */
    private void createXMLDocument(String content){
        File xmlFile = new File("usersInfo.xml");
        
        try {
            xmlFile.createNewFile();
        } catch (IOException ex) {
            System.err.println("I/O exception");
        }
        xmlFile.canWrite();
        xmlFile.canRead();
        
        try {
            out = new Formatter("usersInfo.xml");
        } catch (FileNotFoundException ex) {
            System.err.println("File not found");
        } 
        
        try{
            out.format("%s%n", content);
        }catch(FormatterClosedException formatterClosedException){
            System.err.println("Formatter closed");
        } catch(NoSuchElementException elementException){
            out.close();
            System.err.println("No such element");
        } finally {
            if(out != null){
                out.close();
            }
        }
    }
    
    /**
     * Create a simple XML element with value
     * @param xmlStreamWriter
     * @param elementName
     * @param elementValue 
     */
    private void createSimpleElement(XMLStreamWriter xmlStreamWriter, 
            String elementName, String elementValue){
        try {
            xmlStreamWriter.writeStartElement(elementName);
            xmlStreamWriter.writeCharacters(elementValue);
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException xmlStreamException) {
            System.err.println("Problem with the XML Stream occured.");
        }
    }
}
