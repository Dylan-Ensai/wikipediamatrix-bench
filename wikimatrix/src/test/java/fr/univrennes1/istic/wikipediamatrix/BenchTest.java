package fr.univrennes1.istic.wikipediamatrix;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import bean.Table;

public class BenchTest {
	
	/*
	*  the challenge is to extract as many relevant tables as possible
	*  and save them into CSV files  
	*  from the 300+ Wikipedia URLs given
	*  see below for more details
	**/
	@Test
	public void testBenchExtractors() throws Exception {
		
		String BASE_WIKIPEDIA_URL = "https://en.wikipedia.org/wiki/";
		// directory where CSV files are exported (HTML extractor) 
		String outputDirHtml = "output" + File.separator + "html" + File.separator;
		assertTrue(new File(outputDirHtml).isDirectory());
		// directory where CSV files are exported (Wikitext extractor) 
		String outputDirWikitext = "output" + File.separator + "wikitext" + File.separator;
		assertTrue(new File(outputDirWikitext).isDirectory());
		
		File file = new File("inputdata" + File.separator + "wikiurls.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
	    String url;
	   
	    
	    int nurl = 0;
	    boolean b = true;
	    while ((url = br.readLine()) != null && b) {
	       String wurl = BASE_WIKIPEDIA_URL + url; 
	       System.out.println("Wikipedia url: " + wurl);
	       
	       WikipediaHTMLExtractor wiki = new WikipediaHTMLExtractor(BASE_WIKIPEDIA_URL,
					outputDirHtml,outputDirWikitext);
	       if (testGetDocument(wiki, url)) {
	    	   try {
		    	   List<Table> listTable = wiki.ExtractUrlToCsv(url);
		    	   for (int i = 0; i < listTable.size(); i++) {
		    		   if (! testExtraction(wiki, listTable.get(i), url, i+1)) {
		    			   int j = i+1;
		    			   System.out.println("Le tableau " + j + " n'a pas réussi le test d'extraction");
		    			   b = false;
		    		   }
		    			   
		    	   }
		       }
		       catch (Exception e) {
		    	   
		    	   System.out.println(e.getMessage() + url);
		       }
	       }
	       else System.out.println("GetDocument falled");
	       
	       
	       
	       

	       nurl++;	       
	    }
	    
	    br.close();	    
	    assertEquals(nurl, 336);
	    



	}
	 
	private Boolean testGetDocument(WikipediaHTMLExtractor wiki, String url) {
		try {
			Document doc = wiki.getDocument(url);
			return true;	
		}
		catch (Exception e) {
			return false;
		}
		
		
	}
	 
	private Boolean testExtraction(WikipediaHTMLExtractor wiki, Table table, String url,int n) throws Exception {
		String csvName = mkCSVFileName(url,n);
		Table tableOut = wiki.readerCsv(csvName);
		return table.equals(tableOut);
	}
	

	private String mkCSVFileName(String url, int n) {
		return url.trim() + "-" + n + ".csv";
	}

}
