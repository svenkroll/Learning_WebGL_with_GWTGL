package net.gibberfish.GWTGL3D.client;

import com.google.gwt.core.client.JsArrayNumber;

public final class GLMatrixMat4{

	private float[] mat4;
	private String name;
	
	public String getName() {
		return name;
	}

	public float[] getMat4() {
		return mat4;
	}

	/**
	 * protected standard constructor as specified by
	 * {@link com.google.gwt.core.client.JavaScriptObject}.
	 */
	public GLMatrixMat4(String Name) {
		name = Name;
	}
	
	public void create(){
		mat4 = unwrapArray(GLMatrix.mat4Create(name));			
	}
	
	private float[] unwrapArray(JsArrayNumber jsArray) {
	    float[] result = new float[jsArray.length()];
	    for(int i=0; i<jsArray.length();i++) {
	      result[i]=(float) jsArray.get(i);
	    }
	    return result;
	  }

	public void update(JsArrayNumber mat4Perspective) {
		mat4 = unwrapArray(mat4Perspective);
	}

	public void clone(GLMatrixMat4 mvMatrix) {
		this.mat4 = mvMatrix.getMat4();
	}
}   
