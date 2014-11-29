import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.table.DefaultTableModel;


public class AdaBoost implements Classifier {
	
	 private final Map<Classifier, Double> modelErrorRates = 
		      new HashMap<Classifier, Double>();
	 private int rounds;
	 public AdaBoost(int rounds)
	 {
		 this.rounds = rounds;
	 }
	 
	@Override
	public void TrainClassifier(DefaultTableModel table)
	{
		
		int numSamples = table.getRowCount();
	    
		List<Map.Entry<Object[],Double>>  sample = initSample(table);
	    Double oldWeightsSum = 0.0;
	    for (int k = 1; k <= rounds; k++) {

	      Double errorRate = 0.0;
	      boolean[] correctlyPredicted = new boolean[numSamples];
	      NaiveBayesClassifier nb = null;
	      do {
	        nb = new NaiveBayesClassifier();
	        table = new DefaultTableModel();
	        int currentRow = 0;
	        for (Entry<Object[], Double> entry: sample) {             
		    	Object[] row = entry.getKey();
		    	table.setColumnCount(row.length);
		    	table.setRowCount(sample.size());
		    	for(int i =0; i < row.length; i++)
		    		table.setValueAt(row[i], currentRow, i);
		    	currentRow++;
            }
	        
	        nb.TrainClassifier(table);
	        
	        errorRate = 0.0;
	        oldWeightsSum = 0.0;
	        int correct =0, wrong = 0;
	        int row =0;
	        for (Entry<Object[], Double> entry: sample) { 
              double [] e = new double[entry.getKey().length-1];
              String label = (String) entry.getKey()[0];
              for(int attr = 1; attr < entry.getKey().length; attr++)
              {
            	  e[attr-1]= (double) entry.getKey()[attr];
              }
              
	           if (nb.Classify(e).equals(label)) {
	            correctlyPredicted[row] = true;
	            correct++;
	          }
	          else {
	            errorRate += entry.getValue();
	            System.out.println(entry.getValue());
	            wrong++;
	          }
	          row++;
	          oldWeightsSum += entry.getValue();
	        }
            System.out.println(correct + " " + wrong);
		    sample = getSample(sample);

	      } 
	      while (errorRate > 0.5);
	      Double newWeightsSum =0.0;
	      int currentRow = 0;
	      for (Entry<Object[], Double> entry: sample) { 
	    	  if (correctlyPredicted[currentRow]){
	    	  Double weight = entry.getValue() *(errorRate / (1 - errorRate));
              sample.get(currentRow).setValue(weight);   
	    	  }
               currentRow++;
               newWeightsSum += entry.getValue();
	      }
	      
	      currentRow = 0;
	      double multiplier = oldWeightsSum / newWeightsSum;
	      for (Entry<Object[], Double> entry: sample) { 
	    	  Double weight = entry.getValue();
            sample.get(currentRow).setValue(weight *multiplier ); 
	        currentRow++;
	      }
	      
	      modelErrorRates.put(nb, errorRate);
	      

	    }
	}
	
	List<Map.Entry<Object[],Double>>  initSample(DefaultTableModel instances)
	{		  
			List<Map.Entry<Object[],Double>> rowWeight = new ArrayList<Map.Entry<Object[],Double>>();
			  for (int j = 0; j < instances.getRowCount(); j++)
		      {
		      	Object[] obj = new Object[instances.getColumnCount()];
		      	for(int k =0; k < instances.getColumnCount(); k++)
		       	{
		     		 if(instances.getColumnCount() <= k)
		     			instances.setColumnCount(k+1);
		     		 obj[k]=instances.getValueAt(j, k);
		           		
		       	}    
		        Map.Entry<Object[],Double> pair =new java.util.AbstractMap.SimpleEntry<Object[],Double>(obj, 1.0/instances.getRowCount());
		        rowWeight.add(pair);  
	        }
			return rowWeight; 		
	}
	
	List<Map.Entry<Object[],Double>> getSample(List<Map.Entry<Object[],Double>>  sample)
    {
		List<Map.Entry<Object[],Double>>  newRowWeight = new ArrayList<Map.Entry<Object[],Double>>();
		List<Map.Entry<Object[],Double>>  newSample = new ArrayList<Map.Entry<Object[],Double>>();
		Random rand = new Random();

		   Double total = 0.0;
		    for (Entry<Object[], Double> entry: sample) {
             
		    	Object[] row = entry.getKey();
                  Double weight = entry.getValue();
                  total += weight;
                  Map.Entry<Object[],Double> pair =new java.util.AbstractMap.SimpleEntry<Object[],Double>(row, total);
                  newRowWeight.add(pair);    		   
             }
    	
		    for (int i = 0; i < newRowWeight.size(); i++ ) { 
		    	
		        int  nextIndex = rand.nextInt(newRowWeight.size()-1) + 0;
                double weight = newRowWeight.get(nextIndex).getValue();
                Object[] row = newRowWeight.get(nextIndex).getKey();	
                Map.Entry<Object[],Double> pair =new java.util.AbstractMap.SimpleEntry<Object[],Double>(row, weight);
                newSample.add(pair);    		   
            }
		    
    	return newSample;
    }
    
	
	@Override
	public String Classify(double[] obj) {
        double[] votes = new double[2];
        //+1 is index 0
	    for (Classifier classifier:  modelErrorRates.keySet()) {
	         double errorRate =  modelErrorRates.get(classifier);
	         double vote = Math.log((1 - errorRate) / errorRate);
	      
	        if(classifier.Classify(obj).equals("+1"))
      		votes[0] += vote;
      		else
      		votes[1] += vote;
	      
	    }
	    
            if (votes[0] > votes[1]) {
                return "+1";
            }
            else{ return "-1";
            }   
	}
  

}
