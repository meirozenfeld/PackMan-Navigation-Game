package gameClient;

import MydataStructure.*;
import MyAlgorithms.*;
import MyUtils.*;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;

import MyUtils.Point3D;
import Server.Game_Server;
import Server.game_service;


public class MyGameGui implements ActionListener, MouseListener,Serializable
{
	graph gr;
	MyGameGui G;
	static boolean Allreadydone = false;
	LinkedList<Point3D> points = new LinkedList<Point3D>();
	ArrayList<node_data> sp = new ArrayList<node_data>(); // for shortestPath
	double minx = Integer.MAX_VALUE;
	double miny = Integer.MAX_VALUE;
	double maxx = Integer.MIN_VALUE;
	double maxy = Integer.MIN_VALUE;


	public MyGameGui()
	{
		gr = new DGraph();
		initGUI();
	}
	public MyGameGui(graph g )
	{
		this.gr = g;
		initGUI();
	}
	private void initGUI()
	{
		minx = Integer.MAX_VALUE;
		miny = Integer.MAX_VALUE;
		maxx = Integer.MIN_VALUE;
		maxy = Integer.MIN_VALUE;
		if(!getAllready())
		{
			StdDraw.setCanvasSize(500, 500);
			changeDone();
		}
		StdDraw.setGUI(this);

		if(gr != null)
		{
			Collection<node_data> nd = gr.getV();
			for (node_data node_data : nd) {
				Point3D pos = node_data.getLocation();
				if(pos.x() > maxx)
				{
					maxx= pos.x();
				}
				if(pos.x() < minx)
				{
					minx = pos.x();
				}
				if(pos.y() < miny)
				{
					miny = pos.y();
				}
				if(pos.y() > maxy)
				{
					maxy = pos.y();
				}
			}
		}
		StdDraw.setXscale(minx, maxx);
		StdDraw.setYscale(miny, maxy);
		paint();
		//dDraw.show();
	}
	public void paint()
	{	
		StdDraw.clear();
		if(gr!=null) {
			for (node_data v : gr.getV()) 
			{
				StdDraw.setPenColor(Color.BLACK);
				StdDraw.filledCircle(v.getLocation().x(),v.getLocation().y(),(maxx-minx)*0.005);
				StdDraw.text( v.getLocation().x(), v.getLocation().y()+(maxy-miny)*0.03,Integer.toString(v.getKey()));
				Collection<edge_data> e=gr.getE(v.getKey());
				StdDraw.setPenColor(Color.RED);
				for(edge_data ed: e)
				{
					//				if(this.sp.contains(v)) // if shortPath contain vertex from graph
					//				{
					//					if(ed.getTag()==1)
					//					{
					//						StdDraw.setPenColor(Color.GREEN);
					//						ed.setTag(0);						
					//					}
					//				}
					double xs=gr.getNode(ed.getSrc()).getLocation().x();// x src
					double ys=gr.getNode(ed.getSrc()).getLocation().y();// y src
					double xd=gr.getNode(ed.getDest()).getLocation().x();// x dest
					double yd=gr.getNode(ed.getDest()).getLocation().y();// y dest
					StdDraw.line(xs,ys,xd,yd);
					double xl=(xs+xd)/2;//x string weight of line
					double yl=(ys+yd)/2;//y string weight of line
//					StdDraw.text(xl,yl,Double.toString(ed.getWeight()));

					double xll=(xd+xl)/2;
					double yll=(yd+yl)/2;
					double xlll=(xd+xll)/2;
					double ylll=(yd+yll)/2;
					StdDraw.setPenColor(Color.YELLOW);
					StdDraw.filledCircle(xlll,ylll, (maxx-minx)*0.004);
					StdDraw.setPenColor(Color.BLUE);
					StdDraw.text(xlll,ylll+(maxy-miny)*0.03,Double.toString(ed.getWeight()));
					StdDraw.setPenColor(Color.RED);
				}
			}			
		}
	}
	public void setG(graph g)
	{
		this.gr = g;


	}
	public static void changeDone()
	{
		Allreadydone = true;
	}
	public static boolean getAllready()
	{
		return Allreadydone;
	}
	public void manual() throws JSONException {
		JFrame sen= new JFrame();
		String i = JOptionPane.showInputDialog(sen,"Choose senario game between 0-23");
		game_service game = Game_Server.getServer(Integer.parseInt(i));
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		//		this.gr=gg;
		//		G.setG(gg);
		//		this.G.initGUI();
		//		G.setG(gg);
		//		GuiGraph h=new GuiGraph(gg);
//		gr = gg;
//		initGUI();
		MyGameGui G=new MyGameGui(gg);
		String info = game.toString();
		System.out.println(info);



	}
	@Override
	public void actionPerformed(ActionEvent e) {


	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		Point3D p = new Point3D(x,y);
		points.add(p);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("mouseReleased");

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//System.out.println("mouseEntered");

	}

	@Override
	public void mouseExited(MouseEvent e) {
		//System.out.println("mouseExited");
	}


}