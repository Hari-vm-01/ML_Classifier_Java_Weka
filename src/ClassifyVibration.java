import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PApplet;
import processing.sound.AudioIn;
import processing.sound.FFT;
import processing.sound.Sound;
import processing.sound.Waveform;
import weka.classifiers.functions.SMO;

/* A class with the main function and Processing visualizations to run the demo */

public class ClassifyVibration extends PApplet {

	FFT fft;
	AudioIn in;
	Waveform waveform;
	int bands = 512;
	int nsamples = 1024;
	float[] spectrum = new float[bands];
	float[] fftFeatures = new float[bands];
	String[] classNames = {"quiet", "hand drill", "whistling", "class clapping"};
	int classIndex = 0;
	int dataCount = 0;

	MLClassifier classifier;
	boolean flagL=true;
	Map<String, List<DataInstance>> trainingData = new HashMap<>();
	{for (String className : classNames){
		trainingData.put(className, new ArrayList<DataInstance>());
	}}
	
	DataInstance captureInstance (String label){
		DataInstance res = new DataInstance();
		res.label = label;
		res.measurements = fftFeatures.clone();
		return res;
	}
	
	public static void main(String[] args) {
		PApplet.main("ClassifyVibration");
	}
	
	public void settings() {
		size(512, 400);
	}

	public void setup() {
		
		/* list all audio devices */
		Sound.list();
		Sound s = new Sound(this);
		  
		/* select microphone device */
		s.inputDevice(4);
		    
		/* create an Input stream which is routed into the FFT analyzer */
		fft = new FFT(this, bands);
		in = new AudioIn(this, 0);
		waveform = new Waveform(this, nsamples);
		waveform.input(in);
		
		/* start the Audio Input */
		in.start();
		  
		/* patch the AudioIn */
		fft.input(in);
	}

	public void draw() {
		background(0);
		fill(0);
		stroke(255);
		
		waveform.analyze();

		beginShape();
		  
		for(int i = 0; i < nsamples; i++)
		{
			vertex(
					map(i, 0, nsamples, 0, width),
					map(waveform.data[i], -1, 1, 0, height)
					);
		}
		
		endShape();

		fft.analyze(spectrum);

		for(int i = 0; i < bands; i++){

			/* the result of the FFT is normalized */
			/* draw the line for frequency band i scaling it up by 40 to get more amplitude */
			line( i, height, i, height - spectrum[i]*height*40);
			fftFeatures[i] = spectrum[i];
		} 

		fill(255);
		textSize(30);
		if(classifier != null) {
			String guessedLabel = classifier.classify(captureInstance(null));
			text("classified as: " + guessedLabel, 20, 30);
		}else {
			text(classNames[classIndex], 20, 30);
			dataCount = trainingData.get(classNames[classIndex]).size();
			text("Data collected: " + dataCount, 20, 60);
		}
	}
	
	public void keyPressed() {
		if (key == '.') {
			classIndex = (classIndex + 1) % classNames.length;
		}
		
		else if (key == 't') {
			if(classifier == null) {
				println("Start training ...");
				classifier = new MLClassifier();
				try {
					classifier.train(trainingData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				println(classifier);
			}
//			else if(flagL==true) {
//				//classifier = null;
//				//println(classifier);
//				String guessedLabel = classifier.classify(captureInstance(null));
//				text("classified as: " + guessedLabel, 20, 30);
//			}
			else {
				classifier =null;
			}
		}
		
		else if (key == 's') {
			// Yang: add code to save your trained model for later use
			
			try {
				weka.core.SerializationHelper.write("E:/Downloads/UCLA_Courses/Spring_2022/209AS_Interactive_Systems/Bakeoff_Project/IntMLDemo/save_model.model", classifier);
				//println(classifier);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//println(classifier);
			}
			
//			ObjectOutputStream oos = null;
//			//MLClassifier classifier;
//			String name="Save";
//		    try {
//		        oos = new ObjectOutputStream(
//		                new FileOutputStream("E:/Downloads/UCLA_Courses/Spring_2022/209AS_Interactive_Systems/Bakeoff_Project/IntMLDemo" + name + ".model"));
//		        println(oos);
//		    } catch (FileNotFoundException e1) {
//		        e1.printStackTrace();
//		    } catch (IOException e1) {
//		        e1.printStackTrace();
//		    }
//		    try {
//				oos.writeObject(classifier);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    try {
//				oos.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    try {
//				oos.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
		
		else if (key == 'l') {
			// Yang: add code to load your previously trained model
			//String guessedLabel = classifier.classify(captureInstance(null));
			//text("classified as: " + guessedLabel, 20, 30);
			
//			try {
//				classifier =(MLClassifier) weka.core.SerializationHelper.read("E:/Downloads/UCLA_Courses/Spring_2022/209AS_Interactive_Systems/Bakeoff_Project/IntMLDemo/save_model.model");
//				println(classifier);
//			if(classifier != null) {
//					String guessedLabel = classifier.classify(captureInstance(null));
//					text("classified as: " + guessedLabel, 20, 30);
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				//println(classifier2);
//			
//	}
			
			ObjectInputStream ois=null;
			try {
				ois = new ObjectInputStream(new FileInputStream("E:/Downloads/UCLA_Courses/Spring_2022/209AS_Interactive_Systems/Bakeoff_Project/IntMLDemo/Savedemo.model"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				MLClassifier cls = (MLClassifier) ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		else {
			trainingData.get(classNames[classIndex]).add(captureInstance(classNames[classIndex]));
			println(classifier);
		}
	}

}
