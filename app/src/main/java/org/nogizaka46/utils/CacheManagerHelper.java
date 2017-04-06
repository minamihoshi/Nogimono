package org.nogizaka46.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * 功能:清除内/外缓存， 清除数据库， 清除sharedPreference， 清除files和清除自定义目录
 * */
public class CacheManagerHelper {

	// 清除本应用内部缓存：(/data/data/com.xxx.xxx/cache)
	public static void clearInternalCache(Context context) {
		delelteDirAllFiles(context.getCacheDir());
	}

	// 清除本应用私有数据库：(/data/data/com.xxx.xxx/databases)
	public static void clearDatabases(Context context) {
		deleteFilesOfDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	// 清除本应用SharedPreference：(/data/data/com.xxx.xxx/shared_prefs)
	public static void clearSharedPreference(Context context) {
		deleteFilesOfDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	// 按名字清除本应用数据库：
	public static void clearDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	// 清除内部存储中的数据：（/data/data/com.xxx.xxx/files下的内容）
	public static void clearFiles(Context context) {
		deleteFilesOfDirectory(context.getFilesDir());
	}

	// 清除SD卡中cache目录中的数据：(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	public static void clearExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesOfDirectory(context.getExternalCacheDir());
		}
	}

	// 清除自定义路径下的文件
	public static void clearCustomCache(String filePath) {
		deleteFilesOfDirectory(new File(filePath));
	}

	// 清除本应用所有的cache
	public static void clearApplicationCache(Context context) {
		clearInternalCache(context);
		clearExternalCache(context);
	}

	// 清除本应用所有的数据
	public static void clearApplicationData(Context context, String... filepath) {
		clearInternalCache(context);
		clearExternalCache(context);
		clearDatabases(context);
		clearSharedPreference(context);
		clearFiles(context);
		for (String filePath : filepath) {
			clearCustomCache(filePath);
		}
	}

	// 该方法 只删除某目录下的文件，如果传入的directory是个文件，将不做处理
	private static void deleteFilesOfDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				// Log.i("Helper", "---" + item.toString());
				if (item.isDirectory()) {
					deleteDir(item);
				} else {
					item.delete();
				}
			}
		}
	}

	// File.delete()用于删除“某个文件或者空目录”！所以要删除某个目录及其中的所有文件和子目录，要进行递归删除
	// 递归删除目录下的所有文件及子目录下所有文件
	public static void delelteDirAllFiles(File dir) {
		if (dir.isDirectory()) {
			// 若文件夹非空。枚举、递归删除里面内容
			File[] subFiles = dir.listFiles();
			for (int i = 0; i <= subFiles.length - 1; i++) {
				if (subFiles[i].isDirectory()) {
					delelteDirAllFiles(subFiles[i]);// 递归删除子文件夹内容
				}
				subFiles[i].delete();// 删除子文件夹本身
			}
			dir.delete();// 删除此文件夹本身
		}
	}

	// 递归删除目录下的所有文件及子目录下所有文件写法2
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	//Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
	//Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据

	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
//            return size + "Byte";
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	public static String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		return getFormatSize(cacheSize);
	}


}
