package fr.univrennes1.istic.wikipediamatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import bean.Table;

public class StatsDesc {

	public List<Table> getStats(List<Table> listTable) {

		Table stats = new Table();

		String[] header = { "", "Min", "Max", "Mean", "Std", "Total" };
		stats.setHeader(header);

		int nbTable = listTable.size();
		List<Integer> listNbColonne = new ArrayList<Integer>();
		List<Integer> listNbLine = new ArrayList<Integer>();
		List<Integer> listNbCell = new ArrayList<Integer>();
		Map<String, AtomicInteger> mapNomColonne = new HashMap<String, AtomicInteger>();

		for (Table table : listTable) {
			listNbColonne.add(table.nbColonne());
			listNbLine.add(table.nbLine());
			listNbCell.add(table.nbCellule());

			for (String key : table.getHeader()) {
				mapNomColonne.putIfAbsent(key, new AtomicInteger(0));
				mapNomColonne.get(key).incrementAndGet();
			}

		}
		
		Collections.sort(listNbColonne);
		Collections.sort(listNbLine);
		Collections.sort(listNbCell);
		
		String[] colonne = {"Colonne",
				Integer.toString(listNbColonne.get(0)),
				Integer.toString(listNbColonne.get(nbTable-1)),
				Double.toString(mean(listNbColonne)),
				Double.toString(sd(listNbColonne)),
				Integer.toString(sum(listNbColonne))};
		
		String[] line = {"Ligne",
				Integer.toString(listNbLine.get(0)),
				Integer.toString(listNbLine.get(nbTable-1)),
				Double.toString(mean(listNbLine)),
				Double.toString(sd(listNbLine)),
				Integer.toString(sum(listNbLine))};
		
		String[] cell = {"Cellule",
				Integer.toString(listNbCell.get(0)),
				Integer.toString(listNbCell.get(nbTable-1)),
				Double.toString(mean(listNbCell)),
				Double.toString(sd(listNbCell)),
				Integer.toString(sum(listNbCell))};
		
		String[] tableau = {"Tableau","","","","",String.valueOf(nbTable)};
		
		stats.addLine(colonne);
		stats.addLine(line);
		stats.addLine(cell);
		stats.addLine(tableau);
		
		
		Table occurrence = new Table();
		
		String[] headerOccur = { "Mots", "Occurrence"};
		occurrence.setHeader(headerOccur);
		
		for (String key:mapNomColonne.keySet()) {
			String[] occur = {key,mapNomColonne.get(key).toString()};
			occurrence.addLine(occur);
		}
		
		return Arrays.asList(stats,occurrence);
	}

	public int sum(List<Integer> a) {
		if (a.size() > 0) {
			int sum = 0;
			for (Integer i : a) {
				sum += i;
			}
			return sum;
		}
		return 0;
	}

	public double mean(List<Integer> a) {
		int sum = sum(a);
		double mean = 0;
		if (a.size() > 0) {
			mean = sum / (a.size() * 1.0);
		}
		return mean;
	}

	public double sd(List<Integer> a) {
		if (a.size() > 0) {
			int sum = 0;
			double mean = mean(a);
			for (Integer i : a)
				sum += Math.pow((i - mean), 2);
			return Math.sqrt(sum / a.size()); // On pourrait aussi utiliser l'écart-type corrigé
		}
		return 0;
	}

}
