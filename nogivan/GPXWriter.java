package nogivan;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GPXWriter {
  private PrintWriter writer;
  private String fileName;

  public GPXWriter(String fileName) throws FileNotFoundException {
	  this.fileName = fileName;
  }

  private void writeLine(String line) {
    
  }

  public void close() {
    writer.close();
  }
  
  public void write(RoutingResult rr) {
	  writeGPX(rr);
  }
  
  public void writeGPX(RoutingResult rr) {
    // Todo
	  BufferedWriter writer;
	try {
		writer = new BufferedWriter(new FileWriter(fileName));
		OSMNode[] path = rr.getPath();
		  writer.write("<?xml version=\"1.0\" encoding="+"\"UTF-8\""+ " standalone="+"\"no\""+ "?>\n");
		  writer.write("<gpx version=\"1.1\" creator=\"Ersteller der Datei\">\n");
		  writer.write("<metadata>  </metadata>\n");
		  writer.write("<trk>\n");
		  writer.write("<trkseg>\n");
		  for(int i=0;i<path.length;i++) {
			  writer.write("<trkpt lat=\""+ path[i].getLat()+"\" lon=\""+path[i].getLon()+"\"></trkpt>\n");		  
		  }
		  writer.write("</trkseg>\n");
		  writer.write("</trk>\n");
		  writer.write("</gpx>\n");
		  writer.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	    
	  
  }
}
