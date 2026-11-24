package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import util.DbTableUtil;

public class CustomerListFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    int empId;

    public CustomerListFrame(int empId) {
    	
    	this.empId = empId;

        setTitle("고객 목록 조회");

        setSize(900, 600);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JScrollPane scrollPane =
                createCustomerTable();

        scrollPane.setBounds(20, 20, 840, 500);

        add(scrollPane);

        setVisible(true);
    }

    private JScrollPane createCustomerTable() {

        JTable table =
                new JTable() {

                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        JScrollPane scrollPane =
                new JScrollPane(table);

        try {


            Connection conn =
                    DBConnection.salesman();

            String sql =

                    "SELECT " +

                    "cust_id AS 고객번호, " +

                    "cust_name AS 고객명, " +

                    "cust_phone AS 전화번호, " +

                    "cust_email AS 이메일, " +

                    "employee_emp_id AS 담당사원번호 " +

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

            table.setModel(
                    DbTableUtil.buildTableModel(rs)
            );

            rs.close();
            pstmt.close();
            conn.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }

        return scrollPane;
    }
}