package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.table.DefaultTableModel;

public class DbTableUtil {

    public static DefaultTableModel buildTableModel(
            ResultSet rs
    ) {

        try {

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

            return model;

        } catch(Exception ex) {

            ex.printStackTrace();
        }

        return null;
    }
}
