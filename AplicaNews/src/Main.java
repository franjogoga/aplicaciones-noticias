import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;


public class Main {

	static Hashtable<String, Integer> palabras= new Hashtable<String, Integer>();
	static ArrayList<String> clasesList = new ArrayList<String>();
	static ArrayList<String> clavesList= new ArrayList<String>();
	static String workspace="/home/jonatan/workspace/";
	static String rutaTraining=workspace + "r8-train-stemmed.txt";
	static String rutaTest=workspace + "r8-test-stemmed.txt";
	static String rutaClases=workspace + "clases_noticias.txt";
	static int numeroCaracteristicas = 5;
	static int numeroNoticiasTraining=5485;
	static int numeroAcqTraining=1596;
	static int numeroCrudeTraining=253;
	static int numeroEarnTraining=2840;
	static int numeroGrainTraining=41;
	static int numeroInterestTraining=190;
	static int numeroMoneyFxTraining=206;
	static int numeroShipTraining=108;
	static int numeroTradeTraining=251;
	static double [][] matrizCaracteristicasAcq = new double[numeroAcqTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasCrude = new double[numeroCrudeTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasEarn = new double[numeroEarnTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasGrain = new double[numeroGrainTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasInterest = new double[numeroInterestTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasMoneyFx = new double[numeroMoneyFxTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasShip = new double[numeroShipTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTrade = new double[numeroTradeTraining][numeroCaracteristicas];
		
	public static void main(String[] args) throws Exception{
		getListaClaves();
		getVectoresCaracteristicas();
		

	}
	
	public static void getVectoresCaracteristicas() throws Exception{
		System.out.println("Encontrando vectores caracteristicas de todas las noticias ...");
		
		for(int i=0; i<numeroNoticiasTraining; i++) {
			FileInputStream archTraining = new FileInputStream(rutaTraining);
			DataInputStream entrada = new DataInputStream(archTraining);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));			
			String strLinea;

			while ((strLinea = buffer.readLine()) != null) {
				
			}						
			entrada.close();			
		}				
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
				
		LinkedHashMap<String, Integer> palabrasOrdenado= ordenarPorValores(palabras);
		Iterator<String> iterator = palabrasOrdenado.keySet().iterator();
	
		while (iterator.hasNext()) {  
		   String clave = iterator.next().toString();  		   
		   String frecuencia = palabrasOrdenado.get(clave).toString();		   
		   clavesList.add(clave);
		   System.out.println("  Palabra Clave: "+clave + " " + "\t\tFrecuencia: "+frecuencia);		   		   
		}  									
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
	
	public static <K extends Comparable,V extends Comparable> LinkedHashMap<K,V> ordenarPorValores(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {
            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });      
        
        LinkedHashMap<K,V> sortedMap = new LinkedHashMap<K,V>();        
        for(int i=entries.size()-1, j=0; i>=0 && j!=numeroCaracteristicas; --i, j++) {
        	sortedMap.put(entries.get(i).getKey(), entries.get(i).getValue());
        }    
        return sortedMap;
    }
	
	
}
