package nogivan;

/**
 * Diese Klasse implementiert einen OSM-Knoten.
 */
public class OSMNode implements Comparable<OSMNode> {
  /**
   * Die ID des Knotens.
   */
  private long id;
  private double lat;
  private double lon;
  
  public long getId() {
    return id;
  }
  public double getLat() {
	  return lat;
  }
  public double getLon() {
	  return lon;
  }
  /**
   * Die Koordinaten des Punktes.
   */
  private MapPoint location;
  
  public MapPoint getLocation () {
    return location;
  }
  
  public OSMNode(long id, double lat, double lon) {
    this.id = id;
    this.lat = lat;
    this.lon = lon;
    this.location = new MapPoint(lat, lon);
  }
  
  @Override
  public String toString() {
    return "Node {id = " + id + ", " + location + "}";
  }

  @Override
  public int compareTo(OSMNode o) {
    return ((Long)id).compareTo(o.getId());
  }
}
