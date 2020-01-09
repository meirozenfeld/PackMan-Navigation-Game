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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import MyUtils.Point3D;
import Server.Fruit;
import Server.Game_Server;
import Server.game_service;


public class MyGameGui implements ActionListener, MouseListener,Serializable
{
	graph gr;
	MyGameGui G;
	static boolean Allreadydone = false;
	//	private Hashtable  <Integer, node_data> Fruits; // hashtable for fruits
	ArrayList<node_data> fruits = new ArrayList<node_data>(); //list for fruits
	ArrayList<node_data> robots = new ArrayList<node_data>(); //list for fruits

	//	private int idF=0; // key for fruit
	LinkedList<Point3D> points = new LinkedList<Point3D>();
	ArrayList<node_data> sp = new ArrayList<node_data>(); // for shortestPath
	//	node_data fr=new NodeData();
	double minx = Integer.MAX_VALUE;
	double miny = Integer.MAX_VALUE;
	double maxx = Integer.MIN_VALUE;
	double maxy = Integer.MIN_VALUE;

	public MyGameGui()
	{
		gr = new DGraph();
		//		Fruits=new Hashtable <Integer, node_data>();	
		fruits= new ArrayList<node_data>();
		robots= new ArrayList<node_data>();
		initGUI();
	}


	public MyGameGui(graph g )
	{
		this.gr = g;
		initGUI();
	}

	public MyGameGui(graph g,ArrayList<node_data> fruits , ArrayList<node_data> robots )
	{
		this.gr = g;
		//		this.Fruits=Fruits;
		this.fruits=fruits;
		this.robots=robots;
		initGUI();
	}
	public void DF() //defoult this.Fruits
	{
		//		Fruits=new Hashtable <Integer, node_data>();
		fruits=new ArrayList<node_data>();
	}
	public void DR() //defoult this.Fruits
	{

		robots=new ArrayList<node_data>();
	}
	private void initGUI()
	{
		minx = Integer.MAX_VALUE;
		miny = Integer.MAX_VALUE;
		maxx = Integer.MIN_VALUE;
		maxy = Integer.MIN_VALUE;
		if(!getAllready())
		{
			StdDraw.setCanvasSize(800, 700);
			changeDone();
		}
		StdDraw.setGUI(this);

		if(gr != null)
		{
			Collection<node_data> nd = gr.getV();
			for (node_data node_data : nd) 
			{
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
		//				if(Fruits!= null)
		//				{
		//					for (node_data node_data : getF()) 
		//					{
		//						Point3D pos = node_data.getLocation();
		//						if(pos.x() > maxx)
		//						{
		//							maxx= pos.x();
		//						}
		//						if(pos.x() < minx)
		//						{
		//							minx = pos.x();
		//						}
		//						if(pos.y() < miny)
		//						{
		//							miny = pos.y();
		//						}
		//						if(pos.y() > maxy)
		//						{
		//							maxy = pos.y();
		//						}
		//					}
		//		
		//				}
		StdDraw.setXscale(minx, maxx);
		StdDraw.setYscale(miny, maxy);
		paint();
		//dDraw.show();
	}

	public void initFruit(String f) throws JSONException 
	{
		JSONObject jo = new JSONObject(f);
		try {
			JSONObject fr=jo.getJSONObject("Fruit");
			double value= fr.getDouble("value");
			int type = fr.getInt("type");
			String pos=fr.getString("pos");
			Point3D p=new Point3D(pos);
			node_data v=new NodeData(p, type);
			this.fruits.add(v);
			//			idF++;
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void initRobots(String r) throws JSONException
	{
		JSONObject jo = new JSONObject(r);
		try {
			JSONObject ro=jo.getJSONObject("Robot");
			int id = ro.getInt("id");
			double value= ro.getDouble("value");
			int src = ro.getInt("src");
			int dest = ro.getInt("dest");
			String pos=ro.getString("pos");
			Point3D p=new Point3D(pos);
			node_data v=new NodeData(src);
			this.robots.add(v);
			//			idF++;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	//	public Collection<node_data> getF() {
	//		return this.Fruits.values();
	//	}
	
	public int initInfoGame(String i) throws JSONException
	{
		int r=0;
		JSONObject jo = new JSONObject(i);
		try {
			JSONObject ro=jo.getJSONObject("GameServer");
			r = ro.getInt("robots");
		} catch(Exception e){
			e.printStackTrace();
		}
		return r;
	}
	
	public void paint()
	{	
		StdDraw.clear();

		if(gr!=null) {

			//			if(fr.getTag()==1)StdDraw.setPenColor(Color.PINK); // apple
			//			if(fr.getTag()!=-1)StdDraw.setPenColor(Color.YELLOW); // banana
			//			StdDraw.filledCircle(fr.getLocation().x(),fr.getLocation().y(),(maxx-minx)*0.005);
			//		
			
			for (node_data v : gr.getV()) // paint vertex
			{
				StdDraw.setPenColor(Color.BLACK);
				StdDraw.filledCircle(v.getLocation().x(),v.getLocation().y(),(maxx-minx)*0.005);
				StdDraw.text( v.getLocation().x(), v.getLocation().y()+(maxy-miny)*0.03,Integer.toString(v.getKey()));
				Collection<edge_data> e=gr.getE(v.getKey());
				StdDraw.setPenColor(Color.RED);
				for(edge_data ed: e) // paint edges
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
					double xll=(xd+xl)/2;
					double yll=(yd+yl)/2;
					double xlll=(xd+xll)/2;
					double ylll=(yd+yll)/2;
					StdDraw.setPenColor(Color.GREEN);
					StdDraw.filledCircle(xlll,ylll, (maxx-minx)*0.004);
					StdDraw.setPenColor(Color.BLUE);
					StdDraw.text(xlll,ylll+(maxy-miny)*0.03,Double.toString(ed.getWeight()));
					StdDraw.setPenColor(Color.RED);
				}
			}
			if(fruits!=null)
			{
				for(node_data f:fruits) // paint fruits
				{
					if(f.getTag()==1) // apple
					{
						StdDraw.setPenColor(Color.PINK);
						StdDraw.filledCircle(f.getLocation().x(),f.getLocation().y(),(maxx-minx)*0.005);
					}
					if(f.getTag()==-1) // banana
					{
						StdDraw.setPenColor(Color.YELLOW);
						StdDraw.filledCircle(f.getLocation().x(),f.getLocation().y(),(maxx-minx)*0.005);
					}
				}
			}
			if(robots!=null)
			{
				int i =robots.size();
				Color arr []= {Color.ORANGE,Color.DARK_GRAY,Color.MAGENTA,Color.LIGHT_GRAY,Color.CYAN}; // arr for robots color, max 5
				for(node_data r:robots) // paint fruits
				{
					StdDraw.setPenColor(arr[i]);
					StdDraw.filledCircle(gr.getNode(r.getKey()).getLocation().x(),gr.getNode(r.getKey()).getLocation().y(),(maxx-minx)*0.010);
					i--;
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
		gg.init(g); // add graph
		String infoGame = game.toString();
		System.out.println(infoGame);
		//		
		DF(); //defoult this.Fruits
		for(String f:game.getFruits()) // add fruits
		{
			initFruit(f); //read json fruit and add to this.Fruits
			System.out.println(f);
		}
		
		int src_node = 1;  // arbitrary node, you should start at one of the fruits

		DR(); //defoult this.Fruits
		int rt=initInfoGame(infoGame);
		while(rt>0)
		{
			game.addRobot(src_node);
			src_node++;
			rt--;
		}
		for(String r:game.getRobots()) // add fruits
		{
			initRobots(r); //read json robots and add to this.robots
			System.out.println(r);

		}
		MyGameGui G=new MyGameGui(gg,fruits,robots);
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