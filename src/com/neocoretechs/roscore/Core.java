/**
 * 
 */
package com.neocoretechs.roscore;

import java.util.concurrent.TimeUnit;

import org.ros.RosCore;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;


/**
 * Start the ROS core programmatically
 * @author jg
 *
 */
public class Core {
	private static Core instance = null;
	protected RosCore rosCore;
	protected NodeConfiguration nodeConfiguration;
	protected NodeMainExecutor nodeMainExecutor;
	public static String host = "roscoe2";
	public static Core getInstance() {
		if( instance == null ) {
			instance = new Core();
		}
		return instance;
	}
	private Core() {}
	
	public RosCore getRosCore() {
		return rosCore;
	}
	public NodeConfiguration getNodeConfiguration() {
		return nodeConfiguration;
	}
	public NodeMainExecutor getNodeMainExecutor() {
		return nodeMainExecutor;
	}


	public void setUp() throws InterruptedException {
		    rosCore = RosCore.newPublic(host,8090); // bind port
		    rosCore.start();
		    assert(rosCore.awaitStart(1, TimeUnit.SECONDS));
		    nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		    nodeConfiguration = NodeConfiguration.newPublic(host, rosCore.getUri());
		    System.out.println("ROS Core Uri:"+rosCore.getUri());
	 }

	 public void setUp(String bindHost) throws InterruptedException {
		host = bindHost;
	    rosCore = RosCore.newPublic(bindHost, 8090); // bind port 50000
	    rosCore.start();
	    assert(rosCore.awaitStart(1, TimeUnit.SECONDS));
	    nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
	    nodeConfiguration = NodeConfiguration.newPublic(bindHost, rosCore.getUri());
	    System.out.println("ROS Core Uri:"+rosCore.getUri());
	 }
	 
	 public void tearDown() {
		  nodeMainExecutor.shutdown();
		  rosCore.shutdown();
	 }
	 
	 public static void main(String[] args) throws Exception {
		 if( args.length < 1) {
			 System.out.println("Usage java -cp . com.neocoretechs.roscore.Core <host>");
			 return;
		 }
		 Core.getInstance().setUp(args[0]);
	
			
	 }
}
