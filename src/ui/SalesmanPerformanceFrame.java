package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.DbTableUtil;

public class SalesmanPerformanceFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public SalesmanPerformanceFrame() {

        setTitle("세일즈맨 판매 실적");

        setSize(900, 600);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JScrollPane scrollPane =
                createPerformanceTable();

        scrollPane.setBounds(20, 20, 840, 500);

        add(scrollPane);

        setVisible(true);
    }
    
    private JScrollPane createPerformanceTable() {

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
                    DBConnection.admin();

            Statement stmt =
                    conn.createStatement();
            
            ResultSet rs =
                    stmt.executeQuery(

                        "SELECT " +

                        "e.emp_id AS 사원번호, " +

                        "e.emp_name AS 세일즈맨명, " +

                        "COUNT(sh.sale_id) AS 판매대수, " +

                        "IFNULL(SUM(inv.sale_price),0) AS 총판매금액, " +

                        "IFNULL(ROUND(AVG(inv.sale_price)),0) AS 평균판매금액 " +

                        "FROM employee e " +

                        "LEFT JOIN sale_history sh " +

                        "ON e.emp_id = sh.employee_emp_id " +

                        "LEFT JOIN inventory inv " +

                        "ON sh.inventory_car_id = inv.car_id " +

                        "WHERE e.emp_role='SALESMAN' " +

                        "GROUP BY e.emp_id, e.emp_name " +

                        "ORDER BY e.emp_id"
                    );
            
            table.setModel(
                    DbTableUtil.buildTableModel(rs)
            );

            rs.close();
            stmt.close();
            conn.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }

        return scrollPane;
    }
}