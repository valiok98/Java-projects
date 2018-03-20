package nogivan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import nogivan.MapGraph;

/**
 * Diese Klasse erlaubt es, aus einer Datei im OSM-Format ein MapGraph-Objekt zu
 * erzeugen. Sie nutzt dazu einen XML-Parser.
 */
public class MapParser {
	public static MapGraph parseFile(String fileName) {

		try {
			File inputFile = new File("src\\nogivan\\"+fileName);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			UserHandler userhandler = new UserHandler();
			saxParser.parse(inputFile, userhandler);

			String[] to_remove = userhandler.return_buffered();

			BufferedReader reader = new BufferedReader(new FileReader("src\\nogivan\\"+fileName));
			BufferedWriter writer = new BufferedWriter(new FileWriter("src\\nogivan\\AnotherTestAns.osm"));
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			boolean multiple = false;
			boolean flag = false;
			while (true) {
				line = reader.readLine();
				if(line.equalsIgnoreCase("</osm>") ) {
					stringBuffer.append(line);
					stringBuffer.append("\n");
					break;
				}
				if (multiple && (line.equalsIgnoreCase("  </node>") || line.equalsIgnoreCase("  </way>"))) {
					multiple = false;
					flag = false;
					stringBuffer.append(line);
					stringBuffer.append("\n");
					continue;
				}
				for (int j = 0; j < to_remove.length; j++) {
					if (line.toLowerCase().indexOf(to_remove[j].toLowerCase()) != -1) {
						if (line.charAt(line.length() - 1) == '>' && line.charAt(line.length() - 2) != '/') {
							multiple = true;
						}
						
						flag = true;
						break;
					}

				}
				if (flag || multiple) {
					stringBuffer.append(line);
					stringBuffer.append("\n");
					flag = false;
					continue;
				}
				
				if (line.toLowerCase().indexOf("<way".toLowerCase()) == -1 && line.toLowerCase().indexOf("<nd".toLowerCase()) == -1 && line.toLowerCase().indexOf("<tag".toLowerCase()) == -1 && line.toLowerCase().indexOf("</way".toLowerCase()) == -1 && line.toLowerCase().indexOf("<node".toLowerCase()) == -1 && line.toLowerCase().indexOf("</node".toLowerCase()) == -1 ) {
					stringBuffer.append(line);
					stringBuffer.append("\n");
				}
				
				
				
			}
			reader.close();
			writer.write(stringBuffer.toString());
			writer.close();
		} catch (

		Exception e) {
			e.printStackTrace();
		}

		OSMNode[] osm_node;
		OSMWay[] osm_way;
		try {
			File inputFile1 = new File("src\\nogivan\\AnotherTestAns.osm");
			SAXParserFactory factory1 = SAXParserFactory.newInstance();
			SAXParser saxParser1 = factory1.newSAXParser();
			UserHandler userhandler1 = new UserHandler();
			UserHandler.Phase = 1;
			saxParser1.parse(inputFile1, userhandler1);

			osm_node = userhandler1.getNodes();
			osm_way = userhandler1.getWays();

			Map<Long, OSMNode> map1 = new HashMap<Long, OSMNode>();
			Map<Long, Set<MapEdge>> map2 = new HashMap<Long, Set<MapEdge>>();
			for (int i = 0; i < osm_node.length; i++) {
				map1.put(Long.parseLong(""+osm_node[i].getId()), osm_node[i]);
				map2.put(Long.parseLong(""+osm_node[i].getId()), new HashSet<MapEdge>());
			}
			
			for (int i = 0; i < osm_way.length; i++) {
				
				if(osm_way[i].isOneWay()) {
					
					for (int j = 0; j < osm_way[i].getNodes().length-1; j++) {
						Set<MapEdge> tmp = map2.get(Long.parseLong(""+osm_way[i].getNodes()[j]));
						tmp.add(new MapEdge((long) (osm_way[i].getNodes()[j + 1]), osm_way[i]));
						map2.put(Long.parseLong(""+osm_way[i].getNodes()[j]), tmp);
					}
					
				}else {
					
					for (int j = 0; j < osm_way[i].getNodes().length-1; j++) {
						Set<MapEdge> tmp = map2.get(Long.parseLong(""+osm_way[i].getNodes()[j]));
						tmp.add(new MapEdge((long) (osm_way[i].getNodes()[j + 1]), osm_way[i]));
						map2.put(Long.parseLong(""+osm_way[i].getNodes()[j]), tmp);
					}
					for (int j = osm_way[i].getNodes().length-1; j >= 1 ; j--) {
						Set<MapEdge> tmp = map2.get(Long.parseLong(""+osm_way[i].getNodes()[j]));
						tmp.add(new MapEdge((long) (osm_way[i].getNodes()[j - 1]), osm_way[i]));
						map2.put(Long.parseLong(""+osm_way[i].getNodes()[j]), tmp);
					}
				}
				
			}
			
																					
			MapGraph result = new MapGraph(map1, map2);
			UserHandler.Phase = 0;
			return result;

		} catch (

		Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	public static void main(String args[]) {
		
		MapParser new_parser = new MapParser();
		MapGraph map_g = new_parser.parseFile("src\\nogivan\\campus_garching.osm");
		
		
		
		
	}
}



