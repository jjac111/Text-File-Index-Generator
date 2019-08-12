/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectofinal;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Juan Javier
 */
public class ProyectoFinal extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ProyectoFinal.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static Hashtable<String, ArrayList<Indice>> wordIndexes = new Hashtable<>();
    
    public static void indiceInvertido(String archivo) throws Exception{
        
        //raw=
        
        int fila=0;// variable para indicar en que fila se encuentra

        File excep = new File("EXCEPCIONES.txt"); //archivo que contiene palabras de excepcion.
        File raw= new File(archivo + ".txt"); // accede al directorio del archivo del cual se desea el indice raw=materia prima
        String nombre_archivo=archivo + "_resultados.txt"; // nombre del archivo en el que se almacenara el indice invertido
        
        File resultados = new File(nombre_archivo); // accede al directorio donde se almacena los resultados
        
       //si el archivo a analizar existe crea  un archivo para almacenar los resultados
        if(raw.exists())
        {
            try{
            resultados.createNewFile();
            }catch(IOException io){
             io.printStackTrace();
          }
        
        }

        
        
        
        //Hashtable<String, ArrayList<Indice>> wordIndexes = new Hashtable<>();
        //ArrayList<String> list= new ArrayList<>();// array list para almacenar las palabras que aparecen en el archivo raw(archivo a trabajar )
        ArrayList<String> excepciones = new ArrayList<>();  //Array list que almacena las palabras de excepcion para comparar luego.
        
        //lee el archivo para encontrar palabras no repetidas
        Scanner lector= new Scanner(raw, "UTF-8"); // scanner para leer el arhicov raw
        Scanner excepcionLector = new Scanner(excep); //Scanner para leer el archivo de palabras de excepcion
        
        lector.useDelimiter("[ ,.:;(){}\\[\\]\\n]+");// delimitador para que el scanner separe las palabras en base a los caracteres indicados
        
        
        //lee el archivo de palabras de excepcion  y las aniade a un array list
        while(excepcionLector.hasNext()){
            String palabra = excepcionLector.nextLine();
            excepciones.add(palabra);
        }
        excepcionLector.close();
        
        
        
        //lee el archivo y llena el array list (list) de las  que contiene el archivo (sin repetirlas )
        while(lector.hasNext())
        {
            
            String palabra=lector.next();
            
            if(!wordIndexes.containsKey(palabra) && !excepciones.contains(palabra)) //aÃ±ade la palabra al Hashtable si es Ãºnica y si no es palabra de excepciÃ³n.
                wordIndexes.put(palabra, new ArrayList<>());
            
        }
        lector.close();
        
        //ordenad el array de palabras unicas
        ArrayList<String> palabrasUnicas = new ArrayList<>(wordIndexes.keySet());   //Se obtiene el set de palabras del hashmap
        Comparator<String> c = Comparator.comparing(String::toString);  //Comparador para strings en sort.
        palabrasUnicas.sort(c);
       
        
        PrintWriter printResult= new PrintWriter(resultados);// print writer que escribe el archivo de resultados
        
       //entra en un lazo para encontrar la columna y fila de cada palabra
        for(int j=0; j<palabrasUnicas.size();j++)
        {
            
            String palabra_texto=palabrasUnicas.get(j); // variable que almacena una palabra del array

            Scanner searchfila= new Scanner(raw);//scaner para leer el archivo

            while(searchfila.hasNextLine()) //lee el archivo por filas
            {

                String palabra=searchfila.nextLine(); // variable para almacenar la linea de texto

                String[] a=palabra.split("[ ,.:;(){}\\[\\]\\n]+"); // descompone la linea en base a los delimitadores
               // y los almacena en un array
                int index=0;
                    //recorre el array descompuesto (linea)
                    for(int i=0;i<a.length;i++){

                        if(a[i].equals(palabra_texto)){ // si encuentra la palabra (unica) en esa linea
                        //busca el indice
                            //setea le indice en cero

                            if(i==0){// cuando el indice sea 0 entra , para que pueda leer las palabras iniciales
                                index = palabra.indexOf(palabra_texto); //quiero hacer la impresion al archivo en base al hashtable  //printResult.print( ": ("  + fila + "," + index + ")"); //escribe en el archivo de resultados el indice
                                wordIndexes.get(palabra_texto).add(new Indice(fila, index));
                            }else {
                                //suma 1 al indice anterior , para que busque el indice de la palabra siguiente
                                index = palabra.indexOf(palabra_texto ,index+1);//printResult.print( ": ("  + fila + "," + index + ")");//escribe en el archivo de resultados el indice
                                wordIndexes.get(palabra_texto).add(new Indice(fila, index));

                            }



                        }

                    }


                fila++; //como el archivo es leido por lineas , cada vez que pase por el while, se suma uno para indicar en que fila esta
            }
                //printResult.println( ""); // imprime un salto de linea , para que quede bonito xddd
                fila=0;//setea la fila en cero para iniciar el proceso con una nueva palabra


        }
        for(String palabra : palabrasUnicas){           //for para recorrer las palabras unicas
            String line = "";
            line += palabra + ":\t";         
            for(Indice idx : wordIndexes.get(palabra)){     //for para recorrer los indices almacenados para una palabra en el Hashtable
                line += "("+ idx.row + ", " + idx.col +") "; //añade cada indice de la palabra en la misma linea.
            }
            line +="\n";        //un salto de linea por palabra
            printResult.println(line);  //imprime la linea en el archivo.
        }
        printResult.close(); // se cierra el writer
        
    }
    
    
    //metodo para buscar una palabra
    ///////////  ///////////  ///////////  ///////////  ///////////  ///////////  ///////////
      ///////////  ///////////  ///////////  ///////////  ///////////  ///////////  ///////////
    
    public static String getPalabra(String palabra_buscada) throws Exception
    {
        String cadena="";
        if(wordIndexes.containsKey(palabra_buscada)){
            for(Indice idx : wordIndexes.get(palabra_buscada)){
                cadena=cadena + "("+ idx.row + ", " + idx.col +") ";
            }
            return cadena;
            
        }
        else
        return "palabra no encontrada"; // si no la encuentra retorna el mensaje
    }
    
    //metodo para buscar varias palabras
    ///////////  ///////////  ///////////  ///////////  ///////////  ///////////  ///////////
      ///////////  ///////////  ///////////  ///////////  ///////////  ///////////  ///////////
    public static String getPalabras(String[] palabras){
        String resultado = "";          //String que se retorna
        ArrayList<Integer> lineas = new ArrayList<>();      //Colección de lineas que contienen todas las palabras.
        ArrayList<ArrayList<Indice>> pals = new ArrayList<>();  //pals = Lista que almacena los ArrayList<Indice> de cada palabra a buscar
        for(String pal : palabras){                            //Lleno pals con este for
            pals.add(wordIndexes.get(pal));
        }
        int maxLines = 0;                       //Se usa para sabes cual es el ArrayList<Indice> con más indices. 
        ArrayList<Indice> maxIndices = new ArrayList<Indice>(); //almacena la colección de indices de una palabra con más indices
        for(ArrayList<Indice> idxs : pals){     //Recorro las palabras ingresadas
            if(idxs.size() > maxLines){         //reasigno si encuentro una mayor
                maxLines = idxs.size();
                maxIndices = idxs;
            }
        }
        for(Indice idx : maxIndices){           //Recorro cada indice de la palabra con más indices para compararlo con todos los otros indices de las demas palabras
            boolean lineHasAllWords = true;     //si idx.row está en todas las palabras, esta variable se mantiene true.
            for(ArrayList<Indice> pal : pals){  //Cada palabra a comparar
                boolean thisWord = false;       //Si esta palabra contiene idx.row, se setea a true
                for(Indice idxPal : pal){       //Cada indice de una palabra
                    if(idx.row ==idxPal.row)    //Comprueba si la palabra aparece en la misma linea que idx.row
                        if(idx != idxPal)
                            thisWord = true;    //La palabra si está
                }
                if(!thisWord)                   //Si una sola palabra no está, esa linea no aparece
                    lineHasAllWords = false;    //idx.row no está en al menos una palabra, entonces esa linea no contiene todas las palabras
            }
            if(lineHasAllWords)                 //Si la linea contiene todas las palabras, ingresa esa linea a lineas.
                if(!lineas.contains(idx.row))
                    lineas.add(idx.row);
        }
        for(String word : palabras)         //Arma el string a devolver para imprimir en el GUI.
            resultado += " " + word;
        resultado += "\n";
        for(Integer line : lineas)
            resultado += line + "\n";
        return resultado;                   //Devuelve el string
    }
    ///////////  ///////////  ///////////  ///////////  ///////////  ///////////  ///////////
    ///////////  ///////////  ///////////  ///////////  ///////////  ///////////  ///////////
    

    
    public static void main(String[] args) {
        launch(args);
    }
    
}
