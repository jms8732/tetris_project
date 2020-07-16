package genetic_tetris;

import java.util.*;

import genetic_tetris.Generic.Weight;

public class play_game {
	public static int removed_line = 0;
	private static int[][] board;
	private static int[] ud = { -1, 0, 1, 0 };
	private static int[] rl = { 0, 1, 0, -1 };

	public static void main(String[] args) {
		Random rn = new Random();

		tetris_block block = new tetris_block();
		tetris_board tetris_board = new tetris_board();

		board = new int[20][10];
		tetris_board.set_board(board);

		Generic g = new Generic(100); // 유전자 생성
		Weight[] w = g.get_weight();
		while (true) {

			for (int i = 0; i < w.length; i++) {
				while (true) {
					Weight current_w = w[i];
					int n = (rn.nextInt(7) + 1);
					List<Point> b = block.make_block(n);
					move(b, n, current_w);
				}
			}
		}

	}

	// 블록 두기
	private static void move(List<Point> b, int n, Weight w) {
		for (int i = 0; i < 4; i++) { // 회전
			for (int j = -4; j < 6; j++) {
				for (int k = 19; k >= -1; k--) {
					if (can_put(k, j, b)) { // 블록을 둘 수 있을 경우
						put_there(k, j, b, n, w);
					}
				}
			}
			rotate(b);
		}
	}

	private static void rotate(List<Point> b) {
		int pivot_x = b.get(1).x, pivot_y = b.get(1).y;

		for (Point p : b) {
			p.x = p.x - pivot_x;
			p.y = p.y - pivot_y;
		}

		int x_min = 0, x_max = 9, y_max = 19, y_min = 0;
		for (Point p : b) {
			int nx = p.x * 0 + -1 * p.y;
			int ny = p.x * 1 + 0 * p.y;

			p.x = nx + pivot_x;
			p.y = ny + pivot_y;

			if (p.x < 0)
				x_min = Math.min(p.x, x_min);

			if (p.x >= 10)
				x_max = Math.max(p.x, x_max);

			if (p.y < 0)
				y_min = Math.min(p.y, y_min);

			if (p.y >= 20)
				y_max = Math.max(p.y, y_max);
		}

		x_min = Math.abs(x_min);
		y_min = Math.abs(y_min);

		for (Point p : b) {
			p.x += x_min;
			p.y += y_min;
		}

		x_max -= 9;
		y_max -= 19;

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

	// 블록을 둔다.
	private static void put_there(int x, int y, List<Point> b, int n, Weight w) {
		List<Point> tmp = new ArrayList<>();
		int blank_count =0 , connected = 0;
		
		for (Point p : b) {
			tmp.add(new Point(p.x + x, p.y + y));
		}

		for (int i = 0; i < tmp.size(); i++) {
			
			for(int j =0 ; j < 4;  j++) {
				int nx = tmp.get(i).x + ud[i];
				int ny = tmp.get(i).y + rl[i];
				
				//배열 범위 밖인 경우
				if(nx <0 || nx >= 20 || ny <0 || ny >= 10)
					continue;
				
				if(board[nx][ny] == 1)
					connected++;
				else
					blank_count++;
			}
		}
		
		
	}

	// 블록을 해당 위치에서 둘 수 있는지 없는지 판단.
	private static boolean can_put(int x, int y, List<Point> b) {
		for (Point p : b) {
			int nx = p.x + x;
			int ny = p.y + y;

			// 현재 좌표에 배열 범위 밖인 경우
			if (nx < 0 || nx >= 20 || ny < 0 || ny >= 10)
				return false;

			// 블록을 둘 자리에 이미 블록이 존재한 경우
			if (board[nx][ny] != 0)
				return false;
		}

		return true;
	}
}
