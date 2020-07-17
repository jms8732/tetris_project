package genetic_tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class tetris_board extends JFrame {
	private MyPanel panel = new MyPanel();
	private int[][] board;
	private Color[] color = { Color.green, Color.blue, Color.red, Color.cyan, Color.yellow, Color.MAGENTA,Color.pink };

	public tetris_board() {
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 700);
		setResizable(false);
		setContentPane(panel);
		setLocation(500, 200);
		setVisible(true);
	}

	public void set_board(int[][] board) {
		this.board = board;
	}

	/*
	 * 선 그리는 것은 좌표로 계산, setSize 같은 경우는 픽셀 단위로 계산 x,y 좌표를 2차원 배열의 인덱스로 생각 x => 뒤집어서
	 * 생각
	 */
	private class MyPanel extends JPanel {
		public MyPanel() {
			this.setFocusable(true);
			this.requestFocus();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			draw_board(g);
		}

		public void draw_board(Graphics g) {
			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 10; j++) {
					if (board[i][j] != 0) {
						g.setColor(color[board[i][j] - 1]);
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

			g.setFont(new Font("Arial", Font.ITALIC, 20));

			g.drawString("MOM gene : " + play_game.MOM, 320, 210);
			g.drawString("Maximum line : " + play_game.MAXIMUM_line, 320, 240);
			
			g.drawString("Generation : " + play_game.generation, 320, 270);
			g.drawString("Gene : " + play_game.current_gene,320, 300);

			g.drawString("lines: " + play_game.removed_line, 320, 330);
		}
	}

}
