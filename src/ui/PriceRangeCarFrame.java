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

import util.DbTableUtil;

public class PriceRangeCarFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextField minPriceField;
    JTextField maxPriceField;

    JTable table;

    JButton searchButton;
    JButton cancelButton;

    public PriceRangeCarFrame() {

        setTitle("가격 범위 차량 조회");

        setSize(950, 650);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JLabel minLabel =
                new JLabel("최소 가격");

        minLabel.setBounds(20, 20, 100, 30);

        add(minLabel);

        minPriceField =
                new JTextField();

        minPriceField.setBounds(20, 50, 150, 30);

        add(minPriceField);

        JLabel maxLabel =
                new JLabel("최대 가격");

        maxLabel.setBounds(220, 20, 100, 30);

        add(maxLabel);

        maxPriceField =
                new JTextField();

        maxPriceField.setBounds(220, 50, 150, 30);

        add(maxPriceField);

        searchButton =
                new JButton("조회");

        searchButton.setBounds(420, 45, 100, 40);

        add(searchButton);

        cancelButton =
                new JButton("취소");

        cancelButton.setBounds(540, 45, 100, 40);

        add(cancelButton);

        table =
                new JTable();

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBounds(20, 120, 880, 430);

        add(scrollPane);

        searchButton.addActionListener(e -> {

            searchCars();
        });

        cancelButton.addActionListener(e -> {

            clearFields();
        });

        setVisible(true);
    }

    private void searchCars() {

        try {

            Integer minPrice = null;
            Integer maxPrice = null;

            if(!minPriceField.getText().trim().isEmpty()) {

                minPrice =
                        Integer.parseInt(
                                minPriceField.getText()
                        );
            }

            if(!maxPriceField.getText().trim().isEmpty()) {

                maxPrice =
                        Integer.parseInt(
                                maxPriceField.getText()
                        );
            }

            if(minPrice == null && maxPrice == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "최소 가격 또는 최대 가격을 입력하세요."
                );

                return;
            }

            if((minPrice != null && minPrice < 0)
                    || (maxPrice != null && maxPrice < 0)) {

                JOptionPane.showMessageDialog(
                        this,
                        "가격은 0 이상이어야 합니다."
                );

                return;
            }

            if(minPrice != null
                    && maxPrice != null
                    && minPrice > maxPrice) {

                JOptionPane.showMessageDialog(
                        this,
                        "최소 가격은 최대 가격보다 클 수 없습니다."
                );

                return;
            }


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

                    "WHERE i.status='판매가능' ";

            if(minPrice != null && maxPrice != null) {

                sql +=
                        "AND i.sale_price BETWEEN ? AND ? ";

            } else if(minPrice != null) {

                sql +=
                        "AND i.sale_price >= ? ";

            } else {

                sql +=
                        "AND i.sale_price <= ? ";
            }

            sql +=
                    "ORDER BY i.car_id";

            PreparedStatement pstmt =
                    conn.prepareStatement(sql);

            if(minPrice != null
                    && maxPrice != null) {

                pstmt.setInt(
                        1,
                        minPrice
                );

                pstmt.setInt(
                        2,
                        maxPrice
                );

            } else if(minPrice != null) {

                pstmt.setInt(
                        1,
                        minPrice
                );

            } else {

                pstmt.setInt(
                        1,
                        maxPrice
                );
            }

            ResultSet rs =
                    pstmt.executeQuery();

            if(!rs.isBeforeFirst()) {

                JOptionPane.showMessageDialog(
                        this,
                        "조건에 맞는 차량이 없습니다."
                );

                table.setModel(
                        new javax.swing.table.DefaultTableModel()
                );

                rs.close();
                pstmt.close();
                conn.close();

                return;
            }

            table.setModel(
                    DbTableUtil.buildTableModel(rs)
            );

            rs.close();
            pstmt.close();
            conn.close();

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "가격은 숫자만 입력하세요."
            );

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "조회 실패"
            );

            ex.printStackTrace();
        }
    }
    
    private void clearFields() {

        minPriceField.setText("");

        maxPriceField.setText("");

        table.setModel(
                new javax.swing.table.DefaultTableModel()
        );
    }
}