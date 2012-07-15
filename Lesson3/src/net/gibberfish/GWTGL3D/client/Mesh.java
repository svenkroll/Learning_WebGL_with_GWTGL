package net.gibberfish.GWTGL3D.client;

public class Mesh {

	private float[] vertices;
	private float[] colors;
	private int vertexBufferSize;
    private int vertexBufferItems;
    private int colorBufferSize;
    private int colorBufferItems;
    private double rotation;
    
    public Mesh(){
    	rotation = 0;
    }
    
    public void initTriangle(){
   
        vertices = new float[]{
             0.0f,  1.0f,  -5.0f, // first vertex
            -1.0f, -1.0f,  -5.0f, // second vertex
             1.0f, -1.0f,  -5.0f  // third vertex
        };
        
        vertexBufferSize = 3;
        vertexBufferItems = 3;
        
        colors = new float[]{
        		1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f
           };
        colorBufferItems = 3;
        colorBufferSize = 4;
    }

	public float[] getVertices() {
		return vertices;
	}
	
	public float[] getColors(){
		return colors;
	}

	public int getColorBufferSize() {
		return colorBufferSize;
	}

	public int getColorBufferItems() {
		return colorBufferItems;
	}

	public int getVertexBufferSize() {
		return vertexBufferSize;
	}

	public int getVertexBufferItems() {
		return vertexBufferItems;
	}
	
	public double getRotationDegree() {
		return rotation;
	}
	
	public double getRotationRad() {
		return rotation * Math.PI / 180;
	}
	
	public void rotate(long elapsed) {
		rotation += (90 * elapsed) / 1000.0;
	}
	
}
