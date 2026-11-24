package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.DbTableUtil;

public class ImportSalesStatisticsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public ImportSalesStatisticsFrame() {

        setTitle("차종별 수입 금액 및 판매 통계");

        setSize(900, 600);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JScrollPane scrollPane =
                createStatisticsTable();

        scrollPane.setBounds(20, 20, 840, 500);

        add(scrollPane);

        setVisible(true);
    }
    
    private JScrollPane createStatisticsTable() {

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

                        "cs.series_name AS 모델명, " +

                        "COUNT(DISTINCT i.car_id) AS 수입대수, " +

                        "IFNULL(SUM(i.import_price),0) AS 총수입금액, " +

                        "IFNULL(ROUND(AVG(i.import_price)),0) AS 평균수입금액, " +

                        "SUM(CASE " +
                        "WHEN inv.status='판매완료' " +
                        "THEN 1 " +
                        "ELSE 0 " +
                        "END) AS 판매대수, " +

                        "IFNULL(SUM(CASE " +
                        "WHEN inv.status='판매완료' " +
                        "THEN inv.sale_price " +
                        "ELSE 0 " +
                        "END),0) AS 총판매금액, " +

                        "IFNULL(ROUND(AVG(CASE " +
                        "WHEN inv.status='판매완료' " +
                        "THEN inv.sale_price " +
                        "END)),0) AS 평균판매금액 " +

                        "FROM car_series cs " +

                        "JOIN series_model sm " +
                        "ON cs.series_id = sm.car_series_series_id " +

                        "LEFT JOIN `import` i " +
                        "ON sm.model_id = i.series_model_model_id " +

                        "LEFT JOIN inventory inv " +
                        "ON i.car_id = inv.car_id " +

                        "GROUP BY cs.series_id, cs.series_name " +

                        "ORDER BY cs.series_id"
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