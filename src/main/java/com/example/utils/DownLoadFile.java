package com.example.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @Discription 文件下载，支持断点
 * @Author houfengli
 * @CreateDate 2016/5/10 11:57
 * @Version 1.0
 */
public class DownLoadFile {
    private static Log log = LogFactory.getLog(DownLoadFile.class);

    /**
     * 下载http 文件
     * @param request
     * @param response
     */
    public static void downLoad(InputStream iputstream, String fileName,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        // 下载
        ServletOutputStream out= null;
        BufferedOutputStream bufferOut= null;

        try {
            BufferedInputStream bis = new BufferedInputStream(iputstream);

            String userAgent=request.getHeader("User-Agent").toLowerCase();
            // IE浏览器，只能采用URLEncoder编码
            String rtn;
            if (userAgent.indexOf("msie") != -1)
            {
                rtn =  URLEncoder.encode(fileName, "UTF8");
            }else {
                rtn =  new String(fileName.getBytes("UTF-8"),"ISO-8859-1") ;
            }

            response.setHeader("Content-Disposition", "attachment; filename=\""+rtn+'"');

            int readed;
            byte[] buffer = new byte[1024];

            out = response.getOutputStream();
            bufferOut = new BufferedOutputStream(out);
            while((readed = bis.read(buffer)) != -1){
                bufferOut.write(buffer,0,readed);
            }

            bufferOut.flush();
            bufferOut.close();
            bis.close();
        } catch (Exception e) {
            log.error("下载文件异常", e);
        }
    }

    /**
     * 下载http 文件
     * @param fileName
     * @param request
     * @param response
     */
    public static void downLoad(InputStream iputstream, Long fSize, String fileName, HttpServletRequest request,
                                HttpServletResponse response) {
        // 下载
        ServletOutputStream out= null;
        BufferedOutputStream bufferOut= null;

        try {
            BufferedInputStream bis = new BufferedInputStream(iputstream);
            bis.mark(Integer.MAX_VALUE);
            byte[] buffer = new byte[1024];
            int readed;
            log.info("文件总长："+fSize);
            long pos = 0;

            response.setContentType("application/x-download;charset=utf-8");
            response.setHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", String.valueOf(fSize));
            response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(fileName.getBytes("utf-8"),"ISO-8859-1")+'"');

            if (null != request.getHeader("Range")) {
                // 断点续传
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                try {
                    pos = Long.parseLong(request.getHeader("Range").replaceAll(
                            "bytes=", "").replaceAll("-", ""));
                } catch (NumberFormatException e) {
                    log.error(request.getHeader("Range") + " is not Number!");
                    pos = 0;
                }
                log.info("开始下载位置：" + pos);
            }
            String contentRange = new StringBuffer("bytes ").append(
                    new Long(pos).toString()).append("-").append(
                    new Long(fSize - 1).toString()).append("/").append(
                    new Long(fSize).toString()).toString();
            log.info(contentRange);
            response.setHeader("Content-Range", contentRange);

            out = response.getOutputStream();
            bufferOut = new BufferedOutputStream(out);
            bis.skip(pos);
            while((readed = bis.read(buffer)) != -1){
                bufferOut.write(buffer,0,readed);
            }

            bufferOut.flush();
            bufferOut.close();
            bis.close();
        } catch (Exception e) {
            log.error("下载文件异常", e);
        }
    }


    /**
     * 下载http 文件
     * @param httPUrl
     * @param fileName
     * @param request
     * @param response
     */
    public static void downLoad(String httPUrl, String fileName, HttpServletRequest request,
                                HttpServletResponse response) {
        log.info("下载 "+fileName+" URL:"+httPUrl);
        // 下载
        InputStream iputstream = null;
        ServletOutputStream out= null;
        BufferedOutputStream bufferOut= null;

        try {
            URL url = new URL(httPUrl);
            HttpURLConnection uc =(HttpURLConnection) url.openConnection();
            uc.addRequestProperty("Accept-Charset","utf-8");
            uc.connect();
            iputstream = uc.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(iputstream);
            int fSize = uc.getContentLength();
            log.info("文件总长："+fSize);
            long pos = 0;

            response.setContentType("application/x-download;charset=utf-8");
            response.setHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", String.valueOf(fSize));
            response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(fileName.getBytes("utf-8"),"ISO-8859-1")+'"');

            if (null != request.getHeader("Range")) {
                // 断点续传
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                try {
                    pos = Long.parseLong(request.getHeader("Range").replaceAll(
                            "bytes=", "").replaceAll("-", ""));
                } catch (NumberFormatException e) {
                    log.error(request.getHeader("Range") + " is not Number!");
                    pos = 0;
                }
                log.info("开始下载位置：" + pos);
            }
            String contentRange = new StringBuffer("bytes ").append(
                    new Long(pos).toString()).append("-").append(
                    new Long(fSize - 1).toString()).append("/").append(
                    new Long(fSize).toString()).toString();
            log.info(contentRange);
            response.setHeader("Content-Range", contentRange);

            out = response.getOutputStream();
            bufferOut = new BufferedOutputStream(out);
            byte[] buffer = new byte[1024];
            bis.skip(pos);
            int readed;
            while((readed = bis.read(buffer)) != -1){
                bufferOut.write(buffer,0,readed);
            }

            bufferOut.flush();
            bufferOut.close();
            bis.close();
        } catch (Exception e) {
            log.error("下载文件异常", e);
        }
    }

    /**
     * 下载本地文件
     * @param proposeFile
     * @param request
     * @param response
     */
    public static void downLoad(File proposeFile, HttpServletRequest request,
                                HttpServletResponse response) {
        if (null == proposeFile || !proposeFile.exists()) {
            log.warn("下载文件不存在");
            return;
        }
        if (!proposeFile.canRead()) {
            log.warn("下载文件不可读");
            return;
        }
        log.info("下载文件路径：" + proposeFile.getPath());
        long fSize = proposeFile.length();
        log.info("下载文件大小：" + fSize);
        // 下载
        try {
            long pos = 0;
            if (null != request.getHeader("Range")) {
                // 断点续传
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                try {
                    pos = Long.parseLong(request.getHeader("Range").replaceAll(
                            "bytes=", "").replaceAll("-", ""));
                } catch (NumberFormatException e) {
                    log.error(request.getHeader("Range") + " is not Number!");
                    pos = 0;
                }
                log.info("开始下载位置：" + pos);
            }
            response.setContentType("application/x-download");
            response.setHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", String.valueOf(fSize));
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + new String(proposeFile.getName().getBytes("utf-8"),
                    "ISO-8859-1")+'"');

            ServletOutputStream out = response.getOutputStream();
            BufferedOutputStream bufferOut = new BufferedOutputStream(out);
            String contentRange = new StringBuffer("bytes ").append(
                    new Long(pos).toString()).append("-").append(
                    new Long(fSize - 1).toString()).append("/").append(
                    new Long(fSize).toString()).toString();
            log.info(contentRange);
            response.setHeader("Content-Range", contentRange);

            byte[] buffer = new byte[1024];

            RandomAccessFile raf = new RandomAccessFile(proposeFile, "r");
            raf.seek(pos);
            int i = 0;
            while ((i = raf.read(buffer)) != -1) {
                bufferOut.write(buffer,0,i);
            }
            bufferOut.flush();
            bufferOut.close();
            raf.close();
        } catch (Exception e) {
            log.error("下载文件异常", e);
        }
    }
    /**
     * 下载远程文件并保存到本地
     * @param remoteFilePath 远程文件路径
     * @param localFilePath 本地文件路径
     */
    public static void downloadFile(String remoteFilePath, String localFilePath){
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        File f = new File(localFilePath);

        try {
            if (!f.exists()) {
                // 先得到文件的上级目录，并创建上级目录，在创建文件
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e)
        {
           log.error("下载远程文件并保存到本地异常",e);
        }
        finally
        {
            try
            {
                bis.close();
                bos.close();
            }
            catch (IOException e)
            {
                log.error("下载远程文件并保存到本地关闭流异常",e);
            }
        }
    }

}
