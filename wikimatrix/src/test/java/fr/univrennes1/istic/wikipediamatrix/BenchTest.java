package fr.univrennes1.istic.wikipediamatrix;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;

import org.junit.Test;

import bean.Table;

public class BenchTest {

	static Logger logger = Logger.getLogger("BenchTest");

	private String BASE_WIKIPEDIA_URL = "https://en.wikipedia.org/wiki/";
	private String outputDirHtml = "output" + File.separator + "html" + File.separator;

	private String outputDirWikitext = "output" + File.separator + "wikitext" + File.separator;
	private File file = new File("inputdata" + File.separator + "wikiurls.txt");

	@Test
	public void testFileOk() throws Exception {
		// directory where CSV files are exported (HTML extractor)
		assertTrue(new File(outputDirHtml).isDirectory());
		// directory where CSV files are exported (Wikitext extractor)
		assertTrue(new File(outputDirWikitext).isDirectory());
	}

	@Test
	public void testBenchExtractors() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String url;

		int nurl = 0;

		int nTabKo = 0;
		List<Table> allTable = new ArrayList<Table>();

		while ((url = br.readLine()) != null) {
			String wurl = BASE_WIKIPEDIA_URL + url;
			logger.info("Wikipedia url: " + wurl);

			WikipediaHTMLExtractor wiki = new WikipediaHTMLExtractor(BASE_WIKIPEDIA_URL, outputDirHtml,
					outputDirWikitext);
			
			if (testGetDocument(wiki, url)) {
				try {
					List<Table> listTable = wiki.ExtractUrlToCsv(url);
					for (int i = 0; i < listTable.size(); i++) {
						if (!testExtraction(wiki, listTable.get(i), url, i + 1)) {
							int j = i + 1;
							logger.info("Le tableau " + j + " n'a pas réussi le test d'extraction");
							nTabKo++;
						}

					}
					allTable.addAll(listTable);
				} catch (Exception e) {
					logger.info(e.getMessage() + url);
				}
			} else
				logger.warning("Page introuvable");
			nurl++;
		}
		br.close();
		
		
		// On calcule les statitiques 
		StatsDesc stats = new StatsDesc();
		List<Table> statistiques = stats.getStats(allTable);
		// Puis on les écrit dans des csv
		Writer.writeCsvFromTable(statistiques.get(0), "StatistiquesDescriptives.csv", "");
		Writer.writeCsvFromTable(statistiques.get(1), "NomColonnes.csv", "");

		
		assertEquals(nTabKo, 0);
		assertEquals(nurl, 336);
		logger.info("Test validé, 0 tableau KO");
	}

	@Test
	public void testSpecifiques() throws Exception {

		int nbTableau = 1;
		int nbColonne = 22;
		int nbLigne = 73;

		String url = "Comparison_of_digital_SLRs";
		WikipediaHTMLExtractor wiki = new WikipediaHTMLExtractor(BASE_WIKIPEDIA_URL, outputDirHtml, outputDirWikitext);

		assertTrue(testGetDocument(wiki, url));
		List<Table> listTable = wiki.ExtractUrlToCsv(url);

		assertTrue(listTable.size() == nbTableau);

		// Ici on pourrait choisir le tableau dans le cas ou il y en a plusieurs

		Table table = listTable.get(0);

		assertTrue(table.nbColonne() == nbColonne);
		assertTrue(table.nbLine() == nbLigne);

	}

	private Boolean testGetDocument(WikipediaHTMLExtractor wiki, String url) {
		try {
			Document doc = wiki.getDocument(url);
			return true;
		} catch (Exception e) {
			logger.info(e.toString());
			return false;
		}

	}

	private Boolean testExtraction(WikipediaHTMLExtractor wiki, Table table, String url, int n) throws Exception {
		String csvName = wiki.mkCSVFileName(url, n);
		Table tableOut = Reader.readerCsv(csvName, outputDirHtml);
		return table.equals(tableOut);
	}

}
