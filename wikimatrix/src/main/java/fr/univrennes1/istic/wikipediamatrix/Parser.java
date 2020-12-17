package fr.univrennes1.istic.wikipediamatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import bean.Table;

public class Parser {
	
	static Logger logger = Logger.getLogger("Parser");
	

	public static Table getTable(Element table){
		Table tableau = new Table();
		try {
			Elements lines = table.select("tr");
			// Header 
			Element firstTr = lines.get(0);
        	Elements cellulesHeader = firstTr.select("th,td");
        	List<String> header = new ArrayList<String>();
        	
        	for(Element td : cellulesHeader) {
        		header.add(td.text());
        	}
        	
        	tableau.setHeader(header.toArray(new String[0]));
        	
        	// Content
        	
        	
        	for(int i=1; i<lines.size(); i++) {
        		Element tr = lines.get(i);
        		
        		Elements cellules = tr.select("th,td");
        		List<String> line = new ArrayList<String>();
        		for (Element td : cellules) {
        			String text = td.text();
        			line.add(text);
                }
        		tableau.addLine(line.toArray(new String[0]));
        		
        	}
        
        	return tableau;
		}
		catch(Exception e) {
			logger.info(e.toString());
			return null;
		}
	}
	
}
