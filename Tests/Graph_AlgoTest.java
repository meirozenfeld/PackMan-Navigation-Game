package Tests;
import algorithms.*;
import dataStructure.*;
import gui.GuiGraph;
import utils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class Graph_AlgoTest {

	@Test
	void testInitGraph() {
		graph g=new DGraph ();
		Point3D p1=new Point3D(300,200,0);
		Point3D p2=new Point3D(200,100,0);
		NodeData n1=new NodeData(1,p1);
		NodeData n2=new NodeData(2,p2);
		g.addNode(n1);
		g.addNode(n2);
		g.connect(1, 2, 1);
		graph_algorithms gr= new Graph_Algo();
		try {
			gr.init(g);
		} catch (Exception e) {
			fail();
		}
	}

//	@Test
//	void testInitString() {
//	checked manual with gui
//	}

//	@Test
//	void testSave() {
//	checked manual with gui
//	}

	@Test
	void testIsConnected() {
		graph g=new DGraph();
		int j=10;
		for (int i = 1; i <= 10; i++) 
		{
			Point3D p=new Point3D(i*20,i*20,0);
			NodeData n=new NodeData(i,p);
			g.addNode(n);
		}
		for (int i = 1; i < 9; i++) 
		{
			g.connect(i, i+1, 10);
			if(j>1)g.connect(j, j-1, 10);
			j--;
		}
		graph_algorithms grFalse= new Graph_Algo();
		grFalse.init(g);
		assertEquals(false,grFalse.isConnected());

		g.connect(9, 10, 10);
		g.connect(2, 1, 10);
		graph_algorithms grTrue= new Graph_Algo();
		grTrue.init(g);
		assertEquals(true,grTrue.isConnected());
	}

	@Test
	void testShortestPathDist() {
		graph g=new DGraph();
		int j=10;
		for (int i = 1; i <= 10; i++) 
		{
			Point3D p=new Point3D(i*20,i*20,0);
			NodeData n=new NodeData(i,p);
			g.addNode(n);	
		}
		for (int i = 1; i < 10; i++) 
		{
			g.connect(i, i+1, 10);
			if(j>1)g.connect(j, j-1, 10);
			j--;
		}
		graph_algorithms gr= new Graph_Algo();
		gr.init(g);
		assertEquals(gr.shortestPathDist(1, 10),90);
		assertEquals(gr.shortestPathDist(10,1),90);
		g.connect(1, 10, 10);
		assertEquals(gr.shortestPathDist(1,10),10);
	}

	@Test
	void testShortestPath() {
		graph g=new DGraph();
		int j=10;
		ArrayList<node_data> expected = new ArrayList<node_data>();
		for (int i = 1; i <= 10; i++) 
		{
			Point3D p=new Point3D(i*20,i*20,0);
			NodeData n=new NodeData(i,p);
			g.addNode(n);
			expected.add(n);
		}
		for (int i = 1; i < 10; i++) 
		{
			g.connect(i, i+1, 10);
			if(j>1)g.connect(j, j-1, 10);
			j--;
		}
		graph_algorithms gr= new Graph_Algo();
		gr.init(g);
		assertEquals(expected, gr.shortestPath(1, 10));
		expected.clear();
		gr.init(g);
		g.connect(1, 10, 10);
		expected.add(g.getNode(1));
		expected.add(g.getNode(10));
		assertEquals(expected, gr.shortestPath(1, 10));
	}

	@Test
	void testTSP() {
		graph g=new DGraph();
		int j=1;
		for (int i = 1; i <= 1000000; i++) 
		{
			Point3D p=new Point3D(j,j,0);
			NodeData n=new NodeData(i,p);
			g.addNode(n);	
		}
		for (int i = 1; i < 1000000; i++) 
		{
			g.connect(i, i+1, 10);
		}
		graph_algorithms gr= new Graph_Algo();
		gr.init(g);
		assertEquals(g.getV().size(),1000000);	
	}

	@Test
	void testCopy() {
		graph g=new DGraph();
		for (int i = 1; i <= 10; i++) 
		{
			Point3D p=new Point3D(i*20,i*20,0);
			NodeData n=new NodeData(i,p);
			g.addNode(n);
			if(i<10)g.connect(i, i+1, 5);
		}
		graph_algorithms actual= new Graph_Algo();
		actual.init(g);
		if(actual.equals(actual.copy()))
		{
			fail();
		}
	}
	@Test
	void milionVertex() {
		graph g=new DGraph();
		int j=1;
		for (int i = 1; i <= 1000000; i++) 
		{
			Point3D p=new Point3D(j,j,0);
			NodeData n=new NodeData(i,p);
			g.addNode(n);	
		}
		for (int i = 1; i < 1000000; i++) 
		{
			g.connect(i, i+1, 10);
			g.connect(j+1, i+1, 10);
			g.connect(j+2, i+1, 10);
			g.connect(j+3, i+1, 10);
			g.connect(j+4, i+1, 10);
			g.connect(j+5, i+1, 10);
			g.connect(j+6, i+1, 10);
			g.connect(j+7, i+1, 10);
			g.connect(j+8, i+1, 10);
			g.connect(j+9, i+1, 10);
		}
		graph_algorithms gr= new Graph_Algo();
		gr.init(g);
		assertEquals(g.getV().size(),1000000);			
	}
}
