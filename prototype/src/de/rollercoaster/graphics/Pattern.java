/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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

	
	private void init() throws FileNotFoundException{
		f = new File("D:/Dokumente/Uni/SEP/course.obj");
		fr = new FileReader(f);
		br = new BufferedReader(fr);
	}
	
	private void readLines() throws IOException{
		String s;
		//solange ich Zeilen habe:...
		while((s=br.readLine())!=null){
			//Zeile steht in s drin
			interpredLine(s);
		}
	}
	
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
			System.out.printf("Do nothing at:%s\n", inputLine);
			break;
		}   //end of switch
                
	}
        
        public Trip getTrip(int count){
            return (trips.size()>count?trips.get(count):null);
        }
        
        public Vector3f getVertextes(int count){
            return (vertexs.size()>count?vertexs.get(count):null);
        }
        
        public Vector3f getNormalVertexes(int count){
            return (normalVertexs.size()>count?normalVertexs.get(count):null);
        }
        

	
	public Pattern(){
		try{
			init();
			readLines();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]){
		new Pattern();
//		Scanner s = new Scanner("v 0.768166 -0.773011 -0.106110");
//		String sa[] = "v 0.768166 -0.773011 -0.106110".split(" ");
//		System.out.println(Arrays.toString(sa));
//		while(s.hasNext()){
//			System.out.printf("%s\n", s.next());
//		}
	}
}

