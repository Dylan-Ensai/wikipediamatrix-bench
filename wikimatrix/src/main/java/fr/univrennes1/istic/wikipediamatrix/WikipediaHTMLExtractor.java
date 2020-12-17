package fr.univrennes1.istic.wikipediamatrix;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import bean.Table;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WikipediaHTMLExtractor {
	
	private String BASE_WIKIPEDIA_URL;
	private String outputDirHtml;
	private String outputDirWikitext;
	
	public WikipediaHTMLExtractor(String BASE_WIKIPEDIA_URL, String outputDirHtml, String outputDirWikitext) {
		this.BASE_WIKIPEDIA_URL = BASE_WIKIPEDIA_URL;
		this.outputDirHtml = outputDirHtml;
		this.outputDirWikitext = outputDirWikitext;		
	}

	

	public Document getDocument(String url) throws IOException {
		Document doc = Jsoup.connect(BASE_WIKIPEDIA_URL + url).get();
		return doc;
	}
	
	
	public Boolean isWikitable(Element table) {
		if ( table.className().contains("wikitable") ) {
			return true;
		}
		return false;
	}
	
	public Boolean ContainSpan(Element table) {
		for(Element td : table.select("td,th")) {
			if (td.hasAttr("colspan") || td.hasAttr("rowspan") )
				return true;
		}
		for(Element tr : table.select("tr")) {
			if (tr.hasAttr("rowspan") || tr.hasAttr("colspan"))
				return true;
		}
		return false;
		
	}
	
	public Boolean ContainTable(Element table) {
		if (table.children().select("table").isEmpty()){
			return false;
		}
		
		return true;
	}
	
	public String mkCSVFileName(String url, int n) {
		return url.trim() + "-" + n + ".csv";
	}
	
	public List<Table> ExtractUrlToCsv(String url) throws IOException{
		Document doc = getDocument(url);
		Elements tables = doc.select("table");
		List<Table> listTable = new ArrayList<Table>();
		int n = 1;
		for (int i = 0; i < tables.size(); i++) {
			Element table = tables.get(i);
			if (isWikitable(table) && ! ContainSpan(table) && ! ContainTable(table)) {
				
				Table tableau = Parser.getTable(table);
	        	if (tableau.nbColonne() == 1) {
	        		System.out.println(mkCSVFileName(url, n));
	        	}
				Writer.writeCsvFromTable(tableau, mkCSVFileName(url, n),outputDirHtml);
				n ++;
				listTable.add(tableau);
				
			}
		}
		return listTable;
	}
	public static void main(String[] args) throws IOException {
		
		String BASE_WIKIPEDIA_URL = "https://en.wikipedia.org/wiki/";
		String outputDirHtml = "output" + File.separator + "html" + File.separator;
		String outputDirWikitext = "output" + File.separator + "wikitext" + File.separator;
		
		WikipediaHTMLExtractor wiki = new WikipediaHTMLExtractor(BASE_WIKIPEDIA_URL,
				outputDirHtml,outputDirWikitext);
		
		String url = "Comparison_of_netbooks";
		
		List<Table> table = wiki.ExtractUrlToCsv(url);
		
		
	}
			
		
		
		
	}

