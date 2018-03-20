package com.ltf.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    private static final int DEFAULT_QUALITY = 97;
    /** 最大允许5M的路径 */
    private static final long MAX_FILE_LENGTH = 800 * 1024;
    
    private static final int DEFAULT_WIDTH=480;
    private static final int DEFAULT_HEIGHT=480;
    

    /**
     * 图片小于<=5M 返回true ，压缩至5M内返回true ，否则返回false
     */
    public static boolean compressImage(File imageFile, String suffix, File temp) {
        long fileSize = -1;
        if (TextUtils.isEmpty(suffix) || imageFile == null || !imageFile.exists()
                || (fileSize = imageFile.length()) == 0) {
            return false;
        }

        if (fileSize >= MAX_FILE_LENGTH) {
            if ("bmp".equals(suffix) || "gif".equals(suffix)) {
                // bmp || gif的 暂时无法压缩//TODO
                return false;
            }
        } else {// 小于 @param MAX_FILE_LENGTH 直接返回
            return true;
        }

        CompressFormat format = null;
        if ("png".equals(suffix)) {
            format = CompressFormat.PNG;
        } else {
            format = CompressFormat.JPEG;
        }

        FileOutputStream fos = null;
        try {
            File compressImageDisplay = compressImageDisplay(format, imageFile, temp);
            if (compressImageDisplay != null) {
                return true;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
        return false;
    }

    /**
     * 压缩图片到5M以内
     * 
     * @param format
     * @param imageFile
     * @return
     */
    private static File compressImageDisplay(CompressFormat format, File imageFile, File temp) {
        try {
            long fileLength = imageFile.length();
            if (MAX_FILE_LENGTH >= fileLength) {
                return imageFile;
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                int scale = (int) Math.max(2, fileLength / MAX_FILE_LENGTH);// 至少压缩2倍防止OOM
                options.inSampleSize = scale;
                Bitmap compressBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(temp);
                    compressBitmap.compress(format, DEFAULT_QUALITY, fos);
                    compressBitmap.recycle();
                    return compressImageDisplay(format, temp, temp);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IllegalStateException("compressing image error");
                } finally {
                    close(fos);
                }
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据图片文件进行旋转角度调整
     * 
     * @param filePath
     * @return
     */
    public static float rotateDegree(String filePath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        float degree = 0F;
        if (exif != null) {
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                case ExifInterface.ORIENTATION_ROTATE_90 :
                    degree = 90F;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180 :
                    degree = 180F;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270 :
                    degree = 270F;
                    break;
                default :
                    break;
            }
        }
        return degree;
    }

    /**
     * 压缩图片到文件
     * 
     * @param bitmap
     *            内存中的图片
     * @param filePath
     *            要保存的路径
     * @return true返回保存的路径 false返回null
     */
    public static String compressBitmap2File(Bitmap bitmap, String filePath) {
        return compressBitmap2File(bitmap, filePath, DEFAULT_QUALITY);
    }

    /**
     * 压缩图片到文件
     * 
     * @param bitmap
     *            内存中的图片
     * @param filePath
     *            要保存的路径
     * @param quality
     *            保存图片质量
     * @return true返回保存的路径 false返回null
     */
    public static String compressBitmap2File(Bitmap bitmap, String filePath, int quality) {
        if (bitmap == null || TextUtils.isEmpty(filePath)) {
            return null;
        }
        BufferedOutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(filePath), 4 * 1024);
            boolean compress = bitmap.compress(CompressFormat.JPEG, quality, fos);
            return compress ? filePath : null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
        return null;
    }

    /*
     * 按指定高*宽缩小图片
     * 
     */
    public static boolean compressBitmap2File(String sSrcFile, String sDestFile, int width, int height, int quality)
    {	
    	boolean bResult=false;
    	try {    	
    		
	    	Bitmap resizeBmp= BitmapFactory.decodeFile(sSrcFile);
	    	if(resizeBmp==null) return bResult;
	    	
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        resizeBmp.compress(CompressFormat.JPEG, 100, baos);
	        BitmapFactory.Options opts = new BitmapFactory.Options();
	        opts.inJustDecodeBounds = true;  
	        resizeBmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
	                baos.toByteArray().length, opts);  
	        // 得到图片的宽度、高度；  
	        int imgWidth = opts.outWidth;  
	        int imgHeight = opts.outHeight;  
	        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数；  
	        int widthRatio = (int) Math.ceil(imgWidth / (float) width);
	        int heightRatio = (int) Math.ceil(imgHeight / (float) height);
	        if (widthRatio > 1 && widthRatio > 1) {  
	            if (widthRatio > heightRatio) {  
	                opts.inSampleSize = widthRatio;  
	            } else {  
	                opts.inSampleSize = heightRatio;  
	            }  
	        }  
	        // 设置好缩放比例后，加载图片进内存；  
	        opts.inJustDecodeBounds = false;  
	        resizeBmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
	                baos.toByteArray().length, opts);   	

	    	//Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(bmp, width, height);  
	    	
	        File saveFile=new File(sDestFile);

	
			if(!saveFile.exists())
			{
				saveFile.createNewFile();
			}
		    FileOutputStream fileOutputStream=new FileOutputStream(saveFile);
		    if (fileOutputStream!=null) {
		      //imageBitmap.compress(format, quality, stream);
		  //把位图的压缩信息写入到一个指定的输出流中
		  //第一个参数format为压缩的格式
		  //第二个参数quality为图像压缩比的值,0-100.0 意味着小尺寸压缩,100意味着高质量压缩
		  //第三个参数stream为输出流
		    	resizeBmp.compress(CompressFormat.JPEG, quality, fileOutputStream);
		    }
		    fileOutputStream.flush();
		    fileOutputStream.close();
		    bResult=true;
		} catch (IOException e) {
		    e.printStackTrace();
		}

    	return bResult;
    }
    public static boolean compressBitmap2File(String sSrcFile, String sDestFile)
    {
    	return compressBitmap2File(sSrcFile,sDestFile,DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_QUALITY);
    }

    private static final void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
