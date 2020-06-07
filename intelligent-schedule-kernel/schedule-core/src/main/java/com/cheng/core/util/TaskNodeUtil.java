package com.cheng.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *@Description TODO
 *@Author beedoorwei
 *@Date 2019/7/9 12:49
 *@Version 1.0.0
 */
public class TaskNodeUtil {

	public static String ROOT_LEVEL = "ROOT";

	public static String nextLevel(String current) {
		if (StringUtils.isEmpty(current)) {
			return ROOT_LEVEL;
		}
		if (StringUtils.equals(current, ROOT_LEVEL)) {
			return String.format("LEVEL_1");
		} else {
			String[] s = current.split("_");
			if (s.length == 1) {
				return String.format("%s_%s", current, 1);
			}
			return String.format("%s_%s", s[0], NumberUtils.toInt(s[1], 0) + 1);
		}
	}

	public static void main(String[] args) {
		System.out.println(nextLevel(null));
		System.out.println(nextLevel(nextLevel(null)));
		System.out.println(nextLevel(nextLevel(nextLevel(null))));
		System.out.println(nextLevel(nextLevel(nextLevel(nextLevel(null)))));
	}

	/**
	 * if the task level is first level
	 * @param currentNodeName
	 * @return
	 */
	public static boolean isFirstLevel(String currentNodeName) {
		String[] s = currentNodeName.split("_");
		if (s.length >= 2 && NumberUtils.toInt(s[1]) == 1) {
			return true;
		}
		return false;
	}
}
