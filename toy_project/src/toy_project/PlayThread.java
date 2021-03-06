package toy_project;

import java.util.*;

//실질적인 테트리스 동작을 하는 클래스
public class PlayThread extends Thread {
	private static boolean block_finish = false; //테트리스 블럭이 완전히 두었는 지를 판단하는 변수
	public static boolean game_finish = false; //게임이 종료가 되었는 지 판단하는 변수
	private List<Point> b; 
	private int[][] board;
	
	//아래, 좌 ,우로  좌표 이동을 할 배열
	private int[] ud = { 1, 0, 0 };
	private int[] rl = { 0, -1, 1 };
	
	public static int dir = 0; //현재 테트리스 블럭의 이동방향
	public static int removed_line = 0; //지운 줄 수
	private int n; //Random 클래스를 통해서 얻은 테트리스 블럭 번호
	private tetris_board tetris_board; //테트리스 보드판
	
	@Override
	public void run() {
		tetris_board = new tetris_board();
		tetris_block block = new tetris_block();
		board = new int[20][10];
		tetris_board.set_board(board); //테트리스 보드판을 설정함으로서 tetris_board 클래스에서 해당 이차원 int형 배열을 이용하여 그리기를 수행한다.

		/*
		 * 무한 루프를 통해서 계속해서 테트리스 게임이 수행된다.
		 * 이때, block_finish 변수를 이용하여 현재 테트리스 블럭이 완전히 둘 때까지 테트리스 블럭 생성을 막는다.
		 * game_finish 변수를 통해서 게임의 종료 조건을 확인한다.
		 */
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

					block_finish = true; //block_finish를 true로 함으로서 테트리스 블럭 생성을 막는다.
				}

				if (game_finish) {
					System.out.println("Game Finish");
					
					this.init_game(); //게임이 종료될 조건이면 초기화 시킨다.
					game_finish = false;
					continue;
				}

				Thread.sleep(1);
				if (dir < 3) {
					// 벽에 부딪히고 움직이지 않는 경우
					if (!move()) {
						dir = -1;
						move();
					}
				} else if (dir == 3) { // 위 키를 누른 경우
					if (n != 6)
						rotate();
					else {
						dir = -1;
						move();
					}

				} else if (dir == 32) { // 스페이스 바를 누른 경우
					dir = 0;
					while (true) {
						if (!move()) {
							break;
						}
					}
				}
				tetris_board.repaint(); //repaint 메소드를 이용하게 되면 revalidate() 메소드가 수행되면서 paintComponent 메소드를 호출하여 그리기를 시작한다.
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
	 * 블록 회전 하는 메소드 회전 변환 행렬을 이용하여 회전
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

	//지운 줄 수를 계산하는 메소드
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

	//테트리스 블럭을 이동시키는 메소드
	private boolean move() {
		boolean check = true;

		if (dir != -1) {
			for (Point p : b) {
				int nx = p.y + ud[dir];
				int ny = p.x + rl[dir];

				// 테트리스 블록이 배열 범위 밖으로 넘어갈 경우
				if (nx < 0 || nx >= 20 || ny < 0 || ny >= 10) {
					if (nx < 0 || nx >= 20) {
						block_finish = false;
						board_print();
						remove_line();
					}
					check = false;
					return check;
				}

				// 테트리스 블록이 존재할 경우
				if (board[nx][ny] == 1) {
					// 방향이 아랫방향일 경우
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

	// 보드판에 블록 추가
	private void board_print() {
		for (Point p : b) {
			board[p.y][p.x] = 1;
		}
	}
}
