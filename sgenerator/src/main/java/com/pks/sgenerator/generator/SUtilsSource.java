package com.pks.sgenerator.generator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author pks
 * @date 2020年9月14日
 */
public class SUtilsSource {

	public static void genSourceFile(String sourceName,String saveBasePath){
		File file = new File(saveBasePath + sourceName);
		if(file.exists()) return ;
		DataOutputStream outputStream  = null;
		InputStream inputStream = null;
		try {
			inputStream = SUtilsSource.class.getResourceAsStream(sourceName);
			outputStream = new DataOutputStream(new FileOutputStream(saveBasePath + sourceName));
			int len = inputStream.available();
			//判断长度是否大于1M
			if (len <= 1024 * 1024) {
				byte[] bytes = new byte[len];
				inputStream.read(bytes);
				outputStream.write(bytes);
			} else {
				int byteCount = 0;
				//1M逐个读取
				byte[] bytes = new byte[1024*1024];
				while ((byteCount = inputStream.read(bytes)) != -1){
					outputStream.write(bytes, 0, byteCount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.flush();
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
