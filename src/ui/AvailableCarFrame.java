package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.DbTableUtil;

public class AvailableCarFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public AvailableCarFrame() {

        setTitle("판매 가능 차량 조회");

        setSize(900, 600);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JScrollPane scrollPane =
                createTable();

        scrollPane.setBounds(20, 20, 840, 500);

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

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(

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

                        "ORDER BY i.car_id"
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