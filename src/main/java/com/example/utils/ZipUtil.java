package com.example.utils;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import java.io.File;
import java.io.IOException;

/**
 * 通过Java的Zip输入输出流实现压缩和解压文件
 * 
 * @author liujiduo
 * 
 */
public final class ZipUtil {

 	private static Logger logger = Logger.getLogger(ZipUtil.class);
	public final static String encoding = "GBK";
	/**
	 * 解压缩文件和文件夹
	 *
	 * @param zipFilepath 需要被解压的zip文件路径
	 * @param destDir 将要被解压到哪个文件夹
	 * @throws BuildException
	 * @throws RuntimeException
	 */
	public static void unzip(String zipFilepath, String destDir) throws RuntimeException {
		if (!new File(zipFilepath).exists()) {
			throw new RuntimeException("zip file " + zipFilepath + " does not exist.");
		}
		logger.info("需要被解压的zip文件路径:"+zipFilepath);
		logger.info("目标路径:"+destDir);
		Project proj = new Project();
		Expand expand = new Expand();
		expand.setProject(proj);
		expand.setTaskType("unzip");
		expand.setTaskName("unzip");
		expand.setEncoding(encoding);

		expand.setSrc(new File(zipFilepath));
		expand.setDest(new File(destDir));
		expand.execute();
	}
	public static void main(String[] args) throws IOException {
		String zipfile="E:\\bvdatac\\target\\classes\\upload\\bill\\20885219024064740156_20170707.csv.zip";
		ZipUtil.unzip(zipfile, "E:\\bvdatac\\target\\classes\\upload\\bills");
	}
}