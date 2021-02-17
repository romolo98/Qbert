package br.ol.qbert.dlv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;


public class HandlerDlv {
	private Handler handler;
	private boolean firstTimeEnemy1;
	private boolean firstTimeEnemy2;
    private ASPMapper mapper = ASPMapper.getInstance();
    private String encoding;
    private String facts;
    private Scelta scelta;
    private ArrayList<String> visitate;
    private String nemico1;
    private String nemico2;  
    private String factsEnemy1;
    private String factsEnemy2;
    
    public void init() {
    	try {
	    	handler = new DesktopHandler(new DLV2DesktopService("executable/dlv2.exe"));
	    	mapper.registerClass(CellaScelta.class);
	    	mapper.registerClass(CelleVisitate.class);
	    	mapper.registerClass(Nemico.class);
	    	mapper.registerClass(Scelta.class);
	    	mapper.registerClass(CelleAdiacenti.class);
	    	mapper.registerClass(Strategia.class);
	    	mapper.registerClass(NonVisitate.class);
	    	InputProgram input=new ASPInputProgram();
	    	visitate = new ArrayList<String>();
	    	facts = "encoding/Facts.txt";
	    	encoding = "encoding/ProgettoDLV.txt";
	    	factsEnemy1 = "encoding/ballRed.txt";
	    	factsEnemy2 = "encoding/ballPurple.txt";
	        input.addFilesPath(encoding);
	        input.addFilesPath(facts);
	        input.addFilesPath(factsEnemy1);
	        handler.addProgram(input);
	        firstTimeEnemy1 = true;
	        firstTimeEnemy2 = true;
	        nemico1 = "";
	        nemico2 = "";
    	}
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public String getScelta ()  {
    	AnswerSets answerSets=(AnswerSets)handler.startSync();
        //System.out.println(answerSets.getErrors());

        for(AnswerSet as: answerSets.getAnswersets()){
            try {
				for(Object obj: as.getAtoms()){
					//System.out.println("Atoms= " + as.getAtoms() ); 
					if(obj instanceof Scelta){
				        scelta = (Scelta) obj;
				    }
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
        
        if (scelta!= null) {
        	//System.out.println(scelta.toString());
        	return scelta.toString();
        }
        else {
        	return "";
        }
    }
    public void updatePosizione (String oldPosition,String visitedCell) throws IOException {
      Scanner sc = new Scanner(new File(facts));
      FileWriter writer;
      StringBuffer buffer = new StringBuffer();
      while (sc.hasNextLine()) {
         buffer.append(sc.nextLine()+System.lineSeparator());
      }
      String fileContents = buffer.toString();
      //System.out.println("Contenuto TXT: \r\n" +fileContents);
      sc.close();
      writer = new FileWriter(facts);
      String oldLine = "posizione\\(.+,.+,.+\\).";
      String newLine = oldPosition;
      fileContents = fileContents.replaceAll(oldLine, newLine);
      //System.out.println("TXT Editato: \r\n"+fileContents);
      writer.append(fileContents);
      
      if(!visitate.contains(visitedCell)) {
    		visitate.add(visitedCell);
    		writer.append("\r\n"+ visitedCell);
    		}
      
      writer.flush();
      writer.close();
    }
    
    public void reset() {
    	resetFacts();
    	visitate.clear();
    }
    public void resetFacts () {
    	PrintWriter writer;
    	try {
			writer = new PrintWriter("encoding/Facts.txt");
			writer.print("posizione(16,16,112).\r\n"
					+ "cella(16,16,112).\r\n"
					+ "cella(16,32,96).\r\n"
					+ "cella(16,48,80).\r\n"
					+ "cella(16,64,64).\r\n"
					+ "cella(16,80,48).\r\n"
					+ "cella(16,96,32).\r\n"
					+ "cella(16,112,16).\r\n"
					+ "cella(32,16,96).\r\n"
					+ "cella(32,32,80).\r\n"
					+ "cella(32,48,64).\r\n"
					+ "cella(32,64,48).\r\n"
					+ "cella(32,80,32).\r\n"
					+ "cella(32,96,16).\r\n"
					+ "cella(48,16,80).\r\n"
					+ "cella(48,32,64).\r\n"
					+ "cella(48,48,48).\r\n"
					+ "cella(48,64,32).\r\n"
					+ "cella(48,80,16).\r\n"
					+ "cella(64,16,64).\r\n"
					+ "cella(64,32,48).\r\n"
					+ "cella(64,48,32).\r\n"
					+ "cella(64,64,16).\r\n"
					+ "cella(80,16,48).\r\n"
					+ "cella(80,32,32).\r\n"
					+ "cella(80,48,16).\r\n"
					+ "cella(96,16,32).\r\n"
					+ "cella(96,32,16).\r\n"
					+ "cella(112,16,16).\r\n"
					+ "\r\n"
					+ "strategia(2).\r\n"
					+ "strategia(3).\r\n"
					+ "strategia(4).\r\n"
					+ "strategia(5).\r\n"
					+ "strategia(6).\r\n");
	    	writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
