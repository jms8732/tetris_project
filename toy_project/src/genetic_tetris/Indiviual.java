package genetic_tetris;

public class Indiviual {
	int fittness = 0; // ���յ��� �Ǵ�
	int number = 0; //�ش� ������ ��ȣ
	long score =  0; //���� ����
	
	//��ġ
	double hole_weight, aggregate_weight, complete_line_weight, bumpiness_weight;

	public Indiviual(int number, double hw, double aw, double clw, double bw) {
		this.number = number;
		this.hole_weight = hw;
		this.aggregate_weight = aw;
		this.complete_line_weight = clw;
		this.bumpiness_weight = bw;
	}

}