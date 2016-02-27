package com.xn.alex.data.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;

public class ImageUtility {

	public static Image createTransparencyImage(Image srcImage,int x,int y){
		int w = srcImage .getWidth(null);
		int h = srcImage.getHeight(null);
		BufferedImage image = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) image.createGraphics();
		g2.drawImage(srcImage, 0, 0,w,h,null);
		g2.dispose();
	
		if(x != -1 && y != -1){
			DirectColorModel cm = (DirectColorModel)image.getColorModel().getRGBdefault();
			int colorKey = image.getRGB(x, y);
			int r = cm.getRed(colorKey);
			int g = cm.getGreen(colorKey);
			int b = cm.getBlue(colorKey);
			for(int i = 0; i < image.getWidth(); i++){
				for(int j = 0; j <image.getHeight(); j++){
					int rgb = image.getRGB(i, j);
					int alpha = cm.getAlpha(rgb);
					int red = cm.getRed(rgb);
					int green = cm.getGreen(rgb);
					int blue = cm.getBlue(rgb);
					if(r == red && g == green && b == blue){
						alpha = 0;
					}
					image.setRGB(i, j, alpha<<24|red<<16|green<<8|blue);
				}
			}	
		}
		return image;
	}
	
	public static Image createTransparencyImage(int width, int height){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    	Graphics2D g2d = image.createGraphics();
    	image = g2d.getDeviceConfiguration().createCompatibleImage(width, height,Transparency.TRANSLUCENT);
    	g2d.dispose();
    	return image;
	}
	
	public static Image drawImage(Image srcImage,Image dstImage,int x,int y,int w,int h){
		Graphics2D g2 = (Graphics2D) dstImage.getGraphics();
		g2.drawImage(srcImage,x,y,w,h,null);
		
		return dstImage;
	}
	
	public static Image drawImage(Image srcImage,Image dstImage,int x,int y){
		Graphics2D g2 = (Graphics2D) dstImage.getGraphics();
		g2.drawImage(srcImage,x,y,srcImage.getWidth(null),srcImage.getHeight(null),null);
		
		return dstImage;
	}
	
	public static Image cloneImage(Image srcImage){
		Image image = createTransparencyImage(srcImage.getWidth(null), srcImage.getHeight(null));
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.drawImage(srcImage,0,0,srcImage.getWidth(null),srcImage.getHeight(null),null);
		return image;
	}
}
