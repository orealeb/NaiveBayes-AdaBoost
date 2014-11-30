package classification;
import javax.swing.table.DefaultTableModel;



public interface Classifier {

	
	
	public abstract String Classify(double[] obj);

	public abstract void TrainClassifier(DefaultTableModel table);


}
