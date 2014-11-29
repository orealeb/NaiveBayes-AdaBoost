import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;



public class NaiveBayesClassifier implements Classifier {
	
private Map<String, Double> outerLabelCounts = 
new HashMap<String, Double>();
private ArrayList<Map<String, Map<String, Integer>>> attrValLabelCounts = 
new ArrayList<Map<String, Map<String, Integer>>>();
int testCount = 0;

@Override
public void TrainClassifier(DefaultTableModel table)
{
	/**dataSet.add(table);
	
	//table
	DefaultTableModel GaussianDistribution = new DefaultTableModel();
	GaussianDistribution.addColumn(table.getColumnName(0));
	//GaussianDistribution.insertValue(0,0, table.getValue(0,0));
	
	
	  //columns
    for (int i = 1; i < table.getColumnCount(); i++)
    {
        GaussianDistribution.addColumn(table.getColumnName(i) + "Mean");
        GaussianDistribution.addColumn(table.getColumnName(i) + "Variance");
    }
    **/
    
  //calc data
	outerLabelCounts = getCountForEachLabel(table); 

	for(int i = 0; i <  table.getRowCount(); i++)
    {
    	//Object[] row = new Object[table.getColumnCount()];
		
		//for each attributes so start from index 1
        for (int j = 1; j < table.getColumnCount(); j++) {

            String value = table.getValueAt(i, j).toString();
            Map<String, Map<String, Integer>> labelCountsForValue;
           
            if (i == 0) {
            	labelCountsForValue = new HashMap<String, Map<String, Integer>>();
                attrValLabelCounts.add(labelCountsForValue);
              }
            //index attributes starts from 1
            labelCountsForValue = attrValLabelCounts.get(j-1);
            Map<String, Integer> labelCounts = labelCountsForValue.get(value);
            if (labelCounts == null) {
            	labelCounts = new HashMap<String, Integer>();
              labelCountsForValue.put(value, labelCounts);
            }
        	
        	
            Integer count = labelCounts.get((String)table.getValueAt(i, 0));
            if (count == null) {
              count = 0;
            }
            labelCounts.put((String)table.getValueAt(i, 0), count + 1);
          }	
    }
}


@Override
public String Classify(double[] obj)
{
	float maxProduct = 0;
    String predictedLabel = null;

    for (Entry<String, Double> entry: outerLabelCounts.entrySet()) {
      String label = entry.getKey();
      Double numMatchingLabel = entry.getValue();
      
      float product = 1;
     // String[] data = vector.getData();
      //start from attributes, index 1
      for (int k = 0; k < obj.length; k++) {
        // get number of x_k with label C_i
     //  System.out.println(k + " " + obj.length + " " + testCount);
        Map<String, Map<String, Integer>> attrKCounts = attrValLabelCounts.get(k);
        Map<String, Integer> valCounts = attrKCounts.get(String.valueOf(obj[k]));

        int numMatchingValLabel = 0;
        Double totalMatchingLabel = numMatchingLabel;
        if (valCounts != null && valCounts.containsKey(label)) {
          numMatchingValLabel = valCounts.get(label);
        }
        else {
          // perform Laplacian correction
          numMatchingValLabel = 1;
          totalMatchingLabel++;
        }
        
        product *= ((float) numMatchingValLabel / totalMatchingLabel);
      }
      
      if (product > maxProduct) {
        maxProduct = product;
        predictedLabel = label;
      }
    }
    
    return predictedLabel;
}

/**
public Map.Entry<String, Double> Max(Map<String, Double> score)
{
	Map.Entry<String, Double> max = new AbstractMap.SimpleEntry<String, Double>("string", (double) 0);
	
	for (Map.Entry<String, Double> entry : score.entrySet())
    {
		if(entry.getValue() > max.getValue())
		{
			max = entry;
		}
    }
	
	return max;
	
}

public static double Mean(ArrayList<Double>  m) {
	 if (m.size() < 1)
         return 0.0;	
	double sum = 0;
    for (int i = 0; i < m.size(); i++) {
        sum += m.get(i);
    }
    return sum / m.size();
}

double Variance(ArrayList<Double>  m)
{
    double mean = Mean(m);
    double temp = 0;
    for(double a :m)
        temp += (mean-a)*(mean-a);
    return temp/m.size();
}

public static double SquareRoot(double source)
{
    return Math.sqrt(source);
}

public static double NormalDist(double x, double mean, double standard_dev)
{
    double fact = standard_dev * Math.sqrt(2.0 * Math.PI);
    double expo = (x - mean) * (x - mean) / (2.0 * standard_dev * standard_dev);
    return Math.exp(-expo) / fact;
}


ArrayList<Double> SelectRows(DefaultTableModel table,int column, String labelFilter )
{
	ArrayList<Double> list = new ArrayList<Double> ();
	
	for(int i =0; i < table.getRowCount(); i++)
	{
		if(table.getValueAt(i, 0).equals((Object)labelFilter))
		{
		list.add((Double) table.getValueAt(i, column));
		}
		
	}
	return list;
	
}
**/

Map<String, Double> getCountForEachLabel(DefaultTableModel table)
{
	 Map<String, Double> results1 = new HashMap<String, Double>();
	 for(int i =0; i < table.getRowCount(); i++)
	 {
		if(results1.containsKey(table.getValueAt(i,0)))
		{
			Double val = results1.get(table.getValueAt(i,0));
			results1.put((String) table.getValueAt(i,0), val + 1);				 
		}
		else
			results1.put((String) table.getValueAt(i,0), (double) 1);				 

	 }
	return results1;
}


	
	
	

}


