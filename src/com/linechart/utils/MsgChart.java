package com.linechart.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

/**
 * @description:
 * @date: 2015年11月25日 上午10:33:13
 * @author: chenpenglong
 * @version 1.0.0
 */
public class MsgChart {

	private static String mUrl;
	private static String mAppVersion;
	/**
	 * 是否向控制台打印Log
	 */
	private static boolean isPrint = true;
	/**
	 * 是否向Sdcard中记录日志
	 */
	private static boolean isSdcard = false;
	/**
	 * 非异常日志是否写到Sdcard
	 */
	private static boolean isPrintToSdcard = false;

	/**
	 * 只打印only日志，其他的不打印
	 */
	private static boolean isOnly = false;
	/**
	 * 输出的tag
	 */
	private static String TAG = "csn";

	/**
	 * 输出级别 verbose
	 */
	public final static int TYPE_VERBOSE = 0;

	/**
	 * 输出级别 debug
	 */
	public final static int TYPE_DEBUG = 1;

	/**
	 * 输出级别 info
	 */
	public final static int TYPE_INFO = 2;

	/**
	 * 输出级别 warn
	 */
	public final static int TYPE_WARN = 3;

	/**
	 * 输出级别 error
	 */
	public final static int TYPE_ERROR = 4;
	/**
	 * 分隔
	 */
	private static final String SEPERATOR = "\n";
	/**
	 * 日志文件夹
	 */
	private static File mFilePath;
	/**
	 * 日志文件
	 */
	private static File mFile;
	/**
	 * 压缩文件
	 */
	private static File mFileZip;

	/**
	 * Log文件夹路径
	 */
	private static String mLogPath;
	/**
	 * Log文件路径
	 */
	private static String mLogFilePath;
	/**
	 * 压缩文件路径
	 */
	private static String mLogZipFilePath;
	/**
	 * 日志最大容量
	 */
	private static final int MAX_LOG_SIZE = 100 * 1024;

	public static void init(Context context, String url) {
		mUrl = url;
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			mLogPath = Environment.getExternalStorageDirectory().toString() + File.separator + "csn";
			mLogFilePath = mLogPath + "/LogInfo.txt";
			mLogZipFilePath = mLogPath + "/LogInfo.zip";
		} else {
			mLogPath = Environment.getExternalStorageDirectory().toString() + File.separator + "csn";
			mLogFilePath = mLogPath + "/LogInfo.txt";
			mLogZipFilePath = mLogPath + "/LogInfo.zip";
		}
		mAppVersion = getVersionName(context);
	}

	/**
	 * 用法1：Msg.i("csn%s=%d", "onCreate",1);输出内容例子：[1] MainActivity.onCreate:
	 * csnonCreate=1 /用法2:Msg.i("csn");输出内容例子：[1] MainActivity.onCreate: csn
	 */
	public static void i(String format, Object... args) {
		if (!isOnly) {
			print(TYPE_INFO, buildMessage(format, args));
		}
		if (isPrintToSdcard) {
			writeLogToSdcard(buildMessage(format, args));
		}
	}

	/**
	 * 用法1：Msg.v("csn%s=%d", "onCreate",1);输出内容例子：[1] MainActivity.onCreate:
	 * csnonCreate=1 /用法2:Msg.v("csn");输出内容例子：[1] MainActivity.onCreate: csn
	 */
	public static void v(String format, Object... args) {
		if (!isOnly) {
			print(TYPE_VERBOSE, buildMessage(format, args));
		}
		if (isPrintToSdcard) {
			writeLogToSdcard(buildMessage(format, args));
		}
	}

	/**
	 * 用法1：Msg.d("csn%s=%d", "onCreate",1);输出内容例子：[1] MainActivity.onCreate:
	 * csnonCreate=1 /用法2:Msg.d("csn");输出内容例子：[1] MainActivity.onCreate: csn
	 */
	public static void d(String format, Object... args) {
		if (!isOnly) {
			print(TYPE_DEBUG, buildMessage(format, args));
		}
		if (isPrintToSdcard) {
			writeLogToSdcard(buildMessage(format, args));
		}
	}

	/**
	 * 用法1：Msg.w("csn%s=%d", "onCreate",1);输出内容例子：[1] MainActivity.onCreate:
	 * csnonCreate=1 /用法2:Msg.w("csn");输出内容例子：[1] MainActivity.onCreate: csn
	 */
	public static void w(String format, Object... args) {
		if (!isOnly) {
			print(TYPE_WARN, buildMessage(format, args));
		}
		if (isPrintToSdcard) {
			writeLogToSdcard(buildMessage(format, args));
		}
	}

	/**
	 * 用法1：Msg.e("分母不能为0%s","我有");输出内容例子: [1] MainActivity.onCreate: 分母不能为0我有
	 */
	public static void e(String format, Object... args) {
		if (!isOnly) {
			print(TYPE_ERROR, buildMessage(format, args));
		}
		if (isPrintToSdcard) {
			writeLogToSdcard(buildMessage(format, args));
		}
	}

	/**
	 * 用法1：Msg.e(e, "分母不能为0");输出内容例子: [1] MainActivity.onCreate: 分母不能为0
	 * java.lang.ArithmeticException: divide by zero ...
	 */
	public static void e(Throwable tr, String format, Object... args) {
		if (!isOnly) {
			print(TYPE_ERROR, buildMessage(format, args), tr);
		}
		if (isPrintToSdcard) {
			StringWriter sw = null;
			PrintWriter pw = null;
			try {
				if (tr == null) {
					return;
				}
				sw = new StringWriter();
				pw = new PrintWriter(sw);
				tr.printStackTrace(pw);
				writeLogToSdcard(buildMessage(format + sw.toString(), args));
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					sw.close();
					pw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void only(String format, Object... args) {
		isOnly = true;
		print(TYPE_INFO, buildMessage(format, args));
		if (isPrintToSdcard) {
			writeLogToSdcard(buildMessage(format, args));
		}
	}

	public static void only(int type, String format, Object... args) {
		isOnly = true;
		print(type, buildMessage(format, args));
		if (isPrintToSdcard) {
			writeLogToSdcard(buildMessage(format, args));
		}
	}

	public static void only(Throwable tr, String format, Object... args) {
		isOnly = true;
		print(TYPE_ERROR, buildMessage(format, args));
		if (isPrintToSdcard) {
			StringWriter sw = null;
			PrintWriter pw = null;
			try {
				if (tr == null) {
					return;
				}
				sw = new StringWriter();
				pw = new PrintWriter(sw);
				tr.printStackTrace(pw);
				writeLogToSdcard(buildMessage(format + sw.toString(), args));
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					sw.close();
					pw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * Log信息输出到控制台
	 */
	private static void print(int grade, Object... msg) {
		if (isPrint) {
			switch (grade) {
			case TYPE_INFO:
				Log.i(TAG, (String) msg[0]);
				break;
			case TYPE_VERBOSE:
				Log.v(TAG, (String) msg[0]);
				break;
			case TYPE_DEBUG:
				Log.d(TAG, (String) msg[0]);
				break;
			case TYPE_WARN:
				Log.w(TAG, (String) msg[0]);
				break;
			case TYPE_ERROR:
				try {
					Log.e(TAG, (String) msg[0], (Throwable) msg[1]);
				} catch (Exception e) {
					Log.e(TAG, (String) msg[0]);
				}
				break;
			}
		}
	}

	private static String buildMessage(String format, Object... args) {
		try {
			String msg = (args == null) ? format : String.format(Locale.US, format, args);
			StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
			String caller = "<unknown>";
			for (int i = 2; i < trace.length; i++) {
				Class<?> clazz = trace[i].getClass();
				if (!clazz.equals(MsgChart.class)) {
					String callingClass = trace[i].getClassName();
					callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
					caller = callingClass + "." + trace[i].getMethodName();
					break;
				}
			}
			return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg);
		} catch (Exception e) {
			return format;
		}
	}

	public static void closePrint() {
		isPrint = false;
	}

	public static void closeSdcard() {
		isSdcard = false;
	}

	public static void openPrintToSdcard() {
		isPrintToSdcard = true;
	}

	public static void setTag(String tag) {
		TAG = tag;
	}

	/**
	 * 写入Exception类型异常日志
	 */
	public static void write(Throwable e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			if (e == null) {
				return;
			}
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			write(sw.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				sw.close();
				pw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 写入字符串类型异常日志
	 */
	public static void write(String msg) {
		writeLogToSdcard(msg);
	}

	/**
	 * 将日志写到Sdcard
	 * 
	 * @param msg
	 */
	private static void writeLogToSdcard(String msg) {
		if (!isSdcard) {
			return;
		}
		FileWriter fw = null;
		try {
			mFilePath = new File(mLogPath);
			mFilePath.mkdirs();
			mFile = new File(mLogFilePath);
			long fileLength = mFile.length();
			// if (fileLength >= MAX_LOG_SIZE) {
			// sendLogZiptoHost();
			// }
			if (!mFile.exists()) {
				Log.d(TAG, "file not exist");
				mFile.createNewFile();
			}
			if (mFile.length() > MAX_LOG_SIZE) {
				mFile.delete();
				mFile.createNewFile();
			}
			fw = new FileWriter(mFile, true);
			buildTag(fw, fileLength);
			fw.append(getTime() + msg + SEPERATOR);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void buildTag(FileWriter fw, long length) {
		try {
			if (length == 0) {
				fw.write("Device: " + android.os.Build.DEVICE + "\n");// 设备参数
				fw.write("Board: " + android.os.Build.BOARD + "\n");// 主板
				fw.write("Brand: " + android.os.Build.BRAND + "\n");// 设备厂商
				fw.write("CPU_abi: " + android.os.Build.CPU_ABI + "\n");// cpu指令集
				fw.write("Display: " + android.os.Build.DISPLAY + "\n");// 显示屏参数
				fw.write("Host: " + android.os.Build.HOST + "\n");//
				fw.write("ID: " + android.os.Build.ID + "\n");// 修订版本列表
				fw.write("Model: " + android.os.Build.MODEL + "\n");// 版本即最终用户可见的名称
				fw.write("Product: " + android.os.Build.PRODUCT + "\n");// 整个产品的名称
				fw.write("Tags: " + android.os.Build.TAGS + "\n");// 描述build的标签,如未签名，debug等等
				fw.write("Fingerprint: " + android.os.Build.FINGERPRINT + "\n");// 唯一识别码
				fw.write("SDK: " + android.os.Build.VERSION.SDK + "\n");// SDK版本号
				fw.write("Version: " + mAppVersion + "\n");// 软件版本号
			}
			fw.write("--------------------------------------------------------------------\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getVersionName(Context context) {
		String versionName = "1.0";
		try {
			PackageInfo e = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = e.versionName;
		} catch (NameNotFoundException var3) {
			var3.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取特定格式的时间
	 */
	private static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate) + "\t";
	}

	/**
	 * 向服务器发送日志
	 */
	private static void sendLogtoHost() {
		BufferedReader br = null;
		File file = new File(mLogFilePath);
		if (!file.exists()) {
			return;
		}
		try {
			br = new BufferedReader(new java.io.FileReader(file));
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				sb.append(line + SEPERATOR);
				line = br.readLine();
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 向服务器发送Zip包日志
	 */

}
