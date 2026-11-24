package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;



import javax.swing.JOptionPane;

public class AdminMainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;

	JButton resetButton;
	JButton allTableButton;
	JButton inventoryStatusButton;
	JButton importHistoryButton;
	JButton statisticsButton;
	JButton salesmanPerformanceButton;
	JButton unsoldInventoryButton;
	JButton importManageButton;
	JButton inventoryRegisterButton;
	JButton customerManageButton;
	JButton logoutButton;
	
    public AdminMainFrame() {

        setTitle("관리자 화면");

        setSize(520, 400);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label =
                new JLabel("관리자 메인 화면");

        label.setBounds(30, 10, 200, 30);

        add(label);
        
        resetButton =
                new JButton("DB 초기화");

        resetButton.setBounds(30, 50, 200, 40);

        add(resetButton);

        resetButton.addActionListener(e -> {

            executeSqlFile();
        });
        
        allTableButton =
                new JButton("전체 테이블 조회");

        allTableButton.setBounds(30, 100, 200, 40);

        add(allTableButton);

        allTableButton.addActionListener(e -> {

            new AllTableFrame();
        });
        
        inventoryStatusButton =
                new JButton("재고 현황 조회");

        inventoryStatusButton.setBounds(30, 150, 200, 40);

        add(inventoryStatusButton);

        inventoryStatusButton.addActionListener(e -> {

            new InventoryStatusFrame();
        });
        
        importHistoryButton =
                new JButton("전체 차량 수입 내역 조회");

        importHistoryButton.setBounds(30, 200, 200, 40);

        add(importHistoryButton);

        importHistoryButton.addActionListener(e -> {

            new ImportHistoryFrame();
        });
        
        statisticsButton =
                new JButton("차종별 수입/판매 통계 조회");

        statisticsButton.setBounds(30, 250, 200, 40);

        add(statisticsButton);

        statisticsButton.addActionListener(e -> {

            new ImportSalesStatisticsFrame();
        });
        
        salesmanPerformanceButton =
                new JButton("세일즈맨별 판매 실적 조회");

        salesmanPerformanceButton.setBounds(30, 300, 200, 40);

        add(salesmanPerformanceButton);

        salesmanPerformanceButton.addActionListener(e -> {

            new SalesmanPerformanceFrame();
        });
        
        unsoldInventoryButton =
                new JButton("판매되지 않은 재고 차량");

        unsoldInventoryButton.setBounds(280, 50, 200, 40);

        add(unsoldInventoryButton);

        unsoldInventoryButton.addActionListener(e -> {

            new UnsoldInventoryFrame();
        });
        
        importManageButton =
                new JButton("차량 수입 내역 관리");

        importManageButton.setBounds(280, 100, 200, 40);

        add(importManageButton);

        importManageButton.addActionListener(e -> {

            new ImportManageFrame();
        });
        
        inventoryRegisterButton =
                new JButton("판매가능 차량 등록/수정");

        inventoryRegisterButton.setBounds(280, 150, 200, 40);

        add(inventoryRegisterButton);

        inventoryRegisterButton.addActionListener(e -> {

            new InventoryRegisterFrame();
        });
        
        customerManageButton =
                new JButton("고객 추가/수정");

        customerManageButton.setBounds(280, 200, 200, 40);

        add(customerManageButton);

        customerManageButton.addActionListener(e -> {

            new CustomerManageFrame();
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
    
    private void executeSqlFile() {

        try {

            int executedCount = util.DbInitializer.resetDatabase();

            JOptionPane.showMessageDialog(
                    null,
                    "DB 초기화 완료\n실행 SQL 수 : "
                            + executedCount
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "DB 초기화 실패\n" + ex.getMessage()
            );
        }
    }
}
