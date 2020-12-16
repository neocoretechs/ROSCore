/**
 * 
 */
package com.neocoretechs.roscore;

import java.util.ArrayList;

import org.ros.exception.RosRuntimeException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;


/**
 * Start a ROS node, configure it, set it up, do things you want, then execute it after your custom setups.
 * Node.getInstance().setUp(commandLine[]);
 * do things
 * Node.getInstance().exec();
 * @author jg
 *
 */
public class Node {
	private static Node instance = null;
	protected NodeConfiguration nodeConfiguration;
	protected NodeMainExecutor nodeMainExecutor;
	public static Node getInstance() {
		if( instance == null ) {
			instance = new Node();
		}
		return instance;
	}
	private Node() {}
	
	public NodeConfiguration getNodeConfiguration() {
		return nodeConfiguration;
	}
	public NodeMainExecutor getNodeMainExecutor() {
		return nodeMainExecutor;
	}

	public void setUp(String[] args) throws InterruptedException {
		ArrayList<String> l1 = new ArrayList<String>();
		for(int i = 0; i < args.length; i++) l1.add(args[i]);
		CommandLineLoader loader = new CommandLineLoader(l1, System.getenv());
		nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		nodeConfiguration = loader.build();
		System.out.println("Loading node class: " + loader.getNodeClassName());
	 }
	 
	public void exec() {
		NodeMain nodeMain = null;
		String nodeClassName = nodeConfiguration.getCommandLineLoader().getNodeClassName();
		try {
		      nodeMain = nodeConfiguration.getCommandLineLoader().loadClass(nodeClassName);
		} catch (ClassNotFoundException e) {
		      throw new RosRuntimeException("Unable to locate node: " + nodeClassName, e);
		} catch (InstantiationException e) {
		      throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
		} catch (IllegalAccessException e) {
		      throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
		}
		assert(nodeMain != null);
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		nodeMainExecutor.execute(nodeMain, nodeConfiguration);
	}
	 public void tearDown() {
		  nodeMainExecutor.shutdown();
	 }
	 
}
