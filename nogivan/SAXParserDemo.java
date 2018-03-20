package nogivan;


import java.util.ArrayList;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserDemo {

	public static void main(String[] args) {

	
	}
}

class UserHandler extends DefaultHandler {
	private ArrayList<String> buffered_ids = new ArrayList<String>();
	private ArrayList<OSMNode> osm_nodes = new ArrayList<OSMNode>();
	private ArrayList<OSMWay> osm_ways = new ArrayList<OSMWay>();
	private boolean in_way = false;
	private boolean to_add = false;
	private int howmany = 0;
	public static int Phase = 0;

	private boolean in_way_to_save = false;
	private String way_name;
	private ArrayList<Long> way_nodes_id = new ArrayList<Long>();
	private boolean way_oneway = false;
	private long way_id;
	public static boolean to_be_removed = false;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (Phase == 1) {
			if (qName.equalsIgnoreCase("node")) {
				osm_nodes.add(new OSMNode(Long.parseLong(attributes.getValue("id")),
						Double.parseDouble(attributes.getValue("lat")),
						Double.parseDouble(attributes.getValue("lon"))));
			} else if (qName.equalsIgnoreCase("way")) {
				in_way_to_save = true;
				way_id = Long.parseLong(attributes.getValue("id"));
			} else if (in_way_to_save && qName.equalsIgnoreCase("nd")) {
				way_nodes_id.add(Long.parseLong(attributes.getValue("ref")));
			} else if (in_way_to_save && qName.equalsIgnoreCase("tag")) {
				if (attributes.getValue("k").equals("oneway") && attributes.getValue("v").equals("yes")) {
					way_oneway = true;
				} else if (attributes.getValue("k").equals("name")) {
					way_name = attributes.getValue("v");
				}
			}
			return;
		}
		if (qName.equalsIgnoreCase("way")) {
			in_way = true;
			buffered_ids.add(attributes.getValue("id"));
			howmany++;
			return;
		}
		if (in_way && qName.equalsIgnoreCase("nd")) {
			buffered_ids.add(attributes.getValue("ref"));
			howmany++;
			return;
		}
		if (in_way && qName.equalsIgnoreCase("tag")) {

			if (attributes.getValue("k").equals("highway")) {
				to_add = true;
				
				if(attributes.getValue("v").equals("construction") || attributes.getValue("v").equals("proposed"))
					to_add = false;
			}
			
			
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (Phase == 1) {

			if (qName.equalsIgnoreCase("way")) {
				if (in_way_to_save) {
					osm_ways.add(new OSMWay(way_id, way_nodes_id.stream().toArray(Long[]::new), way_oneway, way_name));
				} 
					way_name = "";
					way_nodes_id = new ArrayList<Long>();
					way_oneway = false;
					way_id = 0;
				
			}
			return;
		}
		if (qName.equalsIgnoreCase("way")) {
			in_way = false;
			if (!to_add) {
				for (int i = 0; i < howmany; i++) {
					buffered_ids.remove(buffered_ids.size() - 1);
				}

			}
			howmany = 0;
			to_add = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

	}

	public String[] return_buffered() {
		return buffered_ids.stream().toArray(String[]::new);
	}

	public OSMNode[] getNodes() {
		return osm_nodes.stream().toArray(OSMNode[]::new);
	}

	public OSMWay[] getWays() {
		return osm_ways.stream().toArray(OSMWay[]::new);
	}

}