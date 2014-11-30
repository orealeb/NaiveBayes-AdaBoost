package classification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

//NaiveBayes for only categorical attributes and binary classification
public class NaiveBayesClassifier implements Classifier {
	
	
private Map<String, Double> outerLabelCounts = new HashMap<String, Double>();
private ArrayList<Map<String, Map<String, Integer>>> innerLabelCounts = new ArrayList<Map<String, Map<String, Integer>>>();

@Override
public void TrainClassifier(DefaultTableModel table)
{
    
	outerLabelCounts = getCountForEachLabel(table); 

	for(int i = 0; i <  table.getRowCount(); i++)
    {		
		//for each attributes so start from index 1
        for (int j = 1; j < table.getColumnCount(); j++) {

            String value = table.getValueAt(i, j).toString();
            Map<String, Map<String, Integer>> labelCountsForValue = new HashMap<String, Map<String, Integer>>();
           
            if (i == 0) {
            	innerLabelCounts.add(labelCountsForValue);
              }
            //index attributes starts from 1
            labelCountsForValue = innerLabelCounts.get(j-1);
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

//classifies with laplacian corrrection
@Override
public String Classify(double[] obj)
{
   HashMap<String, Double> labels = new HashMap<String, Double>();
   String[] utilLabels = new String[2];
   int curLabel = 0;
    for (Entry<String, Double> entry: outerLabelCounts.entrySet()) {
      String label = entry.getKey();
      Double countOfLabel = entry.getValue();
      
      double prob = 1;
      //start from attributes, index 1
      for (int i = 0; i < obj.length; i++) {
        Map<String, Map<String, Integer>> attributeCounts = innerLabelCounts.get(i);
        Map<String, Integer> numVal = attributeCounts.get(String.valueOf(obj[i]));

        int countOfValueLabel = 0;
        Double totalSumOfLabel = countOfLabel;
        if (numVal != null && numVal.containsKey(label)) {
        	countOfValueLabel = numVal.get(label);
        } 
        else {
        	countOfValueLabel = 1;
            totalSumOfLabel++;
          }
        
        prob *= ((double) countOfValueLabel / totalSumOfLabel);
      }
      labels.put(label, (double) (prob*entry.getValue()));
      utilLabels[curLabel] = label;
      curLabel++;
    
    }
    
    if (labels.get(utilLabels[0]) > labels.get(utilLabels[1])) {
	        return utilLabels[0];
      }
    
    return utilLabels[1];
}

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

public double TestAccuracy(DefaultTableModel table) {
	
	int correct =0;
	ArrayList<String> classifications = new ArrayList<String>();
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
      	if(Classify(e).equals(label))
      	{
      		correct++;
      	}
         
      }	
	return (double)correct/classifications.size();
}


public ArrayList<String> ClassifyExamples(DefaultTableModel table) {

	ArrayList<String> classifications = new ArrayList<String>();
	  for(int i = 0; i < table.getRowCount(); i++)
      {
          ArrayList<Double> et = new ArrayList<Double>();
      	for(int j =1; j < table.getColumnCount(); j++)
      	{
     			et.add((Double) table.getValueAt(i, j));
      		
      	}
      	double [] e = new double[et.size()];
      	int w = 0;
      	for(Double d : et) {
      	  e[w] = (double) d;
      	  w++;
      	}
      	classifications.add(Classify(e));
         
      }	
	return classifications;
}


	
	
	

}


