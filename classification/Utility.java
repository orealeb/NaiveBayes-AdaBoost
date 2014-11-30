package classification;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class Utility {
    DefaultTableModel readFile(String filename) {
        DefaultTableModel table = new DefaultTableModel();
        BufferedReader    br    = null;

        try {
            br = new BufferedReader(new FileReader(filename));

            String line   = br.readLine();
            int    rowNum = 0;

            table.addColumn("Label");

            while (line != null) {
                if (!line.trim().isEmpty()) {
                    String[] row = line.split(" ");

                    table.setRowCount(table.getRowCount() + 1);    // add row
                    table.setValueAt(row[0], rowNum, 0);

                    for (String s : row) {
                        if (s != row[0]) {
                            String[] attrVal      = s.split(":");
                            int      attributeNum = Integer.parseInt(attrVal[0]);

                            if (table.getColumnCount() <= attributeNum) {
                                table.setColumnCount(attributeNum + 1);
                            }

                            table.setValueAt(Double.parseDouble(attrVal[1]), rowNum, attributeNum);
                        }
                    }
                }

                rowNum++;
                line = br.readLine();
            }
        } catch (IOException ex) {
            System.out.println("File not found");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return table;
    }

    double[] convertToDoubleArray(DefaultTableModel table, ArrayList<String> entry) {

        // array only for attributes
        double[] arr = new double[table.getColumnCount() - 1];

        arr = initializeArray(table.getColumnCount() - 1, arr);

        // int attrIndex = 0;
        for (String s : entry) {
            String[] attrVal = s.split(":");

            if ((Integer.parseInt(attrVal[0]) - 1) < arr.length) {
                arr[(Integer.parseInt(attrVal[0]) - 1)] = Double.parseDouble(attrVal[1]);
            }

            // attrIndex++;
        }

        return arr;
    }

    double[] initializeArray(int arraySize, double[] arr) {
        for (int j = 0; j < arraySize; j++) {
            arr[j] = 0.0;
        }

        return arr;
    }

    void initializeNullValuesToZero(DefaultTableModel table) {

        // Object obj = 1;
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                if ((table.getValueAt(i, j)) == null && (j != 0)) {
                    table.setValueAt(0.0, i, j);
                }
            }
        }
    }
}
