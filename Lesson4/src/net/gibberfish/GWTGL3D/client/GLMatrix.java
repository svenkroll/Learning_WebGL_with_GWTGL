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
	
	public static native JsArrayNumber mat4Create(String name) /*-{
		$wnd[name] = $wnd.mat4.create();
		return $wnd[name];
	}-*/;
	
	public static native JsArrayNumber mat4Perspective(int fieldOfView, int width, int height, double near, double far, String matrix) /*-{
		$wnd.mat4.perspective(fieldOfView, width / height, near, far, $wnd[matrix]);
		return $wnd[matrix];
	}-*/;
	
	public static native JsArrayNumber mat4Identity(String matrix) /*-{
		$wnd.mat4.identity($wnd[matrix]);
		return $wnd[matrix];
	}-*/;
	
	public static native JsArrayNumber mat4Translate(String matrix, float x, float y, float z) /*-{
		$wnd.mat4.translate($wnd[matrix], [x, y, z]);
		return $wnd[matrix];
	}-*/;
	
	public static native JsArrayNumber mat4Rotate(String matrix, double degree, float x, float y, float z) /*-{
		$wnd.mat4.rotate($wnd[matrix], degree, [x, y, z]);
		return $wnd[matrix];
	}-*/;
	
}
