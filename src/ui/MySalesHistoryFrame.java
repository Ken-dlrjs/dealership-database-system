package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DbTableUtil;

public class MySalesHistoryFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    int empId;

    public MySalesHistoryFrame(int empId) {

        this.empId = empId;

        setTitle("내 판매 내역 조회");

        setSize(1000, 600);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JScrollPane scrollPane =
                createTable();

        scrollPane.setBounds(20, 20, 940, 500);

        add(scrollPane);

        setVisible(true);
    }

    private JScrollPane createTable() {

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

                    "sh.sale_id AS 판매번호, " +

                    "sh.sale_date AS 판매일자, " +
                    
					"c.cust_id AS 고객번호, " +

                    "c.cust_name AS 고객명, " +

                    "CONCAT(cs.series_name,' ',sm.trim_name) AS 차량명, " +

                    "inv.sale_price AS 판매가격 " +

                    "FROM sale_history sh " +

                    "JOIN customer c " +

                    "ON sh.customer_cust_id = c.cust_id " +

                    "JOIN inventory inv " +

                    "ON sh.inventory_car_id = inv.car_id " +

                    "JOIN series_model sm " +

                    "ON inv.series_model_model_id = sm.model_id " +

                    "JOIN car_series cs " +

                    "ON sm.car_series_series_id = cs.series_id " +

                    "WHERE sh.employee_emp_id = ? " +

                    "ORDER BY sh.sale_id";

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
