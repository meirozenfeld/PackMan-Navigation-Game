package gui;

import MyAlgorithms.Graph_Algo;
import MyAlgorithms.graph_algorithms;
import MydataStructure.DGraph;
import MydataStructure.NodeData;
import MydataStructure.graph;
import MydataStructure.node_data;
import gameClient.MyGameGui;
import MyUtils.Point3D;

public class mainGui {

	public static void main(String[] args) {
		//		DGraph g=new DGraph ();
		//		Point3D p1=new Point3D(36,20,0);
		//		Point3D p2=new Point3D(37,22,0);
		//		Point3D p3=new Point3D(38,24,0);
		//		Point3D p4=new Point3D(25,20,0);
		//		Point3D p5=new Point3D(400,350,0);
		//		NodeData n1=new NodeData(1,p1);
		//		NodeData n2=new NodeData(2,p2);
		//		NodeData n3=new NodeData(3,p3);
		//		NodeData n4=new NodeData(4,p4);
		//		NodeData n5=new NodeData(5,p5);
		//		g.addNode(n1);
		//		g.addNode(n2);
		//		g.addNode(n3);
		//		g.addNode(n4);
		//		g.addNode(n5);
		//		g.connect(1, 2, 1);
		//		g.connect(2, 3, 2);
		//		g.connect(3, 4, 3);
		//		g.connect(4, 5, 4);
		//		g.connect(1, 5, 2);
		//		g.connect(2, 4, 2);
		//		g.connect(5, 4, 2);
		//		g.connect(4, 3, 2);
		//		g.connect(3, 2, 2);
		//		g.connect(2, 1, 2);



		//		g.connect(1, 5, 1000);
		//		g.connect(5, 3, 1000);
		//		graph_algorithms gr= new Graph_Algo();
		//		gr.init(g);
		//		GuiGraph window=new GuiGraph (g);
		//		

		//window.setVisible(true);


		//Window window = new Window();
		//window.setVisible(true);
		Point3D a = new Point3D(25,90,0);
		Point3D b = new Point3D(100,300,0);
		Point3D c = new Point3D(190,200,0);
		Point3D d = new Point3D(390,400,0);
		Point3D e = new Point3D(500,600,0);
		Point3D f = new Point3D(300,550,0);
		Point3D g = new Point3D(300,450,0);
		//Point3D h = new Point3D(300,550,0);

		node_data v1 = new NodeData(1, a);
		node_data v2 = new NodeData(2, b);
		node_data v3 = new NodeData(3, c);
		node_data v4 = new NodeData(4, d);
		node_data v5 = new NodeData(5, e);
		node_data v6 = new NodeData(6, f);
		node_data v7 = new NodeData(7, g);
		//node_data v8 = new Node(8, h);
		//
		DGraph grap = new DGraph();
		// graph_algorithms test = new Graph_Algo();
		// test.init(grap);
		grap.addNode(v1);
		grap.addNode(v2);
		grap.addNode(v3);
		grap.addNode(v4);
		grap.addNode(v5);
		grap.addNode(v6);
		grap.addNode(v7);
		// grap.addNode(v8);


		grap.connect(v1.getKey(), v2.getKey(), 6);
		grap.connect(v2.getKey(), v3.getKey(), 10);
		grap.connect(v3.getKey(), v4.getKey(), 9);
		grap.connect(v4.getKey(), v5.getKey(), 3);
		grap.connect(v5.getKey(), v1.getKey(), 1);
		grap.connect(v1.getKey(), v3.getKey(), 5);
		grap.connect(v3.getKey(), v5.getKey(), 6);
		grap.connect(v2.getKey(), v4.getKey(), 1);
		grap.connect(v2.getKey(), v6.getKey(), 4);
		grap.connect(v4.getKey(), v7.getKey(), 3);
		grap.connect(v7.getKey(), v6.getKey(), 1);
		grap.connect(v7.getKey(), v5.getKey(), 6);
		grap.connect(v2.getKey(), v7.getKey(), 1);
		grap.connect(v6.getKey(), v5.getKey(), 1);
		grap.connect(v4.getKey(), v6.getKey(), 3);
		// Window window = new Window();
		MyGameGui window = new MyGameGui();

		// window.setVisible(true);


	}


}


