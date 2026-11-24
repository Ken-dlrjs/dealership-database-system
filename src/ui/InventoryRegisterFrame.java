package ui;


import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

import util.DbTableUtil;

public class InventoryRegisterFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextField carIdField;

    JTextField modelIdField;

    JTextField colorField;

    JTextField importPriceField;

    JTextField salePriceField;

    JButton searchButton;

    JButton registerButton;
    
    JButton updateButton;
    
    JButton cancelButton;

    public InventoryRegisterFrame() {

        setTitle("판매가능 차량 등록");

        setSize(1000, 650);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JLabel carIdLabel =
                new JLabel("차량번호");

        carIdLabel.setBounds(20, 20, 80, 30);

        add(carIdLabel);

        carIdField =
                new JTextField();

        carIdField.setBounds(20, 50, 80, 30);

        add(carIdField);

        searchButton =
                new JButton("조회");

        searchButton.setBounds(110, 50, 80, 30);

        add(searchButton);

        JLabel modelLabel =
                new JLabel("모델ID");

        modelLabel.setBounds(220, 20, 80, 30);

        add(modelLabel);

        modelIdField =
                new JTextField();

        modelIdField.setBounds(220, 50, 100, 30);

        modelIdField.setEditable(false);

        add(modelIdField);

        JLabel colorLabel =
                new JLabel("색상");

        colorLabel.setBounds(340, 20, 80, 30);

        add(colorLabel);

        colorField =
                new JTextField();

        colorField.setBounds(340, 50, 100, 30);

        colorField.setEditable(false);

        add(colorField);

        JLabel importPriceLabel =
                new JLabel("수입가격");

        importPriceLabel.setBounds(460, 20, 100, 30);

        add(importPriceLabel);

        importPriceField =
                new JTextField();

        importPriceField.setBounds(460, 50, 120, 30);

        importPriceField.setEditable(false);

        add(importPriceField);

        JLabel salePriceLabel =
                new JLabel("판매가격");

        salePriceLabel.setBounds(600, 20, 100, 30);

        add(salePriceLabel);

        salePriceField =
                new JTextField();

        salePriceField.setBounds(600, 50, 120, 30);

        add(salePriceField);

        registerButton =
                new JButton("재고 등록");

        registerButton.setBounds(730, 45, 120, 40);

        add(registerButton);
        
        updateButton =
                new JButton("재고 수정");

        updateButton.setBounds(730, 95, 120, 40);

        add(updateButton);
        
        cancelButton =
                new JButton("취소");

        cancelButton.setBounds(860, 45, 120, 40);

        add(cancelButton);

        updateButton.addActionListener(e -> {

            updateInventory();
        });

        searchButton.addActionListener(e -> {

            loadImportData();
        });

        registerButton.addActionListener(e -> {

            registerInventory();
        });
        
        cancelButton.addActionListener(e -> {

            clearFields();
        });

        JTabbedPane tabbedPane =
                new JTabbedPane();

        tabbedPane.setBounds(20, 120, 940, 440);

        tabbedPane.addTab(
                "미등록 수입 차량",
                createImportTable()
        );

        tabbedPane.addTab(
                "판매 가능 차량",
                createAvailableInventoryTable()
        );

        add(tabbedPane);

        setVisible(true);
    }

    private void loadImportData() {

        try {
        	
        	if(carIdField.getText().trim().isEmpty()) {

        	    JOptionPane.showMessageDialog(
        	            this,
        	            "차량번호를 입력하세요."
        	    );

        	    return;
        	}

            int carId =
                    Integer.parseInt(
                            carIdField.getText()
                    );


            Connection conn =
                    DBConnection.admin();

            PreparedStatement pstmt =
                    conn.prepareStatement(

                            "SELECT * " +
                            "FROM import " +
                            "WHERE car_id = ?"
                    );

            pstmt.setInt(
                    1,
                    carId
            );

            ResultSet rs =
                    pstmt.executeQuery();

            if(rs.next()) {

                PreparedStatement invStmt =
                        conn.prepareStatement(
                                "SELECT sale_price, status " +
                                "FROM inventory " +
                                "WHERE car_id=?"
                        );

                invStmt.setInt(
                        1,
                        carId
                );

                ResultSet invRs =
                        invStmt.executeQuery();

                if(invRs.next()) {

                    if("판매완료".equals(
                            invRs.getString("status")
                    )) {

                        JOptionPane.showMessageDialog(
                                this,
                                "이미 판매 완료된 차량입니다."
                        );
                        
                        clearFields();

                        invRs.close();
                        invStmt.close();
                        rs.close();
                        pstmt.close();
                        conn.close();

                        return;
                    }

                    salePriceField.setText(
                            invRs.getString("sale_price")
                    );

                } else {

                    salePriceField.setText("");
                }

                modelIdField.setText(
                        rs.getString(
                                "series_model_model_id"
                        )
                );

                colorField.setText(
                        rs.getString(
                                "car_color"
                        )
                );

                importPriceField.setText(
                        rs.getString(
                                "import_price"
                        )
                );

                invRs.close();
                invStmt.close();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "존재하지 않는 차량번호입니다."
                );
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "차량번호는 숫자만 입력하세요."
            );

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "조회 실패"
            );
        }
    }

    private void registerInventory() {

        try {
        	
        	if(carIdField.getText().trim().isEmpty()
                    || salePriceField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "차량번호와 판매가격은 빈칸일 수 없습니다."
                );

                return;
            }
        	
        	if(modelIdField.getText().trim().isEmpty()) {

        	    JOptionPane.showMessageDialog(
        	            this,
        	            "먼저 차량 조회를 해주세요."
        	    );

        	    return;
        	}

            int carId =
                    Integer.parseInt(
                            carIdField.getText()
                    );

            int salePrice =
                    Integer.parseInt(
                            salePriceField.getText()
                    );

            int importPrice =
                    Integer.parseInt(
                            importPriceField.getText()
                    );
            
            if(salePrice > 1000000000) {

                JOptionPane.showMessageDialog(
                        this,
                        "판매가격은 10억원 이하만 입력 가능합니다."
                );

                return;
            }

            if(salePrice <= importPrice) {

                JOptionPane.showMessageDialog(
                        this,
                        "판매가격은 수입가격보다 커야 합니다."
                );

                return;
            }
           


            Connection conn =
                    DBConnection.admin();

            PreparedStatement checkStmt =
                    conn.prepareStatement(

                            "SELECT COUNT(*) " +
                            "FROM inventory " +
                            "WHERE car_id = ?"
                    );

            checkStmt.setInt(
                    1,
                    carId
            );

            ResultSet rs =
                    checkStmt.executeQuery();

            rs.next();

            if(rs.getInt(1) > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "이미 재고 등록된 차량입니다."
                );

                rs.close();
                checkStmt.close();
                conn.close();

                return;
            }

            rs.close();
            checkStmt.close();

            PreparedStatement pstmt =
                    conn.prepareStatement(

                            "INSERT INTO inventory " +

                            "(car_id, " +

                            "series_model_model_id, " +

                            "import_price, " +

                            "sale_price, " +

                            "car_color, " +

                            "status) " +

                            "VALUES (?, ?, ?, ?, ?, '판매가능')"
                    );

            pstmt.setInt(
                    1,
                    carId
            );

            pstmt.setInt(
                    2,
                    Integer.parseInt(
                            modelIdField.getText()
                    )
            );

            pstmt.setInt(
                    3,
                    importPrice
            );

            pstmt.setInt(
                    4,
                    salePrice
            );

            pstmt.setString(
                    5,
                    colorField.getText()
            );

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "재고 등록 완료"
            );

            pstmt.close();
            conn.close();

            dispose();

            new InventoryRegisterFrame();

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "판매가격은 숫자만 입력하세요."
            );

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "등록 실패"
            );
        }
    }

    private JScrollPane createImportTable() {

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

                            "i.import_price AS 수입가격 " +

                            "FROM import i " +

                            "JOIN series_model sm " +

                            "ON i.series_model_model_id = sm.model_id " +

                            "JOIN car_series cs " +

                            "ON sm.car_series_series_id = cs.series_id " +

                            "WHERE NOT EXISTS " +

                            "(SELECT * FROM inventory inv " +

                            "WHERE inv.car_id = i.car_id) " +

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
    
    private JScrollPane createAvailableInventoryTable() {

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
    
    private void updateInventory() {

        try {

            if(carIdField.getText().trim().isEmpty()
                    || salePriceField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "차량번호와 판매가격을 입력하세요."
                );

                return;
            }

            int carId =
                    Integer.parseInt(
                            carIdField.getText()
                    );

            int salePrice =
                    Integer.parseInt(
                            salePriceField.getText()
                    );

            int importPrice =
                    Integer.parseInt(
                            importPriceField.getText()
                    );

            if(salePrice <= importPrice) {

                JOptionPane.showMessageDialog(
                        this,
                        "판매가격은 수입가격보다 커야 합니다."
                );

                return;
            }


            Connection conn =
                    DBConnection.admin();

            PreparedStatement pstmt =
                    conn.prepareStatement(

                            "UPDATE inventory " +

                            "SET sale_price=? " +

                            "WHERE car_id=? " +
                            
							"AND status='판매가능'"
                    );

            pstmt.setInt(
                    1,
                    salePrice
            );

            pstmt.setInt(
                    2,
                    carId
            );

            int result =
                    pstmt.executeUpdate();

            if(result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "재고 수정 완료"
                );

                dispose();

                new InventoryRegisterFrame();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "재고 등록되지 않은 차량입니다."
                );
            }

            pstmt.close();
            conn.close();

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "판매가격은 숫자만 입력하세요."
            );

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "수정 실패"
            );
        }
    }
    
    private void clearFields() {

        carIdField.setText("");

        modelIdField.setText("");

        colorField.setText("");

        importPriceField.setText("");

        salePriceField.setText("");
    }
}