package net.gibberfish.GWTGL3D.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

public final class GLMatrixHelper {
	
	public static JsArrayNumber wrapArray(float[] srcArray) {
	    JsArrayNumber result = JavaScriptObject.createArray().cast();
	    for (int i = 0; i < srcArray.length; i++) {
	      result.set(i, srcArray[i]);
	    }
	    return result;
	  }
	public static float[] unwrapArray(JsArrayNumber jsArray) {
	    float[] result = new float[jsArray.length()];
	    for(int i=0; i<jsArray.length();i++) {
	      result[i]=(float) jsArray.get(i);
	    }
	    return result;
	  }

}
