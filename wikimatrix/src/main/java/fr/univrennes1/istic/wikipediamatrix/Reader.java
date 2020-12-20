package fr.univrennes1.istic.wikipediamatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;

import bean.Table;

public class Reader {

	static Logger logger = Logger.getLogger("Reader");
	public static Table readerCsv(String csvName, String outputDir) throws IOException {

		Table tableau = new Table();

		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get(outputDir + csvName));
			  
			 // Il y a un problème avec ce parser qui saute les backslashes
			 
			CSVParser parser = new CSVParserBuilder()
					.withSeparator(';')
					.withIgnoreQuotations(false)
					.build();
			 
			
			RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder()
					.withSeparator(';')
					.build();
			

			CSVReader csvReader = new CSVReaderBuilder(reader)
					.withSkipLines(0)
					.withCSVParser(rfc4180Parser)
					.build();

			String[] line = csvReader.readNext();
			tableau.setHeader(line);

			while ((line = csvReader.readNext()) != null) {
				tableau.addLine(line);
			}

			return tableau;

		} catch (IOException ex) {
			logger.info(ex.toString());
			return null;
		}
	}
}
