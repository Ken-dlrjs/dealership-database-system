package ui;


import util.DBConnection;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

public class AllTableFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

    public AllTableFrame() {

        setTitle("전체 테이블 조회");

        setSize(900, 600);

        setLocationRelativeTo(null);

        JTabbedPane tabbedPane =
                new JTabbedPane();

        tabbedPane.addTab(
                "employee",
                createTablePanel("employee")
        );
        
        tabbedPane.addTab(
                "car_series",
                createTablePanel("car_series")
        );
        
        tabbedPane.addTab(
                "series_model",
                createTablePanel("series_model")
        );

        tabbedPane.addTab(
                "import",
                createTablePanel("import")
        );
        
        tabbedPane.addTab(
                "inventory",
                createTablePanel("inventory")
        );

        tabbedPane.addTab(
                "sale_history",
                createTablePanel("sale_history")
        );
        
        tabbedPane.addTab(
                "customer",
                createTablePanel("customer")
        );

        add(tabbedPane);

        setVisible(true);
    }
    
    private JScrollPane createTablePanel(
            String tableName
    ) {

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
                            "SELECT * FROM "
                                    + tableName
                    );

            ResultSetMetaData metaData =
                    rs.getMetaData();

            int columnCount =
                    metaData.getColumnCount();

            String[] columnNames =
                    new String[columnCount];

            for(int i = 0; i < columnCount; i++) {

                columnNames[i] =
                        metaData.getColumnName(i + 1);
            }

            DefaultTableModel model =
                    new DefaultTableModel(
                            columnNames,
                            0
                    );

            while(rs.next()) {

                String[] row =
                        new String[columnCount];

                for(int i = 0; i < columnCount; i++) {

                    row[i] =
                            rs.getString(i + 1);
                }

                model.addRow(row);
            }

            table.setModel(model);

            rs.close();
            stmt.close();
            conn.close();

        } catch(Exception ex) {

            ex.printStackTrace();
        }

        return scrollPane;
    }
}