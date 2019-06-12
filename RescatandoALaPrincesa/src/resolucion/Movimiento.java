package resolucion;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Movimiento {
	
	private int cantNodos;
	private int cantSenderos;
	private int cantDragones;
	private int posPrincipe;
	private int posPrincesa;
	private int []posDragones;
	private int [][]matrizAdyacencia;
	private int []procedenciaPrincesa;
	private ArrayList<Integer> noSolucion = new ArrayList<Integer>();
	
	public Movimiento(Scanner entrada){
		this.cantNodos = entrada.nextInt();
		this.cantSenderos = entrada.nextInt();
		this.cantDragones = entrada.nextInt();
		this.posPrincesa = (entrada.nextInt()-1);
		this.posPrincipe = (entrada.nextInt()-1);
		this.posDragones = new int[this.cantDragones];
		for(int i=0; i<this.cantDragones; i++)
			this.posDragones[i] = (entrada.nextInt() -1);
		this.matrizAdyacencia = new int[this.cantNodos][this.cantNodos];
		for(int[] row:matrizAdyacencia )
			Arrays.fill(row, (int)Integer.MAX_VALUE);
		int arista1, arista2, valor;
		this.procedenciaPrincesa = new int[this.cantNodos];
		Arrays.fill(procedenciaPrincesa, this.posPrincesa);
		for(int i=0; i<this.cantSenderos; i++){
			arista1 = entrada.nextInt();
			arista2 = entrada.nextInt();
			valor = entrada.nextInt();
			this.matrizAdyacencia[arista1-1][arista2-1] = valor;
			this.matrizAdyacencia[arista2-1][arista1-1] = valor;
		}
		for(int i=0; i<this.cantNodos ; i++) {
			if( i != this.posPrincesa)
				noSolucion.add(i);
		}
	}
	
	private Integer encontrarMenor() {
		Integer menor;
		menor = this.noSolucion.get(0);
		for(int i=0; i<this.noSolucion.size(); i++) {
			if(this.matrizAdyacencia[this.posPrincesa][this.noSolucion.get(i)] <  
					this.matrizAdyacencia[this.posPrincesa][menor])
				menor = this.noSolucion.get(i);
		}
		return menor;
	}
	
	private int esMenorElNuevo(int w, int indice) {
		if(this.matrizAdyacencia[this.posPrincesa][indice] < 
			(this.matrizAdyacencia[this.posPrincesa][w] + this.matrizAdyacencia[w][indice])	)
			return this.matrizAdyacencia[this.posPrincesa][indice];
		else
			this.procedenciaPrincesa[indice] = w;
		return (this.matrizAdyacencia[this.posPrincesa][w] + this.matrizAdyacencia[w][indice]);
	}
	
	private void calcularDistancias(int w) {
		int indice;
		for(int i=0; i<this.noSolucion.size(); i++) {
			indice = this.noSolucion.get(i);
			if(this.matrizAdyacencia[w][indice] != (int)Integer.MAX_VALUE)
				this.matrizAdyacencia[this.posPrincesa][indice] = esMenorElNuevo(w, indice);
		}
	}
	
	public void dijkstra(){
		Integer w=0;
		while(this.noSolucion.isEmpty() == false) {
			w = this.encontrarMenor();
			this.noSolucion.remove(w);
			this.calcularDistancias(w);
		}
	}
	
	private boolean principeLlegaAPrincesa() {
		return this.matrizAdyacencia[this.posPrincesa][this.posPrincipe] != (int)Integer.MAX_VALUE;
	}
	
	private boolean principeSalvaAPrincesa() {
		boolean salvaPrincesa = true;
		int i = 0;
		while(i < this.posDragones.length && salvaPrincesa == true) {
			if(this.matrizAdyacencia[this.posPrincesa][this.posPrincipe] > 
					this.matrizAdyacencia[this.posPrincesa][this.posDragones[i]])
				salvaPrincesa = false;
			i++;
		}
		return salvaPrincesa;
	}
	
	private void llenarCamino(ArrayList<Integer> camino) {
		int posicion = this.posPrincipe;
		camino.add(this.posPrincipe);
		while(posicion != this.posPrincesa) {
			camino.add(this.procedenciaPrincesa[posicion]);
			posicion = this.procedenciaPrincesa[posicion];
		}
		camino.add(this.posPrincesa);
	}
	
	public void resolver(PrintWriter salida){
		this.dijkstra();
		if(this.principeLlegaAPrincesa() == false) {
			salida.println("NO HAY CAMINO");
		}
		else {
			if(this.principeSalvaAPrincesa() == false) {
				salida.println("INTERCEPTADO");
			}
			else {
				ArrayList<Integer> camino = new ArrayList<Integer>();
				this.llenarCamino(camino);
				int i = 0;
				while(i < camino.size()) {
					salida.print((camino.get(i) + 1));
					i++;
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		Scanner entrada = new Scanner(new FileReader("rescate.in"));
		Movimiento grafo = new Movimiento(entrada);
		entrada.close();
		PrintWriter salida = new PrintWriter(new FileWriter("rescate.out"));
		grafo.resolver(salida);
		salida.close();
	}
	
}