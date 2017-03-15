/*
 * XML Editor
 * 2015.05.19
 * Written by JiHun Choi, YoungJae Kwon 
 */

package xml;

//JAXP API's
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;
import java.io.*; 

//W3C definition for DOM and DOM Exceptions
import org.w3c.dom.*;

//identify errors
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import javax.swing.*;
import element.*;

public class XMLEditer {
	private Document XMLDoc; //★Document Type Html or XML Type ▶XML을 사용하기위해 Document 사용.
	
	// All element names except elements for components	▶리터럴로 변환.

	static public String E_AVENGERSGAME = "PoketMonsterGame";
	static public String E_MAPSET = "MapSet";
	static public String E_STAGE = "Stage";
	static public String E_BACKGROUND = "Background";
	static public String E_BLOCK = "Block";
	static public String E_OBJ = "Obj";

	//reference
	private Node avengersGameElement=null;
	private Node mapSetElement=null;
	private Node stageElement = null;
	private Node backgroundElement = null;
	private Node blockElement = null;
	private Node objElement = null;

	//getter method
	public Node getAvengersGameElement() {return avengersGameElement;}
	public Node getMapSetElement() {return mapSetElement;}
	public Node getStageElement() {return stageElement;}
	public Node getBackgroundElement() {return backgroundElement;}
	public Node getBlockElement() {return blockElement;}
	public Node getObjElement() {return objElement;}

	public XMLEditer(String XMLFile) {
		read(XMLFile); 		// XMLFile을 읽어 파싱하고 XMLDoc를 생성한다.
		process(XMLDoc);	// 
	}
	public void write(String XMLFile, MapSet mapSet)  {
		Node avengers = XMLDoc.getFirstChild();			//avengers Tag
		Element MapSetNode = XMLDoc.createElement("MapSet");		
		//MapSet
		MapSetNode.setAttribute("name", mapSet.getName());//<- getName Method만들기.
		MapSetNode.setTextContent("\n");
		//Stage
		for(int i=0; i<mapSet.getStage().size(); i++){
			Element StageNode = XMLDoc.createElement("Stage");
			StageNode.setAttribute("level",Integer.toString(mapSet.getStage().get(i).getLevel()));
			StageNode.setAttribute("time",Integer.toString(mapSet.getStage().get(i).getTime()));
			StageNode.setAttribute("backgroundType",Integer.toString(mapSet.getStage().get(i).getBackgroundType()));
			//Block
			for(int j=0; j<mapSet.getStage().get(i).getBlock().size(); j++){
				Element BlockNode = XMLDoc.createElement("Block");
				BlockNode.setAttribute("x", Integer.toString(mapSet.getStage().get(i).getBlock().get(j).getX()));
				BlockNode.setAttribute("y", Integer.toString(mapSet.getStage().get(i).getBlock().get(j).getY()));
				BlockNode.setAttribute("width",Integer.toString(mapSet.getStage().get(i).getBlock().get(j).getWidth()));
				BlockNode.setAttribute("height",Integer.toString(mapSet.getStage().get(i).getBlock().get(j).getHeight()));
				switch (mapSet.getStage().get(i).getBlock().get(j).getClass().getName()) {
				case "element.Block":
					BlockNode.setAttribute("kind", "normal");
					break;
				case "element.ItemBlock":
					BlockNode.setAttribute("kind", "item");
					break;
				case "element.SpecialBlock":					
					BlockNode.setAttribute("kind", "special");
					break;
				}								
				BlockNode.setAttribute("type",Integer.toString(mapSet.getStage().get(i).getBlock().get(j).getType()));
				StageNode.appendChild(BlockNode);
			}
			MapSetNode.appendChild(StageNode);
		}
		//Append

	
		avengers.appendChild(MapSetNode);	
		//Write XML File.
		try{
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			DOMSource source = new DOMSource(XMLDoc);
			StreamResult result = new StreamResult(new File(XMLFile));
			trans.transform(source, result);
		}catch (Exception e) {			
		};
		read(XMLFile);
		process(XMLDoc);
		System.out.println("DONE!");
	}
	// XMLFile을 읽고 파싱하여 XMLDoc 객체 생성
	private void read(String XMLFile) {
		DocumentBuilderFactory      factory=null;
	    DocumentBuilder             builder=null;

		factory = DocumentBuilderFactory.newInstance();

		// set  configuration options
//		factory.setValidating(true);
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		try {
		    builder = factory.newDocumentBuilder();

			// set error handler before parse() is called
			// if validation signal is set on, error handler should be attached
			OutputStreamWriter errStreamWriter = new OutputStreamWriter(System.err, "UTF-8");
	    	builder.setErrorHandler(new XMLBuilderErrorHandler(new PrintWriter(errStreamWriter, true)));

			File f = new File(XMLFile);
		    XMLDoc = builder.parse(f);	//Passing 부분 ★
		}
		catch (SAXException sxe) {		//XML잘못만들면 Exception 발생★
		    // Error generated during parsing
		    Exception  x = sxe;
		    if (sxe.getException() != null)
				x = sxe.getException();
		    x.printStackTrace();
		}
		catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();
		}
		catch (IOException ioe) {
			// I/O error
		    ioe.printStackTrace();
		} // end of try-catch block
		
	}
	
	public void process(Node parentNode) {
        for (Node node = parentNode.getFirstChild(); node != null;
             node = node.getNextSibling()) { // parentNode의 일차 자식들 검색
			if(node.getNodeType() != Node.ELEMENT_NODE)	//element아니면 continue; (text부분 이면)
				continue; // we search for element nodes
		    if(node.getNodeName().equals(E_AVENGERSGAME))	
				avengersGameElement = node;
		    else if(node.getNodeName().equals(E_MAPSET)) {
		    	mapSetElement = node;
		    }
		    else if(node.getNodeName().equals(E_STAGE)) {
		    	stageElement = node;
		    }
		    else if(node.getNodeName().equals(E_BACKGROUND)) {
		    	backgroundElement = node;
		    }
		    else if(node.getNodeName().equals(E_BLOCK)) {
		    	blockElement = node;
		    }
		    else if(node.getNodeName().equals(E_OBJ)) {
		    	objElement = node;
		    }
			printNode(node);
			process(node); // recursion
        }
	} // end of method

	static public Node getNode(Node parentNode, String nodeName) {
		Node node = null;
		for (node = parentNode.getFirstChild(); node != null;
           node = node.getNextSibling()) {
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue; // we search for element nodes
		    if(node.getNodeName().equals(nodeName))
				return node;
		    else {
		    	Node n = getNode(node, nodeName);
		    	if(n != null)
		    		return n;
		    }
		}
		return node;	
	}
	
	static public String getAttr(Node element, String attrName)
	{
		// get a list of Atribute Nodes
		NamedNodeMap attrs = element.getAttributes();
		for(int i=0; i<attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			if(name.equals(attrName)) {
				return attr.getNodeValue();
			}
		}// end of for

		return null; // error or default
	}
	
	void printNode(Node element) {
		// print node name
		//System.out.print(element.getNodeName()+ " ");
		
		// print all attrs
		
		// get a list of Atribute Nodes
		NamedNodeMap attrs = element.getAttributes();
		
		for(int i=0; i<attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			String value = attr.getNodeValue();
			//System.out.print(name + "=" + value +" ");			
		}// end of for	
		
		//String text = element.getTextContent();
		//System.out.println(text);
		//System.out.println();
	}
	
	// Error handler to report errors and warnings
	class XMLBuilderErrorHandler implements ErrorHandler
	{
		/** Error handler output goes here */
		private PrintWriter out;

		XMLBuilderErrorHandler(PrintWriter out) {
	            this.out = out;
		}

		private String getParseExceptionInfo(SAXParseException spe) {
			String systemId = spe.getSystemId();
		    if (systemId == null) {
			    systemId = "null";
			}
			String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();
			return info;
		}

		// The following methods are three standard SAX ErrorHandler methods.
		public void warning(SAXParseException spe) throws SAXException {
			out.println("Warning: " + getParseExceptionInfo(spe));
		}

		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
		    throw new SAXException(message);
		}

		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}
	} // end of class
}

