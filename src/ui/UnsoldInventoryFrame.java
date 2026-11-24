package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.DbTableUtil;

public class UnsoldInventoryFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JLabel countLabel;

    public UnsoldInventoryFrame() {

        setTitle("판매되지 않은 재고 차량");

        setSize(900, 600);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        countLabel = new JLabel();

        countLabel.setBounds(30, 20, 300, 30);

        add(countLabel);

        loadCount();

        JScrollPane scrollPane =
                createTable();

        scrollPane.setBounds(30, 70, 820, 430);

        add(scrollPane);

        setVisible(true);
    }
    
    private void loadCount() {

        try {


            Connection conn =
                    DBConnection.admin();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT COUNT(*) " +
                            "FROM inventory " +
                            "WHERE status='판매가능'"
                    );

            if(rs.next()) {

                countLabel.setText(
                        "판매되지 않은 재고 차량 : "
                        + rs.getInt(1)
                        + "대"
                );
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }
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
                    DBConnection.admin();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(

                        "SELECT " +

                        "i.car_id AS 차량번호, " +

                        "CONCAT(cs.series_name,' ',sm.trim_name) AS 차량명, " +

                        "i.car_color AS 색상, " +

                        "i.import_price AS 수입가격, " +

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