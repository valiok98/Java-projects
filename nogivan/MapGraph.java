package nogivan;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import nogivan.OSMNode;

/**
 * Diese Klasse repräsentiert den Graphen der Straßen und Wege aus
 * OpenStreetMap.
 */
public class MapGraph {
  private Map<Long, OSMNode> nodes;
  private Map<Long, Set<MapEdge>> edges;
  private Map<Long, Boolean> visited;
  private Map<Long, Boolean> inHeap;
  private Map<Long, Object> getHandle;
  
  public MapGraph(Map<Long, OSMNode> nodes, Map<Long, Set<MapEdge>> edges) {
	  this.nodes = nodes;
	  this.edges = edges;
	  visited = new HashMap<Long, Boolean>();
	  inHeap = new HashMap<Long, Boolean>();
	  getHandle = new HashMap<Long, Object>();
	  System.out.println("Length of vertices: " + nodes.size());
	  System.out.println("Length of vertices: " + edges.size());
  }
  
  /**
   * Ermittelt, ob es eine Kante im Graphen zwischen zwei Knoten gibt.
   * 
   * @param from der Startknoten
   * @param to der Zielknoten
   * @return 'true' falls es die Kante gibt, 'false' sonst
   */
  boolean hasEdge(OSMNode from, OSMNode to) {
    /*
     * Todo
     */
	  
	  Set<MapEdge> testSet = new HashSet<MapEdge>();
	  testSet = edges.get(from.getId());
	  for(MapEdge edge : testSet) {
		  if(edge.getTo() == to.getId()) {
			  return true;
		  }
	  }
	  return false;
	  
  }

  /**
   * Diese Methode findet zu einem gegebenen Kartenpunkt den
   * nähesten OpenStreetMap-Knoten. Gibt es mehrere Knoten mit
   * dem gleichen kleinsten Abstand zu, so wird derjenige Knoten
   * von ihnen zurückgegeben, der die kleinste Id hat.
   * 
   * @param p der Kartenpunkt
   * @return der OpenStreetMap-Knoten
   */
  public OSMNode closest (MapPoint p) {
    /*
     * Todo
     */
	  OSMNode result = null;
	  int dist = Integer.MAX_VALUE;
	  for(Long node_id : nodes.keySet()) {
		  visited.put(node_id, Boolean.parseBoolean(""+false));
		  inHeap.put(node_id, Boolean.parseBoolean(""+false));
		  if(p.distance(nodes.get(node_id).getLocation()) <= dist) {
			  if(dist == p.distance(nodes.get(node_id).getLocation())) {
				  result = result.getId() > node_id ? nodes.get(node_id) : result;
				  continue;
			  }
			  dist = p.distance(nodes.get(node_id).getLocation());
			  
			  result = nodes.get(node_id);
		  }
	  }
	  
	  return result;
  }

  /**
   * Diese Methode sucht zu zwei Kartenpunkten den kürzesten Weg durch
   * das Straßen/Weg-Netz von OpenStreetMap.
   * 
   * @param from der Kartenpunkt, bei dem gestartet wird
   * @param to der Kartenpunkt, der das Ziel repräsentiert
   * 
   * @return eine mögliche Route zum Ziel und ihre Länge; die Länge
   * des Weges bezieht sich nur auf die Länge im Graphen, der Abstand
   * von 'from' zum Startknoten bzw. 'to' zum Endknoten wird
   * vernachlässigt.
   */
  public RoutingResult route (MapPoint from, MapPoint to) {
    /*
     * Todo
     */
	  Map<ComparedObj,ComparedObj> list_of_objects = new HashMap<ComparedObj,ComparedObj>();
	  BinomialHeap<ComparedObj> new_heap = new BinomialHeap<ComparedObj>();
	  OSMNode start = closest(from);
	  OSMNode end = closest(to);
	  //assertEquals(end.getId(),277698572L);
	  ComparedObj starting = new ComparedObj(null,start.getId(),start.getLocation().getLat(),start.getLocation().getLon());
	  ComparedObj ending = new ComparedObj(null,end.getId(),end.getLocation().getLat(),end.getLocation().getLon());
	  starting.setDistance(0);
	  getHandle.put(starting.getId(), new_heap.insert(starting));
	  int counter = 0;
	  while(new_heap.getSize() != 0) {
		  
		  ComparedObj curr = (ComparedObj) new_heap.poll();
		  if(curr.getId() == ending.getId())
			  ending = curr;
		  visited.put(Long.parseLong(""+curr.getId()), Boolean.parseBoolean(""+true));
		  inHeap.put(Long.parseLong(""+curr.getId()), Boolean.parseBoolean(""+false));
		  Set<MapEdge> get_edges = edges.get(curr.getId());
		  for(MapEdge edge : get_edges) {
			  if(visited.get(Long.parseLong(""+edge.getTo())).equals(Boolean.parseBoolean(""+true)))
				  continue;
			  ComparedObj child = new ComparedObj(curr,edge.getTo(),nodes.get(edge.getTo()).getLocation().getLat(),nodes.get(edge.getTo()).getLocation().getLon());
			  list_of_objects.put(child,curr);
			  if(child.getId() == ending.getId())
				  ending = child;
			  if(visited.get(Long.parseLong(""+child.getId())).equals(Boolean.parseBoolean(""+false)) && curr.getDistance() + child.getDistanceToPrev() < child.getDistance()) {
				  child.setDistance(curr.getDistance() + child.getDistanceToPrev());
				  if(inHeap.get(Long.parseLong(""+child.getId())).equals(Boolean.parseBoolean(""+true))) {
					  @SuppressWarnings("unchecked")
					BinomialHeapHandle<ComparedObj> handleCasted = (BinomialHeapHandle<ComparedObj>) getHandle.get(Long.parseLong(""+child.getId())) ;
					if(handleCasted.getNode().getElement().getDistance() >= child.getDistance()) {
						new_heap.replaceWithSmallerElement(getHandle.get(Long.parseLong(""+child.getId())),child);
					}
				  else {
					 getHandle.put(Long.parseLong(""+child.getId()), new_heap.insert(child));
					 inHeap.put(Long.parseLong(""+child.getId()), Boolean.parseBoolean(""+true));
				  }
				  }
				  else {
						 getHandle.put(Long.parseLong(""+child.getId()), new_heap.insert(child));
						 inHeap.put(Long.parseLong(""+child.getId()), Boolean.parseBoolean(""+true));
					  }
			  }
		  }  
	  }
	  
	  if(ending.getDistance() == Integer.MAX_VALUE) {
		  System.out.println("Id of ending " + ending.getId());
		  return null;
	  }
	  System.out.println("id of ending Node " + ending.getId() + "  Distance " + ending.getDistance()+1);
	  ComparedObj tmp = list_of_objects.get(ending);
	  
	  ArrayList<OSMNode> way = new ArrayList<OSMNode>();
	  way.add(nodes.get(ending.getId()));
	  while(tmp != starting ) { 
		  
		  way.add(nodes.get(tmp.getId()));
		  tmp = list_of_objects.get(tmp);
	  }
	  way.add(nodes.get(starting.getId()));
	  Collections.reverse(way);
	  System.out.println("length of way : " + way.size());
	  return new RoutingResult(way.stream().toArray(OSMNode[]::new),ending.getDistance()+1);
	  
  }
}
