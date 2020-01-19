package MydataStructure;
import java.util.Collection;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import MyUtils.Point3D;

import java.io.Serializable;


public class DGraph implements graph, Serializable{
	private Hashtable  <Integer, node_data> Nodes;
	private Hashtable <node_data,Hashtable<Integer, edge_data>> Edges; // hashtable into hashtable with Node key (and dest key)

	private int edgeSize;
	private int mc=0; // mode count


	public DGraph() {
		Nodes=new Hashtable <Integer, node_data>();
		Edges=new Hashtable<node_data,Hashtable<Integer, edge_data>>();
		edgeSize=0;
		this.mc=0;
	}
	public void init(String strG) throws JSONException {
		JSONObject jo = new JSONObject(strG); // read all text to json file
		try {
			JSONArray arrayN = jo.getJSONArray("Nodes"); // array to nodes
			JSONArray arrayE = jo.getJSONArray("Edges"); // array to edges
			for (int i = 0; i <  arrayN.length(); i++) //bulid nodes for graph
			{
				JSONObject oN= arrayN.getJSONObject(i);
				int id=oN.getInt("id");
				String pos=oN.getString("pos");
				node_data v=new NodeData(id, new Point3D (pos));
				int k=v.getKey();
				this.Nodes.put(k ,v);
				this.Edges.put(v, new Hashtable<Integer,edge_data>());
			}
			for (int i = 0; i <  arrayE.length(); i++) //bulid edges for graph
			{
				JSONObject oE= arrayE.getJSONObject(i);
				int src=oE.getInt("src");
				double w=oE.getDouble("w");
				int dest=oE.getInt("dest");
				connect(src,dest,w);
			}
		} catch(Exception e) {e.printStackTrace();}
	}
	

	@Override
	public node_data getNode(int key) {
		return Nodes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(Nodes.get(src)==null||Nodes.get(dest)==null||src==dest)return null;
		node_data v=Nodes.get(src);
		return Edges.get(v).get(dest);
	}

	@Override
	public void addNode(node_data n) {
		int k=n.getKey();
		this.Nodes.put(k ,n);
		this.Edges.put(n, new Hashtable<Integer,edge_data>());
		this.mc++;
	}
	
	@Override
	public void connect(int src, int dest, double w) {
		try {
			edge_data e=new EdgeData(src,dest,w);
			Edges.get(Nodes.get(src)).put(dest, e);
			edgeSize++;
			this.mc++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public Collection<node_data> getV() {
		return this.Nodes.values();
	}


	@Override
	public Collection<edge_data> getE(int node_id) {
		return Edges.get(Nodes.get(node_id)).values();
	}

	@Override
	public node_data removeNode(int key) {
		node_data temp=Nodes.get(key);
		if(temp==null)
		{
			return temp;
		}
		this.Nodes.remove(key);
		this.mc++;
		return temp;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		edge_data temp=Edges.get(Nodes.get(src)).get(dest);
		if(temp==null)
		{
			return temp;
		}
		Edges.get(Nodes.get(src)).remove(dest);
		this.mc++;
		return temp;
	}

	@Override
	public int nodeSize() {
		return Nodes.size();
	}

	@Override
	public int edgeSize() {
		return this.edgeSize;
	}

	@Override
	public int getMC() {
		return this.mc;
	}

	public String tostring () {
		String ans="";

		for(Integer key: this.Nodes.keySet()){
			ans+="      "+key+": "+ this.Edges.get(this.Nodes.get(key)).toString();

		}
		return ans;
	}

}
