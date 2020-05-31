package com.cheng.core.register;



import com.cheng.core.register.listener.ServerStateChangeListener;

import java.util.List;


public interface ServerRegister {

    /**
     * do register user
	 */
	public boolean register()throws Exception;


	/**
	 * 解除服务注册
	 * @return
	 * @throws Exception
	 */
	public boolean unRegister()throws Exception;

	/**
	 * get all server
	 * @return
	 */
	public List<ServerInstance> getAllServer(String groupId)throws Exception;


	/**
	 * add server change listener
	 * @param serverStateChangeListener
	 */
	public void addServerChangeListener(String groupId, ServerStateChangeListener serverStateChangeListener)throws Exception;


    /**
     * get the register name
     * @return
     */
   public String registerCode();

}

