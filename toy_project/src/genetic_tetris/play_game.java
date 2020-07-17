package genetic_tetris;

import java.util.*;

import genetic_tetris.Generic.Weight;

public class play_game {
	public static int removed_line = 0;
	private static int[][] board;
	private static int[] ud = { -1, 0, 1, 0 };
	private static int[] rl = { 0, 1, 0, -1 };
	public static int generation = 1;
	public static int current_gene = 1;
	public static int MOM = 1;
	public static int MAXIMUM_line = 0;

	public static void main(String[] args) {
		Random rn = new Random();

		tetris_block block = new tetris_block();
		tetris_board tetris_board = new tetris_board();

		Generic g = new Generic(10); // 유전자 생성
		try {
			while (true) {
				Weight[] w = g.get_weight();

				for (int i = 0; i < w.length; i++) {
					Weight current_w = w[i];
					current_gene = current_w.number;
					board = new int[20][10];
					tetris_board.set_board(board);
					removed_line = 0;

					while (true) {
						int n = (rn.nextInt(7) + 1);
						List<Point> b = block.make_block(n);

						if (isFinish(b))
							break;

						List<Point> tmp = move(b, n, current_w);
						decide_put_there(tmp, n);
						remove_line(tmp);
						tetris_board.repaint();

						Thread.sleep(100);
					}

					if (removed_line > MAXIMUM_line) {
						MAXIMUM_line = removed_line;
						MOM = current_gene;
					}
					
					current_w.delete_line += removed_line;

				}

				// 크로스 오버
				g.cross_over();
				generation++;
				MAXIMUM_line =0 ;
				MOM = 0;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// 줄 지우기
	private static void remove_line(List<Point> tmp) {
		List<Integer> idx = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			int cont = 0;
			for (int j = 0; j < 10; j++) {
				if (board[i][j] != 0)
					cont++;
			}

			if (cont == 10) {
				Arrays.fill(board[i], 0);
				idx.add(i);
			}
		}

		for (int i : idx) {
			for (int j = i - 1; j >= 0; j--) {
				System.arraycopy(board[j], 0, board[j + 1], 0, 10);
			}
		}

		removed_line += idx.size();
	}

	// 해당 위치에 블럭 두기
	private static void decide_put_there(List<Point> tmp, int n) {
		for (Point p : tmp) {
			board[p.x][p.y] = n;
		}
	}

	// 게임이 끝나는지 확인
	private static boolean isFinish(List<Point> b) {
		for (Point p : b) {
			if (board[p.x][p.y] != 0)
				return true;
		}

		return false;
	}

	// 블록 두기
	private static List<Point> move(List<Point> b, int n, Weight w) {
		double result = Integer.MIN_VALUE;
		List<Point> tmp_save = new ArrayList<>();

		for (int i = 0; i < 3; i++) { // 회전
			outter: for (int j = 0; j >= -4; j--) {
				List<Point> temp = new ArrayList<>();
				for (Point p : b) {
					int nx = p.x;
					int ny = p.y + j;

					if (nx < 0 || nx >= 20 || ny < 0 || ny >= 10)
						break outter;

					if (board[nx][ny] != 0)
						break outter;
					temp.add(new Point(nx, ny));
				}

				go_down(temp);

				double tmp = put_there(temp, n, w);

				if (result < tmp) {
					result = tmp;

					if (!tmp_save.isEmpty())
						tmp_save.clear();

					for (Point p : temp) {
						tmp_save.add(new Point(p.x, p.y));
					}
				}

			}

			outter: for (int j = 1; j <= 6; j++) {
				List<Point> temp = new ArrayList<>();
				for (Point p : b) {
					int nx = p.x;
					int ny = p.y + j;

					if (nx < 0 || nx >= 20 || ny < 0 || ny >= 10)
						break outter;

					if (board[nx][ny] != 0)
						break outter;
					temp.add(new Point(nx, ny));
				}

				go_down(temp);

				double tmp = put_there(temp, n, w);

				if (result < tmp) {
					result = tmp;

					if (!tmp_save.isEmpty())
						tmp_save.clear();

					for (Point p : temp) {
						tmp_save.add(new Point(p.x, p.y));
					}
				}

			}

			rotate(b);
		}

		return tmp_save;
	}

	// 현재 위치에 블럭을 둔다.
	private static double put_there(List<Point> tmp, int n, Weight w) {
		double result = 0.0;
		int blank_count = 0, block_count = 0, predict_remove_line = 0, height = 0;

		// 둔 지점의 블럭의 빈칸의 개수와 블럭의 개수를 센다
		for (Point p : tmp) {
			for (int i = 0; i < 3; i++) {
				int nx = p.x + ud[i];
				int ny = p.y + rl[i];

				if (nx < 0 || nx >= 20 || ny < 0 || ny >= 10)
					continue;

				if (board[nx][ny] == 0)
					blank_count++;
				else
					block_count++;
			}
		}

		predict_remove_line = predict_remove_line(tmp, n);
		height = cal_height(tmp);

		result = (blank_count * w.blank_weight + block_count * w.connected_weight
				+ predict_remove_line * w.complete_line_weight + height * w.height_weight) / 20;

		return result;
	}

	// 현재 둔 블록의 높이를 계산
	private static int cal_height(List<Point> tmp) {
		int h = Integer.MAX_VALUE;

		for (Point p : tmp) {
			h = Math.min(h, p.x);
		}

		return 20 - h;
	}

	// 해당 위치에 블럭을 두어 지울 수 있는 줄 수를 예측
	private static int predict_remove_line(List<Point> tmp, int n) {
		for (Point p : tmp) {
			board[p.x][p.y] = n;
		}

		int ret = 0;
		for (int i = 0; i < 20; i++) {
			int cont = 0;
			for (int j = 0; j < 10; j++) {
				if (board[i][j] != 0)
					cont++;
			}

			if (cont == 10)
				ret++;
		}

		for (Point p : tmp) {
			board[p.x][p.y] = 0;
		}

		return ret;
	}

	// 블록을 아래로 내린다.
	private static void go_down(List<Point> tmp) {
		outter: while (true) {
			for (Point p : tmp) {
				int nx = p.x + 1;
				int ny = p.y;

				// 배열의 범위 밖인 경우
				if (nx < 0 || nx >= 20 || ny < 0 || ny >= 10)
					break outter;

				// 아래에 블록이 존재하는 경우
				if (board[nx][ny] != 0)
					break outter;
			}

			for (Point p : tmp) {
				p.x += 1;
			}
		}
	}

	private static void rotate(List<Point> b) {
		int pivot_x = b.get(1).x, pivot_y = b.get(1).y;

		for (Point p : b) {
			p.x = p.x - pivot_x;
			p.y = p.y - pivot_y;
		}

		int x_min = 0, x_max = 19, y_max = 9, y_min = 0;
		for (Point p : b) {
			int nx = p.x * 0 + -1 * p.y;
			int ny = p.x * 1 + 0 * p.y;

			p.x = nx + pivot_x;
			p.y = ny + pivot_y;

			if (p.x < 0)
				x_min = Math.min(p.x, x_min);

			if (p.x >= 20)
				x_max = Math.max(p.x, x_max);

			if (p.y < 0)
				y_min = Math.min(p.y, y_min);

			if (p.y >= 10)
				y_max = Math.max(p.y, y_max);
		}

		x_min = Math.abs(x_min);
		y_min = Math.abs(y_min);

		for (Point p : b) {
			p.x += x_min;
			p.y += y_min;
		}

		x_max -= 19;
		y_max -= 9;

		for (Point p : b) {
			p.x -= x_max;
			p.y -= y_max;
		}
	}

	private static void print() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
