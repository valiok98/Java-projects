package nogivan;

/**
 * Diese Klasse implementiert einen Kartenpunkt. Ein
 * Kartenpunkt hat einen Position in Form eines Länge-
 * und Breitengrades.
 */
public class MapPoint {
  /**
   * Der Breitengrad
   */
  private double lat;
  
  public double getLat () {
    return lat;
  }
  
  /**
   * Der Längengrad
   */
  private double lon;
  
  public double getLon () {
    return lon;
  }
  
  public MapPoint (double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }
  
  /**
   * Diese Methode berechnet den Abstand dieses Kartenpunktes
   * zu einem anderen Kartenpunkt.
   * 
   * @param other der andere Kartenpunkt
   * @return der Abstand in Metern
   */
  public int distance(MapPoint other) {
    
	  /*double R = 6371e3; // metres
	  double f1 = Math.toRadians(lat);
	  double f2 = Math.toRadians(other.getLat());
	  double diff1 = Math.toRadians(other.getLat()-lat);
	  double diff_l = Math.toRadians(other.getLon()-lon);

	  double a = Math.sin(diff1/2) * Math.sin(diff1/2) +
	          Math.cos(f1) * Math.cos(f2) *
	          Math.sin(diff_l/2) * Math.sin(diff_l/2);
	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

	  double d = R * c;
	  
	  return (int)d;*/
	  
	  
	  
	  
		
	  double f1 = Math.toRadians(lat);
	  double f2 = Math.toRadians(other.getLat());
	  double diff1 = Math.toRadians((other.getLon()-lon));
	  double R = 6371e3; // gives d in metres
	  double d = Math.acos( Math.sin(f1)*Math.sin(f2) + Math.cos(f1)*Math.cos(f2) * Math.cos(diff1) ) * R;
	  return (int)d;
  }
  
  @Override public String toString () {
    return  "lat = " + lat + ", lon = " + lon;
  }

}
