package genetic_tetris;

import java.util.*;

public class Generic {
	private Weight[] w;
	private int size;

	public Generic(int count) {
		this.size = count;
		w = new Weight[this.size];

		double upper = 1.0, lower = -1.0;
		Random rn = new Random();
		for (int i = 0; i < w.length; i++) {
			int n = i + 1;
			int r = 0;
			double b = Math.random() * (upper - lower) + lower;
			double c = Math.random() * (upper - lower) + lower;
			double cc = Math.random() * (upper - lower) + lower;
			double hw = Math.random() * (upper - lower) + lower;
			w[i] = new Weight(n, r, b, c, cc, hw);
		}
	}

	public Weight[] get_weight() {
		return w;

	}

	public void cross_over() {
		double result = Integer.MIN_VALUE;
		int idx = 0;

		// rank가 가장 큰 유전자 선택
		for (int i = 0; i < size; i++) {
			if (result < w[i].delete_line) {
				result = w[i].delete_line;
				idx = i;
			}
		}
		Weight fitness = w[idx];

		System.out.println("select gene number : #" + idx);
		System.out.println("Delete line : " + fitness.delete_line);
		System.out.println("blank : " + fitness.blank_weight + " connected : " + fitness.connected_weight);
		System.out.println("line : " + fitness.complete_line_weight + " height weight : " + fitness.height_weight);

		double mutation = 0.0;
		System.out.println("Mutation : " + mutation);
		w = new Weight[this.size];

		w[0] = fitness;
		Random rn = new Random();
		for (int i = 1; i < size; i++) {
			if (rn.nextBoolean())
				mutation = (Math.abs(fitness.blank_weight) + Math.abs(fitness.complete_line_weight)
						+ Math.abs(fitness.connected_weight) + Math.abs(fitness.height_weight)) / 10;

			double b = make_percentage(fitness.blank_weight, mutation);
			double c = make_percentage(fitness.complete_line_weight, mutation);
			double cc = make_percentage(fitness.connected_weight, mutation);
			double hw = make_percentage(fitness.height_weight, mutation);
			w[i] = new Weight(i + 1, 0, b, c, cc, hw);
		}

		System.out.println("complete generate\n");
	}

	private double make_percentage(double percent, double mutation) {
		double upper = 0.0, lower = 0.0;
		upper = Math.abs(Math.abs(percent) - mutation);
		lower = -upper;

		double result = Math.random() * (upper - lower) + lower;
		return result;

	}

	public class Weight {
		int number = 0;
		double delete_line;
		double blank_weight;
		double complete_line_weight;
		double connected_weight;
		double height_weight;

		public Weight(int n, double r, double b, double c, double cc, double hw) {
			this.number = n;
			this.delete_line = r;
			this.blank_weight = b;
			this.complete_line_weight = c;
			this.connected_weight = cc;
			this.height_weight = hw;
		}

	}
}
