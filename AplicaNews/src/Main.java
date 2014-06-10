import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class Main {

	static Hashtable<String, Integer> palabras= new Hashtable<String, Integer>();
	static ArrayList<String> clasesList = new ArrayList<String>();
	static String workspace="/home/jonatan/workspace/";
	static String rutaTraining=workspace + "r8-train-stemmed.txt";
	static String rutaTest=workspace + "r8-test-stemmed.txt";
	static String rutaClases=workspace + "clases_noticias.txt";
	
	
	
	
	public static void main(String[] args) throws Exception{
		getListaClaves();

	}
	
	public static void getListaClaves() throws Exception{
		getClases();
		System.out.println("Encontrando caracteristicas (palabras mas frecuentes) ...");
		
		File trainingArch = new File(rutaTraining);		
		Scanner trainingScanArch = new Scanner(new FileReader(trainingArch));
		String palabra;
		
		while (trainingScanArch.hasNext()){		
		    palabra = trainingScanArch.next();
		    palabra = palabra.toLowerCase();
		    
		    if (!clasesList.contains(palabra)) {
			    if(palabras.containsKey(palabra))
			    	palabras.put(palabra, palabras.get(palabra)+1);		    
			    else 
			    	palabras.put(palabra, 1);		
		    }
		}				
		trainingScanArch.close();			
	}
	
	public static void getClases() throws Exception {
		System.out.println("Encontrando lista de clases de noticias...");
		File clasesArch = new File(rutaClases);		
		Scanner clasesScanArch = new Scanner(new FileReader(clasesArch));
		String palabra;
		while (clasesScanArch.hasNext()){		
		    palabra = clasesScanArch.next();		    
		    clasesList.add(palabra);				    					    		    				   		    			   
		}
		clasesScanArch.close();					
	}
	
	
	
	
}
