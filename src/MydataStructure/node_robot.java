package MydataStructure;

import java.util.List;

import MyUtils.Point3D;

public interface node_robot {
	/**
	 * Return the key (id) associated with this node.
	 * @return
	 */
	public int getId();
	/** Return the location (of applicable) of this node, if
	 * none return null.
	 * 
	 * @return
	 */
	public int getSrc();
	/** Return the source of node
	 * 
	 * 
	 * @return
	 */
	public Point3D getLocation();
	/** Allows changing this node's location.
	 * 
	 * @param p - new new location  (position) of this node.
	 */
	public void setLocation(Point3D p);
	/**
	 * Return the path that robot need to go throw
	 * @return
	 */

	public List<node_data> getPath();
	/** 
	 *  change the robot path
	 *  @param list of vertex
	 */
	public void setPath(List<node_data> p);
}
