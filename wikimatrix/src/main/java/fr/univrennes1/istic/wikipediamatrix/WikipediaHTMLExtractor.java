package fr.univrennes1.istic.wikipediamatrix;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;

import bean.Table;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	
	
	public Table getTable(Element table){
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
        			if (text.contains(";")) {
        				
        				// text = "\"" + text + "\"";
        				text = text.replace(";",":");
        			}
        			line.add(text);
                }
        		tableau.addLine(line.toArray(new String[0]));
        		
        	}
        
        	return tableau;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public Boolean isWikitable(Element table) {
		if (! table.className().contains("wikitable") ) {
			return false;
		}
		return true;
	}
	
	public Boolean ContainSpan(Element table) {
		for(Element td : table.select("td")) {
			if (td.hasAttr("colspan"))
				return true;
		}
		for(Element tr : table.select("tr")) {
			if (tr.hasAttr("rowspan"))
				return true;
		}
		return false;
		
	}
	
	
	public void writeCsvFromTable(Table table, String csvName) {
		
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(outputDirWikitext + csvName));
			
			ICSVWriter csvWriter = new CSVWriterBuilder(writer)
		            .withSeparator(';')
		            .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
		            .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
		            .withLineEnd(CSVWriter.DEFAULT_LINE_END)
		            .build();
			
			csvWriter.writeNext(table.getHeader());
			csvWriter.writeAll(table.getLines());
			
		    csvWriter.close();
		    writer.close();
			
		}
		catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		

	}
	
	public Table readerCsv(String csvName) throws IOException{
		
		Table tableau = new Table();
		
		try {
			Reader reader = Files.newBufferedReader(Paths.get(outputDirWikitext + csvName));
			
			CSVParser parser = new CSVParserBuilder()
				    .withSeparator(';')
				    .withIgnoreQuotations(true)
				    .build();
			
			CSVReader csvReader = new CSVReaderBuilder(reader)
				    .withSkipLines(0)
				    .withCSVParser(parser)
				    .build();
			
			
			String[] line = csvReader.readNext();
			tableau.setHeader(line);
			
			while ((line = csvReader.readNext()) != null) {
		        tableau.addLine(line);
		    }
			
			return tableau;
			
		}
		catch (IOException ex) {
		    ex.printStackTrace();
		    return null;
		}	
	}

	
	private String mkCSVFileName(String url, int n) {
		return url.trim() + "-" + n + ".csv";
	}
	
	public List<Table> ExtractUrlToCsv(String url) throws IOException{
		Document doc = getDocument(url);
		Elements tables = doc.select("table");
		List<Table> listTable = new ArrayList<Table>();
		int n = 1;
		for (int i = 0; i < tables.size(); i++) {
			Element table = tables.get(i);
			if (isWikitable(table) && ! ContainSpan(table)) {
				Table tableau = getTable(table);
				writeCsvFromTable(tableau, mkCSVFileName(url, n));
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
		
		String url = "Comparison_of_Java_and_C++";
		
		// List<Table> listTable = wiki.ExtractUrlToCsv(url);
	
		Document doc = wiki.getDocument(url);
		Elements tables = doc.select("table");
		
		Element table = tables.get(2);
		System.out.println(tables.size());
		Table tableau = wiki.getTable(table);
		
		System.out.println(tableau);
		
			
	}
			
		
		
		
	}

