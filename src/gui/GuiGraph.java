package gui;
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
import java.util.LinkedList;
import algorithms.*;
import dataStructure.*;
import utils.*;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class GuiGraph extends JFrame implements ActionListener, MouseListener, Serializable
{
	LinkedList<Point3D> points = new LinkedList<Point3D>();
	ArrayList<node_data> sp = new ArrayList<node_data>(); // for shortestPath
	private graph gr;
	private static JFrame frame;
	public GuiGraph()
	{
		initGUI();
	}
	public GuiGraph(graph g) {
		this.gr=g;
		initGUI();
	}
	private void initGUI() 
	{
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //stop running window
		MenuBar menuBar = new MenuBar();
		//Menu
		Menu menu = new Menu("Menu");
		this.setMenuBar(menuBar);
		menuBar.add(menu);
		MenuItem save = new MenuItem("save");
		save.addActionListener(this);
		MenuItem init = new MenuItem("init");
		init.addActionListener(this);
		menu.add(save);
		menu.add(init);
		//Graph algorithms
		Menu algo = new Menu("Graph algorithms");
		menuBar.add(algo);
		MenuItem isconnected = new MenuItem("is connected");
		isconnected.addActionListener(this);
		MenuItem shortestPathDist = new MenuItem("shortest path distance");
		shortestPathDist.addActionListener(this); 
		MenuItem shortestPath = new MenuItem("shortest path");
		shortestPath.addActionListener(this);
		MenuItem tsp = new MenuItem("tsp");
		tsp.addActionListener(this);
		algo.add(isconnected);
		algo.add(shortestPathDist);
		algo.add(shortestPath);
		algo.add(tsp);
		this.addMouseListener(this);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		for (node_data v : gr.getV()) 
		{
			g.setColor(Color.BLACK);
			g.fillOval(v.getLocation().ix(),v.getLocation().iy(), 10, 10);
			g.drawString(Integer.toString(v.getKey()), v.getLocation().ix()+2, v.getLocation().iy()-5);
			Collection<edge_data> e=gr.getE(v.getKey());
			g.setColor(Color.RED);
			for(edge_data ed: e)
			{
				if(this.sp.contains(v)) // if shortPath contain vertex from graph
				{
					if(ed.getTag()==1)
					{
						g.setColor(Color.GREEN);
						ed.setTag(0);						
					}
				}

				int xs=gr.getNode(ed.getSrc()).getLocation().ix();// x src
				int ys=gr.getNode(ed.getSrc()).getLocation().iy();// y src
				int xd=gr.getNode(ed.getDest()).getLocation().ix();// x dest
				int yd=gr.getNode(ed.getDest()).getLocation().iy();// y dest
				g.drawLine(xs+4,ys+4,xd+4,yd+4);
				int xl=(xs+xd)/2;//x string weight of line
				int yl=(ys+yd)/2;//y string weight of line
				g.drawString(Double.toString(ed.getWeight()), xl,yl);

				int xll=(xd+xl)/2;
				int yll=(yd+yl)/2;
				int xlll=(xd+xll)/2;
				int ylll=(yd+yll)/2;
				g.setColor(Color.YELLOW);
				g.fillOval(xlll,ylll, 7, 7);

				g.setColor(Color.RED);
			}

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 	{
		String str = e.getActionCommand();
		switch(str) 
		{
		case "save":
			graph_algorithms gSave=new Graph_Algo();
			gSave.init(this.gr);
			FileDialog sa=new FileDialog(GuiGraph.frame,"save .txt",FileDialog.SAVE);
			sa.setVisible(true);
			String file=null;
			file=sa.getFile();
			gSave.save(sa.getDirectory()+file+".txt");
			break;
		case "init":
			graph_algorithms gInit=new Graph_Algo();
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			JFrame frame = new JFrame("JComboBox Test");
			frame.setLayout(new FlowLayout());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JFileChooser chooser = new JFileChooser();
			int returnValue = chooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) 
			{
				File selected = chooser.getSelectedFile();
				gInit.init(selected.getPath());
				gr = gInit.copy();
				repaint();
			}
			frame.pack();
			break;


		case "is connected":
			graph_algorithms g=new Graph_Algo();
			g.init(this.gr);
			boolean ans = g.isConnected();
			JFrame result = new JFrame();
			JOptionPane.showMessageDialog(result,ans);
			break;
		case "shortest path distance":
			graph_algorithms gg=new Graph_Algo();
			gg.init(this.gr);
			JFrame ask = new JFrame();
			String src = JOptionPane.showInputDialog(ask,"Enter source point");
			String dest = JOptionPane.showInputDialog(ask,"Enter destination point");
			try
			{
				int s=Integer.parseInt(src);
				int d=Integer.parseInt(dest);
				double ansDist= gg.shortestPathDist(s,d);
				JFrame res= new JFrame();
				JOptionPane.showMessageDialog(res,"The shortest path distance from "+s+" to "+d+" is "+ansDist);
			} catch (Exception e1){
				e1.printStackTrace();
			}
			break;
		case "shortest path":
			graph_algorithms ggg=new Graph_Algo();
			ggg.init(this.gr);
			JFrame ask1 = new JFrame();
			String src1 = JOptionPane.showInputDialog(ask1,"Enter source point");
			String dest1 = JOptionPane.showInputDialog(ask1,"Enter destination point");
			try
			{
				int s1=Integer.parseInt(src1);
				int d1=Integer.parseInt(dest1);
				this.sp= (ArrayList<node_data>) ggg.shortestPath(s1,d1);
				JFrame res1= new JFrame();
				String ansPath="";
				if(sp.size()==1)
				{
					ansPath=sp.get(0).getKey()+"";
				}
				else {
					for (int i = 0; i < sp.size(); i++) 
					{
						if(i!=sp.size()-1) 
						{
							ansPath+=sp.get(i).getKey()+"-->";
							gr.getEdge(sp.get(i).getKey(), sp.get(i+1).getKey()).setTag(1);
						}
						else {
							ansPath+=sp.get(i).getKey();
							gr.getEdge(sp.get(i-1).getKey(), sp.get(i).getKey()).setTag(1);
						}
					}
				}
				JOptionPane.showMessageDialog(res1,ansPath);
				repaint();
			} catch (Exception e1){
				e1.printStackTrace();
			}
			break;
		case "tsp":
			graph_algorithms gggg=new Graph_Algo();
			gggg.init(this.gr);
			ArrayList<Integer> targets=new ArrayList<Integer>();
			String inTarget="";
			try {
				do
				{
					JFrame ask2 = new JFrame();
					inTarget=JOptionPane.showInputDialog(ask2,"Enter the point you want to go through\nto finish enter stop");
					if(!inTarget.equals("stop"))targets.add(Integer.parseInt(inTarget));
				}while(!inTarget.equals("stop"));
				ArrayList<node_data> ts=(ArrayList<node_data>) gggg.TSP(targets);
				String ansPath1="";
				JFrame res2= new JFrame();
				for (int i = 0; i < ts.size(); i++) 
				{
					if(i!=ts.size()-1) 
					{
						ansPath1+=ts.get(i).getKey()+"-->";
					}
					else {
						ansPath1+=ts.get(i).getKey();
					}
				}
				JOptionPane.showMessageDialog(res2,ansPath1);
				repaint();
			} catch(Exception e2) {
				e2.printStackTrace();
			}

			break;
		default:
		}
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
		//		repaint();
		//		System.out.println("mousePressed");

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
