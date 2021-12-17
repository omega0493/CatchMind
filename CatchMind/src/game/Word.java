package game;

public class Word {
	
	public static void main(String[] args) {
		Word wd = new Word();
		for(int I=0; I<50; I++)
			System.out.println(wd.getStr());

	}
	
	int[] rNum = new int[50];
	String wordList[] = {
			"����", "����","ȣ����", "�����", "������", "��踻", "�ϸ�", "�⸰", "���", "����", 	
			"��", "�ƺ�ī��", "���", "������", "�ٳ���", "Ű��", "û����","��", "��ȭ��", "����",	
			"������", "�ҹ��", "��ȣ��", "������", "�ǻ�", "BTS", "����ũ", "�Ѱ�","�Ƕ�̵�", "����ũ��",			
			"����", "�Ϻ�", "����", "�ĸ�", "�ѱ�", "�̱�","��", "�౸", "�豸", "�߱�",		
			"�뷡", "����", "¥���", "«��", "������", "������ġ", "�����ݶ�", "�ｺ��", "�ڷγ�", "����ũ"};		
	
	String[] wordQuiz = new String[50];
	String[] alreadyWord = new String[50]; 
	int num = 0;
	int flag = 0;
	
	public Word() {

		// ����
		for(int I=0; I<rNum.length; I++) {
			rNum[I] = (int)(Math.random()*50)+0;
			// �ߺ� ����
			for(int j=0; j<I; j++) {
				if(rNum[j] == rNum[I]) {
					I--;
					break;
				}
			}
			this.wordQuiz[I] = this.wordList[rNum[I]];
		}
	
		num++;
	}
	
	public String getStr() {
		String a = wordList[rNum[flag]];
		flag++;
		return a;
	}
}
