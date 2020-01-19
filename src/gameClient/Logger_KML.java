package gameClient;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import MydataStructure.edge_data;
import MydataStructure.graph;
import MydataStructure.node_data;
import MydataStructure.node_fruit;
import MydataStructure.node_robot;
import Server.game_service;

public class Logger_KML {
	game_service game;
	private graph gr;
	FileWriter file;
	String str;
	String fileName;
	ArrayList<node_fruit> fruits; //list for fruits
	ArrayList<node_robot> robots; //list for robots
	long t;// time of game
	public Logger_KML()
	{
		this.game=null;
		this.gr=null;
		this.file=null;
		this.str=null;
		this.fileName=null;
		this.fruits=new ArrayList<node_fruit>();
		this.robots=new ArrayList<node_robot>();
		this.t=0;
	}
	public void setGame (game_service g)
	{
		this.game=g;
		this.t=game.timeToEnd();
	}
	/**
	 * 
	 * @param fruits - list of fruits
	 * @param now - start time for google map
	 * @param after - end time for google map
	 */
	public void setFruits(ArrayList<node_fruit> fruits,String now,String after)
	{
		this.fruits=fruits;
		for(node_fruit f: this.fruits)
		{
			if(f.getType()==-1) // banana=cheese=dollar
			{
			str+="<name>iconBanana.kml</name>\r\n" + 
					"	<StyleMap id=\"msn_dollar\">\r\n" + 
					"		<Pair>\r\n" + 
					"			<key>normal</key>\r\n" + 
					"			<styleUrl>#sn_dollar</styleUrl>\r\n" + 
					"		</Pair>\r\n" + 
					"		<Pair>\r\n" + 
					"			<key>highlight</key>\r\n" + 
					"			<styleUrl>#sh_dollar</styleUrl>\r\n" + 
					"		</Pair>\r\n" + 
					"	</StyleMap>\r\n" + 
					"	<Style id=\"sh_dollar\">\r\n" + 
					"		<IconStyle>\r\n" + 
					"			<color>ff00f000</color>\r\n" + 
					"			<scale>1</scale>\r\n" + 
					"			<Icon>\r\n" + 
					"				<href>http://maps.google.com/mapfiles/kml/shapes/dollar.png</href>\r\n" + 
					"			</Icon>\r\n" + 
					"			<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\r\n" + 
					"		</IconStyle>\r\n" + 
					"		<BalloonStyle>\r\n" + 
					"		</BalloonStyle>\r\n" + 
					"		<ListStyle>\r\n" + 
					"		</ListStyle>\r\n" + 
					"	</Style>\r\n" + 
					"	<Style id=\"sn_dollar\">\r\n" + 
					"		<IconStyle>\r\n" + 
					"			<color>ff00f000</color>\r\n" + 
					"			<scale>0.7</scale>\r\n" + 
					"			<Icon>\r\n" + 
					"				<href>http://maps.google.com/mapfiles/kml/shapes/dollar.png</href>\r\n" + 
					"			</Icon>\r\n" + 
					"			<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\r\n" + 
					"		</IconStyle>\r\n" + 
					"		<BalloonStyle>\r\n" + 
					"		</BalloonStyle>\r\n" + 
					"		<ListStyle>\r\n" + 
					"		</ListStyle>\r\n" + 
					"	</Style>\r\n" + 
					"	<Placemark>\r\n" + 
					"		<name></name>\r\n" + 
					"		<LookAt>\r\n" + 
					"			<longitude>35.20024537475233</longitude>\r\n" + 
					"			<latitude>32.10484153064817</latitude>\r\n" + 
					"			<altitude>0</altitude>\r\n" + 
					"			<heading>-1.633537973287152e-008</heading>\r\n" + 
					"			<tilt>45.00051407854731</tilt>\r\n" + 
					"			<range>2813.773629704574</range>\r\n" + 
					"			<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>\r\n" + 
					"		</LookAt>\r\n" + 
					"        <TimeSpan>\r\n" + 
					"          <begin>"+now+"</begin>\r\n" + 
					"          <end>"+after+"</end>\r\n" + 
					"        </TimeSpan>\r\n"+
					"		<styleUrl>#msn_dollar</styleUrl>\r\n" + 
					"		<Point>\r\n" + 
					"			<gx:drawOrder>1</gx:drawOrder>\r\n" + 
					"			<coordinates>"+f.getLocation().toString()+"</coordinates>\r\n" + 
					"		</Point>\r\n" + 
					"	</Placemark>\r\n" + 
					"";
			}
			else // 1=apple=cookie=euro
			{
				str+="<name>iconApple.kml</name>\r\n" + 
						"	<Style id=\"sn_euro\">\r\n" + 
						"		<IconStyle>\r\n" + 
						"			<color>ffff5500</color>\r\n" + 
						"			<scale>1</scale>\r\n" + 
						"			<Icon>\r\n" + 
						"				<href>http://maps.google.com/mapfiles/kml/shapes/euro.png</href>\r\n" + 
						"			</Icon>\r\n" + 
						"			<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\r\n" + 
						"		</IconStyle>\r\n" + 
						"		<BalloonStyle>\r\n" + 
						"		</BalloonStyle>\r\n" + 
						"		<ListStyle>\r\n" + 
						"		</ListStyle>\r\n" + 
						"	</Style>\r\n" + 
						"	<StyleMap id=\"msn_euro\">\r\n" + 
						"		<Pair>\r\n" + 
						"			<key>normal</key>\r\n" + 
						"			<styleUrl>#sn_euro</styleUrl>\r\n" + 
						"		</Pair>\r\n" + 
						"		<Pair>\r\n" + 
						"			<key>highlight</key>\r\n" + 
						"			<styleUrl>#sh_euro</styleUrl>\r\n" + 
						"		</Pair>\r\n" + 
						"	</StyleMap>\r\n" + 
						"	<Style id=\"sh_euro\">\r\n" + 
						"		<IconStyle>\r\n" + 
						"			<color>ffff5500</color>\r\n" + 
						"			<scale>0.945455</scale>\r\n" + 
						"			<Icon>\r\n" + 
						"				<href>http://maps.google.com/mapfiles/kml/shapes/euro.png</href>\r\n" + 
						"			</Icon>\r\n" + 
						"			<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\r\n" + 
						"		</IconStyle>\r\n" + 
						"		<BalloonStyle>\r\n" + 
						"		</BalloonStyle>\r\n" + 
						"		<ListStyle>\r\n" + 
						"		</ListStyle>\r\n" + 
						"	</Style>\r\n" + 
						"	<Placemark>\r\n" + 
						"		<name>apple</name>\r\n" + 
						"		<LookAt>\r\n" + 
						"			<longitude>35.1995021398652</longitude>\r\n" + 
						"			<latitude>32.10644220010536</latitude>\r\n" + 
						"			<altitude>0</altitude>\r\n" + 
						"			<heading>-0.001191563769536952</heading>\r\n" + 
						"			<tilt>53.5440244065213</tilt>\r\n" + 
						"			<range>2560.605395589177</range>\r\n" + 
						"			<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>\r\n" + 
						"		</LookAt>\r\n" +
						"        <TimeSpan>\r\n" + 
						"          <begin>"+now+"</begin>\r\n" + 
						"          <end>"+after+"</end>\r\n" + 
						"        </TimeSpan>\r\n"+
						"		<styleUrl>#msn_euro</styleUrl>\r\n" + 
						"		<Point>\r\n" + 
						"			<gx:drawOrder>1</gx:drawOrder>\r\n" + 
						"			<coordinates>"+f.getLocation().toString()+"</coordinates>\r\n" + 
						"		</Point>\r\n" + 
						"	</Placemark>";
			}
		}
	}
	
	/**
	 * 
	 * @param robots - list of robots
	 * @param now - start time for google map
	 * @param after - end time for google map
	 */
	public void setRobots(ArrayList<node_robot> robots,String now,String after)
	{
		this.robots=robots;
		for(node_robot r:this.robots)
		{
			
			str+="<name>iconRob.kml</name>\r\n" + 
					"	<StyleMap id=\"m_ylw-pushpin\">\r\n" + 
					"		<Pair>\r\n" + 
					"			<key>normal</key>\r\n" + 
					"			<styleUrl>#s_ylw-pushpin</styleUrl>\r\n" + 
					"		</Pair>\r\n" + 
					"		<Pair>\r\n" + 
					"			<key>highlight</key>\r\n" + 
					"			<styleUrl>#s_ylw-pushpin_hl</styleUrl>\r\n" + 
					"		</Pair>\r\n" + 
					"	</StyleMap>\r\n" + 
					"	<Style id=\"s_ylw-pushpin\">\r\n" + 
					"		<IconStyle>\r\n" + 
					"			<color>ff0080ff</color>\r\n" + 
					"			<scale>1.3</scale>\r\n" + 
					"			<Icon>\r\n" + 
					"				<href>http://maps.google.com/mapfiles/kml/shapes/horsebackriding.png</href>\r\n" + 
					"			</Icon>\r\n" + 
					"			<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\r\n" + 
					"		</IconStyle>\r\n" + 
					"		<ListStyle>\r\n" + 
					"		</ListStyle>\r\n" + 
					"	</Style>\r\n" + 
					"	<Style id=\"s_ylw-pushpin_hl\">\r\n" + 
					"		<IconStyle>\r\n" + 
					"			<color>ff0080ff</color>\r\n" + 
					"			<scale>1.3</scale>\r\n" + 
					"			<Icon>\r\n" + 
					"				<href>http://maps.google.com/mapfiles/kml/shapes/horsebackriding.png</href>\r\n" + 
					"			</Icon>\r\n" + 
					"			<hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\r\n" + 
					"		</IconStyle>\r\n" + 
					"		<ListStyle>\r\n" + 
					"		</ListStyle>\r\n" + 
					"	</Style>\r\n" + 
					"	<Placemark>\r\n" + 
					"		<name>"+r.getId()+"</name>\r\n" + 
					"		<LookAt>\r\n" + 
					"			<longitude>35.18542817221795</longitude>\r\n" + 
					"			<latitude>32.10423577987925</latitude>\r\n" + 
					"			<altitude>0</altitude>\r\n" + 
					"			<heading>0.1990980303955647</heading>\r\n" + 
					"			<tilt>7.249485255264151</tilt>\r\n" + 
					"			<range>5857.957528229067</range>\r\n" + 
					"			<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>\r\n" + 
					"		</LookAt>\r\n" +
					"        <TimeSpan>\r\n" + 
					"          <begin>"+now+"</begin>\r\n" + 
					"          <end>"+after+"</end>\r\n" + 
					"        </TimeSpan>\r\n"+
					"		<styleUrl>#m_ylw-pushpin</styleUrl>\r\n" + 
					"		<Point>\r\n" + 
					"			<gx:drawOrder>1</gx:drawOrder>\r\n" + 
					"			<coordinates>"+r.getLocation().toString()+"</coordinates>\r\n" + 
					"		</Point>\r\n" + 
					"	</Placemark>\r\n" + 
					"";
		}
	}
	/**
	 * 
	 * @param g - graph from MyGameGui
	 */
	public void setGraph (graph g) 
	{
		this.gr=g;
		this.str="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\r\n" + 
				"<Document>\r\n" + 
				"";
	}
	
	/**
	 * 
	 * @param i - senario for save the name kmlFile
	 */
	public void setName(String i)
	{
		fileName=i+".kml";
		str+="<name>"+fileName+"</name>";
	}
	
	/**
	 *  build graph by vertex and edges
	 */
	public void buildGraph ()
	{
		for(node_data v: gr.getV())
		{
			str+="	<Placemark>\r\n" + 
					"		<name>"+v.getKey()+"</name>\r\n" + 
					"		<LookAt>\r\n" + 
					"			<gx:TimeStamp><when>2020-01-12T09:10:33.577278137Z</when>\r\n" + 
					"</gx:TimeStamp>\r\n" + 
					"			<gx:ViewerOptions>\r\n" + 
					"				<gx:option enabled=\"0\" name=\"historicalimagery\"></gx:option>\r\n" + 
					"				<gx:option name=\"sunlight\"></gx:option>\r\n" + 
					"				<gx:option enabled=\"0\" name=\"streetview\"></gx:option>\r\n" + 
					"			</gx:ViewerOptions>\r\n" + 
					"			<longitude>35.24764933928396</longitude>\r\n" + 
					"			<latitude>32.17216844027814</latitude>\r\n" + 
					"			<altitude>0</altitude>\r\n" + 
					"			<heading>-5.043624673281415</heading>\r\n" + 
					"			<tilt>0</tilt>\r\n" + 
					"			<range>56228.21959268493</range>\r\n" + 
					"			<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>\r\n" + 
					"		</LookAt>\r\n" + 
					"		<styleUrl>#msn_lodging</styleUrl>\r\n" + 
					"" + 
					"		<Point>\r\n" + 
					"			<gx:drawOrder>1</gx:drawOrder>\r\n" + 
					"			<coordinates>"+v.getLocation().x()+","+ v.getLocation().y() +",0</coordinates>\r\n" + 
					"		</Point>\r\n" + 
					"	</Placemark>\r\n" + 
					"";
		}
		for(node_data v: gr.getV())
		{
			for(edge_data e: gr.getE(v.getKey()))
			{
				str+="<name>ED.kml</name>\r\n" + 
						"	<StyleMap id=\"msn_ylw-pushpin\">\r\n" + 
						"		<Pair>\r\n" + 
						"			<key>normal</key>\r\n" + 
						"			<styleUrl>#sn_ylw-pushpin</styleUrl>\r\n" + 
						"		</Pair>\r\n" + 
						"		<Pair>\r\n" + 
						"			<key>highlight</key>\r\n" + 
						"			<styleUrl>#sh_ylw-pushpin</styleUrl>\r\n" + 
						"		</Pair>\r\n" + 
						"	</StyleMap>\r\n" + 
						"	<Style id=\"sh_ylw-pushpin\">\r\n" + 
						"		<IconStyle>\r\n" + 
						"			<scale>1.3</scale>\r\n" + 
						"			<Icon>\r\n" + 
						"				<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\r\n" + 
						"			</Icon>\r\n" + 
						"			<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" + 
						"		</IconStyle>\r\n" + 
						"		<BalloonStyle>\r\n" + 
						"		</BalloonStyle>\r\n" + 
						"		<LineStyle>\r\n" + 
						"			<color>ff0000ff</color>\r\n" + 
						"			<width>2</width>\r\n" + 
						"		</LineStyle>\r\n" + 
						"	</Style>\r\n" + 
						"	<Style id=\"sn_ylw-pushpin\">\r\n" + 
						"		<IconStyle>\r\n" + 
						"			<scale>1.1</scale>\r\n" + 
						"			<Icon>\r\n" + 
						"				<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\r\n" + 
						"			</Icon>\r\n" + 
						"			<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" + 
						"		</IconStyle>\r\n" + 
						"		<BalloonStyle>\r\n" + 
						"		</BalloonStyle>\r\n" + 
						"		<LineStyle>\r\n" + 
						"			<color>ff0000ff</color>\r\n" + 
						"			<width>2</width>\r\n" + 
						"		</LineStyle>\r\n" + 
						"	</Style>\r\n" + 
						"	<Placemark>\r\n" + 
						"		<name>ED</name>\r\n" + 
						"		<styleUrl>#msn_ylw-pushpin</styleUrl>\r\n" + 
						"		<LineString>\r\n" + 
						"			<tessellate>1</tessellate>\r\n" + 
						"			<coordinates>\r\n" + 
						"				"+v.getLocation().x()+","+v.getLocation().y()+",0 "+gr.getNode(e.getDest()).getLocation().x()+","+gr.getNode(e.getDest()).getLocation().y()+",0 \r\n" + 
						"			</coordinates>\r\n" + 
						"		</LineString>\r\n" + 
						"	</Placemark>\r\n" + 
						"";
			}
		}
	}
	/**
	 * end of string 
	 */
	public void finalText ()
	{
		str+="</Document>\r\n" + 
				"</kml>\r\n" + 
				"";
	}
	/**
	 * save kml file
	 */
	public void saveKml()
	{
		try {
			file = new FileWriter("data/"+fileName);
			file.write(this.str);
			file.close();
		} catch (RuntimeException | IOException e) {
			e.printStackTrace();
		}
	}

}
