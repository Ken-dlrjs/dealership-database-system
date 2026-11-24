package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.DbTableUtil;

public class InventoryStatusFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JLabel totalLabel;
    JLabel availableLabel;
    JLabel soldLabel;

    public InventoryStatusFrame() {

        setTitle("재고 현황 조회");

        setSize(900, 650);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        totalLabel = new JLabel();
        totalLabel.setBounds(30, 20, 300, 30);
        add(totalLabel);

        availableLabel = new JLabel();
        availableLabel.setBounds(30, 50, 300, 30);
        add(availableLabel);

        soldLabel = new JLabel();
        soldLabel.setBounds(30, 80, 300, 30);
        add(soldLabel);

        loadStatistics();

        JTabbedPane tabbedPane =
                new JTabbedPane();

        tabbedPane.setBounds(
                30,
                130,
                820,
                430
        );

        tabbedPane.addTab(
                "판매 가능 차량",
                createInventoryTable("판매가능")
        );

        tabbedPane.addTab(
                "판매 완료 차량",
                createInventoryTable("판매완료")
        );

        add(tabbedPane);

        setVisible(true);
    }

    private void loadStatistics() {

        try {


            Connection conn =
                    DBConnection.admin();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs;

            rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM inventory"
            );

            if(rs.next()) {

                totalLabel.setText(
                        "전체 재고 차량 : "
                                + rs.getInt(1)
                                + "대"
                );
            }

            rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM inventory " +
                    "WHERE status='판매가능'"
            );

            if(rs.next()) {

                availableLabel.setText(
                        "판매 가능 차량 : "
                                + rs.getInt(1)
                                + "대"
                );
            }

            rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM inventory " +
                    "WHERE status='판매완료'"
            );

            if(rs.next()) {

                soldLabel.setText(
                        "판매 완료 차량 : "
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

    private JScrollPane createInventoryTable(
            String status
    ) {

        JTable table =
                new JTable() {

                    private static final long serialVersionUID = 1L;

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
                            "i.car_id, " +
                            "CONCAT(cs.series_name, ' ', sm.trim_name) AS 차량명, " +
                            "i.car_color, " +
                            "i.import_price, " +
                            "i.sale_price, " +
                            "i.status " +
                            "FROM inventory i " +
                            "JOIN series_model sm " +
                            "ON i.series_model_model_id = sm.model_id " +
                            "JOIN car_series cs " +
                            "ON sm.car_series_series_id = cs.series_id " +
                            "WHERE i.status = '" + status + "' " +
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
