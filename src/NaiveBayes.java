import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;


public class NaiveBayes {
	
	
	static DefaultTableModel  table = new DefaultTableModel ();

	
	 public static void main(String [ ] args) throws FileNotFoundException, IOException
    {
		 
		 
		 try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Oreoluwa\\Desktop\\dataset_assign4\\dataset\\adult.train")))
			{
	 
			 String line;
             int rowNum = 0;
             table.addColumn("Label");
             while ((line = br.readLine()) != null)
             {
            	 String[] row = line.split(" ");
                 table.setRowCount(table.getRowCount()+1);   //add row
                 table.setValueAt(row[0], rowNum,0);
                 for (String s: row)
                 {
                     if (s != row[0])
                     {
						String[] attrVal = s.split(":");
                        int attributeNum = Integer.parseInt(attrVal[0]);
                        if(table.getColumnCount() <= attributeNum)
                        	table.setColumnCount(attributeNum+1);
                        table.setValueAt(Double.parseDouble(attrVal[1]), rowNum,attributeNum);
                     }
                 }
                 rowNum++;

			}	
			}
		 
         initializeValuesToZero();

         Classifier nbClassifier = new NaiveBayesClassifier();
         nbClassifier.TrainClassifier(table);
        //Classifier classifier = new Classifier();
        //classifier.TrainClassifier(table);

	
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
                if (nbClassifier.Classify(e).equals(label))
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
                if (nbClassifier.Classify(e).equals(label))
                {
                    trueNeg++;
                }
                else
                {
                    falsePos++;

                }
            }
            // Console.WriteLine(index.ToString() + " " + classifier.Classify(e) + " " + res);
            //index++;
        }
        System.out.println(Integer.toString(truePos)+ " " + Integer.toString(falseNeg) + " " + Integer.toString(falsePos) + " " + Integer.toString(trueNeg)
        		+ " " + ((double)(truePos+trueNeg)/(truePos+trueNeg+falseNeg+falsePos))*100
        		);
        truePos = trueNeg = falsePos = falseNeg = 0;
		 try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Oreoluwa\\Desktop\\dataset_assign4\\dataset\\adult.test")))
        {
			 String line; 
             //ArrayList<String> et = new ArrayList<String>();
             while ((line = br.readLine()) != null)
            {
            	 String[] row = line.split(" ");
            	 String label = row[0];
            	 ArrayList<String> et= new ArrayList<String>(Arrays.asList(row));  
            	 et.remove(0);
                 double[] e = convertToDoubleArray(et);
                if (label.equals("+1") )
                {
                    if (nbClassifier.Classify(e).equals(label))
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
                    if (nbClassifier.Classify(e).equals(label))
                    {
                        trueNeg++;
                    }
                    else
                    {
                        falsePos++;

                    }
                }
               // Console.WriteLine(index.ToString() + " " + classifier.Classify(e) + " " + res);
                //index++;
            }
        }
        System.out.println(Integer.toString(truePos)+ " " + Integer.toString(falseNeg) + " " + Integer.toString(falsePos) + " " + Integer.toString(trueNeg)+ 
        		" " + ((double)(truePos+trueNeg)/(truePos+trueNeg+falseNeg+falsePos))*100
        		);
        
        
    }
	 
     static double[] convertToDoubleArray(ArrayList<String> entry)
     {
    	 //array only for attributes
         double[] arr = new double[table.getColumnCount()-1];
         arr = initializeArray(table.getColumnCount()-1, arr);
        // int attrIndex = 0;
         for(String s : entry)
         {
        	 String[] attrVal = s.split(":");
             if ((Integer.parseInt(attrVal[0]) - 1) < arr.length)
             {
                 arr[(Integer.parseInt(attrVal[0]) - 1)] = Double.parseDouble(attrVal[1]);
             }
            //     attrIndex++;
         }
         return arr;
     }
     
     static double [] initializeArray(int arraySize, double []arr )
     {
        
             for (int j = 0; j < arraySize; j++)
             {
                 
                     arr[j] = 0.0;
                 
             }
        return arr;
     }

	static void initializeValuesToZero()
       {
           //Object obj = 1;
           for (int i = 0; i < table.getRowCount(); i++)
           {
               for (int j = 0; j < table.getColumnCount(); j++)
               {
                   if ((table.getValueAt(i,j)) == null && j != 0)
                   {
                       table.setValueAt(0.0, i,j);
                   }
               }
           }
       }

}
