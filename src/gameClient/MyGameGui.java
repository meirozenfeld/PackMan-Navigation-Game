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
import java.util.List;

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


public class MyGameGui 
{
	graph gr;
	//MyGameGui G;
	static boolean Allreadydone = false;
	private final double EPS=0.000001;
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
	double x;
	double y;
	boolean RobotClicked = false;

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
	public void setxsety(double x,double y)
	{
		this.x=x;
		this.y =y;
		//System.err.println(x + " " + y);
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
		StdDraw.enableDoubleBuffering();
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
		StdDraw.setXscale(minx, maxx);
		StdDraw.setYscale(miny, maxy);
		paint();
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
			node_data v=new NodeData(id,p);
			this.robots.add(v);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

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
				StdDraw.text( v.getLocation().x(), v.getLocation().y()+(maxy-miny)*0.04,Integer.toString(v.getKey()));
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
					double w=Math.floor(ed.getWeight()*100)/100; // just 2 number after the point
					StdDraw.text(xlll,ylll+(maxy-miny)*0.03,Double.toString(w));
					StdDraw.setPenColor(Color.RED);
				}
			}

			if(robots!=null) // paint robots
			{
				int i =robots.size();
				Color arr []= {Color.ORANGE,Color.DARK_GRAY,Color.MAGENTA,Color.LIGHT_GRAY,Color.CYAN}; // arr for robots color, max robots=5
				for(node_data r:robots)
				{
					StdDraw.setPenColor(arr[i]);
					//StdDraw.circle(gr.getNode(r.getKey()).getLocation().x(),gr.getNode(r.getKey()).getLocation().y(),(maxx-minx)*0.010);
					StdDraw.circle(r.getLocation().x(),r.getLocation().y(),(maxx-minx)*0.010);
					i--;
				}
			}
			if(fruits!=null) // paint fruits
			{
				for(node_data f:fruits)
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


		}
		StdDraw.show();
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

	public void smartPosition ()
	{
		for(node_data v: gr.getV())
		{
			for(edge_data e:gr.getE(v.getKey()))
			{
				for(node_data f:fruits)
				{
					double dx1=v.getLocation().x()-gr.getNode(e.getDest()).getLocation().x();
					double dy1=v.getLocation().y()-gr.getNode(e.getDest()).getLocation().y();
					double disN=Math.sqrt(Math.pow(dx1, 2)+Math.pow(dy1, 2)); // distance between src to dest

					double dx2=v.getLocation().x()-f.getLocation().x();
					double dy2=v.getLocation().y()-f.getLocation().y();
					double disSF =Math.sqrt(Math.pow(dx2, 2)+Math.pow(dy2, 2)); // distance between src to fruit

					double dx3=gr.getNode(e.getDest()).getLocation().x()-f.getLocation().x();
					double dy3=gr.getNode(e.getDest()).getLocation().y()-f.getLocation().y();
					double disDF =Math.sqrt(Math.pow(dx3, 2)+Math.pow(dy3, 2)); // distance between dest to fruit

					if(Math.abs(disN-(disSF+disDF))<this.EPS) // if the fruit is on the edge
					{
						int a=v.getTag();
						if(f.getTag()==1)v.setTag(++a); 

						int b=gr.getNode(e.getDest()).getTag();
						if(f.getTag()==-1)gr.getNode(e.getDest()).setTag(++b);
					}
				}
			}
		}
	}
	private static int nextNode(graph g, int src)
	{
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
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


		DF(); //defoult this.Fruits
		for(String f:game.getFruits()) // add fruits
		{
			initFruit(f); //read json fruit and add to this.Fruits
			System.out.println(f);
		}


		DR(); //defoult this.Fruits
		this.gr=gg;
		smartPosition ();
		int rt=initInfoGame(infoGame); // how many robots in the game
		int maxF=400;  
		while(maxF>=1) // loop to position on the edge with the most fruits
		{
			for(node_data v: gr.getV())
			{
				if(rt>0&&v.getTag()==maxF)
				{
					game.addRobot(v.getKey());
					rt--;
				}
			}
			maxF--;
		}

		for(String r:game.getRobots()) // add fruits
		{
			initRobots(r); //read json robots and add to this.robots
			System.out.println(r);
		}
		initGUI();
		game.startGame();

		node_data theRob=null;
		while(game.isRunning())
		{
			
			DR();
			List<String> a = game.getRobots();
			robots.clear();
			for (String str : a) {
				initRobots(str);
			}
			if(theRob!=null)
			{
				for (node_data Currrob : robots) {
					theRob.setLocation(Currrob.getLocation());
				}
			}
			DF();
			List<String> currF = game.getFruits();
			fruits.clear();
			for (String string : currF) {
				initFruit(string);
			}
			paint();
			
			
			long t = game.timeToEnd();
			Point3D CurrClick = new Point3D(this.x, this.y);

			if(!RobotClicked)
			{
				for(node_data rob : robots)
				{

					Point3D robPos = rob.getLocation();
					//					System.out.println(robPos);
					//System.err.println(CurrClick);
					if(robPos.distance2D(CurrClick) <= 0.0001)
					{
						theRob = rob;
						RobotClicked = true;
						System.out.println("Rob selceted");
						System.out.println(theRob.getLocation());
						x=0;
						y=0;
						break;
					}
				}
			}
			else {
				Collection<node_data> nd = gr.getV();
				//				Point3D CurrClick = new Point3D(this.x, this.y);
				for (node_data node_data : nd) {
					Point3D currNode = node_data.getLocation();
					if(currNode.distance2D(CurrClick) <= 0.0001)
					{


						game.chooseNextEdge(0, node_data.getKey());

						RobotClicked = false;

						x=0;
						y=0;
						

						break;
					}
				}
			}
			game.move();
			paint();
			

		}
		System.out.println(game.toString());
		//			System.out.println("Time to end: "+t/1000);

		//					try {
		//						line = new JSONObject(robot_json);
		//						JSONObject ttt = line.getJSONObject("Robot");
		//						int rid = ttt.getInt("id");
		//						int src = ttt.getInt("src");
		//						int dest = ttt.getInt("dest");
		//						
		//						if(dest==-1) {	
		//							dest = nextNode(gg, src);
		//							game.chooseNextEdge(rid, dest);
		//							System.out.println("Turn to node: "+dest);
		//							System.out.println(ttt);
		//						}
		//					} catch (JSONException e) {
		//						// TODO Auto-generated catch block
		//						e.printStackTrace();
		//					}
		//			}
		//		game.stopGame();
	}





}