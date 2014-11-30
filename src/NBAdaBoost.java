package classification;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.table.DefaultTableModel;


public class NBAdaBoost {

	static DefaultTableModel  table = new DefaultTableModel ();

	static Utility util = new Utility();
	
	 public static void main(String [] args)
    {
		 
		 String trainingFileName = args[0];
		 String testFileName = args[1];
		 table = util.readFile(trainingFileName);	 
		 util.initializeNullValuesToZero(table);

         Classifier adaboost = new AdaBoost(50);
         adaboost.TrainClassifier(table);
        
         //training dataset evaluation
        int truePos = 0, trueNeg = 0, falsePos = 0, falseNeg = 0;
        for(int i = 0; i < table.getRowCount(); i++)
        {
            ArrayList<Double> et = new ArrayList<Double>();
            String label = "";
        	for(int j =0; j < table.getColumnCount(); j++)
        	{
        		if(j == 0)
        			label = (String)table.getValueAt(i, j);
        		else
        			et.add((Double) table.getValueAt(i, j));
        		
        	}
        	double [] e = new double[et.size()];
        	int w = 0;
        	for(Double d : et) {
        	  e[w] = (double) d;
        	  w++;
        	}
            if (label.equals("+1"))
            {
                if (adaboost.Classify(e).equals(label))
                {
                    truePos++;
                }
                else
                {
                    falseNeg++;
                }
            }
            if (label.equals("-1"))
            {
                if (adaboost.Classify(e).equals(label))
                {
                    trueNeg++;
                }
                else
                {
                    falsePos++;

                }
            }
         }
        System.out.println(Integer.toString(truePos)+ " " + Integer.toString(falseNeg) + " " + Integer.toString(falsePos) + " " + Integer.toString(trueNeg)
        		+ " " + ((double)(truePos+trueNeg)/(truePos+trueNeg+falseNeg+falsePos))*100
        		);
        
        //test dataset evaluation
		BufferedReader br = null;
        truePos = trueNeg = falsePos = falseNeg = 0;
		 try 
        {
			 br = new BufferedReader(new FileReader(testFileName));
			 String line = br.readLine();
             //ArrayList<String> et = new ArrayList<String>();
             while (line != null)
            {
            	 if(!line.trim().isEmpty())
            	 {
            	 String[] row = line.split(" ");
            	 String label = row[0];
            	 ArrayList<String> et= new ArrayList<String>(Arrays.asList(row));  
            	 et.remove(0);
                 double[] e = util.convertToDoubleArray(table, et);
	                if (label.equals("+1") )
	                {
	                    if (adaboost.Classify(e).equals(label))
	                    {
	                        truePos++;
	                    }
	                    else
	                    {
	                        falseNeg++;
	                    }
	                }
	                if (label.equals("-1"))
	                {
	                    if (adaboost.Classify(e).equals(label))
	                    {
	                        trueNeg++;
	                    }
	                    else
	                    {
	                        falsePos++;
	
	                    }
	                }
                }
                line = br.readLine();
            }
        }
		catch(IOException ex)
		{
				System.out.println("File not found");
		}
        System.out.println(Integer.toString(truePos)+ " " + Integer.toString(falseNeg) + " " + Integer.toString(falsePos) + " " + Integer.toString(trueNeg)+ 
        		" " + ((double)(truePos+trueNeg)/(truePos+trueNeg+falseNeg+falsePos))*100
        		);
        
        
    }
	
}
