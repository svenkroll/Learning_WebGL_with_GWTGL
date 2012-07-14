package net.gibberfish.GWTGL3D.client;

public class Mesh {

	private float[] vertices;
	private float[] colors;
	private int vertexBufferSize;
    private int vertexBufferItems;
    private int colorBufferSize;
    private int colorBufferItems;
    
    public Mesh(){
    	
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
}
