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

import util.DbTableUtil;

public class CustomerManageFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextField custIdField;

    JTextField nameField;

    JTextField phoneField;

    JTextField emailField;

    JTextField employeeField;

    JButton searchButton;

    JButton insertButton;

    JButton updateButton;
    
    JButton cancelButton;

    public CustomerManageFrame() {

        setTitle("고객 추가/수정");

        setSize(1000, 650);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        JScrollPane scrollPane =
                createCustomerTable();

        scrollPane.setBounds(20, 180, 940, 350);

        add(scrollPane);

        setVisible(true);
    }

    private void createUI() {

        JLabel custIdLabel =
                new JLabel("고객번호");

        custIdLabel.setBounds(20, 20, 80, 30);

        add(custIdLabel);

        custIdField =
                new JTextField();

        custIdField.setBounds(20, 50, 80, 30);

        add(custIdField);

        searchButton =
                new JButton("조회");

        searchButton.setBounds(110, 50, 80, 30);

        add(searchButton);

        JLabel nameLabel =
                new JLabel("고객명");

        nameLabel.setBounds(220, 20, 80, 30);

        add(nameLabel);

        nameField =
                new JTextField();

        nameField.setBounds(220, 50, 120, 30);

        add(nameField);

        JLabel phoneLabel =
                new JLabel("전화번호");

        phoneLabel.setBounds(360, 20, 100, 30);

        add(phoneLabel);

        phoneField =
                new JTextField();

        phoneField.setBounds(360, 50, 140, 30);

        add(phoneField);

        JLabel emailLabel =
                new JLabel("이메일");

        emailLabel.setBounds(520, 20, 80, 30);

        add(emailLabel);

        emailField =
                new JTextField();

        emailField.setBounds(520, 50, 180, 30);

        add(emailField);

        JLabel empLabel =
                new JLabel("담당직원ID");

        empLabel.setBounds(720, 20, 100, 30);

        add(empLabel);

        employeeField =
                new JTextField();

        employeeField.setBounds(720, 50, 100, 30);

        add(employeeField);

        insertButton =
                new JButton("고객 등록");

        insertButton.setBounds(840, 20, 120, 30);

        add(insertButton);

        updateButton =
                new JButton("고객 수정");

        updateButton.setBounds(840, 60, 120, 30);

        add(updateButton);
        
        cancelButton =
                new JButton("취소");

        cancelButton.setBounds(840, 100, 120, 30);

        add(cancelButton);

        searchButton.addActionListener(e -> {

            loadCustomerData();
        });

        insertButton.addActionListener(e -> {

            insertCustomer();
        });

        updateButton.addActionListener(e -> {

            updateCustomer();
        });
        
        cancelButton.addActionListener(e -> {

            clearFields();
        });
    }
    
    private void loadCustomerData() {

        try {

            if(custIdField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "고객번호를 입력하세요."
                );

                return;
            }

            int custId =
                    Integer.parseInt(
                            custIdField.getText()
                    );


            Connection conn =
                    DBConnection.admin();

            PreparedStatement pstmt =
                    conn.prepareStatement(

                            "SELECT * " +

                            "FROM customer " +

                            "WHERE cust_id=?"
                    );

            pstmt.setInt(
                    1,
                    custId
            );

            ResultSet rs =
                    pstmt.executeQuery();

            if(rs.next()) {

                nameField.setText(
                        rs.getString(
                                "cust_name"
                        )
                );

                phoneField.setText(
                        rs.getString(
                                "cust_phone"
                        )
                );

                emailField.setText(
                        rs.getString(
                                "cust_email"
                        )
                );

                employeeField.setText(
                        rs.getString(
                                "employee_emp_id"
                        )
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "존재하지 않는 고객번호입니다."
                );
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "고객번호는 숫자만 입력하세요."
            );

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "조회 실패"
            );
        }
    }

    private void insertCustomer() {

        saveCustomer(false);
    }

    private void updateCustomer() {

        saveCustomer(true);
    }

    private void saveCustomer(
            boolean isUpdate
    ) {

        try {
        	
        	if(isUpdate) {

        	    if(custIdField.getText().trim().isEmpty()) {

        	        JOptionPane.showMessageDialog(
        	                this,
        	                "고객 조회를 먼저하세요."
        	        );

        	        return;
        	    }
        	}

            if(nameField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "고객명은 필수 입력입니다."
                );

                return;
            }

            if(phoneField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "전화번호는 필수 입력입니다."
                );

                return;
            }

            if(!phoneField.getText().matches(
                    "\\d{3}-\\d{4}-\\d{4}"
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "전화번호 형식은 000-0000-0000 입니다."
                );

                return;
            }

            if(!emailField.getText().trim().isEmpty()
                    && !emailField.getText().contains("@")) {

                JOptionPane.showMessageDialog(
                        this,
                        "올바른 이메일 형식이 아닙니다."
                );

                return;
            }
            
            if(employeeField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "담당직원ID는 필수 입력입니다."
                );

                return;
            }

            int empId =
                    Integer.parseInt(
                            employeeField.getText()
                    );


            Connection conn =
                    DBConnection.admin();

            PreparedStatement checkEmp =
                    conn.prepareStatement(

                            "SELECT COUNT(*) " +

                            "FROM employee " +

                            "WHERE emp_id=?"
                    );

            checkEmp.setInt(
                    1,
                    empId
            );

            ResultSet rs =
                    checkEmp.executeQuery();

            rs.next();

            if(rs.getInt(1) == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "존재하지 않는 직원번호입니다."
                );

                rs.close();
                checkEmp.close();
                conn.close();

                return;
            }

            rs.close();
            checkEmp.close();
            
            if(!isUpdate) {

                PreparedStatement phoneCheck =
                        conn.prepareStatement(

                                "SELECT COUNT(*) " +

                                "FROM customer " +

                                "WHERE cust_phone=?"
                        );

                phoneCheck.setString(
                        1,
                        phoneField.getText()
                );

                ResultSet phoneRs =
                        phoneCheck.executeQuery();

                phoneRs.next();

                if(phoneRs.getInt(1) > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "이미 사용 중인 전화번호입니다."
                    );

                    phoneRs.close();
                    phoneCheck.close();
                    conn.close();

                    return;
                }

                phoneRs.close();
                phoneCheck.close();
            }
            
            if(isUpdate) {

                PreparedStatement phoneCheck =
                        conn.prepareStatement(

                                "SELECT COUNT(*) " +

                                "FROM customer " +

                                "WHERE cust_phone=? " +

                                "AND cust_id<>?"
                        );

                phoneCheck.setString(
                        1,
                        phoneField.getText()
                );

                phoneCheck.setInt(
                        2,
                        Integer.parseInt(
                                custIdField.getText()
                        )
                );

                ResultSet phoneRs =
                        phoneCheck.executeQuery();

                phoneRs.next();

                if(phoneRs.getInt(1) > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "이미 사용 중인 전화번호입니다."
                    );

                    phoneRs.close();
                    phoneCheck.close();
                    conn.close();

                    return;
                }

                phoneRs.close();
                phoneCheck.close();
            }

            if(isUpdate) {

                if(custIdField.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "수정할 고객번호를 입력하세요."
                    );

                    conn.close();

                    return;
                }

                PreparedStatement pstmt =
                        conn.prepareStatement(

                                "UPDATE customer " +

                                "SET cust_name=?, " +

                                "cust_phone=?, " +

                                "cust_email=?, " +

                                "employee_emp_id=? " +

                                "WHERE cust_id=?"
                        );

                pstmt.setString(
                        1,
                        nameField.getText()
                );

                pstmt.setString(
                        2,
                        phoneField.getText()
                );

                pstmt.setString(
                        3,
                        emailField.getText()
                );

                pstmt.setInt(
                        4,
                        empId
                );

                pstmt.setInt(
                        5,
                        Integer.parseInt(
                                custIdField.getText()
                        )
                );

                pstmt.executeUpdate();

                pstmt.close();

                JOptionPane.showMessageDialog(
                        this,
                        "고객 수정 완료"
                );

            } else {

                PreparedStatement pstmt =
                        conn.prepareStatement(

                                "INSERT INTO customer " +

                                "(cust_name, " +

                                "cust_phone, " +

                                "cust_email, " +

                                "employee_emp_id) " +

                                "VALUES (?, ?, ?, ?)"
                        );

                pstmt.setString(
                        1,
                        nameField.getText()
                );

                pstmt.setString(
                        2,
                        phoneField.getText()
                );

                pstmt.setString(
                        3,
                        emailField.getText()
                );

                pstmt.setInt(
                        4,
                        empId
                );

                pstmt.executeUpdate();

                pstmt.close();

                JOptionPane.showMessageDialog(
                        this,
                        "고객 등록 완료"
                );
            }

            conn.close();

            dispose();

            new CustomerManageFrame();

        } catch(NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "담당직원ID는 숫자만 입력하세요."
            );

        } catch(java.sql.SQLIntegrityConstraintViolationException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "이미 사용 중인 이메일입니다."
            );

        } catch(Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "처리 실패"
            );
        }
    }

    private JScrollPane createCustomerTable() {
    	
    	JLabel sampleLabel = new JLabel("현재 고객 목록");
    	
    	sampleLabel.setBounds(20, 150, 200, 30);
    	
    	add(sampleLabel);

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

                            "cust_id AS 고객번호, " +

                            "cust_name AS 고객명, " +

                            "cust_phone AS 전화번호, " +

                            "cust_email AS 이메일, " +

                            "employee_emp_id AS 담당직원 " +

                            "FROM customer " +

                            "ORDER BY cust_id "
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
    
    private void clearFields() {

        custIdField.setText("");

        nameField.setText("");

        phoneField.setText("");

        emailField.setText("");

        employeeField.setText("");
    }
}