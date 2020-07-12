package toy_project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class tetris_board extends JFrame {
	private MyPanel panel = new MyPanel();
	private int[][] board = new int[20][10];
	private static List<Point> block;
	private static boolean block_finish = false;
	private static boolean game_finish = false;

	public static void main(String[] args) {
		tetris_board board = new tetris_board();
		Random rand = new Random();
		int num = rand.nextInt(5);

		tetris_block b = new tetris_block();
		block = b.make_block(num);

	}

	public tetris_board() {
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 700);
		setResizable(false);
		setContentPane(panel);
		setVisible(true);
	}

	/*
	 * �� �׸��� ���� ��ǥ�� ���, setSize ���� ���� �ȼ� ������ ��� x,y ��ǥ�� 2���� �迭�� �ε����� ���� x => �����
	 * ����
	 */
	private class MyPanel extends JPanel {
		int ud[] = { 0, 1, 0 };
		int rl[] = { -1, 0, 1 };
		int dir = 0;
		int check = 3;

		public MyPanel() {
			this.addKeyListener(new MyKeyListener());
			this.setFocusable(true);
			this.requestFocus();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			draw_board(g);
		}

		public void draw_board(Graphics g) {
			for (Point p : block) {
				g.setColor(Color.green);
				g.fillRect(p.x * 30, p.y * 30, 30, 30);
			}
			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 10; j++) {
					if (board[i][j] == 1) {
						g.fillRect(j * 30, i * 30, 30, 30);
					}
				}
			}

			g.setColor(Color.black);
			for (int i = 1; i <= 10; i++) {
				g.drawLine(i * 30, 0, i * 30, 600);
			}

			for (int i = 1; i <= 20; i++) {
				g.drawLine(0, i * 30, 300, i * 30);
			}

		}

		private int can_move() {
			List<Point> tmp = new ArrayList<>(block);
			int priority = 3;
			for (Point p : tmp) {
				int nx = p.x + rl[dir];
				int ny = p.y + ud[dir];

				// �¿� �� ���� ������ ���
				if (nx < 0 || nx >= 10) {
					if (priority > 1)
						priority = 1;
				}

				// �ٴڿ� ���� ���
				if (ny >= 20) {
					if (priority > -1)
						priority = -1;
				}

				// �¿� �� �ȿ� �����鼭 �Ʒ��� ������ ������ ���
				if (nx >= 0 && nx < 10 && ny < 20 && board[ny][nx] == 1) {
					if (priority > -1)
						priority = -1;
				}

			}

			return priority;
		}

		private void move_block() {
			if (check == 3) {
				for (Point p : block) {
					p.y += ud[dir];
					p.x += rl[dir];
				}
				repaint(); // Ű���忡�� ���� �� �� �ٽ� �׸���.
			} else if (check == 1) {

			} else {
				for (Point p : block) {
					board[p.y][p.x] = 1;
				}

				Random rand = new Random();
				int num = rand.nextInt(5);

				tetris_block b = new tetris_block();
				block = b.make_block(num);
			}
			
			check = 3;
			dir =1;
		}

		public class MyKeyListener extends KeyAdapter {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
					dir = 1;
				else if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
					dir = 0;
				else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
					dir = 2;

				check = can_move();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				super.keyReleased(arg0);
				move_block();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				super.keyTyped(arg0);
			}
		}
	}
}