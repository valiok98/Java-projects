package nogivan;

public class ComparedObj implements Comparable<ComparedObj> {
	
	 private long id;
	  private int distance;
	  public long getId() {
	    return id;
	  }
	  public void setDistance(int distance) {
		  this.distance = distance;
	  }
	  public int getDistance() {
		  return distance;
	  }
	  /**
	   * Die Koordinaten des Punktes.
	   */
	  private MapPoint location;
	  
	  public MapPoint getLocation () {
	    return location;
	  }
	  
	  
	  
	  @Override
	  public String toString() {
	    return "Node {id = " + id + ", " + location + "}";
	  }

	public ComparedObj getPrev() {
		return o1;
	}
	public int getDistanceToPrev() {
		
		return location.distance(o1.getLocation());
		
	}
	private ComparedObj o1;

	
	public ComparedObj(ComparedObj o1, long id, double lat, double lon) {
		this.o1 = o1;
		this.id = id;
		this.location = new MapPoint(lat, lon);
		this.distance = Integer.MAX_VALUE;
	}
	
	@Override
	public int compareTo(ComparedObj o) {
		if(this == o)
			return 0;
		int distance1 = this.getDistance();
		int distance2 = o.getDistance();
		if(distance1 == distance2)
			return 0;
		
		if(distance1 > distance2)
			return 1;
		else {
			
			return -1;
		}
	}
}
