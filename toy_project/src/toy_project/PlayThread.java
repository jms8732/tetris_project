package toy_project;

import java.util.*;

public class PlayThread extends Thread {
	private static boolean block_finish = false;
	public static boolean game_finish = false;
	private List<Point> b;
	private int[][] board;
	private int[] ud = { 1, 0, 0 };
	private int[] rl = { 0, -1, 1 };
	public static int dir = 0;
	public static long time = 1000;
	public static int removed_line = 0;
	private int n;
	private tetris_board tetris_board;
	
	@Override
	public void run() {
		tetris_board = new tetris_board();
		tetris_block block = new tetris_block();
		board = new int[20][10];
		tetris_board.set_board(board);

		while (true) {
			try {
				if (!block_finish) {
					Random rand = new Random();
					n = rand.nextInt(7);
					b = block.make_block(n);

					tetris_board.set_block(b);

					for (Point p : b) {
						if (board[p.y][p.x] == 1) {
							game_finish = true;
							break;
						}
					}

					block_finish = true;
				}

				if (game_finish) {
					System.out.println("Game Finish");
					
					this.init_game();
					game_finish = false;
					continue;
				}

				Thread.sleep(1);
				if (dir < 3) {
					// ���� �ε����� �������� �ʴ� ���
					if (!move()) {
						dir = -1;
						move();
					}
				} else if (dir == 3) { // �� Ű�� ���� ���
					if (n != 6)
						rotate();
					else {
						dir = -1;
						move();
					}

				} else if (dir == 32) { // �����̽� �ٸ� ���� ���
					dir = 0;
					while (true) {
						if (!move()) {
							break;
						}
					}
				}
				tetris_board.repaint();
				dir = -1;

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void init_game() {
		board = new int[20][10];
		tetris_board.set_board(board);
		removed_line = 0;
	}

	/*
	 * ���� ȸ�� �ϴ� �޼ҵ� ȸ�� ��ȯ ����� �̿��Ͽ� ȸ��
	 */
	private void rotate() {
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

	private void remove_line() {
		Set<Integer> idx = new LinkedHashSet<>();
		for (int i = 0; i < 20; i++) {
			int count = 0;
			for (int j = 0; j < 10; j++) {
				if (board[i][j] == 1)
					count++;
			}

			if (count == 10) {
				Arrays.fill(board[i], 0);
				idx.add(i);
			}
		}

		removed_line += idx.size();
		for (int i : idx) {
			for (int j = i - 1; j >= 0; j--) {
				for (int k = 0; k < 10; k++) {
					if (board[j][k] == 1) {
						board[j][k] = 0;
						board[j + 1][k] = 1;
					}
				}
			}
		}

	}

	private boolean move() {
		boolean check = true;

		if (dir != -1) {
			for (Point p : b) {
				int nx = p.y + ud[dir];
				int ny = p.x + rl[dir];

				// ��Ʈ���� ������ �迭 ���� ������ �Ѿ ���
				if (nx < 0 || nx >= 20 || ny < 0 || ny >= 10) {
					if (nx < 0 || nx >= 20) {
						block_finish = false;
						board_print();
						remove_line();
					}
					check = false;
					return check;
				}

				// ��Ʈ���� ������ ������ ���
				if (board[nx][ny] == 1) {
					// ������ �Ʒ������� ���
					if (dir == 0) {
						block_finish = false;
						board_print();
						remove_line();
					}
					check = false;
					return check;
				}
			}

			if (check) {
				for (Point p : b) {
					p.y += ud[dir];
					p.x += rl[dir];
				}
			}
		}
		return check;
	}

	// �����ǿ� ���� �߰�
	private void board_print() {
		for (Point p : b) {
			board[p.y][p.x] = 1;
		}
	}
}