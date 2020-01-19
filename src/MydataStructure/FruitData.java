package MydataStructure;

import MyUtils.Point3D;

public class FruitData implements node_fruit {
	private int type;
	private int tagF;
	private int src;
	private int dest;
	private int withRobot;
	private Point3D location;

	public FruitData () { // default stracture to fruit
		this.dest=-1;
		this.src=-1;
		this.withRobot=0;
		this.tagF=0;
		this.location=null;
		this.type=0;
	}
	public FruitData (Point3D p,int type) { // stracture to fruit
		this.location=p;
		this.type=type;
	}

	@Override
	public int getType() {
		return this.type;
	}
	@Override
	public int getSrc() {
		return this.src;
	}
	@Override
	public int getDest() {
		return this.dest;
	}
	@Override
	public Point3D getLocation() {
		return this.location;
	}
	@Override
	public void setLocation(Point3D p) {
		this.location=p;		
	}
	@Override
	public int getTag() {
		return this.tagF;
	}
	@Override
	public void setTag(int t) {
		this.tagF=t;		
	}

	@Override
	public int getWithRobot() {
		return this.withRobot;
	}

	@Override
	public void setWithRobot(int t) {
		this.withRobot=t;		
	}

	@Override
	public void setSrc(int s) {
		this.src=s;
	}

	@Override
	public void setDest(int d) {
		this.dest=d;		
	}
}
