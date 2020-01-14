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
import java.util.Set;

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
	static boolean Allreadydone = false;
	private final double EPS=0.000001;
	private final double clickEPS=0.00051;
	ArrayList<node_fruit> fruits = new ArrayList<node_fruit>(); //list for fruits
	ArrayList<node_robot> robots = new ArrayList<node_robot>(); //list for robots
	double minx = Integer.MAX_VALUE;
	double miny = Integer.MAX_VALUE;
	double maxx = Integer.MIN_VALUE;
	double maxy = Integer.MIN_VALUE;
	double x; // x of click
	double y; // y of click
	boolean RobotClicked = false; // if robot clicked or not (for manual game)

	public MyGameGui()
	{
		gr = new DGraph();
		fruits= new ArrayList<node_fruit>();
		robots= new ArrayList<node_robot>();
		initGUI();
	}


	public MyGameGui(graph g )
	{
		this.gr = g;
		initGUI();
	}
	public void setXY(double x,double y)
	{
		this.x=x;
		this.y=y;
		//System.err.println(x + " " + y);
	}
	public MyGameGui(graph g,ArrayList<node_fruit> fruits , ArrayList<node_robot> robots )
	{
		this.gr = g;
		this.fruits=fruits;
		this.robots=robots;
		initGUI();
	}
	public void DF() //defoult this.Fruits
	{
		fruits=new ArrayList<node_fruit>();
	}
	public void DR() //defoult this.robots
	{
		robots=new ArrayList<node_robot>();
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
			node_fruit v=new NodeFruit(p, type);
			v.setWithRobot(0);
			this.fruits.add(v);
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
			node_robot v=new NodeRobot(id,src,p);
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
			for (node_data v : gr.getV()) // paint vertex
			{
				StdDraw.setPenColor(Color.BLACK);
				StdDraw.filledCircle(v.getLocation().x(),v.getLocation().y(),(maxx-minx)*0.005);
				StdDraw.text( v.getLocation().x(), v.getLocation().y()+(maxy-miny)*0.04,Integer.toString(v.getKey()));
				Collection<edge_data> e=gr.getE(v.getKey());
				StdDraw.setPenColor(Color.RED);
				for(edge_data ed: e) // paint edges
				{
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
				for(node_robot r:robots)
				{
					StdDraw.setPenColor(arr[i]);
					StdDraw.filledCircle(r.getLocation().x(),r.getLocation().y(),(maxx-minx)*0.010);
					i--;
				}
			}
			if(fruits!=null) // paint fruits
			{
				for(node_fruit f:fruits)
				{
					if(f.getType()==1) // apple
					{
						StdDraw.setPenColor(Color.PINK);
						StdDraw.filledCircle(f.getLocation().x(),f.getLocation().y(),(maxx-minx)*0.005);
					}
					if(f.getType()==-1) // banana
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
				for(node_fruit f:fruits)
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
						int av=v.getTag();
						int ae=e.getTag();
						if(f.getType()==1)
						{
							v.setTag(++av); 
							e.setTag(++ae);
						}

						int bv=gr.getNode(e.getDest()).getTag();
						if(f.getType()==-1)
						{
							gr.getNode(e.getDest()).setTag(++bv);
							e.setTag(++ae);
						}
						f.setSrc(e.getSrc());
						f.setDest(e.getDest());

					}
				}
			}
		}
	}

	public void initTag()
	{
		for(node_data v: gr.getV())
		{
			v.setTag(0);
			for(edge_data e:gr.getE(v.getKey()))
			{
				e.setTag(0);
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
		//choose senario, draw senario.
		JFrame sen= new JFrame();
		String i = JOptionPane.showInputDialog(sen,"Choose senario game between 0-23");
		game_service game = Game_Server.getServer(Integer.parseInt(i));
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g); // add graph
		String infoGame = game.toString();
//		System.out.println(infoGame);


		DF(); //defoult this.Fruits
		for(String f:game.getFruits()) // add fruits
		{
			initFruit(f); //read json fruit and add to this.Fruits
			System.out.println(f);
		}

		DR(); //defoult this.Fruits
		this.gr=gg;
		smartPosition (); // smart start position of robots
		int rt=initInfoGame(infoGame); // how many robots in the game
		int maxF=fruits.size();  
		while(maxF>0) // loop to position on the edge with the most fruits
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
		JFrame s= new JFrame(); // window to start game
		JOptionPane.showMessageDialog(s, "To start playing click ~ok~, select a robot to start moving ");
		//start manual game
		game.startGame();
		node_robot selRob=null; // default selected robot
		while(game.isRunning())
		{
			//update robots and fruits
			long t = game.timeToEnd();
			System.out.println("Time to end: "+t/1000);
			//			DR();
			List<String> currR = game.getRobots();
			robots.clear();
			for (String str : currR) 
			{
				initRobots(str);
			}
			if(selRob!=null)
			{
				for (node_robot Currrob : robots)
				{
					selRob.setLocation(Currrob.getLocation());
				}
			}
			//			DF();
			List<String> currF = game.getFruits();
			fruits.clear();
			for (String string : currF) 
			{
				initFruit(string);
			}
			paint();


			//check robot move by clicks
			Point3D CurrClick = new Point3D(this.x, this.y);

			if(!RobotClicked)
			{
				for(node_robot rob : robots) 
				{

					Point3D robPos = rob.getLocation(); 
					//					System.out.println(robPos);
					//System.err.println(CurrClick);
					if(robPos.distance2D(CurrClick) <= clickEPS) // distance between click to robot
					{
						selRob = rob;
						RobotClicked = true;	
						System.out.println("Robot sel");
						this.x=0;
						this.y=0;
						break;
					}
				}
			}
			else {
				Collection<node_data> nd = gr.getV();
				//				Point3D CurrClick = new Point3D(this.x, this.y);
				for (node_data v : nd) 
				{
					Point3D currNode = v.getLocation();
					if(currNode.distance2D(CurrClick) <= clickEPS&& selRob.getLocation().distance2D(CurrClick)>=clickEPS)//distance between click to vertex (except vertex himself)
					{
						game.chooseNextEdge(selRob.getId(), v.getKey());
						RobotClicked = false;
						System.out.println("Dest sel");
						this.x=0;
						this.y=0;
						break;
					}
				}
			}
			game.move(); // move the robot to requested location
			paint();
		}
		System.out.println(game.toString());
		JFrame go= new JFrame(); // window to game over
		JOptionPane.showMessageDialog(go, "Game Over");
		game.stopGame();
		fruits.clear();
		robots.clear();
		//			System.out.println("Time to end: "+t/1000);
	}


/*
 * function to automat game
 */
	private void moveAuto(game_service game) throws JSONException {
		Graph_Algo ga=new Graph_Algo();
		ga.init(gr);
		node_fruit wantedFruit=new NodeFruit();
		for(node_robot rob: robots)
		{
			double minDist=Double.MAX_VALUE; // veriable to shortest distance
			for(node_fruit f: fruits)
			{
				if(f.getWithRobot()==0)
				{
					if(f.getType()==1) //apple= src to dest
					{
						double tmp= ga.shortestPathDist(rob.getSrc(),f.getSrc());
						tmp+= ga.shortestPathDist(f.getSrc(),f.getDest());
						if(tmp<minDist)
						{
							minDist=tmp;
							wantedFruit=f;
						}
					}
					else // banana dest to src
					{
						double tmp= ga.shortestPathDist(rob.getSrc(),f.getDest());
						tmp+= ga.shortestPathDist(f.getDest(),f.getSrc());

						if(tmp<minDist)
						{
							minDist=tmp;
							wantedFruit=f;
						}
					}
				}
			}
			if(wantedFruit.getType()==1)// apple
			{
				List<node_data> spa=ga.shortestPath(rob.getSrc(), wantedFruit.getSrc());
				spa.remove(0);
				spa.add(gr.getNode(wantedFruit.getDest()));//**
				wantedFruit.setWithRobot(1);
				rob.setPath(spa);
			}
			else //banana
			{
				List<node_data> spa=ga.shortestPath(rob.getSrc(), wantedFruit.getDest());
				spa.remove(0);
				spa.add(gr.getNode(wantedFruit.getSrc()));//**
				wantedFruit.setWithRobot(1);
				rob.setPath(spa);
			}

		}
		for(node_robot rob: robots)
		{
			while(rob.getPath().size()>0)
			{
				game.chooseNextEdge(rob.getId(), rob.getPath().get(0).getKey());
				game.move();
				rob.getPath().remove(0);
			}
		}
		List<String> currF = game.getFruits();
		fruits.clear();
		for (String string : currF) 
		{
			initFruit(string);
		}
		smartPosition();
		List<String> currR = game.getRobots();
		robots.clear();
		for (String str : currR) 
		{
			initRobots(str);
		}
		paint();

	}
	public void automatic() throws JSONException {
		//choose senario, draw senario.
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
		smartPosition (); // smart start position of robots
		int rt=initInfoGame(infoGame); // how many robots in the game
		int maxF=fruits.size();  
		while(maxF>0) // loop to position on the edge with the most fruits
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

		for(String r:game.getRobots()) // add robots
		{
			initRobots(r); //read json robots and add to this.robots
			System.out.println(r);
		}
		initGUI();

		//start auto game

		game.startGame();
		while(game.isRunning())
		{
			moveAuto(game);
		}

		JFrame go= new JFrame(); // window to game over
		JOptionPane.showMessageDialog(go, "Game Over");
		game.stopGame();
		fruits.clear();
		robots.clear();
	}
}