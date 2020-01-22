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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
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
	Logger_KML kml;
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
	long t1;
	private double score;
	private int numRob;

	public MyGameGui()
	{
		gr =null;
		fruits= new ArrayList<node_fruit>();
		robots= new ArrayList<node_robot>();
		//		this.kml=new Logger_KML();

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
	public MyGameGui(graph g,ArrayList<node_fruit> fruits , ArrayList<node_robot> robots  )
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

	/**
	 * order the window scale
	 */
	private void initGUI()
	{
		if (gr != null ) {
			minx = Integer.MAX_VALUE;
			miny = Integer.MAX_VALUE;
			maxx = Integer.MIN_VALUE;
			maxy = Integer.MIN_VALUE;
		}
		else //scale for Game server info
		{
			minx = 0;
			miny = 0;
			maxx = 800;
			maxy = 700;
		}
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

	/**
	 *  get string fruit and put node_fruit on this.fruits
	 * @param f - string of fruit
	 * @throws JSONException
	 */
	public void initFruit(String f) throws JSONException 
	{
		JSONObject jo = new JSONObject(f);
		try {
			JSONObject fr=jo.getJSONObject("Fruit");
			int value= fr.getInt("value");
			int type = fr.getInt("type");
			String pos=fr.getString("pos");
			Point3D p=new Point3D(pos);
			node_fruit v=new FruitData(p, type,value);
			v.setWithRobot(0);
			this.fruits.add(v);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 *  get string robot and put node_robot on this.robots
	 * @param r - string of fruit
	 * @throws JSONException
	 */
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
			node_robot v=new RobotData(id,src,p);
			v.setOnTheWay(0);
			this.robots.add(v);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void initInfoGame(String info) throws JSONException //udate num of robots and score
	{
		JSONObject jo = new JSONObject(info);
		try {
			JSONObject ro=jo.getJSONObject("GameServer");
			numRob = ro.getInt("robots");
			score=ro.getDouble("grade");
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * paint the game
	 * vertex, edges,directions,weights,time to end,score,robots and fruits
	 */
	public void paint()
	{	
		StdDraw.clear();
		if(gr!=null) {
			for (node_data v : gr.getV()) // paint vertex
			{
				StdDraw.setPenColor(Color.BLACK);
				StdDraw.filledCircle(v.getLocation().x(),v.getLocation().y(),(maxx-minx)*0.005);
				StdDraw.text( v.getLocation().x(), v.getLocation().y()+(maxy-miny)*0.04,Integer.toString(v.getKey()));
				StdDraw.text(maxx-0.0009,maxy,"Time: "+Double.toString(t1/1000));//draw the time to end
				StdDraw.text(maxx-0.0009,maxy-0.00025,"Score: "+Double.toString(score));//draw the score of game
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
				for(node_robot r:robots)
				{
					StdDraw.picture(r.getLocation().x(),r.getLocation().y(),"mouse.png",(maxx-minx)*0.045,(maxy-miny)*0.045);
				}
			}
			if(fruits!=null) // paint fruits
			{
				for(node_fruit f:fruits)
				{
					if(f.getType()==1) // apple (cookie)
					{
						StdDraw.setPenColor(Color.PINK);
						StdDraw.picture(f.getLocation().x(),f.getLocation().y(),"cookie.png",(maxx-minx)*0.035,(maxy-miny)*0.035);
						//						StdDraw.filledCircle(f.getLocation().x(),f.getLocation().y(),(maxx-minx)*0.005);
					}
					if(f.getType()==-1) // banana (cheese)
					{

						StdDraw.setPenColor(Color.YELLOW);
						StdDraw.picture(f.getLocation().x(),f.getLocation().y(),"cheese.png",(maxx-minx)*0.035,(maxy-miny)*0.035);
						//						StdDraw.filledCircle(f.getLocation().x(),f.getLocation().y(),(maxx-minx)*0.005);
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
	/**
	 * smartPosition- position the robots in a smart place
	 * and also connecting between fruit to edge
	 **/
	public void smartPosition ()
	{

		for(node_data v: gr.getV())
		{
			int av=0;
			int bv=0;
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
						av+=v.getTag()+f.getValue();
						int ae=e.getTag();
						if(f.getType()==1)
						{
							v.setTag(++av); 
							e.setTag(++ae);
						}

						bv+=gr.getNode(e.getDest()).getTag()+f.getValue();
						if(f.getType()==-1)
						{
							gr.getNode(e.getDest()).setTag(++bv);
							e.setTag(++ae);
						}
						f.setSrc(e.getSrc()); // set source of fruit
						f.setDest(e.getDest()); // set dest of fruit

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
	/**
	 *  method for manual game.. by click on mouse
	 * @throws JSONException
	 */
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
			//			System.out.println(f);
		}
		this.gr=gg;
		smartPosition (); // smart start position of robots
		initInfoGame(infoGame); // how many robots in the game
		int maxF=fruits.size()+2000; 
		while(maxF>0) // loop to position on the edge with the most fruits
		{
			for(node_data v: gr.getV())
			{
				if(numRob>0&&v.getTag()==maxF)
				{
					game.addRobot(v.getKey());
					v.setWeight(-4);
					numRob--;
				}
			}
			maxF--;
		}

		for(String r:game.getRobots()) // add fruits
		{
			initRobots(r); //read json robots and add to this.robots
			//			System.out.println(r);
		}
		initGUI();
		JFrame s= new JFrame(); // window to start game

		JOptionPane.showMessageDialog(s, "To start playing click ok, select a mouse to start moving ");
		//start manual game
		game.startGame();
		node_robot selRob=null; // default selected robot
		while(game.isRunning())
		{
			t1 = game.timeToEnd();
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
					//System.out.println(robPos);
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
			infoGame = game.toString();
			initInfoGame(infoGame);
			paint();
		}
		System.out.println(game.toString());
		infoGame = game.toString();
		initInfoGame(infoGame);
		JFrame go= new JFrame(); // window to game over
		JOptionPane.showMessageDialog(go, "Game Over! your score is: "+score);
		game.stopGame();
		fruits.clear();
		robots.clear();
	}


	/**
	 * help method in isRunning loop to automatic game
	 **/
	private void moveAuto(game_service game) throws JSONException {

		t1 = game.timeToEnd(); // update time
		String infoGame = game.toString();
		initInfoGame(infoGame); //update score
		//System.out.println("Time to end: "+t1/1000);
		Graph_Algo ga=new Graph_Algo();
		ga.init(gr);
		node_fruit wantedFruit=new FruitData();
		int maxVal=-1;
		for(node_robot rob: robots)
		{

			double minDist=Double.MAX_VALUE; // veriable to shortest distance
			for(node_fruit f: fruits)
			{
				if(f.getWithRobot()==0)
				{
					int tmp1=f.getValue();
					if(f.getType()==1) //apple= src to dest
					{
						double tmp= ga.shortestPathDist(rob.getSrc(),f.getSrc());
						tmp+= ga.shortestPathDist(f.getSrc(),f.getDest());
						if(tmp<minDist)
						{
							minDist=tmp;
							wantedFruit=f;
							maxVal=tmp1;
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
							maxVal=tmp1;
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
				spa.remove(0); // remove the start vertex himself
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

	/**
	 * method to send kml object the fruits on isRunning loop
	 */
	Thread t;
	public void threadKml (game_service g)
	{
		t=new Thread (new  Runnable() {
			public void run() {
				while(g.isRunning())
				{
					try {
						Thread.sleep(60);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String now=java.time.LocalDate.now()+"T"+java.time.LocalTime.now();
					LocalTime lt=LocalTime.now();
					lt=lt.plusNanos(100*1000000);
					String after=java.time.LocalDate.now()+"T"+lt;
					ArrayList<node_fruit> kmlFruits = new ArrayList<node_fruit>(); //list for kml fruits
					ArrayList<node_robot> kmlRobots = new ArrayList<node_robot>(); //list for kml robots
					List<String> currF = g.getFruits();
					kmlFruits.clear();
					for (String string : currF) 
					{
						JSONObject jo = null;
						try {
							jo = new JSONObject(string);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							JSONObject fr=jo.getJSONObject("Fruit");
							//							double value= fr.getDouble("value");
							int type = fr.getInt("type");
							int value = fr.getInt("value");
							String pos=fr.getString("pos");
							Point3D p=new Point3D(pos);
							node_fruit v=new FruitData(p, type,value);
							v.setWithRobot(0);
							kmlFruits.add(v);
						} catch(Exception e){
							e.printStackTrace();
						}
					}
					List<String> currR = g.getRobots();
					kmlRobots.clear();
					for (String str : currR) 
					{
						JSONObject jo = null;
						try {
							jo = new JSONObject(str);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							JSONObject ro=jo.getJSONObject("Robot");
							int id = ro.getInt("id");
							double value= ro.getDouble("value");
							int src = ro.getInt("src");
							int dest = ro.getInt("dest");
							String pos=ro.getString("pos");
							Point3D p=new Point3D(pos);
							node_robot v=new RobotData(id,src,p);
							kmlRobots.add(v);
						} catch(Exception e){
							e.printStackTrace();
						}
					}
					kml.setFruits(kmlFruits, now, after);
					kml.setRobots(kmlRobots, now, after);

				}
			}

		});
		t.start();
	}
	/**
	 * method of automatic game 
	 * @throws JSONException
	 * @throws InterruptedException
	 */


	public void automatic() throws JSONException, InterruptedException {

		//choose senario, draw senario.
		JFrame sen= new JFrame();
		String i = JOptionPane.showInputDialog(sen,"Choose senario game between 0-23");
		game_service game = Game_Server.getServer(Integer.parseInt(i));
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g); // add graph
		String infoGame = game.toString();
		System.out.println(infoGame);

//		int id=311605315;// my id
//		Game_Server.login(id); // connect server
		this.kml=new Logger_KML(); // new kml
		this.gr=gg;
		kml.setGraph(gr); // set graph for kml
		kml.setName(i); // set name file kml by senario
		kml.buildGraph(); // set all vertexs and edges
		kml.setGame(game);
		DF(); //defoult this.Fruits
		for(String f:game.getFruits()) // add fruits
		{
			initFruit(f); //read json fruit and add to this.Fruits
			//			System.out.println(f);
		}
		DR(); //default this.robots

		smartPosition (); // smart start position of robots
		initInfoGame(infoGame); // how many robots in the game
		int maxF=fruits.size()+2000; 
		while(maxF>0) // loop to position on the edge with the most fruits
		{
			for(node_data v: gr.getV())
			{
				if(numRob>0&&v.getTag()==maxF)
				{
					game.addRobot(v.getKey());
					v.setWeight(-4);
					numRob--;
				}
			}
			maxF--;
		}


		for(String r:game.getRobots()) // add robots
		{
			initRobots(r); //read json robots and add to this.robots
			//			System.out.println(r);
		}
		initGUI();
		//start auto game
		game.startGame();
		threadKml(game);
		long tHelp=game.timeToEnd();
		int space=0;
		while(game.isRunning())
		{
			if(Integer.parseInt(i)!=5)// if not senario 5
			{
				moveAuto(game);
			}
			switch (Integer.parseInt(i)) 
			{
			case 0:
				space =102;
				tHelp=timeToMove(tHelp,game,space);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 6:
			case 7:
			case 8:
				space =101;
				tHelp=timeToMove(tHelp,game,space);
				break;
			case 5:
				moveAutoFive(game);
				space =118;
				tHelp=timeToMove(tHelp,game,space);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 20:
			case 21:
			case 22:
				space =100;
				tHelp=timeToMove(tHelp,game,space);
				break;
			case 19:
				space =110;
				if(tHelp<13000)
				{
					space=75;
				}
				//				System.out.println(space);
				tHelp=timeToMove(tHelp,game,space);
				break;
			case 23:
				space =45;
				tHelp=timeToMove(tHelp,game,space);
				break;
			default:
				break;
			} 
		}	
		//		kml.finalText(); // close the kml text
		//		kml.saveKml(); // save kml file
		game.sendKML(kml.str);
		System.out.println(game.toString());
		infoGame = game.toString();
		initInfoGame(infoGame);
		JFrame go= new JFrame(); // window to game over and score
		JOptionPane.showMessageDialog(go, "Game Over! your score is: "+score);
		game.stopGame();
		fruits.clear();
		robots.clear();
	}

	/**
	 * method for how many times call move order
	 * @param tHelp
	 * @param g = the game
	 * @param space = space between call move
	 * @return
	 */
	private long timeToMove(long tHelp, game_service g, int space) {
		if(tHelp-g.timeToEnd()>space) // efficient formula to call move
		{
			g.move();
			tHelp=g.timeToEnd();
		}
		return tHelp;
	}

	static int gpCount=0; // how many games
	static int level=0; // max level
	static int myGrade []=new int [24];// best scores
	static int moveArr [] =new int [24];// requested move
	static Hashtable<Integer, Integer> allGrade=new Hashtable<Integer, Integer>(); // key=id, value=senario (value not used)
	static int myPlace []= new int [24]; // 
	/**
	 * method for "Game server info" in menubar
	 * get details from printLog() method and draw them:
	 * how many games played, my best score and my place by senario
	 */
	public void gameServerInfo() {
		StdDraw.clear();
		int y=0; // parameter to down line
		StdDraw.setPenColor(Color.BLACK);
		printLog();
		StdDraw.textLeft(minx, maxy-y, "Game played : "+gpCount);
		y+=30;
		StdDraw.textLeft(minx, maxy-y, "Max level: "+level);
		y+=30;
		String grades="";

		for (int i = 0; i < myGrade.length; i++) 
		{
			if(myGrade[i]>0) // if releveant senario
			{
				grades="Game "+i+": my Best score is "+myGrade[i];
				StdDraw.textLeft(minx,maxy-y, grades); // draw my best score
				StdDraw.textLeft(minx+300,maxy-y, "My place is "+myPlace[i]);// draw my place
				y+=30;
			}

		}
		StdDraw.show();
	}
	/**
	 * help method for gameServerInfo() 
	 * get detail from server 
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(SimpleDB.jdbcUrl, SimpleDB.jdbcUser, SimpleDB.jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID=311605315;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			moveArr [0]=290; 
			moveArr [1]=580; 
			moveArr [3]=580;
			moveArr [5]=500;
			moveArr [9]=580;
			moveArr [11]=580;
			moveArr [13]=580;
			moveArr [16]=290;
			moveArr [19]=580;
			moveArr [20]=290;
			moveArr [23]=1140;

			while(resultSet.next())
			{
				gpCount++;
				int tempLevel=resultSet.getInt("levelID");
				int tempMove=resultSet.getInt("moves");
				int tempGrade=resultSet.getInt("score");

				if(tempLevel>level&&tempMove<=moveArr[tempLevel])
				{
					level=tempLevel;
				}
				if(tempGrade>myGrade[tempLevel]&&tempMove<=moveArr[tempLevel])
				{
					myGrade[tempLevel]=tempGrade;
				}				
				//				System.out.println("Id: " + resultSet.getInt("UserID")+","+resultSet.getInt("levelID")+","+resultSet.getInt("moves")+","+resultSet.getDate("time")+","+resultSet.getInt("score"));
			}


			//for my place
			for (int j = 0; j < myGrade.length; j++) 
			{
				myPlace[j]=1; // start from place 1
				if(myGrade[j]>0 ) // if relevant senario
				{
					String allCustomersQuery2 = "SELECT * FROM Logs WHERE levelID= "+j+" and moves <="+moveArr[j]+" and score >"+myGrade[j]+";";
					ResultSet resultSet2 = statement.executeQuery(allCustomersQuery2);
					while (resultSet2.next()) 
					{
						int id=resultSet2.getInt("userID");
						int idLength=Integer.toString(id).length(); // length of id 
						if(idLength==9)// length of id should be 9
						{
							allGrade.put(id, j);
							//							System.out.println("user id: "+resultSet2.getInt("userID") + " is score: " + resultSet2.getInt("score")+" senario: "+j);
						}
					}
				}
				myPlace[j]+=allGrade.size(); // size of all who pass me in senario j
				//				System.out.println(myPlace[j]);
				allGrade.clear();
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 * help method in isRunning loop to automatic game Senario 5 only - tricky senario
	 **/
	private void moveAutoFive(game_service game) throws JSONException {

		t1 = game.timeToEnd(); // update time
		String infoGame = game.toString();
		initInfoGame(infoGame); //update score
		//System.out.println("Time to end: "+t1/1000);
		Graph_Algo ga=new Graph_Algo();
		ga.init(gr);
		node_fruit wantedFruit=new FruitData();
		int maxVal=-1;
		for(node_robot rob: robots)
		{

			double minDist=Double.MAX_VALUE; // veriable to shortest distance
			for(node_fruit f: fruits)
			{
				if(f.getWithRobot()==0)
				{
					int tmp1=f.getValue();
					if(f.getType()==1) //apple= src to dest
					{
						double tmp= ga.shortestPathDist(rob.getSrc(),f.getSrc());
						tmp+= ga.shortestPathDist(f.getSrc(),f.getDest());
						if(tmp<minDist&&tmp1>maxVal)
						{
							minDist=tmp;
							wantedFruit=f;
							maxVal=tmp1;
						}
					}
					else // banana dest to src
					{
						double tmp= ga.shortestPathDist(rob.getSrc(),f.getDest());
						tmp+= ga.shortestPathDist(f.getDest(),f.getSrc());

						if(tmp<minDist&&tmp1>maxVal)
						{
							minDist=tmp;
							wantedFruit=f;
							maxVal=tmp1;
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
				spa.remove(0); // remove the start vertex himself
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

}
