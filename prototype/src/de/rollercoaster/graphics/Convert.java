import java.io.*;
import java.util.*;


public class Convert {
	private File f;
	private BufferedInputStream bis; //überhaupt nötig?
	private BufferedReader br;
	private FileReader fr;
	private LinkedList<float[]> v_list, vn_list;
	private LinkedList<int[]> f_list;

	
	private void init() throws FileNotFoundException{
		f = new File("D:/Dokumente/Uni/SEP/course.obj");
		fr = new FileReader(f);
		br = new BufferedReader(fr);
		v_list = new LinkedList<float[]>();
		vn_list = new LinkedList<float[]>();
		f_list = new LinkedList<int[]>();
	}
	
	private void readLines() throws IOException{
		String s;
		//solange ich Zeilen habe:...
		while((s=br.readLine())!=null){
			//Zeile steht in s drin
			//System.out.printf("->%s<-\n", s);	//Kontrollausgabe
			interpredLine(s);
		}
	}
	
	private void interpredLine(String inputLine){
		//laufindex
		int i = 0;
		//erster Buchstabe zur Identifizierung der Zeile
		char firstChar = inputLine.charAt(0);
		
		//Welcher Typ von Zeile ist es?
		switch (firstChar) {
		case 'f':
			//System.out.printf("Starts with \'f\':%s\n", inputLine);
			int[] temp_int = new int[8];
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
			

			f_list.add(temp_int);
			break;

		case 'v':
			float[] temp_float = new float[3];
			String temp2[] = inputLine.split(" ");
			for(i = 0;i<3;i++){
				temp_float[i] = Float.parseFloat(temp2[i+1]);
			}
			if (inputLine.charAt(1)=='n'){
				vn_list.add(temp_float);
			}
			else if (inputLine.charAt(1)==' '){
				v_list.add(temp_float);
			}else{
				System.out.printf("Do nothing at:%s\n", inputLine);
			}
			
			break;
		
		default:
			System.out.printf("Do nothing at:%s\n", inputLine);
			break;
		}
	}
	
	private void printLists(){
		System.out.printf("V-Values(%d)\n", v_list.size());
		System.out.println("-----------------");
		for (float[] fa: v_list){
			System.out.printf("%s\n", Arrays.toString(fa));
		}
		System.out.printf("VN-Values(%d)\n", vn_list.size());
		System.out.println("-----------------");
		for (float[] fa: vn_list){
			System.out.printf("%s\n", Arrays.toString(fa));
		}
		System.out.printf("F-Values(%d)\n", f_list.size());
		System.out.println("-----------------");
		for (int[] ia: f_list){
			System.out.printf("%s\n", Arrays.toString(ia));
		}
	}
	
	public Convert(){
		try{
			init();
			readLines();
		} catch(Exception e){
			e.printStackTrace();
		}
		printLists();
	}
	
	
	public static void main(String args[]){
		new Convert();
//		Scanner s = new Scanner("v 0.768166 -0.773011 -0.106110");
//		String sa[] = "v 0.768166 -0.773011 -0.106110".split(" ");
//		System.out.println(Arrays.toString(sa));
//		while(s.hasNext()){
//			System.out.printf("%s\n", s.next());
//		}
	}
}
