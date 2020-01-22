package MydataStructure;

import java.util.List;

import MyUtils.Point3D;

public class RobotData implements node_robot {
	private int id;
	private int src;
	private Point3D location;
	private List<node_data> path;
	private int onTheWay;


	public RobotData() {
		this.id=-1;
		this.src=-1;
		this.location=null;
		this.path=null;
		this.onTheWay=0;

	}
	public RobotData(int id,int src, Point3D p) {
		this.id=id;
		this.src=src;
		this.location=p;
	}
	@Override
	public int getId() {
		return this.id;
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
	public int getSrc() {
		return this.src;
	}
	@Override
	public List<node_data> getPath() {
		return this.path;		
	}
	@Override
	public void setPath(List<node_data> p) {
		this.path=p;		
	}
	@Override
	public int getOnTheWay() {
		return this.onTheWay;
	}
	@Override
	public void setOnTheWay(int otw) {
		this.onTheWay=otw;

	}
}
