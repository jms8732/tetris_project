package toy_project;

import java.util.*;

public class play_game {
	
	public static void main(String[] args) {
		PlayThread pt = new PlayThread();

		// 블록을 1초씩 떨어뜨리기 하기 위한 장치
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				PlayThread.dir = 0;
			}
		};

		timer.schedule(task, 0, 1000); //1초마다 task를 수행한다.

		pt.start();
	}
}
