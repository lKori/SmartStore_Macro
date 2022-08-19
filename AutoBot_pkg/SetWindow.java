package AutoBot_pkg;


import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class SetWindow {
	public static AutoInput bot = new AutoInput();
	
	public static void SelectMode() {
		JFrame window = new JFrame("스마트 스토어 자동 입력 매크로");	// 프레임 생성
		window.setSize(500, 400);	// 프레임 크기 설정
		window.getContentPane().setBackground(Color.DARK_GRAY);
		window.setLocationRelativeTo(null);	// 프레임을 화면 중앙에 배치
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 프레임 닫으면 메모리에서 삭제
		
		
		window.getContentPane().setLayout(null);	// 레이아웃 설정
		
		// 버튼 생성
		JButton btn1 = new JButton("SATA");
		JButton btn2 = new JButton("Enterprise");
		// 버튼위치 및 크기 설정(x좌표, y좌표, 가로길이, 세로길이)
		btn1.setBounds(92, 200, 100, 50);
		btn1.setBackground(Color.WHITE);
		btn2.setBounds(292, 200, 100, 50);
		btn2.setBackground(Color.WHITE);
		
		// 프레임에 버튼 추가
		window.getContentPane().add(btn1);
		window.getContentPane().add(btn2);
		
		// 라벨 설정
		JLabel label = new JLabel();
		label.setOpaque(true);
		label.setBackground(null);
		label.setForeground(Color.WHITE);
		label.setBounds(117, 80, 250, 100);
		label.setText("제품군을 선택해주세요");
		label.setHorizontalAlignment(JLabel.CENTER);	// 글자 가운데 정렬
		Font myFont = new Font("Gulim", Font.BOLD, 20);
		label.setFont(myFont);
		window.getContentPane().add(label);
		
		// SATA 선택
		btn1.addActionListener(event -> {
			bot.SHEET_NUM = 1;
			bot.ProductCheck();
			window.dispose();
			bot.AutoBot();
		});
		// Enterprise 선택
		btn2.addActionListener(event -> {
			bot.SHEET_NUM = 2;
			bot.ProductCheck();
			window.dispose();
			bot.AutoBot();
		});
		
		window.setVisible(true);	// 프레임 보이게 설정
		
		System.out.println(window.getContentPane().getSize());	// 프레임 컨텐츠 영역 크기 출력
	}

	public static void main(String[] args) {
		SelectMode(); // 실행
//		a.AutoBot();
		}
	}
