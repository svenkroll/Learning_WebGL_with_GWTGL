package net.gibberfish.GWTGL3D.client;

public final class GLMatrixMat4{

	private float[] mat4;
	private String name;
	
	/**
	 * protected standard constructor as specified by
	 * {@link com.google.gwt.core.client.JavaScriptObject}.
	 */
	public GLMatrixMat4(String Name) {
		name = Name;
		mat4 = GLMatrix.mat4Create(name);
	}
	
	public void update(float[] Mat4) {
		mat4 = Mat4;
	}
	
	public String getName() {
		return name;
	}

	public float[] getMat4() {
		return mat4;
	}
}   
