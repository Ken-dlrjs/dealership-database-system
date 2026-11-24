package ui;


import util.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import util.DbTableUtil;

public class ImportHistoryFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public ImportHistoryFrame() {

        setTitle("전체 차량 수입 내역");

        setSize(1000, 600);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JLabel importingLabel =
                new JLabel("수입중 차량");

        importingLabel.setBounds(20, 10, 200, 30);

        add(importingLabel);

        JScrollPane importingTable =
                createImportingTable();

        importingTable.setBounds(20, 40, 940, 200);

        add(importingTable);

        JLabel registeredLabel =
                new JLabel("판매등록 완료 차량");

        registeredLabel.setBounds(20, 260, 200, 30);

        add(registeredLabel);

        JScrollPane registeredTable =
                createRegisteredTable();

        registeredTable.setBounds(20, 290, 940, 220);

        add(registeredTable);

        setVisible(true);
    }

    private JScrollPane createImportingTable() {

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
                            "i.import_date AS 수입일자, " +
                            "CONCAT(cs.series_name,' ',sm.trim_name) AS 차량명, " +
                            "i.car_color AS 색상, " +
                            "i.import_price AS 수입가격 " +
                            "FROM `import` i " +
                            "LEFT JOIN inventory inv " +
                            "ON i.car_id = inv.car_id " +
                            "JOIN series_model sm " +
                            "ON i.series_model_model_id = sm.model_id " +
                            "JOIN car_series cs " +
                            "ON sm.car_series_series_id = cs.series_id " +
                            "WHERE inv.car_id IS NULL " +
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

    private JScrollPane createRegisteredTable() {

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
                            "i.import_date AS 수입일자, " +
                            "CONCAT(cs.series_name,' ',sm.trim_name) AS 차량명, " +
                            "i.car_color AS 색상, " +
                            "i.import_price AS 수입가격 " +
                            "FROM `import` i " +
                            "JOIN inventory inv " +
                            "ON i.car_id = inv.car_id " +
                            "JOIN series_model sm " +
                            "ON i.series_model_model_id = sm.model_id " +
                            "JOIN car_series cs " +
                            "ON sm.car_series_series_id = cs.series_id " +
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