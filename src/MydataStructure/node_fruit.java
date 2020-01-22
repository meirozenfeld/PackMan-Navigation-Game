package MydataStructure;

import MyUtils.Point3D;

public interface node_fruit {

	/**
	 * Return the type banana or apple
	 * @return
	 */
	public int getType();
	/** Return the source of node
	 * 
	 * 
	 * @return
	 */
	public int getSrc();
	/** Allows changing this node's src
	 * 
	 * @param s - new src
	 */
	public void setSrc(int s);
	/** Return the dest of node
	 * 
	 * 
	 * @return
	 */
	public int getDest();
	/** Allows changing this node's dest
	 * 
	 * @param d - new dest
	 */
	public void setDest(int d);
	/** Return the location (of applicable) of this node, if
	 * none return null.
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
	 * Return the tag (how many fruits on edge)
	 * @return
	 */

	public int getTag();
	/** 
	 * set tag
	 * @param t -tag
	 */
	public void setTag(int t);
	
	/**
	 * Return the answer (if robot going to fruit)
	 * @return
	 */

	public int getWithRobot();
	/** 
	 * change with robot or not
	 * @param t - 0 or 1
	 */
	public void setWithRobot(int t);
	
	
	/** Return the value of node
	 * 
	 * 
	 * @return
	 */
	public int getValue();

}

