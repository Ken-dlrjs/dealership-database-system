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

public class CustomerEditFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    int empId;

    JTextField custIdField;
    JTextField nameField;
    JTextField phoneField;
    JTextField emailField;

    JButton searchButton;
    JButton updateButton;
    JButton cancelButton;

    JTable customerTable;

    public CustomerEditFrame(int empId) {

        this.empId = empId;

        setTitle("고객 정보 수정");

        setSize(1100, 700);

        setLayout(null);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadCustomerTable();

        setVisible(true);
    }

    private void createUI() {

        JLabel custIdLabel =
                new JLabel("고객번호");

        custIdLabel.setBounds(20, 20, 80, 30);

        add(custIdLabel);

        custIdField =
                new JTextField();

        custIdField.setBounds(80, 20, 60, 30);

        add(custIdField);

        searchButton =
                new JButton("조회");

        searchButton.setBounds(160, 20, 60, 30);

        add(searchButton);

        JLabel nameLabel =
                new JLabel("고객명");

        nameLabel.setBounds(20, 80, 80, 30);

        add(nameLabel);

        nameField =
                new JTextField();

        nameField.setBounds(80, 80, 80, 30);

        add(nameField);

        JLabel phoneLabel =
                new JLabel("전화번호");

        phoneLabel.setBounds(220, 80, 80, 30);

        add(phoneLabel);

        phoneField =
                new JTextField();

        phoneField.setBounds(290, 80, 110, 30);

        add(phoneField);

        JLabel emailLabel =
                new JLabel("이메일");

        emailLabel.setBounds(460, 80, 80, 30);

        add(emailLabel);

        emailField =
                new JTextField();

        emailField.setBounds(520, 80, 160, 30);

        add(emailField);

        updateButton =
                new JButton("수정");

        updateButton.setBounds(740, 80, 120, 30);

        add(updateButton);

        cancelButton =
                new JButton("취소");

        cancelButton.setBounds(880, 80, 120, 30);

        add(cancelButton);

        customerTable =
                new JTable();

        JScrollPane scrollPane =
                new JScrollPane(customerTable);

        scrollPane.setBounds(20, 220, 1040, 380);

        add(scrollPane);

        searchButton.addActionListener(e -> {

            searchCustomer();
        });

        updateButton.addActionListener(e -> {

            updateCustomer();
        });

        cancelButton.addActionListener(e -> {

            clearFields();
        });
    }

    private void loadCustomerTable() {

        try {


            Connection conn =
                    DBConnection.salesman();

            String sql =

                    "SELECT " +

                    "cust_id AS 고객번호, " +

                    "cust_name AS 고객명, " +

                    "cust_phone AS 전화번호, " +

                    "cust_email AS 이메일 " +

                    "FROM customer " +

                    "WHERE employee_emp_id=? " +

                    "ORDER BY cust_id";

            PreparedStatement pstmt =
                    conn.prepareStatement(sql);

            pstmt.setInt(
                    1,
                    empId
            );

            ResultSet rs =
                    pstmt.executeQuery();

            customerTable.setModel(
                    DbTableUtil.buildTableModel(rs)
            );

            rs.close();
            pstmt.close();
            conn.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }
    }

    private void searchCustomer() {

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
                    DBConnection.salesman();

            String sql =

                    "SELECT * " +

                    "FROM customer " +

                    "WHERE cust_id=? " +

                    "AND employee_emp_id=?";

            PreparedStatement pstmt =
                    conn.prepareStatement(sql);

            pstmt.setInt(
                    1,
                    custId
            );

            pstmt.setInt(
                    2,
                    empId
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

            } else {
            	
            	clearFields();

                JOptionPane.showMessageDialog(
                        this,
                        "본인 고객만 조회 가능합니다."
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

            ex.printStackTrace();
        }
    }

    private void updateCustomer() {

        try {

            if(custIdField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "고객번호를 먼저 조회하세요."
                );

                return;
            }

            if(nameField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "고객명을 입력하세요."
                );

                return;
            }

            if(phoneField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "전화번호를 입력하세요."
                );

                return;
            }
            
            String phone =
                    phoneField.getText().trim();

            if(!phone.matches("\\d{3}-\\d{4}-\\d{4}")) {

                JOptionPane.showMessageDialog(
                        this,
                        "전화번호는 000-0000-0000 형식으로 입력하세요."
                );

                return;
            }
            
            String email =
                    emailField.getText().trim();

            if(!email.isEmpty()) {

                if(!email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                )) {

                    JOptionPane.showMessageDialog(
                            this,
                            "올바른 이메일 형식이 아닙니다."
                    );

                    return;
                }
            }

            int custId =
                    Integer.parseInt(
                            custIdField.getText()
                    );


            Connection conn =
                    DBConnection.salesman();
            
            if(!email.isEmpty()) {

                PreparedStatement emailCheck =
                        conn.prepareStatement(

                                "SELECT COUNT(*) " +

                                "FROM customer " +

                                "WHERE cust_email=? " +

                                "AND cust_id<>?"
                        );

                emailCheck.setString(
                        1,
                        email
                );

                emailCheck.setInt(
                        2,
                        custId
                );

                ResultSet rs =
                        emailCheck.executeQuery();

                rs.next();

                if(rs.getInt(1) > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "이미 사용 중인 이메일입니다."
                    );

                    rs.close();
                    emailCheck.close();

                    return;
                }

                rs.close();
                emailCheck.close();
            }

            String sql =

                    "UPDATE customer " +

                    "SET cust_name=?, " +

                    "cust_phone=?, " +

                    "cust_email=? " +

                    "WHERE cust_id=? " +

                    "AND employee_emp_id=?";

            PreparedStatement pstmt =
                    conn.prepareStatement(sql);

            pstmt.setString(
                    1,
                    nameField.getText()
            );

            pstmt.setString(
                    2,
                    phoneField.getText()
            );

            if(emailField.getText().trim().isEmpty()) {

                pstmt.setNull(
                        3,
                        java.sql.Types.VARCHAR
                );

            } else {

                pstmt.setString(
                        3,
                        emailField.getText()
                );
            }

            pstmt.setInt(
                    4,
                    custId
            );

            pstmt.setInt(
                    5,
                    empId
            );

            int result =
                    pstmt.executeUpdate();

            if(result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "고객 정보 수정 완료"
                );

                clearFields();
                loadCustomerTable();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "본인 고객만 수정 가능합니다."
                );
            }

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
                    "수정 실패"
            );

            ex.printStackTrace();
        }
    }

    private void clearFields() {

        custIdField.setText("");

        nameField.setText("");

        phoneField.setText("");

        emailField.setText("");
    }
}
