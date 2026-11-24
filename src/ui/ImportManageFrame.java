package ui;


import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.sql.ResultSet;
import java.sql.Statement;
import util.DbTableUtil;

public class ImportManageFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextField modelIdField;
    JTextField dateField;
    JTextField priceField;
    JTextField colorField;
    JTextField carIdField;

    JButton insertButton;
    JButton searchButton;
    JButton updateButton;
    JButton cancelButton;

    public ImportManageFrame() {

        setTitle("차량 수입 내역 관리");

        setSize(1100, 650);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );
        
        JLabel carIdLabel =
                new JLabel("차량번호");

        carIdLabel.setBounds(20, 20, 80, 30);

        add(carIdLabel);

        JLabel modelLabel =
                new JLabel("모델ID");

        modelLabel.setBounds(220, 20, 80, 30);

        add(modelLabel);

        JLabel dateLabel =
                new JLabel("수입 날짜");

        dateLabel.setBounds(340, 20, 80, 30);

        add(dateLabel);

        JLabel priceLabel =
                new JLabel("수입가격");

        priceLabel.setBounds(480, 20, 80, 30);

        add(priceLabel);

        JLabel colorLabel =
                new JLabel("색상");

        colorLabel.setBounds(620, 20, 80, 30);

        add(colorLabel);
        
        carIdField =
                new JTextField();

        carIdField.setBounds(20, 50, 80, 30);

        add(carIdField);
     
        modelIdField =
                new JTextField();

        modelIdField.setBounds(220, 50, 100, 30);

        add(modelIdField);

        dateField =
                new JTextField();

        dateField.setBounds(340, 50, 120, 30);

        add(dateField);

        priceField =
                new JTextField();

        priceField.setBounds(480, 50, 120, 30);

        add(priceField);

        colorField =
                new JTextField();

        colorField.setBounds(620, 50, 120, 30);

        add(colorField);

        insertButton =
                new JButton("수입 등록");

        insertButton.setBounds(750, 45, 100, 40);

        add(insertButton);

        insertButton.addActionListener(e -> {

            insertImport();
        });
        
        searchButton =
                new JButton("조회");

        searchButton.setBounds(110, 50, 80, 30);

        add(searchButton);

        searchButton.addActionListener(e -> {

            loadImportData();
        });
        
        updateButton =
                new JButton("수정");

        updateButton.setBounds(860, 45, 100, 40);

        add(updateButton);

        updateButton.addActionListener(e -> {

            updateImport();
        });
        
        cancelButton =
                new JButton("취소");

        cancelButton.setBounds(970, 45, 100, 40);

        add(cancelButton);
        
        cancelButton.addActionListener(e -> {

            clearFields();
        });
        
        JLabel sampleLabel =
                new JLabel("수입 내역 데이터");

        sampleLabel.setBounds(20, 130, 200, 30);

        add(sampleLabel);

        JScrollPane scrollPane =
                createSampleTable();

        scrollPane.setBounds(20, 170, 940, 330);

        add(scrollPane);

        setVisible(true);
    }

    private void insertImport() {

        try {
        	
        	if(modelIdField.getText().trim().isEmpty()
                    || dateField.getText().trim().isEmpty()
                    || priceField.getText().trim().isEmpty()
                    || colorField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "모든 항목을 입력하세요 (차량번호 제외)."
                );

                return;
            }

            int modelId;
            int price;

            try {

                modelId =
                        Integer.parseInt(
                                modelIdField.getText()
                        );

                price =
                        Integer.parseInt(
                                priceField.getText()
                        );

            } catch(NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "모델ID와 수입가격은 숫자만 입력하세요."
                );

                return;
            }
            
            if(modelId < 1 || modelId > 16) {
            	
            	JOptionPane.showMessageDialog(
                        this,
                        "모델ID는 1번 ~ 16번 사이여야 합니다."
                );

                return;
            }

            if(!dateField.getText().matches(
                    "\\d{4}-\\d{2}-\\d{2}"
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "날짜 형식은 YYYY-MM-DD 입니다."
                );

                return;
            }
            
            if(price < 50000000
                    || price > 500000000) {

                JOptionPane.showMessageDialog(
                        this,
                        "수입가격은 50,000,000원 ~ 500,000,000원 사이여야 합니다."
                );

                return;
            }
            
            if(colorField.getText().matches(".*\\d.*")) {

                JOptionPane.showMessageDialog(
                        this,
                        "색상에는 숫자를 입력할 수 없습니다."
                );

                return;
            }


            Connection conn =
                    DBConnection.admin();

            String sql =

                    "INSERT INTO import " +

                    "(series_model_model_id, " +

                    "import_date, " +

                    "import_price, " +

                    "car_color) " +

                    "VALUES (?, ?, ?, ?)";

            PreparedStatement pstmt =
                    conn.prepareStatement(sql);

            pstmt.setInt(
                    1,
                    Integer.parseInt(
                            modelIdField.getText()
                    )
            );

            pstmt.setString(
                    2,
                    dateField.getText()
            );

            pstmt.setInt(
                    3,
                    Integer.parseInt(
                            priceField.getText()
                    )
            );

            pstmt.setString(
                    4,
                    colorField.getText()
            );

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    null,
                    "수입 등록 완료"
            );
            
            dispose();

            new ImportManageFrame();

            modelIdField.setText("");
            dateField.setText("");
            priceField.setText("");
            colorField.setText("");

            pstmt.close();
            conn.close();

        } catch(Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "등록 실패"
                    + ex.getMessage()
            );
        }
    }
    
    private JScrollPane createSampleTable() {

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

                        "car_id AS 차량번호, " +

                        "series_model_model_id AS 모델ID, " +

                        "import_date AS 수입일, " +

                        "import_price AS 수입가격, " +

                        "car_color AS 색상 " +

                        "FROM `import` " +

                        "ORDER BY car_id "
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

                        "FROM `import` " +

                        "WHERE car_id = ?"
                    );

            pstmt.setInt(
                    1,
                    carId
            );

            ResultSet rs =
                    pstmt.executeQuery();

            if(rs.next()) {

                modelIdField.setText(
                        rs.getString(
                                "series_model_model_id"
                        )
                );

                dateField.setText(
                        rs.getString(
                                "import_date"
                        )
                );

                priceField.setText(
                        rs.getString(
                                "import_price"
                        )
                );

                colorField.setText(
                        rs.getString(
                                "car_color"
                        )
                );

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
    
    private void updateImport() {

        try {

            if(carIdField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "수정할 차량번호를 조회하세요."
                );

                return;
            }
            
            if(modelIdField.getText().trim().isEmpty()
                    || dateField.getText().trim().isEmpty()
                    || priceField.getText().trim().isEmpty()
                    || colorField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "모든 항목을 입력하세요."
                );

                return;
            }

            int carId =
                    Integer.parseInt(
                            carIdField.getText()
                    );

            int modelId =
                    Integer.parseInt(
                            modelIdField.getText()
                    );

            int price =
                    Integer.parseInt(
                            priceField.getText()
                    );

            if(modelId < 1 || modelId > 16) {

                JOptionPane.showMessageDialog(
                        this,
                        "모델ID는 1~16 사이여야 합니다."
                );

                return;
            }
            
            if(!dateField.getText().matches(
                    "\\d{4}-\\d{2}-\\d{2}"
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "날짜 형식은 YYYY-MM-DD 입니다."
                );

                return;
            }

            if(price < 50000000
                    || price > 500000000) {

                JOptionPane.showMessageDialog(
                        this,
                        "수입가격은 50,000,000원 ~ 500,000,000원 사이여야 합니다."
                );

                return;
            }

            if(colorField.getText().matches(".*\\d.*")) {

                JOptionPane.showMessageDialog(
                        this,
                        "색상에는 숫자를 입력할 수 없습니다."
                );

                return;
            }


            Connection conn =
                    DBConnection.admin();

            String sql =

                    "UPDATE `import` " +

                    "SET series_model_model_id=?, " +

                    "import_date=?, " +

                    "import_price=?, " +

                    "car_color=? " +

                    "WHERE car_id=?";

            PreparedStatement pstmt =
                    conn.prepareStatement(sql);

            pstmt.setInt(
                    1,
                    modelId
            );

            pstmt.setString(
                    2,
                    dateField.getText()
            );

            pstmt.setInt(
                    3,
                    price
            );

            pstmt.setString(
                    4,
                    colorField.getText()
            );

            pstmt.setInt(
                    5,
                    carId
            );

            int result =
                    pstmt.executeUpdate();

            if(result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "수정 완료"
                );

                dispose();

                new ImportManageFrame();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "존재하지 않는 차량번호입니다."
                );
            }

            pstmt.close();
            conn.close();

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "차량번호, 모델ID, 수입가격은 숫자만 입력하세요."
            );

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "수정 실패\n" +
                    ex.getMessage()
            );
        }
    }
    
    private void clearFields() {

        carIdField.setText("");
        modelIdField.setText("");
        dateField.setText("");
        priceField.setText("");
        colorField.setText("");
    }
}