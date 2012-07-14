package net.gibberfish.GWTGL3D.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

public final class GLMatrix extends JavaScriptObject{

	/**
	 * protected standard constructor as specified by
	 * {@link com.google.gwt.core.client.JavaScriptObject}.
	 */
	protected GLMatrix() {
	}
	
	/*
	 * 
	 */
	public static float[] mat4Create(String matrix){
		JsArrayNumber js = i_mat4Create(matrix);
		return GLMatrixHelper.unwrapArray(js);
	}
	
	public static native JsArrayNumber i_mat4Create(String name) /*-{
		$wnd[name] = $wnd.mat4.create();
		return $wnd[name];
	}-*/;
	
	/*
	 * 
	 */
	public static void mat4Perspective(int fieldOfView, int width, int height, double near, double far, GLMatrixMat4 matrix){
		JsArrayNumber js = i_mat4Perspective(fieldOfView,  width,  height,  near,  far,  matrix.getName());
		matrix.update(GLMatrixHelper.unwrapArray(js));
	}
	
	private static native JsArrayNumber i_mat4Perspective(int fieldOfView, int width, int height, double near, double far, String matrix) /*-{
		$wnd.mat4.perspective(fieldOfView, width / height, near, far, $wnd[matrix]);
		return $wnd[matrix];
	}-*/;
	
	/*
	 * 
	 */
	public static void mat4Identity(GLMatrixMat4 matrix){
		JsArrayNumber js = GLMatrixHelper.wrapArray(matrix.getMat4());
		js = i_mat4Identity(js, matrix.getName());
		matrix.update(GLMatrixHelper.unwrapArray(js));
	}
	
	private static native JsArrayNumber i_mat4Identity(JsArrayNumber js, String matrix) /*-{
		$wnd[matrix] = js;
		$wnd.mat4.identity($wnd[matrix]);
		return $wnd[matrix];
	}-*/;
	
	/*
	 * 
	 */
	public static void mat4Translate(GLMatrixMat4 matrix, float x, float y, float z) {
		JsArrayNumber js = GLMatrixHelper.wrapArray(matrix.getMat4());
		js = i_mat4Translate(js, matrix.getName(), x, y, z);
		matrix.update(GLMatrixHelper.unwrapArray(js));
	}
	
	private static native JsArrayNumber i_mat4Translate(JsArrayNumber js, String matrix, float x, float y, float z) /*-{
		$wnd[matrix] = js;
		$wnd.mat4.translate($wnd[matrix], [x, y, z]);
		return $wnd[matrix];
	}-*/;
	
}
