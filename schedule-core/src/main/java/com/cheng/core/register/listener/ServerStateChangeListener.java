package com.cheng.core.register.listener;


import com.cheng.core.register.ServerInstance;

import java.util.List;


public interface ServerStateChangeListener {

	/**
	 * remove server
	 * @param serverInstance
	 */
	public void serverRemoved(ServerInstance serverInstance);

	/**
	 * add server
	 * @param serverInstance
	 */
	public void serverAdd(ServerInstance serverInstance);

	/**
	 * refresh all server
	 * @param serverInstanceList
	 */
	public void refreshAll(List<ServerInstance> serverInstanceList);
}
