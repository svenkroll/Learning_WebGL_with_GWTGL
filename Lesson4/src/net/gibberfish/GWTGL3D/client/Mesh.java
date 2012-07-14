package net.gibberfish.GWTGL3D.client;

public class Mesh {

	private float[] vertices;
	private int vertexBufferSize;
    private int vertexBufferItems;
    private double rotation;
    
    public Mesh(){
    	rotation = 0;
    }
    
    public double getRotationDegree() {
		return rotation;
	}
    
    public double getRotationRad() {
		return rotation * Math.PI / 180;
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

    public void initPyramid(){
    	vertices = new float[]{
            // Front face
             0.0f,  1.0f,  0.0f,
            -1.0f, -1.0f,  1.0f,
             1.0f, -1.0f,  1.0f,

            // Right face
             0.0f,  1.0f,  0.0f,
             1.0f, -1.0f,  1.0f,
             1.0f, -1.0f, -1.0f,

            // Back face
             0.0f,  1.0f,  0.0f,
             1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,

            // Left face
             0.0f,  1.0f,  0.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f
    	};
    	
    	vertexBufferSize = 3;
        vertexBufferItems = 12;
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

	public void rotate(long elapsed) {
		rotation += (90 * elapsed) / 1000.0;
	}
}
