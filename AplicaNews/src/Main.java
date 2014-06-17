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
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.analysis.function.Log;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import Jama.Matrix;
import pca_transform.*;

public class Main {

	static Hashtable<String, Integer> palabras= new Hashtable<String, Integer>();
	static ArrayList<String> clasesList = new ArrayList<String>();
	static ArrayList<String> clavesList= new ArrayList<String>();
	static String workspace="/home/jonatan/workspace/";
	static String rutaTraining=workspace + "r8-train-stemmed.txt";
	static String rutaTest=workspace + "r8-test-stemmed.txt";
	static String rutaClases=workspace + "clases_noticias.txt";
	static int numeroCaracteristicas = 100; //parametrizado
	static int numeroNoticiasTraining=5485;
	static int numeroAcqTraining=1596;
	static int numeroCrudeTraining=253;
	static int numeroEarnTraining=2840;
	static int numeroGrainTraining=41;
	static int numeroInterestTraining=190;
	static int numeroMoneyFxTraining=206;
	static int numeroShipTraining=108;
	static int numeroTradeTraining=251;
	static double [][] matrizCaracteristicasTrainAcq = new double[numeroAcqTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTrainCrude = new double[numeroCrudeTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTrainEarn = new double[numeroEarnTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTrainGrain = new double[numeroGrainTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTrainInterest = new double[numeroInterestTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTrainMoneyFx = new double[numeroMoneyFxTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTrainShip = new double[numeroShipTraining][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTrainTrade = new double[numeroTradeTraining][numeroCaracteristicas];
	static double [][] vectorMediaAcq = new double[numeroCaracteristicas][1];
	static double [][] vectorMediaCrude = new double[numeroCaracteristicas][1];
	static double [][] vectorMediaEarn = new double[numeroCaracteristicas][1];
	static double [][] vectorMediaGrain = new double[numeroCaracteristicas][1];
	static double [][] vectorMediaInterest = new double[numeroCaracteristicas][1];
	static double [][] vectorMediaMoneyFx = new double[numeroCaracteristicas][1];
	static double [][] vectorMediaShip = new double[numeroCaracteristicas][1];
	static double [][] vectorMediaTrade = new double[numeroCaracteristicas][1];
	static double [][] matrizCovarianzaAcq;
	static double [][] matrizCovarianzaCrude;
	static double [][] matrizCovarianzaEarn;
	static double [][] matrizCovarianzaGrain;
	static double [][] matrizCovarianzaInterest;
	static double [][] matrizCovarianzaMoneyFx;
	static double [][] matrizCovarianzaShip;
	static double [][] matrizCovarianzaTrade;
	static int numeroNoticiasTest=2189;
	static int numeroAcqTest=696;
	static int numeroCrudeTest=121;
	static int numeroEarnTest=1083;
	static int numeroGrainTest=10;
	static int numeroInterestTest=81;
	static int numeroMoneyFxTest=87;
	static int numeroShipTest=36;
	static int numeroTradeTest=75;
	static double [][] matrizCaracteristicasTestAcq = new double[numeroAcqTest][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTestCrude = new double[numeroCrudeTest][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTestEarn = new double[numeroEarnTest][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTestGrain = new double[numeroGrainTest][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTestInterest = new double[numeroInterestTest][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTestMoneyFx = new double[numeroMoneyFxTest][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTestShip = new double[numeroShipTest][numeroCaracteristicas];
	static double [][] matrizCaracteristicasTestTrade = new double[numeroTradeTest][numeroCaracteristicas];	
	static int [][] matrizConfusion= new int[8][8];
		
	public static void main(String[] args) throws Exception{
		getListaClaves();
		getVectoresCaracteristicas();
		getVectoresMedia();
		getMatricesCovarianza();
		getVectoresCarateristicasTest();
		getMatrizConfusion();
		imprimirMatrizConfusion();
		
		
		
		
//		System.out.println("Running a demonstrational program on some sample data ...");
//        Matrix trainingData = new Matrix(matrizCaracteristicasTrainAcq);
//        PCA pca = new PCA(trainingData);
//        Matrix testData = new Matrix(new double[][] {
//        		 {1, 2, 1, 2, 1, 2}});
//        Matrix transformedData =
//            pca.transform(trainingData, PCA.TransformationType.WHITENING);
//        System.out.println("Transformed data:");
//        for(int r = 0; r < transformedData.getRowDimension(); r++){
//        	System.out.println("linea"+r+":");
//            for(int c = 0; c < transformedData.getColumnDimension(); c++){
//                System.out.print("c"+ c+":"+transformedData.get(r, c));
//                if (c == transformedData.getColumnDimension()-1) continue;
//                System.out.print(", ");
//            }
//            System.out.println("");
//        }
	}
	
	public static void imprimirMatrizConfusion() {
		System.out.println("Imprimiendo matriz de confusion:");
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				System.out.print(matrizConfusion[i][j]+"\t");
			}
			System.out.println("");
		}
	}
	
	public static void getMatrizConfusion() {
		System.out.println("Encontrando matriz de confusion ...");
		double acq, crude, earn, grain, interest, moneyfx, ship, trade;
		double mayor;		
		double [][] x= new double[numeroCaracteristicas][1];
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				matrizConfusion[i][j]=0;
			}
		}
				
		for(int c=0; c<numeroAcqTest; c++) {
			for(int i=0; i<numeroCaracteristicas; i++) {
				x[i][0]=matrizCaracteristicasTestAcq[c][i];
			}
			acq=funcionClasificadoraAcq(x);
			crude=funcionClasificadoraCrude(x);
			earn=funcionClasificadoraEarn(x);
			grain=funcionClasificadoraGrain(x);
			interest=funcionClasificadoraInterest(x);
			moneyfx=funcionClasificadoraMoneyFx(x);
			ship=funcionClasificadoraShip(x);
			trade=funcionClasificadoraTrade(x);
			mayor=getMayor(acq, crude, earn, grain, interest, moneyfx, ship, trade);
			
			if (mayor==acq) matrizConfusion[0][0]=matrizConfusion[0][0]+1;
			if (mayor==crude) matrizConfusion[0][1]=matrizConfusion[0][1]+1;;
			if (mayor==earn) matrizConfusion[0][2]=matrizConfusion[0][2]+1;;
			if (mayor==grain) matrizConfusion[0][3]=matrizConfusion[0][3]+1;;
			if (mayor==interest) matrizConfusion[0][4]=matrizConfusion[0][4]+1;;
			if (mayor==moneyfx) matrizConfusion[0][5]=matrizConfusion[0][5]+1;
			if (mayor==ship) matrizConfusion[0][6]=matrizConfusion[0][6]+1;;
			if (mayor==trade) matrizConfusion[0][7]=matrizConfusion[0][7]+1;	
		}
		
		for(int c=0; c<numeroCrudeTest; c++) {
			for(int i=0; i<numeroCaracteristicas; i++) {
				x[i][0]=matrizCaracteristicasTestCrude[c][i];
			}
			acq=funcionClasificadoraAcq(x);
			crude=funcionClasificadoraCrude(x);
			earn=funcionClasificadoraEarn(x);
			grain=funcionClasificadoraGrain(x);
			interest=funcionClasificadoraInterest(x);
			moneyfx=funcionClasificadoraMoneyFx(x);
			ship=funcionClasificadoraShip(x);
			trade=funcionClasificadoraTrade(x);
			mayor=getMayor(acq, crude, earn, grain, interest, moneyfx, ship, trade);
			
			if (mayor==acq) matrizConfusion[1][0]=matrizConfusion[1][0]+1;
			if (mayor==crude) matrizConfusion[1][1]=matrizConfusion[1][1]+1;;
			if (mayor==earn) matrizConfusion[1][2]=matrizConfusion[1][2]+1;;
			if (mayor==grain) matrizConfusion[1][3]=matrizConfusion[1][3]+1;;
			if (mayor==interest) matrizConfusion[1][4]=matrizConfusion[1][4]+1;;
			if (mayor==moneyfx) matrizConfusion[1][5]=matrizConfusion[1][5]+1;
			if (mayor==ship) matrizConfusion[1][6]=matrizConfusion[1][6]+1;;
			if (mayor==trade) matrizConfusion[1][7]=matrizConfusion[1][7]+1;	
		}
		
		for(int c=0; c<numeroEarnTest; c++) {
			for(int i=0; i<numeroCaracteristicas; i++) {
				x[i][0]=matrizCaracteristicasTestEarn[c][i];
			}
			acq=funcionClasificadoraAcq(x);
			crude=funcionClasificadoraCrude(x);
			earn=funcionClasificadoraEarn(x);
			grain=funcionClasificadoraGrain(x);
			interest=funcionClasificadoraInterest(x);
			moneyfx=funcionClasificadoraMoneyFx(x);
			ship=funcionClasificadoraShip(x);
			trade=funcionClasificadoraTrade(x);
			mayor=getMayor(acq, crude, earn, grain, interest, moneyfx, ship, trade);
			
			if (mayor==acq) matrizConfusion[2][0]=matrizConfusion[2][0]+1;
			if (mayor==crude) matrizConfusion[2][1]=matrizConfusion[2][1]+1;;
			if (mayor==earn) matrizConfusion[2][2]=matrizConfusion[2][2]+1;;
			if (mayor==grain) matrizConfusion[2][3]=matrizConfusion[2][3]+1;;
			if (mayor==interest) matrizConfusion[2][4]=matrizConfusion[2][4]+1;;
			if (mayor==moneyfx) matrizConfusion[2][5]=matrizConfusion[2][5]+1;
			if (mayor==ship) matrizConfusion[2][6]=matrizConfusion[2][6]+1;;
			if (mayor==trade) matrizConfusion[2][7]=matrizConfusion[2][7]+1;	
		}
		
		for(int c=0; c<numeroGrainTest; c++) {
			for(int i=0; i<numeroCaracteristicas; i++) {
				x[i][0]=matrizCaracteristicasTestGrain[c][i];
			}
			acq=funcionClasificadoraAcq(x);
			crude=funcionClasificadoraCrude(x);
			earn=funcionClasificadoraEarn(x);
			grain=funcionClasificadoraGrain(x);
			interest=funcionClasificadoraInterest(x);
			moneyfx=funcionClasificadoraMoneyFx(x);
			ship=funcionClasificadoraShip(x);
			trade=funcionClasificadoraTrade(x);
			mayor=getMayor(acq, crude, earn, grain, interest, moneyfx, ship, trade);
			
			if (mayor==acq) matrizConfusion[3][0]=matrizConfusion[3][0]+1;
			if (mayor==crude) matrizConfusion[3][1]=matrizConfusion[3][1]+1;;
			if (mayor==earn) matrizConfusion[3][2]=matrizConfusion[3][2]+1;;
			if (mayor==grain) matrizConfusion[3][3]=matrizConfusion[3][3]+1;;
			if (mayor==interest) matrizConfusion[3][4]=matrizConfusion[3][4]+1;;
			if (mayor==moneyfx) matrizConfusion[3][5]=matrizConfusion[3][5]+1;
			if (mayor==ship) matrizConfusion[3][6]=matrizConfusion[3][6]+1;;
			if (mayor==trade) matrizConfusion[3][7]=matrizConfusion[3][7]+1;	
		}
		
		for(int c=0; c<numeroInterestTest; c++) {
			for(int i=0; i<numeroCaracteristicas; i++) {
				x[i][0]=matrizCaracteristicasTestInterest[c][i];
			}
			acq=funcionClasificadoraAcq(x);
			crude=funcionClasificadoraCrude(x);
			earn=funcionClasificadoraEarn(x);
			grain=funcionClasificadoraGrain(x);
			interest=funcionClasificadoraInterest(x);
			moneyfx=funcionClasificadoraMoneyFx(x);
			ship=funcionClasificadoraShip(x);
			trade=funcionClasificadoraTrade(x);
			mayor=getMayor(acq, crude, earn, grain, interest, moneyfx, ship, trade);
			
			if (mayor==acq) matrizConfusion[4][0]=matrizConfusion[4][0]+1;
			if (mayor==crude) matrizConfusion[4][1]=matrizConfusion[4][1]+1;;
			if (mayor==earn) matrizConfusion[4][2]=matrizConfusion[4][2]+1;;
			if (mayor==grain) matrizConfusion[4][3]=matrizConfusion[4][3]+1;;
			if (mayor==interest) matrizConfusion[4][4]=matrizConfusion[4][4]+1;;
			if (mayor==moneyfx) matrizConfusion[4][5]=matrizConfusion[4][5]+1;
			if (mayor==ship) matrizConfusion[4][6]=matrizConfusion[4][6]+1;;
			if (mayor==trade) matrizConfusion[4][7]=matrizConfusion[4][7]+1;	
		}
		
		for(int c=0; c<numeroMoneyFxTest; c++) {
			for(int i=0; i<numeroCaracteristicas; i++) {
				x[i][0]=matrizCaracteristicasTestMoneyFx[c][i];
			}
			acq=funcionClasificadoraAcq(x);
			crude=funcionClasificadoraCrude(x);
			earn=funcionClasificadoraEarn(x);
			grain=funcionClasificadoraGrain(x);
			interest=funcionClasificadoraInterest(x);
			moneyfx=funcionClasificadoraMoneyFx(x);
			ship=funcionClasificadoraShip(x);
			trade=funcionClasificadoraTrade(x);
			mayor=getMayor(acq, crude, earn, grain, interest, moneyfx, ship, trade);
			
			if (mayor==acq) matrizConfusion[5][0]=matrizConfusion[5][0]+1;
			if (mayor==crude) matrizConfusion[5][1]=matrizConfusion[5][1]+1;;
			if (mayor==earn) matrizConfusion[5][2]=matrizConfusion[5][2]+1;;
			if (mayor==grain) matrizConfusion[5][3]=matrizConfusion[5][3]+1;;
			if (mayor==interest) matrizConfusion[5][4]=matrizConfusion[5][4]+1;;
			if (mayor==moneyfx) matrizConfusion[5][5]=matrizConfusion[5][5]+1;
			if (mayor==ship) matrizConfusion[5][6]=matrizConfusion[5][6]+1;;
			if (mayor==trade) matrizConfusion[5][7]=matrizConfusion[5][7]+1;	
		}
		
		for(int c=0; c<numeroShipTest; c++) {
			for(int i=0; i<numeroCaracteristicas; i++) {
				x[i][0]=matrizCaracteristicasTestShip[c][i];
			}
			acq=funcionClasificadoraAcq(x);
			crude=funcionClasificadoraCrude(x);
			earn=funcionClasificadoraEarn(x);
			grain=funcionClasificadoraGrain(x);
			interest=funcionClasificadoraInterest(x);
			moneyfx=funcionClasificadoraMoneyFx(x);
			ship=funcionClasificadoraShip(x);
			trade=funcionClasificadoraTrade(x);
			mayor=getMayor(acq, crude, earn, grain, interest, moneyfx, ship, trade);
			
			if (mayor==acq) matrizConfusion[6][0]=matrizConfusion[6][0]+1;
			if (mayor==crude) matrizConfusion[6][1]=matrizConfusion[6][1]+1;;
			if (mayor==earn) matrizConfusion[6][2]=matrizConfusion[6][2]+1;;
			if (mayor==grain) matrizConfusion[6][3]=matrizConfusion[6][3]+1;;
			if (mayor==interest) matrizConfusion[6][4]=matrizConfusion[6][4]+1;;
			if (mayor==moneyfx) matrizConfusion[6][5]=matrizConfusion[6][5]+1;
			if (mayor==ship) matrizConfusion[6][6]=matrizConfusion[6][6]+1;;
			if (mayor==trade) matrizConfusion[6][7]=matrizConfusion[6][7]+1;	
		}
		
		for(int c=0; c<numeroTradeTest; c++) {
			for(int i=0; i<numeroCaracteristicas; i++) {
				x[i][0]=matrizCaracteristicasTestTrade[c][i];
			}
			acq=funcionClasificadoraAcq(x);
			crude=funcionClasificadoraCrude(x);
			earn=funcionClasificadoraEarn(x);
			grain=funcionClasificadoraGrain(x);
			interest=funcionClasificadoraInterest(x);
			moneyfx=funcionClasificadoraMoneyFx(x);
			ship=funcionClasificadoraShip(x);
			trade=funcionClasificadoraTrade(x);
			mayor=getMayor(acq, crude, earn, grain, interest, moneyfx, ship, trade);
			
			if (mayor==acq) matrizConfusion[7][0]=matrizConfusion[7][0]+1;
			if (mayor==crude) matrizConfusion[7][1]=matrizConfusion[7][1]+1;;
			if (mayor==earn) matrizConfusion[7][2]=matrizConfusion[7][2]+1;;
			if (mayor==grain) matrizConfusion[7][3]=matrizConfusion[7][3]+1;;
			if (mayor==interest) matrizConfusion[7][4]=matrizConfusion[7][4]+1;;
			if (mayor==moneyfx) matrizConfusion[7][5]=matrizConfusion[7][5]+1;
			if (mayor==ship) matrizConfusion[7][6]=matrizConfusion[7][6]+1;;
			if (mayor==trade) matrizConfusion[7][7]=matrizConfusion[7][7]+1;	
		}
		
	}	
	
	public static double getMayor(double acq, double crude, double earn, double grain, double interest, double moneyfx, double ship, double trade) {
		double mayor=Math.max(acq, crude);
		mayor=Math.max(mayor, earn);
		mayor=Math.max(mayor, grain);
		mayor=Math.max(mayor, interest);
		mayor=Math.max(mayor, moneyfx);
		mayor=Math.max(mayor, ship);
		mayor=Math.max(mayor, trade);
		return mayor;
	}
	
	public static void getVectoresCarateristicasTest() throws Exception {
		System.out.println("Encontrando vectores caracteristicas de todas las noticias test ...");
		int cuentaAcq=0, cuentaCrude=0, cuentaEarn=0, cuentaGrain=0, cuentaInterest=0, cuentaMoneyFx=0, cuentaShip=0, cuentaTrade=0;
		FileInputStream archTraining = new FileInputStream(rutaTest);
		DataInputStream entrada = new DataInputStream(archTraining);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));	
		String strLinea;
		Random r = new Random();
		for(int i=0; i<numeroNoticiasTest && (strLinea = buffer.readLine()) != null; i++) {										
			if(strLinea.contains(clasesList.get(0)+"\t")) {				
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTestAcq[cuentaAcq][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaAcq++;
				continue;
			}
			if(strLinea.contains(clasesList.get(1)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTestCrude[cuentaCrude][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaCrude++;
				continue;
			}
			if(strLinea.contains(clasesList.get(2)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTestEarn[cuentaEarn][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaEarn++;
				continue;
			}
			if(strLinea.contains(clasesList.get(3)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTestGrain[cuentaGrain][j] = r.nextFloat()/100.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaGrain++;
				continue;
			}
			if(strLinea.contains(clasesList.get(4)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTestInterest[cuentaInterest][j] = r.nextFloat()/100.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaInterest++;
				continue;
			}
			if(strLinea.contains(clasesList.get(5)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTestMoneyFx[cuentaMoneyFx][j] = r.nextFloat()/100.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaMoneyFx++;
				continue;
			}
			if(strLinea.contains(clasesList.get(6)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTestShip[cuentaShip][j] = r.nextFloat()/100.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaShip++;
				continue;
			}
			if(strLinea.contains(clasesList.get(7)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTestTrade[cuentaTrade][j] = r.nextFloat()/100.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaTrade++;
				continue;
			}
		}
		entrada.close();
	}
	
	public static double funcionClasificadoraAcq(double [][] x) {		
		RealMatrix vectorMedia = MatrixUtils.createRealMatrix(vectorMediaAcq);
    	RealMatrix matrizCovarianza = MatrixUtils.createRealMatrix(matrizCovarianzaAcq);    	   	    	
    	RealMatrix matrizCovInv = new LUDecomposition(matrizCovarianza).getSolver().getInverse();
    	RealMatrix vector = MatrixUtils.createRealMatrix(x);
    	RealMatrix subsMatriz = vector.subtract(vectorMedia);    	
    	RealMatrix transp = subsMatriz.transpose();
    	double determ = new LUDecomposition(matrizCovarianza).getDeterminant();
    	int d = numeroCaracteristicas;
    	RealMatrix mult1 = matrizCovInv.preMultiply(transp);
    	RealMatrix mult2 = subsMatriz.preMultiply(mult1);
    	double[][]data = mult2.getData();
    	double valor = 2*3.141591;
    	double logarit = new Log().value(valor);
    	double logarit2 = new Log().value(determ);
    	    	
		return -1*0.5*data[0][0] - (d/2)*logarit - 0.5*logarit2+100;
	}
	
	public static double funcionClasificadoraCrude(double [][] x) {		
		RealMatrix vectorMedia = MatrixUtils.createRealMatrix(vectorMediaCrude);
    	RealMatrix matrizCovarianza = MatrixUtils.createRealMatrix(matrizCovarianzaCrude);    	   	    	
    	RealMatrix matrizCovInv = new LUDecomposition(matrizCovarianza).getSolver().getInverse();
    	RealMatrix vector = MatrixUtils.createRealMatrix(x);
    	RealMatrix subsMatriz = vector.subtract(vectorMedia);    	
    	RealMatrix transp = subsMatriz.transpose();
    	double determ = new LUDecomposition(matrizCovarianza).getDeterminant();
    	int d = numeroCaracteristicas;
    	RealMatrix mult1 = matrizCovInv.preMultiply(transp);
    	RealMatrix mult2 = subsMatriz.preMultiply(mult1);
    	double[][]data = mult2.getData();
    	double valor = 2*3.141591;
    	double logarit = new Log().value(valor);
    	double logarit2 = new Log().value(determ);
    	    	
		return -1*0.5*data[0][0] - (d/2)*logarit - 0.5*logarit2+100;
	}
	
	public static double funcionClasificadoraEarn(double [][] x) {		
		RealMatrix vectorMedia = MatrixUtils.createRealMatrix(vectorMediaEarn);
    	RealMatrix matrizCovarianza = MatrixUtils.createRealMatrix(matrizCovarianzaEarn);    	   	    	
    	RealMatrix matrizCovInv = new LUDecomposition(matrizCovarianza).getSolver().getInverse();
    	RealMatrix vector = MatrixUtils.createRealMatrix(x);
    	RealMatrix subsMatriz = vector.subtract(vectorMedia);    	
    	RealMatrix transp = subsMatriz.transpose();
    	double determ = new LUDecomposition(matrizCovarianza).getDeterminant();
    	int d = numeroCaracteristicas;
    	RealMatrix mult1 = matrizCovInv.preMultiply(transp);
    	RealMatrix mult2 = subsMatriz.preMultiply(mult1);
    	double[][]data = mult2.getData();
    	double valor = 2*3.141591;
    	double logarit = new Log().value(valor);
    	double logarit2 = new Log().value(determ);
    	    	
		return -1*0.5*data[0][0] - (d/2)*logarit - 0.5*logarit2+100;
	}
	
	public static double funcionClasificadoraGrain(double [][] x) {		
		RealMatrix vectorMedia = MatrixUtils.createRealMatrix(vectorMediaGrain);
    	RealMatrix matrizCovarianza = MatrixUtils.createRealMatrix(matrizCovarianzaGrain);    	   	    	
    	RealMatrix matrizCovInv = new LUDecomposition(matrizCovarianza).getSolver().getInverse();
    	RealMatrix vector = MatrixUtils.createRealMatrix(x);
    	RealMatrix subsMatriz = vector.subtract(vectorMedia);    	
    	RealMatrix transp = subsMatriz.transpose();
    	double determ = new LUDecomposition(matrizCovarianza).getDeterminant();
    	int d = numeroCaracteristicas;
    	RealMatrix mult1 = matrizCovInv.preMultiply(transp);
    	RealMatrix mult2 = subsMatriz.preMultiply(mult1);
    	double[][]data = mult2.getData();
    	double valor = 2*3.141591;
    	double logarit = new Log().value(valor);
    	double logarit2 = new Log().value(determ);
    	    	
		return -1*0.5*data[0][0] - (d/2)*logarit - 0.5*logarit2+100;
	}
	
	public static double funcionClasificadoraInterest(double [][] x) {		
		RealMatrix vectorMedia = MatrixUtils.createRealMatrix(vectorMediaInterest);
    	RealMatrix matrizCovarianza = MatrixUtils.createRealMatrix(matrizCovarianzaInterest);    	   	    	
    	RealMatrix matrizCovInv = new LUDecomposition(matrizCovarianza).getSolver().getInverse();
    	RealMatrix vector = MatrixUtils.createRealMatrix(x);
    	RealMatrix subsMatriz = vector.subtract(vectorMedia);    	
    	RealMatrix transp = subsMatriz.transpose();
    	double determ = new LUDecomposition(matrizCovarianza).getDeterminant();
    	int d = numeroCaracteristicas;
    	RealMatrix mult1 = matrizCovInv.preMultiply(transp);
    	RealMatrix mult2 = subsMatriz.preMultiply(mult1);
    	double[][]data = mult2.getData();
    	double valor = 2*3.141591;
    	double logarit = new Log().value(valor);
    	double logarit2 = new Log().value(determ);
    	    	
		return -1*0.5*data[0][0] - (d/2)*logarit - 0.5*logarit2+100;
	}
	
	public static double funcionClasificadoraMoneyFx(double [][] x) {		
		RealMatrix vectorMedia = MatrixUtils.createRealMatrix(vectorMediaMoneyFx);
    	RealMatrix matrizCovarianza = MatrixUtils.createRealMatrix(matrizCovarianzaMoneyFx);    	   	    	
    	RealMatrix matrizCovInv = new LUDecomposition(matrizCovarianza).getSolver().getInverse();
    	RealMatrix vector = MatrixUtils.createRealMatrix(x);
    	RealMatrix subsMatriz = vector.subtract(vectorMedia);    	
    	RealMatrix transp = subsMatriz.transpose();
    	double determ = new LUDecomposition(matrizCovarianza).getDeterminant();
    	int d = numeroCaracteristicas;
    	RealMatrix mult1 = matrizCovInv.preMultiply(transp);
    	RealMatrix mult2 = subsMatriz.preMultiply(mult1);
    	double[][]data = mult2.getData();
    	double valor = 2*3.141591;
    	double logarit = new Log().value(valor);
    	double logarit2 = new Log().value(determ);
    	    	
		return -1*0.5*data[0][0] - (d/2)*logarit - 0.5*logarit2+100;
	}
	
	public static double funcionClasificadoraShip(double [][] x) {		
		RealMatrix vectorMedia = MatrixUtils.createRealMatrix(vectorMediaShip);
    	RealMatrix matrizCovarianza = MatrixUtils.createRealMatrix(matrizCovarianzaShip);    	   	    	
    	RealMatrix matrizCovInv = new LUDecomposition(matrizCovarianza).getSolver().getInverse();
    	RealMatrix vector = MatrixUtils.createRealMatrix(x);
    	RealMatrix subsMatriz = vector.subtract(vectorMedia);    	
    	RealMatrix transp = subsMatriz.transpose();
    	double determ = new LUDecomposition(matrizCovarianza).getDeterminant();
    	int d = numeroCaracteristicas;
    	RealMatrix mult1 = matrizCovInv.preMultiply(transp);
    	RealMatrix mult2 = subsMatriz.preMultiply(mult1);
    	double[][]data = mult2.getData();
    	double valor = 2*3.141591;
    	double logarit = new Log().value(valor);
    	double logarit2 = new Log().value(determ);
    	    	
		return -1*0.5*data[0][0] - (d/2)*logarit - 0.5*logarit2+100;
	}
	
	public static double funcionClasificadoraTrade(double [][] x) {		
		RealMatrix vectorMedia = MatrixUtils.createRealMatrix(vectorMediaTrade);
    	RealMatrix matrizCovarianza = MatrixUtils.createRealMatrix(matrizCovarianzaTrade);    	   	    	
    	RealMatrix matrizCovInv = new LUDecomposition(matrizCovarianza).getSolver().getInverse();
    	RealMatrix vector = MatrixUtils.createRealMatrix(x);
    	RealMatrix subsMatriz = vector.subtract(vectorMedia);    	
    	RealMatrix transp = subsMatriz.transpose();
    	double determ = new LUDecomposition(matrizCovarianza).getDeterminant();
    	int d = numeroCaracteristicas;
    	RealMatrix mult1 = matrizCovInv.preMultiply(transp);
    	RealMatrix mult2 = subsMatriz.preMultiply(mult1);
    	double[][]data = mult2.getData();
    	double valor = 2*3.141591;
    	double logarit = new Log().value(valor);
    	double logarit2 = new Log().value(determ);
    	    	
		return -1*0.5*data[0][0] - (d/2)*logarit - 0.5*logarit2+100;
	}
	
	public static void getMatricesCovarianza() {
		System.out.println("Encontrando matrices covarianza ...");
		Covariance covarianzaAcq = new Covariance(matrizCaracteristicasTrainAcq, false);
		RealMatrix realCovarianzaAcq = covarianzaAcq.getCovarianceMatrix();
    	matrizCovarianzaAcq = realCovarianzaAcq.getData();
		
    	Covariance covarianzaCrude = new Covariance(matrizCaracteristicasTrainCrude, false);
		RealMatrix realCovarianzaCrude = covarianzaCrude.getCovarianceMatrix();
    	matrizCovarianzaCrude = realCovarianzaCrude.getData();
    	
    	Covariance covarianzaEarn = new Covariance(matrizCaracteristicasTrainEarn, false);
		RealMatrix realCovarianzaEarn = covarianzaEarn.getCovarianceMatrix();
    	matrizCovarianzaEarn = realCovarianzaEarn.getData();
    	
    	Covariance covarianzaGrain = new Covariance(matrizCaracteristicasTrainGrain, false);
		RealMatrix realCovarianzaGrain = covarianzaGrain.getCovarianceMatrix();
    	matrizCovarianzaGrain = realCovarianzaGrain.getData();
    	
    	Covariance covarianzaInterest = new Covariance(matrizCaracteristicasTrainInterest, false);
		RealMatrix realCovarianzaInterest = covarianzaInterest.getCovarianceMatrix();
    	matrizCovarianzaInterest = realCovarianzaInterest.getData();
    	
    	Covariance covarianzaMoneyFx = new Covariance(matrizCaracteristicasTrainMoneyFx, false);
		RealMatrix realCovarianzaMoneyFx = covarianzaMoneyFx.getCovarianceMatrix();
    	matrizCovarianzaMoneyFx = realCovarianzaMoneyFx.getData();
    	
    	Covariance covarianzaShip = new Covariance(matrizCaracteristicasTrainShip, false);
		RealMatrix realCovarianzaShip = covarianzaShip.getCovarianceMatrix();
    	matrizCovarianzaShip = realCovarianzaShip.getData();
    	
    	Covariance covarianzaTrade = new Covariance(matrizCaracteristicasTrainTrade, false);
		RealMatrix realCovarianzaTrade = covarianzaTrade.getCovarianceMatrix();
    	matrizCovarianzaTrade = realCovarianzaTrade.getData();
	}
	
	public static void getVectoresMedia() {
		System.out.println("Encontrando vectores media ...");
		for(int i=0; i<numeroCaracteristicas; i++)
			vectorMediaAcq[i][0]=0;
		
		for(int i=0; i<numeroCaracteristicas; i++)
			vectorMediaCrude[i][0]=0;
		
		for(int i=0; i<numeroCaracteristicas; i++)
			vectorMediaEarn[i][0]=0;
		
		for(int i=0; i<numeroCaracteristicas; i++)
			vectorMediaGrain[i][0]=0;
		
		for(int i=0; i<numeroCaracteristicas; i++)
			vectorMediaInterest[i][0]=0;
		
		for(int i=0; i<numeroCaracteristicas; i++)
			vectorMediaMoneyFx[i][0]=0;
		
		for(int i=0; i<numeroCaracteristicas; i++)
			vectorMediaShip[i][0]=0;
		
		for(int i=0; i<numeroCaracteristicas; i++)
			vectorMediaTrade[i][0]=0;
		
		//
		for(int i=0; i<numeroAcqTraining; i++) {
			for(int j=0; j<numeroCaracteristicas; j++) {
				vectorMediaAcq[j][0]=vectorMediaAcq[j][0]+matrizCaracteristicasTrainAcq[i][j];
			}
		}
		
		for(int i=0; i<numeroCrudeTraining; i++) {
			for(int j=0; j<numeroCaracteristicas; j++) {
				vectorMediaCrude[j][0]=vectorMediaCrude[j][0]+matrizCaracteristicasTrainCrude[i][j];
			}
		}
		
		for(int i=0; i<numeroEarnTraining; i++) {
			for(int j=0; j<numeroCaracteristicas; j++) {
				vectorMediaEarn[j][0]=vectorMediaEarn[j][0]+matrizCaracteristicasTrainEarn[i][j];
			}
		}
		
		for(int i=0; i<numeroGrainTraining; i++) {
			for(int j=0; j<numeroCaracteristicas; j++) {
				vectorMediaGrain[j][0]=vectorMediaGrain[j][0]+matrizCaracteristicasTrainGrain[i][j];
			}
		}
		
		for(int i=0; i<numeroInterestTraining; i++) {
			for(int j=0; j<numeroCaracteristicas; j++) {
				vectorMediaInterest[j][0]=vectorMediaInterest[j][0]+matrizCaracteristicasTrainInterest[i][j];
			}
		}
		
		for(int i=0; i<numeroShipTraining; i++) {
			for(int j=0; j<numeroCaracteristicas; j++) {
				vectorMediaShip[j][0]=vectorMediaShip[j][0]+matrizCaracteristicasTrainShip[i][j];
			}
		}
		
		for(int i=0; i<numeroTradeTraining; i++) {
			for(int j=0; j<numeroCaracteristicas; j++) {
				vectorMediaTrade[j][0]=vectorMediaTrade[j][0]+matrizCaracteristicasTrainTrade[i][j];
			}
		}			
	}
	
	public static void getVectoresCaracteristicas() throws Exception{
		System.out.println("Encontrando vectores caracteristicas de todas las noticias train ...");
		int cuentaAcq=0, cuentaCrude=0, cuentaEarn=0, cuentaGrain=0, cuentaInterest=0, cuentaMoneyFx=0, cuentaShip=0, cuentaTrade=0;
		FileInputStream archTraining = new FileInputStream(rutaTraining);
		DataInputStream entrada = new DataInputStream(archTraining);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));	
		String strLinea;
		Random r = new Random();
		for(int i=0; i<numeroNoticiasTraining && (strLinea = buffer.readLine()) != null; i++) {										
			if(strLinea.contains(clasesList.get(0)+"\t")) {				
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTrainAcq[cuentaAcq][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaAcq++;
				continue;
			}
			if(strLinea.contains(clasesList.get(1)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTrainCrude[cuentaCrude][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaCrude++;
				continue;
			}
			if(strLinea.contains(clasesList.get(2)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTrainEarn[cuentaEarn][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaEarn++;
				continue;
			}
			if(strLinea.contains(clasesList.get(3)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTrainGrain[cuentaGrain][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaGrain++;
				continue;
			}
			if(strLinea.contains(clasesList.get(4)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTrainInterest[cuentaInterest][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaInterest++;
				continue;
			}
			if(strLinea.contains(clasesList.get(5)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTrainMoneyFx[cuentaMoneyFx][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaMoneyFx++;
				continue;
			}
			if(strLinea.contains(clasesList.get(6)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTrainShip[cuentaShip][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaShip++;
				continue;
			}
			if(strLinea.contains(clasesList.get(7)+"\t")) {
				for(int j=0; j<numeroCaracteristicas; j++)
					matrizCaracteristicasTrainTrade[cuentaTrade][j] = r.nextFloat()/1000.0 +StringUtils.countMatches(strLinea, clavesList.get(j));				
				cuentaTrade++;
				continue;
			}
		}
		entrada.close();
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
