/*
 * Liest aus einer übergebenen Datei oder dem Standartpfad die Vektoren aus
 * TODO -> Standartpfad ändern?
 */
package graphics;

/**
 *
 * @author Robin
 */
import com.jme3.math.Vector3f;
import java.io.*;
import java.util.*;


public class Pattern {
	private File f;
	private BufferedReader br;
	private FileReader fr;
	//private LinkedList<float[]> v_list, vn_list;
	//private LinkedList<int[]> f_list;
        private LinkedList<Trip> trips;
        private LinkedList<Vector3f> vertexs, normalVertexs;

	
	/**
	*Lädt die Datei und initialisert die Buffer um aus der Datei zu lesen
         * und die Listen
	*/
	private void init() throws FileNotFoundException{
	//TODO DAteieingabe klären
		f = (f==null)?new File("D:/Dokumente/Uni/SEP/course.obj"):f;
		fr = new FileReader(f);
		br = new BufferedReader(fr);
                
                this.trips = new LinkedList<Trip>();
                this.vertexs = new LinkedList<Vector3f>();
                this.normalVertexs = new LinkedList<Vector3f>();
	}
	
	/**
	*Liest die geladene Datei zeilenweise aus und gibt sie zur Weiterverarbeiung weiter
	*@see interpredLine
	*
	*/
	private void readLines() throws IOException{
		String s;
		//solange ich Zeilen habe:...
		while((s=br.readLine())!=null){
			//Zeile steht in s drin
			interpredLine(s);
		}
	}
	
	/**
	* Prüft was für eine Zeile es ist, zieht sie dementsprechend auseinander uns ordnet sie
	* in den jeweiligen Listen an
	*
	*@param die zu lesende Zeile
	*/
	private void interpredLine(String inputLine){
		//laufindex
		int i = 0;
                int[] temp_int = null;
		//erster Buchstabe zur Identifizierung der Zeile
		char firstChar = inputLine.charAt(0);
		
		//Welcher Typ von Zeile ist es?
		switch (firstChar) {
		case 'f':
			//System.out.printf("Starts with \'f\':%s\n", inputLine);
			temp_int = new int[8];
			String temp1[] = inputLine.split("//");
			
			//Nr. 0 braucht eine extra-Behandlungm weil da das f noch drinsteht
			temp_int[0] = Integer.parseInt(temp1[0].substring(1).trim());
			
			temp_int[1] = Integer.parseInt(temp1[1].split(" ")[0]);
			temp_int[2] = Integer.parseInt(temp1[1].split(" ")[1]);


			temp_int[3] = Integer.parseInt(temp1[2].split(" ")[0]);
			temp_int[4] = Integer.parseInt(temp1[2].split(" ")[1]);


			temp_int[5] = Integer.parseInt(temp1[3].split(" ")[0]);
			temp_int[6] = Integer.parseInt(temp1[3].split(" ")[1]);
			
			temp_int[7] = Integer.parseInt(temp1[4].trim());
			

			//f_list.add(temp_int);
                        trips.add(new Trip(temp_int));
			break;

		case 'v':
			float[] temp_float = new float[3];
			String temp2[] = inputLine.split(" ");
			for(i = 0;i<3;i++){
				temp_float[i] = Float.parseFloat(temp2[i+1]);
			}
			if (inputLine.charAt(1)=='n'){
                                vertexs.add(new Vector3f(temp_float[0], temp_float[1], temp_float[2]));
			}
			else if (inputLine.charAt(1)==' '){
                                normalVertexs.add(new Vector3f(temp_float[0], temp_float[1], temp_float[2]));
			}else{
				//System.out.printf("Do nothing at:%s\n", inputLine);
			}
			
			break;
		
		default:
			break;
		}   //end of switch
                
	}
        /**
		*Liefert den gefragten Trip aus der Liste
		* @param welcher Trip
		* @return der angeforderete Trip
		*/
        public Trip getTrip(int count){
            return count<0?null:(trips.size()>count?trips.get(count):null);
        }
        
        /**
         * Gibt die Anzahl der Trips zurück
         * @return Anzahl
         */
        public int getAmountOfTrips(){
            return this.trips==null?-1:trips.size();
        }
        
		 /**
		*Liefert den gefragten Vertex-Vektor aus der Liste
		* @param welcher Vektor
		* @return der angeforderete Vector3f
		*/
        public Vector3f getVertextes(int count){
            return count<0?null:(vertexs.size()>count?vertexs.get(count):null);
        }
        
        /**
         * Gibt die Anzahl der Vertex-Vektoren zurück
         * @return Anzahl
         */
        public int getAmountOfVertexes(){
            return this.vertexs==null?-1:vertexs.size();
        }
        
		 /**
		*Liefert den gefragten NormalVertex-Vektor aus der Liste
		* @param welcher NormalVektor
		* @return der angeforderete Vector3f
		*/
        public Vector3f getNormalVertexes(int count){
            return count<0?null:(normalVertexs.size()>count?normalVertexs.get(count):null);
        }
        
        
        /**
         * Gibt die Anzahl der NormalenVertexes zurück
         * @return Anzahl
         */
        public int getAmountOfNVertexes(){
            return this.normalVertexs==null?-1:normalVertexs.size();          
        }
        

	/**
	*Konstruktor ohne Argumente, vll die Datei übergeben
	*/
	public Pattern(){
		try{
			init();
			readLines();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	*Die zu lesende Datei wird übergeben
	*/
	public Pattern(File f){
		this.f = f;
			try{
			init();
			readLines();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	*Main-Methode zum starten/überprüfen, muss aber nicht drin bleiben
	*/
	public static void main(String args[]){
		new Pattern();
	}
}

