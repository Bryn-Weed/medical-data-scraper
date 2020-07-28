package scraper;
/**
 * Created by scottjohnturner on 17/11/2016.
 * Edited by Bryn Weed on 02/01/2018.
 */
//Taken from various sources, including: http://labe.felk.cvut.cz/~xfaigl/mep/xml/java-xml.htm

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class urlreader {
    private static ArrayList<String> name=new ArrayList<String>();
    private static ArrayList<String> height=new ArrayList<String>();
    private static ArrayList<String> weight=new ArrayList<String>();
    private static ArrayList<String> jobTitle=new ArrayList<String>();
    private static ArrayList<String> condition=new ArrayList<String>();
    private static ArrayList<String> classification=new ArrayList<String>();
    private static ArrayList<String> diagnosis=new ArrayList<String>();
 

    public static void main(String[] args) throws Exception {
        URL[] item=new URL[4];
        item[0] = new URL("file:///University/Year 3/AI/public_html/person1.html");
        item[1] = new URL("file:///University/Year 3/AI/public_html/person2.html");
        item[2] = new URL("file:///University/Year 3/AI/public_html/person3.html");
        item[3] = new URL("file:///University/Year 3/AI/public_html/person4.html");
        //array for multiple website / file searching
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
       

        for(int loop1=0;loop1<4;loop1++){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            item[loop1].openStream()));
            String inputLine;
            int size1 = 0,size2=0;
           
            while ((inputLine = in.readLine()) != null)//reads in the html page line by line
            {
                String inputLine11=inputLine.replaceAll("\"", "");//removes speechmarks from text
                String inputLine21=inputLine11.replaceAll("<strong>", "");
                String inputLine2=inputLine21.replaceAll("</strong>", "");
                String[] parts = inputLine2.split("<span itemprop=");//splits into sections based on it is after <span class="
                for (int loop=0;loop<parts.length;loop++)
                {
                    if (parts[loop].contains("name>"))
                    {
                        name.add(parts[loop].substring(5, (parts[loop].length())-11));
                    }
                    size1=name.size();
                    if (parts[loop].contains("height>"))
                    {
                        height.add(parts[loop].substring(7, (parts[loop].length())-11));

                    }
                    if (parts[loop].contains("weight>"))
                    {
                        weight.add(parts[loop].substring(7, (parts[loop].length())-11));

                    }
                    size2=weight.size();
                    if (parts[loop].contains("jobTitle>"))
                    {
                    	jobTitle.add(parts[loop].substring(9, (parts[loop].length())-11));
                    }
                    if (parts[loop].contains("healthCondition>"))
                    {
                    	condition.add(parts[loop].substring(16, (parts[loop].length())-11));
                    }

                }
                //Scraping info via the metadata tag


            }in.close();
            if (size2<size1)
            {
                weight.add(null);
            }
        }
        
     
        
      ArrayList<Double> heightInt = getDoubleArray(height);
      ArrayList<Double> weightInt = getDoubleArray(weight);
     // Calling method to convert string arraylist to double format
      ArrayList<Double> bmiList=new ArrayList<Double>();
     
   
      
      for (int i = 0; i < heightInt.size(); i++){
    	 double calculatedValue = (weightInt.get(i) / heightInt.get(i) / heightInt.get(i));
    	 // calculate BMI
    	 double formattedValue = (double) Math.round(calculatedValue * 100d) / 100d;
    	 // format BMI to 2 decimal points
          bmiList.add(formattedValue);
          // add to new arrayList
          if (formattedValue < 18.5) {
        	  classification.add("Underweight");
          }
          else if (formattedValue > 18.5 && formattedValue < 24.9){
        	  classification.add("Normal weight");
          }
          else if (formattedValue > 25.0 && formattedValue < 29.9){
        	  classification.add("Overweight");
          }
          else if (formattedValue > 30.0 && formattedValue < 34.9){
        	  classification.add("Class 1 obesity");
          }
          else if (formattedValue > 35.0 && formattedValue < 39.9){
        	  classification.add("Class 2 obesity");
          }
          else if (formattedValue >= 40.0){
        	  classification.add("Class 3 obesity");
          }
          else {
        	  classification.add("No BMI detected");
          }
          //populating new arrayList with BMI classifications based on the double value
      }
      
      for (int i = 0; i < condition.size(); i++){
    	  if (condition.get(i).contains("Headache") || condition.get(i).contains("Nausea")){
    		  diagnosis.add("Migraine");
    	  }
    	  else if (condition.get(i).contains("Fever")){
    		  diagnosis.add("Flu");
    	  }
      }
      // populate diagnosis arraylist from condition arraylist
        
        System.out.println("Extracted Name "+name); 
        System.out.println("Extracted Height(m): "+height);  
        System.out.println("Extracted Weight(kg): "+weight);
        System.out.println("Extracted Job Title: "+jobTitle);
        System.out.println("Calculated BMI: " +bmiList );
        System.out.println("BMI classification: " +classification);
        System.out.println("Extracted Conditions: " +condition);
        System.out.println("Potential Diagnosis: " +diagnosis);
        //printing arrayLists
        
        
        List<String> bmiString = new ArrayList<String>();
        for (Double d : bmiList){
        	bmiString.add(d.toString());//converting double to string in array list
        }
        try {
            /////////////////////////////
            //Creating an empty XML Document




            //Creating the XML tree

            Element root = doc.createElement("hcards");
            doc.appendChild(root);
            
         

            for (int loopx=0;loopx<4;loopx++)
            {
                Element hcard=doc.createElement("hcard");
                root.appendChild(hcard);

                Element name1 = doc.createElement("name");
                hcard.appendChild(name1);

                //add a text element to the child
                Text text = doc.createTextNode(name.get(loopx));
                name1.appendChild(text);

          
                Element name2 = doc.createElement("height");
                hcard.appendChild(name2);
                //add a text element to the child
                Text text2 = doc.createTextNode(height.get(loopx));
                name2.appendChild(text2);
               

                if(weight.get(loopx)!=null)
                {
                    Element name3 = doc.createElement("weight");
                    hcard.appendChild(name3);
                    //add a text element to the child
                    Text text3 = doc.createTextNode(weight.get(loopx));
                    name3.appendChild(text3);
                }
                
                Element name4 = doc.createElement("jobTitle");
                hcard.appendChild(name4);
                //add a text element to the child
                Text text4 = doc.createTextNode(jobTitle.get(loopx));
                name4.appendChild(text4);
                
                Element name5 = doc.createElement("BMI");
                hcard.appendChild(name5);
                //add a text element to the child
                Text text5 = doc.createTextNode(bmiString.get(loopx));
                name5.appendChild(text5);
                
                Element name7 = doc.createElement("classification");
                hcard.appendChild(name7);
                //add a text element to the child
                Text text7 = doc.createTextNode(classification.get(loopx));
                name7.appendChild(text7);
                
                Element name6 = doc.createElement("condition");
                hcard.appendChild(name6);
                //add a text element to the child
                Text text6 = doc.createTextNode(condition.get(loopx));
                name6.appendChild(text6);
                
                Element name8 = doc.createElement("diagnosis");
                hcard.appendChild(name8);
                //add a text element to the child
                Text text8 = doc.createTextNode(diagnosis.get(loopx));
                name8.appendChild(text8);
                //Output the XML
            }
            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();

            //print xml
            System.out.println("Here's the xml:\n\n" + xmlString);

        } catch (Exception e) {
            System.out.println(e);

        }
        saveXMLDocument("example2.xml", doc);
    }
    
    private static ArrayList<Double> getDoubleArray(ArrayList<String> stringArray) {
        ArrayList<Double> result = new ArrayList<Double>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Double, and store it into double array list.
                result.add(Double.parseDouble(stringValue));
            } catch(NumberFormatException nfe) {
            	
               System.out.println("Could not parse " + nfe);
               
            } 
        }       
        return result;
    }
    
    

    @SuppressWarnings("resource")
	public static boolean saveXMLDocument(String fileName, Document doc) {
        System.out.println("Saving XML file... " + fileName);
        // open output stream where XML Document will be saved
        File xmlOutputFile = new File(fileName);
        FileOutputStream fos;
        Transformer transformer;
        try {
            fos = new FileOutputStream(xmlOutputFile);
        }
        catch (FileNotFoundException e) {
            System.out.println("Error occured: " + e.getMessage());
            return false;
        }
        // Use a Transformer for output
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformer = transformerFactory.newTransformer();
        }
        catch (TransformerConfigurationException e) {
            System.out.println("Transformer configuration error: " + e.getMessage());
            return false;
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fos);
        // transform source into result will do save
        try {
            transformer.transform(source, result);
        }
        catch (TransformerException e) {
            System.out.println("Error transform: " + e.getMessage());
        }
        System.out.println("XML file saved.");
        return true;
    }
}