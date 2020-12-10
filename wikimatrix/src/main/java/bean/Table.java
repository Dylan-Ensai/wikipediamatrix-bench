package bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
	
	private String[] header;
	private List<String[]> lines = new ArrayList<String[]>();
	
	public String[] getHeader() {
		return header;
	}
	public void setHeader(String[] header) {
		this.header = header;
	}

	public List<String[]> getLines() {
		return this.lines;
	}
	public String[] getLine(int index) {
		return this.lines.get(index);
	}
	public void setLines(List<String[]> lines) {
		this.lines = lines;
	}
	public void addLine(String[] line) {
		this.lines.add(line);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (!Arrays.equals(header, other.header))
			return false;
		
		if (lines == null) {
			if (other.lines != null)
				// System.out.println("lines null");
				return false;
			
		} else {
			if (lines.size() != other.lines.size()) {
				return false;
			}
				
			
			for (int i=0; i<lines.size(); i++) {
				String[] a = lines.get(i);
				String[] b = other.lines.get(i);
				for (int j=0; j<lines.get(i).length; j++) {
					if (! a[j].equals(b[j]) ) {
						System.out.println(a[j]);
						System.out.println(b[j]);
						return false;
						
					}	
					
				}
			}
				
		}
			
		return true;
	}
	
}
