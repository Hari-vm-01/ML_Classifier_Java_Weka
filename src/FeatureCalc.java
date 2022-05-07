import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/* A helper class to calculate ML features */

public class FeatureCalc{
	
	Instances dataset;	
	List<String> classLabels;
	int nfeatures;
	boolean isFirstInstance = true;

	public FeatureCalc(List<String> classLabels) {
		this.classLabels = classLabels;
	}
	/*Extracting Data into Instance*/
	private Instance instanceFromArray(double[] valueArray, String label) {
		Instance instance = new DenseInstance(1.0, valueArray);
		/* Handle cases when Label was not provided as well defined input*/
		instance.setDataset(dataset);
		if(label != null) {
			instance.setClassValue(label);
		} else {
			instance.setClassMissing();
		}

		return instance;
	}

	
	
	
}