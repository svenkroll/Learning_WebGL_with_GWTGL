package net.gibberfish.GWTGL3D.client;

public class Mesh {

	private float[] vertices;
	private int vertexBufferSize;
    private int vertexBufferItems;
    
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
    }

	public float[] getVertices() {
		return vertices;
	}

	public int getVertexBufferSize() {
		return vertexBufferSize;
	}

	public int getVertexBufferItems() {
		return vertexBufferItems;
	}
}
