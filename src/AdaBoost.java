package classification;
import java.util.ArrayList;


import javax.swing.table.DefaultTableModel;


public class AdaBoost implements Classifier {

	 private int rounds;
	 ArrayList<Double>  alphas = new ArrayList<Double>();
	 ArrayList<Classifier> classifiers = new ArrayList<Classifier>();

	 public AdaBoost(int rounds)
	 {
		 this.rounds = rounds;
	 }
	 
	@Override
	public void TrainClassifier(DefaultTableModel table)
	{
	   int numRows = table.getRowCount();
	   double[]   weights = new double[table.getRowCount()];
	   for(int i =0; i < table.getRowCount(); i++)
		   	weights[i] = 1.0 / numRows;
		for(int i =0; i < rounds; i++)
		{
	        double error = 0.0;
	        DefaultTableModel sample = new DefaultTableModel();
	        ArrayList<Integer> randomIndecies = reSample(weights);
	        
	        for (int j = 0; j < randomIndecies.size(); j++)
		      {
	        	sample.setRowCount(sample.getRowCount()+1);   //add row
		      	for(int k =0; k < table.getColumnCount(); k++)
		       	{
		     		 if(sample.getColumnCount() <= k)
		     			sample.setColumnCount(k+1);
		     		 sample.setValueAt(table.getValueAt(randomIndecies.get(j), k), j, k);
		           		
		       	}    
	        }		
	        		
	        		
	        NaiveBayesClassifier nb = new NaiveBayesClassifier();
	        nb.TrainClassifier(sample);
	        ArrayList<String> classifications = nb.ClassifyExamples(table);
	        error = 0;
	        for(int j =0; j < classifications.size(); j++)
	        {
	           String predicted = classifications.get(j);
	           if(!predicted.equals(table.getValueAt(j, 0)) == true){
	        	   error += 1*weights[i];
  	           }
	           else{
	        	   error += 0;
	           }
	        }
	        
	        double utilAlpa = 0.0;
	        if(error == 0.0)
	        {
	        	utilAlpa = 4.0;
	        }
	        else if(error > 0.5)
	        {
	        	//discard classifier with error > 0.5
	        	continue;
	        }
	        else
	        {
	        	utilAlpa = 0.5 * Math.log((1 - error)/error);
	        }
	        alphas.add(utilAlpa);
	        classifiers.add(nb);
	        for(int k =0; k < numRows; k++)
	        {
	            String y = (String) table.getValueAt(k, 0);
	            String h = classifications.get(k);
	            int x,z=0;
	            if (h.equals("-1"))
	            	x = -1;
	            else x = 1;
	            if (y.equals("-1"))
	            	z = -1;
	            else z = 1;
	            weights[i] = weights[i] * Math.exp(-utilAlpa * x * z);
	        }
	        double sumWeights = sum(weights);
	        double [] normalizedWeights = norm(weights,sumWeights);
	        weights = normalizedWeights;

	    
		}
	}
	
	
	private ArrayList<Integer> reSample(double[] weights) {

		ArrayList<Integer> randomIndeces = new ArrayList<Integer>();
		// Now choose a random item
		while(randomIndeces.size() < weights.length)
		{
			int randomIndex = -1;
			double random = Math.random() * 1;
			for (int i = 0; i < weights.length; ++i)
			{
			    random -= weights[i];
			    if (random <= 0.0d)
			    {
			        randomIndex = i;
			        randomIndeces.add(randomIndex);
					break;
			    }
			}
		}
		
		  
		return randomIndeces;
	}

	private double[] norm(double[] weights, double sum) {
		double[] normalized = new double[weights.length];
		for(int i = 0; i < weights.length; i++)
		{
			normalized[i ]=(weights[i]/sum);
		}		
		
		return normalized;
	}

	private double sum(double[] weights) {
		double sum =0;
		for(int i = 0; i < weights.length; i++)
		{
			sum+=weights[i];
		}
		
		return sum;
	}
	
	@Override
	public String Classify(double[] obj) {
		 double classification = 0;
				 int predLabel =0;
				    for(int i=0; i< classifiers.size(); i++)
				    {
				        if (classifiers.get(i).Classify(obj).equals("+1"))
				        	predLabel = 1; 
				        else
				        	predLabel = -1;
				        classification += alphas.get(i)*predLabel;
				    }
				    if(classification > 0)			    	   
				    	return "+1" ;
				    	else return "-1";
		
	}
  

}
