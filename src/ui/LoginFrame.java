	package ui;
	

import util.DBConnection;
	import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JTextField;
	import javax.swing.JPasswordField;
	import javax.swing.JRadioButton;
	import javax.swing.ButtonGroup;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.sql.Connection;
		import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import javax.swing.JOptionPane;
	
	public class LoginFrame extends JFrame implements ActionListener {
		
		private static final long serialVersionUID = 1L;
	
	    JLabel idLabel;
	    JLabel pwLabel;
	
	    JTextField idField;
	    JPasswordField pwField;
	
	    JButton loginButton;
	
	    JRadioButton adminRadio;
	    JRadioButton salesRadio;
	
	    public LoginFrame() {
	
	        setTitle("Porsche Korea");
	
	        setSize(400, 500);
	
	        setLayout(null);
	
	        setLocationRelativeTo(null);
	
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	        adminRadio = new JRadioButton("관리자");
	        adminRadio.setBounds(100, 20, 80, 30);
	        add(adminRadio);
	
	        salesRadio = new JRadioButton("세일즈맨");
	        salesRadio.setBounds(200, 20, 100, 30);
	        add(salesRadio);
	
	        ButtonGroup group = new ButtonGroup();
	        group.add(adminRadio);
	        group.add(salesRadio);
	
	        adminRadio.setSelected(true);
	
	        idLabel = new JLabel("ID");
	        idLabel.setBounds(35, 70, 80, 30);
	        add(idLabel);
	
	        idField = new JTextField();
	        idField.setBounds(125, 70, 150, 30);
	        add(idField);
	
	        pwLabel = new JLabel("PASSWORD");
	        pwLabel.setBounds(35, 120, 80, 30);
	        add(pwLabel);
	
	        pwField = new JPasswordField();
	        pwField.setBounds(125, 120, 150, 30);
	        add(pwField);
	
	        loginButton = new JButton("Login");
	        loginButton.setBounds(150, 170, 100, 40);
	        add(loginButton);
	        loginButton.addActionListener(this);
	
	        JLabel adminLabel = new JLabel("[ 관리자 계정 ]");
	        adminLabel.setBounds(35, 230, 200, 30);
	        add(adminLabel);
	
	        JButton adminBtn = new JButton("admin / ****");
	        adminBtn.setBounds(35, 270, 150, 30);
	        add(adminBtn);
	        adminBtn.addActionListener(e -> {
	        	
	        	adminRadio.setSelected(true);
	        	
	        	quickLogin("admin", "1234");
	        });
	
	        JLabel salesLabel = new JLabel("[ 세일즈맨 계정 ]");
	        salesLabel.setBounds(35, 310, 200, 30);
	        add(salesLabel);
	
	        JButton sales1Btn = new JButton("sales1 / ****");
	        sales1Btn.setBounds(35, 350, 150, 30);
	        add(sales1Btn);
	        sales1Btn.addActionListener(e -> {
	        	
	        	salesRadio.setSelected(true);
	        
	        	quickLogin("sales1", "1234");
	        });
	        
	        JButton sales2Btn = new JButton("sales2 / ****");
	        sales2Btn.setBounds(205, 350, 150, 30);
	        add(sales2Btn);
	        sales2Btn.addActionListener(e -> {
	        	
	        	salesRadio.setSelected(true);
	        
	        	quickLogin("sales2", "1234");
	        });
	        
	
	        JButton sales3Btn = new JButton("sales3 / ****");
	        sales3Btn.setBounds(35, 390, 150, 30);
	        add(sales3Btn);
	        sales3Btn.addActionListener(e -> {
	        	
	        	salesRadio.setSelected(true);
	        
	        	quickLogin("sales3", "1234");
	        });
	        
	        JButton sales4Btn = new JButton("sales4 / ****");
	        sales4Btn.setBounds(205, 390, 150, 30);
	        add(sales4Btn);
	        sales4Btn.addActionListener(e -> {
	        	
	        	salesRadio.setSelected(true);
	        
	        	quickLogin("sales4", "1234");
	        });
	        
	
	        setVisible(true);
	    }
	
	    private void quickLogin(String id, String pw) {
	
	        idField.setText(id);
	        pwField.setText(pw);
	        doLogin(id, pw);
	    }
	
	    @Override
	    public void actionPerformed(ActionEvent e) {
	
	        String id = idField.getText();
	
	        String pw = new String(pwField.getPassword());
	
	        doLogin(id, pw);
	    }
	
	    private void doLogin(String id, String pw) {
	
	        if (!adminRadio.isSelected() && !salesRadio.isSelected()) {
	            JOptionPane.showMessageDialog(null, "관리자 또는 세일즈맨을 선택해주세요.");
	            return;
	        }
	
	        String selectedRole = adminRadio.isSelected() ? "ADMIN" : "SALESMAN";
	
	        try {
	
	            
	            Connection conn;
	
	            if(selectedRole.equals("ADMIN")) {
	
	                conn = DBConnection.admin();
	
	            } else {
	
	                conn = DBConnection.salesman();
	            }
	            
	            String sql =
	                    "SELECT * FROM employee " +
	                    "WHERE account_id = ? " +
	                    "AND account_pw = ? " +
	                    "AND emp_role = ?";
	
	            PreparedStatement pstmt =
	                    conn.prepareStatement(sql);
	
	            pstmt.setString(1, id);
	            pstmt.setString(2, pw);
	            pstmt.setString(3, selectedRole);
	
	            ResultSet rs = pstmt.executeQuery();
	
	            if(rs.next()) {
	
	                String name = rs.getString("emp_name");
	                String role = rs.getString("emp_role");
	                int empId = rs.getInt("emp_id");
	
	                JOptionPane.showMessageDialog(
	                        null,
	                        name + "님 로그인 성공 (" + role + ")"
	                );
	
	                if(role.equals("ADMIN")) {
	
	                    new AdminMainFrame();
	                    dispose();
	
	                } else {
	
	                    new SalesmanMainFrame(empId);
	                    dispose();
	                }
	
	            } else {
	
	                JOptionPane.showMessageDialog(
	                        null,
	                        "로그인 실패"
	                );
	            }
	            
	            rs.close();
	            pstmt.close();
	            conn.close();
	
	        } catch(Exception ex) {
	
	            ex.printStackTrace();
	            
	            JOptionPane.showMessageDialog(
	                    null,
	                    "DB 오류 발생"
	            );
	        }
	    }
	}
