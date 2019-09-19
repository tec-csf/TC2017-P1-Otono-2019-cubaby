import java.util.Random;
import java.util.Scanner;
import java.util.*;

public class MainAVL{
  public static void main(String[] args) {

    AVLtree<Integer> arbol = new AVLtree<>();
    long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
    Scanner teclado = new Scanner(System.in);
    ArrayList<Integer> imp = new ArrayList<Integer>();

    boolean t;
    int sel=0;
    int busqueda =0;
    int n =0;

  System.out.println("Cuantos valores desea crear el árbol AVL");
  n = teclado.nextInt();
  System.out.println("Creando árbol binario con datos aleatorios");

    for (int i =0;i<n ;i++ ){
    //  sel = (int)(Math.random()*10000);
      sel = sel+1;
      arbol.insertar(sel);
      imp.add(sel);
    //System.out.println(sel);
    }

    sel = 0;

    System.out.println("1.- Insertar");
    System.out.println("2.- Borrar");
    System.out.println("3.- Bucar");
    System.out.println("4.- Imprimir");
    System.out.println("5.- Nivel profundidad y altura de nodos");
    System.out.println("6.- Salir");
    sel = teclado.nextInt();

      switch (sel) {

        case 1:
        System.out.println("1.- Insertar");
        System.out.println("Valor a insertar:");
        busqueda = teclado.nextInt();
        arbol.insertar(busqueda);
        imp.add(busqueda);
        System.out.println("Valor insertado:"+busqueda);

        break;

        case 2:
        System.out.println("2.- Borrar");
        System.out.println("Valor a borrar:");
        busqueda = teclado.nextInt();
        arbol.remover(busqueda);
        imp.remove(busqueda);
        System.out.println("Valor borrado:"+busqueda);

        break;

        case 3:

        System.out.println("3.- Buscar");
        System.out.println("Valor a buscar:");
        busqueda = teclado.nextInt();
        if (arbol.contains(busqueda)){
          System.out.println("Valor encontrado:"+busqueda);
        }

        else {
          System.out.println("Valor No encontrado:"+busqueda);
        }
        break;

        case 4:
      //int i = Collections.sort(imp);
        System.out.println("Orden ascendente: "+imp);
        System.out.println("Orden descendente: "+imp);


        break;

        case 5:
        int nodo =3;
        System.out.println("La altura del árbol es: "+arbol.altura());

        System.out.println("La profundidad del nodo: "+ nodo+" es "+ arbol.Naltura(nodo));
      //System.out.println("La profundidad del nodo es:"+arbol.prof());

        break;

        case 6:
        break;

        default:
          break;
      }




  //  t=arbol.contains(212);


  /*  TInicio = System.currentTimeMillis();
    System.out.println("Árbol binario creado seleccione un número");

    busqueda = teclado.nextInt();

    arbol.contains(busqueda);
    TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T

    if (arbol.contains(busqueda)) {
      System.out.println("Número encontrado "+busqueda);
      tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
      System.out.println("Tiempo de ejecución en milisegundos: " + tiempo); //Mostramos en pantalla el tiempo de ejecución en milisegundos

    }

  /*arbol.insertar(99999);
  arbol.insertar(9999);
  arbol.insertar(999);
  arbol.insertar(99);
  arbol.insertar(9);*/

  }
}
