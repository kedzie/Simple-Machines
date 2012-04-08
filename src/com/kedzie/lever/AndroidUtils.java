package com.kedzie.lever;

import rajawali.BaseObject3D;
import rajawali.materials.TextureManager;
import rajawali.parser.ObjParser;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Android Utility functions
 * 
 * @author Marek Kedzierski
 */
public class AndroidUtils {

	/**
	 * Load a mesh object from resource
	 * @param id  resource id of obj file
	 * @return Parsed {@link BaseObject3D}
	 */
	public static  BaseObject3D loadObject(Resources resources, TextureManager textureManager, int id) {
		ObjParser objParser = new ObjParser(resources, textureManager, id);
		objParser.parse();
		return  objParser.getParsedObject();
	}
	
	/**
	 * Show {@link Toast} notification
	 * 
	 * @param ctx message {@link Context}
	 * @param msg Message to show
	 */
	public static void toast(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Is string null of empty?
	 * @param s the string
	 * @return true of null or empty string
	 */
	public static boolean isNullString(String s) {
		return s==null || s.equals("");
	}
	
	/**
	 * Is array null or empty?
	 * @param array the array
	 * @return true of null of empty
	 */
	public static boolean isNullArray(Object []array) {
		return array==null || array.length==0;
	}
}
