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
