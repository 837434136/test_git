package com.cspebank.www.app;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

import com.cspebank.www.db.CspeDB;
import com.cspebank.www.utils.LogTrace;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.litepal.LitePalApplication;

import java.io.File;

/**
 * @author yisinian.deng
 * 2015.11.18
 */
public class CspeApplication extends LitePalApplication{

	private static CspeApplication mContext;
	/**developer mode*/
    public static final boolean DEVELOPER_MODE = true;
    /**window manager arguments*/
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
    private CspeManager mXmManager = new CspeManager();
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	mContext = this;
    	
    	CspeManager.isProcessCreFromKill = true;
    	//初始化Litepal
    	LitePalApplication.initialize(mContext);
        initImageLoader(mContext);

        LogTrace.i("----------> onCreate");
    }
    
	public static CspeApplication getInstance(){
    	return mContext;
    }

    @Override
    public void onLowMemory() {
    	super.onLowMemory();
    }
    
    @Override
    public void onTerminate() {
    	//进程被杀死的时候（不一定）回调，注意自己手动杀死是不会调用的
    	super.onTerminate();
    }
    
    /**
     * exit app
     */
    public void exitApp(){
    	CspeDB.getInstance().closeDB();
    	mXmManager.clear();
        //退出程序之后改变状态
    	mXmManager.isProcessCreFromKill = false;
        //Indicates to the VM that it would be a good time to run the garbage collector.
//        System.gc();
        //销毁静态变量
        System.exit(0);
        //kill the process with the given PID.
//        android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        File cacheDir;
        File sdCardDr = Environment.getExternalStorageDirectory();
        String satus = Environment.getExternalStorageState();
        cacheDir = new File(sdCardDr.getAbsolutePath(), "/News/Images/");
        if (!cacheDir.exists()) {
        	try {
        		cacheDir.createNewFile();
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
        Log.e("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .memoryCacheExtraOptions(480, 800)
                        // Can slow ImageLoader, use it carefully (Better don't use it)
//				 .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
                        // 线程池内加载的数量
//                .memoryCache(new LruMemoryCache(2*1024*1024))
                .threadPoolSize(3)
                        // 线程优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
				/*
				 * When you display an image in a small ImageView
				 *  and later you try to display this image (from identical URI) in a larger ImageView
				 *  so decoded image of bigger size will be cached in memory as a previous decoded image of smaller size.
				 *  So the default behavior is to allow to cache multiple sizes of one image in memory.
				 *  You can deny it by calling this method:
				 *  so when some image will be cached in memory then previous cached size of this image (if it exists)
				 *   will be removed from memory cache before.
				 */
//				.denyCacheImageMultipleSizesInMemory()

                        // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
                        // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                        // .memoryCacheSize(2 * 1024 * 1024)
                        //硬盘缓存50MB
                .diskCacheSize(50 * 1024 * 1024)
                        //将保存的时候的URI名称用MD5
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 加密
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .diskCacheFileCount(100) //缓存的File数量 与diskCacheSize()方法彼此功能重叠了。
                .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
                        // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        // .imageDownloader(new BaseImageDownloader(context, 5 * 1000,
                        // 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }
    
}
