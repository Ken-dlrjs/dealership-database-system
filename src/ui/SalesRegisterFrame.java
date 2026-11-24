package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

import util.DbTableUtil;

public class SalesRegisterFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    int empId;

    JTextField custIdField;
    JTextField carIdField;
    JTextField saleDateField;

    JButton registerButton;
    JButton cancelButton;

    JTable customerTable;
    JTable carTable;

    public SalesRegisterFrame(int empId) {

        this.empId = empId;

        setTitle("차량 판매 등록");

        setSize(1100, 750);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();
        
        loadCustomerTable();

        loadCarTable();

        setVisible(true);
    }

    private void createUI() {

        JLabel custLabel =
                new JLabel("고객번호");

        custLabel.setBounds(20, 20, 80, 30);

        add(custLabel);

        custIdField =
                new JTextField();

        custIdField.setBounds(90, 20, 120, 30);

        add(custIdField);

        JLabel carLabel =
                new JLabel("차량번호");

        carLabel.setBounds(250, 20, 80, 30);

        add(carLabel);

        carIdField =
                new JTextField();

        carIdField.setBounds(320, 20, 120, 30);

        add(carIdField);

        JLabel dateLabel =
                new JLabel("판매일");

        dateLabel.setBounds(480, 20, 80, 30);

        add(dateLabel);

        saleDateField =
                new JTextField();

        saleDateField.setBounds(540, 20, 120, 30);

        add(saleDateField);

        registerButton =
                new JButton("판매 등록");

        registerButton.setBounds(720, 15, 120, 40);

        add(registerButton);
        
        registerButton.addActionListener(e -> {

            registerSale();
        });

        cancelButton =
                new JButton("취소");

        cancelButton.setBounds(860, 15, 120, 40);

        add(cancelButton);

        cancelButton.addActionListener(e -> {

            clearFields();
        });

        JLabel customerTitle =
                new JLabel("본인 고객 목록");

        customerTitle.setBounds(20, 90, 200, 30);

        add(customerTitle);

        customerTable =
                new JTable();

        JScrollPane customerScroll =
                new JScrollPane(customerTable);

        customerScroll.setBounds(20, 120, 1040, 220);

        add(customerScroll);

        JLabel carTitle =
                new JLabel("판매 가능 차량 목록");

        carTitle.setBounds(20, 370, 200, 30);

        add(carTitle);

        carTable =
                new JTable();

        JScrollPane carScroll =
                new JScrollPane(carTable);

        carScroll.setBounds(20, 400, 1040, 250);

        add(carScroll);
    }

    private void clearFields() {

        custIdField.setText("");

        carIdField.setText("");

        saleDateField.setText("");
    }
    
    private void loadCustomerTable() {

        try {


            Connection conn =
                    DBConnection.salesman();

            String sql =

                    "SELECT " +

                    "cust_id AS 고객번호, " +

                    "cust_name AS 고객명, " +

                    "cust_phone AS 전화번호, " +

                    "cust_email AS 이메일 " +

                    "FROM customer " +

                    "WHERE employee_emp_id=? " +

                    "ORDER BY cust_id";

            PreparedStatement pstmt =
                    conn.prepareStatement(sql);

            pstmt.setInt(
                    1,
                    empId
            );

            ResultSet rs =
                    pstmt.executeQuery();

            customerTable.setModel(
                    DbTableUtil.buildTableModel(rs)
            );

            rs.close();
            pstmt.close();
            conn.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }
    }
    
    private void loadCarTable() {

        try {


            Connection conn =
                    DBConnection.salesman();

            String sql =

                    "SELECT " +

                    "i.car_id AS 차량번호, " +

                    "CONCAT(cs.series_name,' ',sm.trim_name) AS 차량명, " +

                    "i.car_color AS 색상, " +

                    "i.sale_price AS 판매가격 " +

                    "FROM inventory i " +

                    "JOIN series_model sm " +

                    "ON i.series_model_model_id = sm.model_id " +

                    "JOIN car_series cs " +

                    "ON sm.car_series_series_id = cs.series_id " +

                    "WHERE i.status='판매가능' " +

                    "ORDER BY i.car_id";

            PreparedStatement pstmt =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    pstmt.executeQuery();

            carTable.setModel(
                    DbTableUtil.buildTableModel(rs)
            );

            rs.close();
            pstmt.close();
            conn.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }
    }
    
    private void registerSale() {

        try {

            if(custIdField.getText().trim().isEmpty()
                    || carIdField.getText().trim().isEmpty()
                    || saleDateField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "모든 항목을 입력하세요."
                );

                return;
            }

            int custId =
                    Integer.parseInt(
                            custIdField.getText()
                    );

            int carId =
                    Integer.parseInt(
                            carIdField.getText()
                    );

            Date saleDate =
                    Date.valueOf(
                            saleDateField.getText()
                    );

            if(saleDate.after(
                    new Date(
                            System.currentTimeMillis()
                    )
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "판매일은 미래일 수 없습니다."
                );

                return;
            }


            // 판매 내역 INSERT 와 재고 상태 UPDATE 를 하나의 트랜잭션으로 처리한다.
            // 둘 중 하나라도 실패하면 rollback 해서 "판매 기록은 있는데 재고는 판매가능" 같은
            // 불일치가 생기지 않게 한다.
            try (Connection conn = DBConnection.salesman()) {

                conn.setAutoCommit(false);

                try {

                    String error = registerSale(conn, custId, carId, saleDate);

                    if (error != null) {

                        conn.rollback();

                        JOptionPane.showMessageDialog(this, error);

                        return;
                    }

                    conn.commit();

                } catch (Exception txEx) {

                    conn.rollback();

                    throw txEx;
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "판매 등록 완료"
            );

            clearFields();

            loadCarTable();
            
        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "고객번호와 차량번호는 숫자만 입력하세요."
            );

        } catch(IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "날짜 형식은 YYYY-MM-DD 입니다."
            );

        } catch(Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "판매 등록 실패"
            );
        }
    }

    /**
     * 트랜잭션 안에서 실행되는 판매 등록 단계.
     * 문제가 있으면 사용자에게 보여줄 메시지를, 성공하면 null 을 반환한다.
     */
    private String registerSale(Connection conn, int custId, int carId, Date saleDate) throws Exception {

        // 1. 본인 담당 고객인지 확인
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM customer WHERE cust_id=? AND employee_emp_id=?")) {

            pstmt.setInt(1, custId);
            pstmt.setInt(2, empId);

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                if (rs.getInt(1) == 0) {
                    return "본인 담당 고객만 판매 등록 가능합니다.";
                }
            }
        }

        // 2. 차량 행을 잠그고(FOR UPDATE) 판매 가능 상태인지 확인
        //    → 두 세일즈맨이 같은 차량을 동시에 판매 등록해도 한 명만 성공한다.
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT status FROM inventory WHERE car_id=? FOR UPDATE")) {

            pstmt.setInt(1, carId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next() || !"판매가능".equals(rs.getString(1))) {
                    return "판매 가능한 차량만 등록 가능합니다.";
                }
            }
        }

        // 3. 판매 내역 등록
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO sale_history " +
                "(sale_date, customer_cust_id, employee_emp_id, inventory_car_id) " +
                "VALUES (?, ?, ?, ?)")) {

            pstmt.setDate(1, saleDate);
            pstmt.setInt(2, custId);
            pstmt.setInt(3, empId);
            pstmt.setInt(4, carId);
            pstmt.executeUpdate();
        }

        // 4. 재고 상태 변경 (판매가능 → 판매완료)
        try (PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE inventory SET status='판매완료' WHERE car_id=? AND status='판매가능'")) {

            pstmt.setInt(1, carId);

            if (pstmt.executeUpdate() != 1) {
                return "차량 상태 변경에 실패했습니다. 다시 시도하세요.";
            }
        }

        return null;
    }
}
