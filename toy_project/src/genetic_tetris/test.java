package genetic_tetris;

import java.util.*;

public class test {
	public static void main(String[] args) {
		double upper = 0.25;
		double lower = -0.25;
		
		for(int i =0 ; i < 10 ; i++) {
			double tmp = Math.random() * (upper - lower ) + lower;
			System.out.print(tmp  + " ");
		}
	}
}
