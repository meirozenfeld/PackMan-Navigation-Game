package dataStructure;

import java.awt.Color;
import java.io.Serializable;
import java.util.Hashtable;

import utils.Point3D;

public class NodeData implements node_data , Serializable{
	private int key;
	private double weight;
	private Point3D location;
	private String info;
	private int tag;

	public NodeData() {
		this.key=0;
		this.weight=Double.POSITIVE_INFINITY;
		this.location=new Point3D(0, 0, 0);
		this.info="";
		this.tag=0;
		

	}
	public NodeData (int key, double weight, Point3D location, String info, int tag) {
		this.key=key;
		this.weight=weight;
		this.location=location;
		this.info=info;
		this.tag=tag;
	}
	public NodeData (int key) {
		this.key=key;
		this.weight=Double.POSITIVE_INFINITY;
		this.location=new Point3D(0, 0, 0);
		this.info="";
		this.tag=0;
	}
	public NodeData (int key,Point3D p) {
		this.key=key;
		this.weight=Double.POSITIVE_INFINITY;
		this.setLocation(p);
		this.info="";
		this.tag=0;
	}
	public NodeData copy() {
		NodeData n=new NodeData(this.key,this.weight,this.location,this.info,this.tag);
		return n;
	}
	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public Point3D getLocation() {
		return this.location;
	}

	@Override
	public void setLocation(Point3D p) {
		this.location=new Point3D(p.x(),p.y(),p.z());

	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(double w) {
		this.weight=w;
	}

	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		this.info=s;

	}

	@Override
	public int getTag() {
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag=t;

	}
}
