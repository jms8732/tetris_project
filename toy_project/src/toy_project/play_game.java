package toy_project;

import java.util.*;

public class play_game {
	
	public static void main(String[] args) {
		PlayThread pt = new PlayThread();

		// ������ 1�ʾ� ����߸��� �ϱ� ���� ��ġ
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				PlayThread.dir = 0;
			}
		};

		timer.schedule(task, 0, 1000);

		pt.start();
	}
}