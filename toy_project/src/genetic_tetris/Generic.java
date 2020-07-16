package genetic_tetris;

import java.util.*;

public class Generic {
	private Weight [] w ;
	private static int generation = 1;
	
	public Generic(int count) {
		w = new Weight[count];
		
		Random rn = new Random();
		for(int i =0 ; i < w.length ; i++) {
			int n = i+1;
			int r =0 ;
			double b = (rn.nextDouble() * 2) - 1;
			double c = (rn.nextDouble() * 2) - 1;
			double cc  =(rn.nextDouble() * 2) - 1;
			w[i] = new Weight(n,r,b,c,cc);
		}
	}
	
	public Weight[] get_weight() {
		return w;
		
	}

	class Weight {
		int number =0 ;
		int row; // ���� �� ��
		double blank_weight;
		double complete_line_weight;
		double connected_weight;

		public Weight(int n, int r, double b, double c, double cc) {
			this.number = n;
			this.row = r;
			this.blank_weight = b;
			this.complete_line_weight = c;
			this.connected_weight = cc;
		}

	}
}