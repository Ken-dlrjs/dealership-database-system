package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

public class SalesmanMainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	JButton availableCarButton;
	JButton priceRangeButton;
	JButton customerListButton;
	JButton salesHistoryButton;
	JButton salesRegisterButton;    
	JButton customerEditButton;
	JButton logoutButton;

    int empId;

    public SalesmanMainFrame(int empId) {

        this.empId = empId;

        setTitle("세일즈맨 화면");

        setSize(520, 400);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("세일즈맨 메인 화면");

        label.setBounds(30, 10, 200, 30);

        add(label);
        
        availableCarButton =
                new JButton("판매 가능 차량 조회");

        availableCarButton.setBounds(30, 50, 200, 40);

        add(availableCarButton);

        availableCarButton.addActionListener(e -> {

            new AvailableCarFrame();
        });
        
        JButton priceSearchButton =
                new JButton("가격 범위 차량 조회");

        priceSearchButton.setBounds(30, 100, 200, 40);

        add(priceSearchButton);

        priceSearchButton.addActionListener(e -> {

            new PriceRangeCarFrame();
        });
        
        JButton customerListButton =
                new JButton("고객 목록 조회");

        customerListButton.setBounds(30, 150, 200, 40);

        add(customerListButton);

        customerListButton.addActionListener(e -> {

            new CustomerListFrame(empId);
        });
        
        JButton salesHistoryButton =
                new JButton("내 판매 내역 조회");

        salesHistoryButton.setBounds(30, 200, 200, 40);

        add(salesHistoryButton);

        salesHistoryButton.addActionListener(e -> {

            new MySalesHistoryFrame(empId);
        });
        
        JButton salesRegisterButton =
                new JButton("차량 판매 등록");

        salesRegisterButton.setBounds(30, 250, 200, 40);

        add(salesRegisterButton);

        salesRegisterButton.addActionListener(e -> {

            new SalesRegisterFrame(empId);
        });
        
        customerEditButton =
                new JButton("내 고객 정보 수정");

        customerEditButton.setBounds(30, 300, 200, 40);

        add(customerEditButton);
        
        customerEditButton.addActionListener(e -> {

            new CustomerEditFrame(empId);
        });
        
        logoutButton =
                new JButton("Logout");

        logoutButton.setBounds(380, 300, 100, 40);

        add(logoutButton);
        
        logoutButton.addActionListener(e -> {

            dispose();

            new LoginFrame();
        });

        setVisible(true);
    }
}