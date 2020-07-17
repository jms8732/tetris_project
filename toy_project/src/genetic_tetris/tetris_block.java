package genetic_tetris;

import java.util.*;

public class tetris_block {
	public List<Point> make_block(int j) {
		List<Point> list = new ArrayList<>();

		if (j == 1) {
			int[] x_idx = { 0, 0, 0, 0 };
			int[] y_idx = { 4, 5, 6, 7 };
			make(list, x_idx, y_idx);

		} else if (j == 2) {
			int[] x_idx = { 1, 1, 0, 1 };
			int[] y_idx = { 4, 5, 5, 6 };
			make(list, x_idx, y_idx);

		} else if (j == 3) {
			int[] x_idx = { 1, 1, 1, 0 };
			int[] y_idx = { 4, 6, 5, 6 };
			make(list, x_idx, y_idx);

		} else if (j == 4) {
			int[] x_idx = { 0, 1, 1, 1 };
			int[] y_idx = { 4, 4, 5, 6 };
			make(list, x_idx, y_idx);

		} else if (j == 5) {
			int[] x_idx = { 0, 0, 1, 1 };
			int[] y_idx = { 4, 5, 5, 6 };
			make(list, x_idx, y_idx);

		} else if (j == 6) {
			int[] x_idx = { 1, 1, 0, 0 };
			int[] y_idx = { 4, 5, 5, 6 };
			make(list, x_idx, y_idx);
		} else {
			int[] x_idx = { 0, 0, 1, 1 };
			int[] y_idx = { 4, 5, 4, 5 };

			make(list, x_idx, y_idx);
		}

		return list;
	}

	private void make(List<Point> list, int[] x_idx, int[] y_idx) {
		for (int i = 0; i < 4; i++) {
			list.add(new Point(x_idx[i], y_idx[i]));
		}
	}
}
