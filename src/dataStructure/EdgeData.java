package dataStructure;

import java.io.Serializable;

public class EdgeData implements edge_data, Serializable {
	private int src;
	private int dest;
	private double weight;
	private String info;
	private int tag;

	public EdgeData() {
		this.src=-1;
		this.dest=-1;
		this.weight=-1;
		this.info="";
		this.tag=-1;
	}

	public	EdgeData(int src,int dest,	double weight,String info,int tag) {
		this.src=src;
		this.dest=dest;
		this.weight=weight;
		this.info=info;
		this.tag=tag;
	}
	public	EdgeData(int src,int dest,double weight) {
		this.src=src;
		this.dest=dest;
		this.weight=weight;
	}
	public EdgeData copy() {
		EdgeData E=new EdgeData(this.src,this.dest,	this.weight,this.info,this.tag);
		return E;
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
	public double getWeight() {
		return this.weight;
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
//public String tostring () {
//	
//	return ""+this.dest+this.weight ;
//	
//}
}
