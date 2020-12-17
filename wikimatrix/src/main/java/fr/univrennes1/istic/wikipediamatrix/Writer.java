package fr.univrennes1.istic.wikipediamatrix;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;

import bean.Table;

public class Writer {
	static Logger logger = Logger.getLogger("Writer");
	
	public static void writeCsvFromTable(Table table, String csvName,String outputDir) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir + csvName));

			ICSVWriter csvWriter = new CSVWriterBuilder(writer).withSeparator(';')
					.withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

			csvWriter.writeNext(table.getHeader());
			csvWriter.writeAll(table.getLines());
			csvWriter.close();
			writer.close();

		} catch (IOException ex) {
			logger.info(ex.toString());
		}

	}
}
