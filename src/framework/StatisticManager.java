package framework;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

import objects.Cell;

public class StatisticManager {

	private static StatisticManager instance = null;
	
	public class BornDieStatistic {
		int nBorn;
		int nDied;
		
		public BornDieStatistic(int b, int d) {
			nBorn = b;
			nDied = d;
		}
	}
	
	public class CellTypeStatistic {
		public int type;		// which type
		public int aliveCount;	// how much alive
		public int deadCount;	// how much dead
		public double dnaVariation;
		ArrayList<Cell> cells;
		
		public CellTypeStatistic(int t) {
			type = t;
			aliveCount = 0;
			deadCount = 0;
			dnaVariation = 0;
			cells = new ArrayList<Cell>();
		}
		
		public String toString() {
			return "Type: " + type + " aliveCount " + aliveCount + " deadCount " + deadCount + " dnaVariation: " + dnaVariation;
		}
		
		public void calcDnaVariation() {
			
			TreeSet<String> DNAs = new TreeSet<String>();
			
			for (Cell c : cells) {
				DNAs.add(c.properties.getDNA());
			}
			
			dnaVariation = (double)DNAs.size() / cells.size();
		}
	}
	
	private ArrayList<ArrayList<CellTypeStatistic>> cellStatistics;
	
	private ArrayList<BornDieStatistic> bornDieStatistics;
	
	private StatisticManager() {
		cellStatistics = new ArrayList<ArrayList<CellTypeStatistic>>();
		bornDieStatistics = new ArrayList<BornDieStatistic>();
	}
	
	public void takeSnapshot(World w, int step) {
	
		// born die statistics
		BornDieStatistic bdStat = new BornDieStatistic(w.lastStepCellsBorn, w.lastStepCellsDied);
		bornDieStatistics.add(bdStat);
		
		// cell statistics
		ArrayList<CellTypeStatistic> stats = new ArrayList<CellTypeStatistic>();
		
		for (Cell c : w.currentCells) {
			CellTypeStatistic statToWorkWith = null;
			for (CellTypeStatistic s : stats) {
				if (s.type == c.type) {
					statToWorkWith = s;
					break;
				}
			}
			if (statToWorkWith == null) {
				statToWorkWith = new CellTypeStatistic(c.type);
				stats.add(statToWorkWith);
			}
			
			if (c.isAlive()) {
				statToWorkWith.aliveCount++;
			} else {
				statToWorkWith.deadCount++;
			}
			
			// for partitioning
			statToWorkWith.cells.add(c);
		}
		
		// do stuff that can be done only after partitioning
		for (CellTypeStatistic s : stats) {
			s.calcDnaVariation();
			// clear memory used for partitioning
			s.cells = new ArrayList<Cell>();
		}
		
		cellStatistics.add(stats);
	}
	
	public ArrayList<ArrayList<CellTypeStatistic>> getCellStatistics() {
		return cellStatistics;
	}
	
	public void printCellStatistics() {
		ArrayList<CellTypeStatistic> lastStats = cellStatistics.get(cellStatistics.size() - 1);
		for (CellTypeStatistic stat : lastStats) {
			System.out.println(stat);
		}
	}
	
	public void printStatisticsToText() {
		try {
			PrintWriter bornDieWriter = new PrintWriter("bornDieStatistics.txt", "UTF-8");
			
			int step = 1;
			for (BornDieStatistic bdStat : bornDieStatistics) {
				bornDieWriter.print(step + ":");
				bornDieWriter.println(bdStat.nBorn + "," + bdStat.nDied + ":");
				step++;
			}
			
			bornDieWriter.close();
			
			PrintWriter writer = new PrintWriter("cellStatistics.txt", "UTF-8");
			
			step = 1;
			for (ArrayList<CellTypeStatistic> cs : cellStatistics) {
				
				Collections.sort(cs, new Comparator<CellTypeStatistic>() {

					@Override
					public int compare(CellTypeStatistic arg0,
							CellTypeStatistic arg1) {
						// TODO Auto-generated method stub
						return Integer.signum(arg0.type - arg1.type);
					}
					
				});
				
				writer.println(step + ":");
				for (CellTypeStatistic stat : cs) {
					writer.println(stat.type + "," + stat.aliveCount + "," + stat.deadCount + "," + stat.dnaVariation);
				}
				step++;
			}
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static StatisticManager getInstance() {
		if (StatisticManager.instance == null) {
			StatisticManager.instance = new StatisticManager();
		}
		
		return StatisticManager.instance;
	}
	
	
	
}
