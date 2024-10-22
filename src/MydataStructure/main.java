package MydataStructure;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import MyAlgorithms.*;
import MyUtils.*;
public class main {

	public static void main(String[] args) throws JSONException  {
		//		DGraph g=new DGraph ();
		//		NodeData n1=new NodeData(1);
		//		NodeData n2=new NodeData(2);
		//		NodeData n3=new NodeData(3);
		//		NodeData n4=new NodeData(4);
		//		NodeData n5=new NodeData(5);
		//		g.addNode(n1);
		//		g.addNode(n2);
		//		g.addNode(n3);
		//		g.addNode(n4);
		//		g.addNode(n5);
		//		g.connect(1, 2, 1000);
		//		g.connect(2, 3, 1000);
		//		g.connect(3, 4, 1000);
		//		g.connect(4, 1, 1000);
		//		g.connect(1, 5, 1000);
		//		g.connect(5, 3, 1000);

		//		
		//		graph_algorithms gr= new Graph_Algo();
		//		gr.init(g);
		//		System.out.println(gr.isConnected());


		//		DGraph g1=new DGraph ();
		//		NodeData m1=new NodeData(8);
		//		NodeData m2=new NodeData(9);
		//		NodeData m3=new NodeData(10);
		//		NodeData m4=new NodeData(11);
		//		g1.addNode(m1);
		//		g1.addNode(m2);	
		//		g1.addNode(m3);
		//		g1.addNode(m4);
		//		g1.connect(8, 9, 1);
		//		g1.connect(9, 10, 0);
		//		g1.connect(9, 11, 0);
		//		g1.connect(10, 11, 1);
		//		g1.connect(8, 11, 2);
		//		g1.connect(11, 8, 1);
		//		graph_algorithms gr1= new Graph_Algo();
		//		gr1.init(g1);
		//		graph gr2=gr1.copy();
		//		if(gr1.equals(gr2))System.out.println("fail");
		//				System.out.println(gr1.shortestPathDist(8, 11));



		//		ArrayList<node_data> sp=(ArrayList<node_data>) gr1.shortestPath(8, 10);
		//		for (int i = 0; i < sp.size(); i++) {
		//			if(i!=sp.size()-1)
		//			System.out.print(sp.get(i).getKey()+"-->");
		//			else {System.out.print(sp.get(i).getKey());}
		//		}

		//		System.out.println();
		//
		//		ArrayList<Integer> targets=new ArrayList<Integer>();
		//		targets.add(8);
		//		targets.add(9);
		//		targets.add(11);
		//		ArrayList<node_data> ts=(ArrayList<node_data>) gr1.TSP(targets);
		//		for (int i = 0; i < ts.size(); i++)
		//		{
		//			if(i!=ts.size()-1)
		//				System.out.print(ts.get(i).getKey()+"-->");
		//			else {System.out.print(ts.get(i).getKey());}
		//		}
		//		g.connect(2, 3, 1000);


		//System.out.println(g.tostring()+" "+g.getEdge(1, 2).getWeight());

		DGraph j=new DGraph ();
		String A="{\"Edges\":[{\"src\":0,\"w\":1.4004465106761335,\"dest\":1},{\"src\":0,\"w\":1.4620268165085584,\"dest\":10},{\"src\":1,\"w\":1.8884659521433524,\"dest\":0},{\"src\":1,\"w\":1.7646903245689283,\"dest\":2},{\"src\":2,\"w\":1.7155926739282625,\"dest\":1},{\"src\":2,\"w\":1.1435447583365383,\"dest\":3},{\"src\":3,\"w\":1.0980094622804095,\"dest\":2},{\"src\":3,\"w\":1.4301580756736283,\"dest\":4},{\"src\":4,\"w\":1.4899867265011255,\"dest\":3},{\"src\":4,\"w\":1.9442789961315767,\"dest\":5},{\"src\":5,\"w\":1.4622464066335845,\"dest\":4},{\"src\":5,\"w\":1.160662656360925,\"dest\":6},{\"src\":6,\"w\":1.6677173820549975,\"dest\":5},{\"src\":6,\"w\":1.3968360163668776,\"dest\":7},{\"src\":7,\"w\":1.0176531013725074,\"dest\":6},{\"src\":7,\"w\":1.354895648936991,\"dest\":8},{\"src\":8,\"w\":1.6449953452844968,\"dest\":7},{\"src\":8,\"w\":1.8526880332753517,\"dest\":9},{\"src\":9,\"w\":1.4575484853801393,\"dest\":8},{\"src\":9,\"w\":1.022651770039933,\"dest\":10},{\"src\":10,\"w\":1.1761238717867548,\"dest\":0},{\"src\":10,\"w\":1.0887225789883779,\"dest\":9}],\"Nodes\":[{\"pos\":\"35.18753053591606,32.10378225882353,0.0\",\"id\":0},{\"pos\":\"35.18958953510896,32.10785303529412,0.0\",\"id\":1},{\"pos\":\"35.19341035835351,32.10610841680672,0.0\",\"id\":2},{\"pos\":\"35.197528356739305,32.1053088,0.0\",\"id\":3},{\"pos\":\"35.2016888087167,32.10601755126051,0.0\",\"id\":4},{\"pos\":\"35.20582803389831,32.10625380168067,0.0\",\"id\":5},{\"pos\":\"35.20792948668281,32.10470908739496,0.0\",\"id\":6},{\"pos\":\"35.20746249717514,32.10254648739496,0.0\",\"id\":7},{\"pos\":\"35.20319591121872,32.1031462,0.0\",\"id\":8},{\"pos\":\"35.19597880064568,32.10154696638656,0.0\",\"id\":9},{\"pos\":\"35.18910131880549,32.103618700840336,0.0\",\"id\":10}]}";
		j.init(A);
		System.out.println(j.tostring());
	}

}
