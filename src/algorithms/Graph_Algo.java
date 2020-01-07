package algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.management.RuntimeErrorException;

import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.edge_data;
import dataStructure.NodeData;
import dataStructure.DGraph;
import dataStructure.EdgeData;



/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable{

	private graph gr;
	public Graph_Algo() {
		this.gr=new DGraph();
	}
	public Graph_Algo(graph _graph) {
		this.gr=_graph;
	}
	@Override
	public void init(graph g) {
		this.gr=g;
	}

	public void clearG(){
		for(node_data v: gr.getV())
		{
			v.setInfo(Double.toString(v.getWeight()));//save the original weight
			v.setWeight(Double.POSITIVE_INFINITY); // set all nodes to infinity
			v.setTag(0); // set tag to 0 (not visited)
		}
	}

	@Override
	public void init(String file_name) {
		try {
			FileInputStream file = new FileInputStream(new File(file_name));
			ObjectInputStream obj = new ObjectInputStream(file);
			Graph_Algo g =  (Graph_Algo) obj.readObject();
			this.gr =g.gr;
		}catch (RuntimeException | IOException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 

	}

	@Override
	public void save(String file_name) {
		try {
			FileOutputStream file = new FileOutputStream(new File(file_name));
			ObjectOutputStream obj = new ObjectOutputStream(file);
			obj.writeObject(this);
			obj.close();
			file.close();
		} catch (RuntimeException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		int counter=0;
		int n=gr.nodeSize();
		if (n==0)return true; // end case
		clearG(); // reset graph 
		Iterator<node_data> N= this.gr.getV().iterator(); // iterator to this.gr nodes
		if(n==1)return true; // end case
		node_data v=N.next();
		Stack<node_data> F = new Stack<node_data>();
		gr.getNode(v.getKey()).setTag(1);// v visited
		F.push(gr.getNode(v.getKey()));
		while(!F.isEmpty())
		{
			node_data x=F.pop();
			counter++;
			gr.getNode(x.getKey()).setTag(1);// x visited
			Collection<edge_data> ne=gr.getE(x.getKey());
			for(edge_data e: ne)
			{
				if(gr.getNode(e.getDest()).getTag()==0)
				{
					F.add(gr.getNode(e.getDest()));
				}
			}
		}
		return (counter==n);
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if(src==dest)return 0;
		clearG();// reset graph 
		gr.getNode(src).setWeight(0);// reset start node to zero (minimum)
		while(!visited(this.gr))
		{
			node_data min=minW(this.gr,src); // save the min weight node
			min.setTag(1);//visited
			Collection<edge_data> ne=gr.getE(min.getKey()); // neighbors of min node
			for(edge_data e:ne)
			{
				double w=min.getWeight()+e.getWeight();// sum weight of vertex and edge neighbor
				if(w<gr.getNode(e.getDest()).getWeight())
				{
					gr.getNode(e.getDest()).setWeight(w);
					gr.getNode(e.getDest()).setInfo(String.valueOf(gr.getNode(e.getSrc()).getKey()));// save parent on info for shortestPath
					//					e.setTag(1);
				}
			}
		}
		return gr.getNode(dest).getWeight();
	}

	/*
	 * check if all graph visited or not
	 */
	public boolean visited(graph g) {
		boolean ans=true;
		for(node_data v:g.getV())
		{
			if(v.getTag()==0)ans=false;
		}
		return ans;
	}

	/*
	 * find node that not visited with min weight 
	 * return key of node
	 */
	public node_data minW(graph g,int src) {
		node_data ver=g.getNode(src);
		double minW=Double.POSITIVE_INFINITY;
		for(node_data v:g.getV())
		{
			if(v.getTag()==0&&v.getWeight()<=minW)//check not visited and min weight
			{
				minW=v.getWeight();
				ver=v;
			}
		}
		return ver;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		ArrayList<node_data> sp=new ArrayList<node_data>();
		if(src==dest) // end case to 1 vertex
		{
			sp.add(gr.getNode(src));
			return sp;
		}

		shortestPathDist(src,dest);	
		sp.add(0,gr.getNode(dest));//add dest
		int key=-1;
		node_data d=gr.getNode(dest);
		while(key!=src)
		{
			node_data x=this.gr.getNode(Integer.parseInt(d.getInfo()));
			sp.add(0, x);
			key=x.getKey();
			d=gr.getNode(key);
		}
		if(sp.size()==0)
		{
			throw new ArithmeticException("no path");
		}
		else {
			return sp;
		}
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		if(targets.size()==0)return null;
		ArrayList<node_data>list=new ArrayList<node_data>();
		if(targets.size()==1)
		{
			list.add(this.gr.getNode(targets.get(0)));
			return list;
		}
		for (int i = 0; i+1 < targets.size(); i++)
		{
			if(list.contains(this.gr.getNode(targets.get(i))) )
			{
				list.remove(this.gr.getNode(targets.get(i)));
			}
			list.addAll(shortestPath(targets.get(i), targets.get(i+1)));
		}
		return list;
	}

	@Override
	public graph copy() {
		graph g = new DGraph();
		for(node_data v:this.gr.getV())
		{
			g.addNode(v);
			for(edge_data e: this.gr.getE(v.getKey()))
			{
				g.connect(e.getSrc(), e.getDest(), e.getWeight());
			}
		}
		return g;
	}
}
