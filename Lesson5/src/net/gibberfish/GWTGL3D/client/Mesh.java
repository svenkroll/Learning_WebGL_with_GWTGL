package net.gibberfish.GWTGL3D.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class Mesh {

	private float[] vertices;
	private float[] colors;
	private float[] textureCoords;
	private int[] indices;
	private int vertexBufferSize;
    private int vertexBufferItems;
    private double rotation;
    private int colorBufferSize;
    private int colorBufferItems;
    private int indiceBufferSize;
    private int indiceBufferItems;
    private int textureBufferSize;
    private int textureBufferItems;
    private boolean initCompleted;
    
    private String textureURL;
    private boolean bTexture;
    final Image texture = new Image();
    
    public Mesh(){
    	rotation = 0;
    	initCompleted = false;
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
        
        colors = new float[]{
        		1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f
           };
        
        colorBufferItems = 3;
        colorBufferSize = 4;
        
        initCompleted = true;
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
        
    	colors = new float[]{
              // Front face
              1.0f, 0.0f, 0.0f, 1.0f,
              0.0f, 1.0f, 0.0f, 1.0f,
              0.0f, 0.0f, 1.0f, 1.0f,
              // Right face
              1.0f, 0.0f, 0.0f, 1.0f,
              0.0f, 0.0f, 1.0f, 1.0f,
              0.0f, 1.0f, 0.0f, 1.0f,
              // Back face
              1.0f, 0.0f, 0.0f, 1.0f,
              0.0f, 1.0f, 0.0f, 1.0f,
              0.0f, 0.0f, 1.0f, 1.0f,
              // Left face
              1.0f, 0.0f, 0.0f, 1.0f,
              0.0f, 0.0f, 1.0f, 1.0f,
              0.0f, 1.0f, 0.0f, 1.0f
    	};
    	
    	colorBufferItems = 12;
        colorBufferSize = 4;
    	
        initCompleted = true;

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
	
	public int getTextureSize(){
		return textureBufferSize;
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

	public void rotate(long elapsed) {
		rotation += (90 * elapsed) / 1000.0;
	}
	
	public void initJsonModel(String sPath) { 
		try { 
			RequestBuilder rb = new RequestBuilder( RequestBuilder.GET, sPath); 
			rb.setCallback(new RequestCallback() { 
				@Override 
				public void onResponseReceived(Request request, Response response) { 
					parseJsonData(response.getText()); 
				}
				@Override 
				public void onError(Request request, Throwable exception) {
					Window.alert("Error occurred" + exception.getMessage()); 
				} 
			}); 
			rb.send(); 
		} 
		catch (RequestException e) { 
			Window.alert("Error occurred" + e.getMessage()); 
		} 
	}	
	
	private void parseJsonData(String json) {

        JSONValue value = JSONParser.parseLenient(json);
        JSONObject modelObj = value.isObject();       
        
        vertexBufferItems = (int) modelObj.get("verticeItems").isNumber().doubleValue();
        vertexBufferSize = (int) modelObj.get("verticeSize").isNumber().doubleValue();
        JSONArray verticesArray = modelObj.get("vertices").isArray();
        vertices = new float[verticesArray.size()];
        for (int i=0;i<verticesArray.size();i++){
        	vertices[i] = Float.parseFloat(verticesArray.get(i).toString());
        }
        
        colorBufferItems = (int) modelObj.get("colorItems").isNumber().doubleValue();
        colorBufferSize = (int) modelObj.get("colorSize").isNumber().doubleValue();
        JSONArray colorsArray = modelObj.get("colors").isArray();
        colors = new float[colorsArray.size()];
        for (int i=0;i<colorsArray.size();i++){
        	colors[i] = Float.parseFloat(colorsArray.get(i).toString());
        }
        
        indiceBufferItems = (int) modelObj.get("indiceItems").isNumber().doubleValue();
        if (indiceBufferItems > 0){
        	indiceBufferSize = (int) modelObj.get("indiceSize").isNumber().doubleValue();
		    JSONArray indicesArray = modelObj.get("indices").isArray();
		    indices = new int[indicesArray.size()];
		    for (int i=0;i<indicesArray.size();i++){
		    	indices[i] = Integer.parseInt(indicesArray.get(i).toString());
		    }
        }
        
        if (modelObj.containsKey("texture")){
        	textureURL = modelObj.get("texture").isString().stringValue();
        	textureBufferItems = (int) modelObj.get("textureItems").isNumber().doubleValue();
        	textureBufferSize = (int) modelObj.get("textureSize").isNumber().doubleValue();
		    JSONArray coordsArray = modelObj.get("textureCoordinates").isArray();
		    textureCoords = new float[coordsArray.size()];
		    for (int i=0;i<coordsArray.size();i++){
		    	textureCoords[i] = Float.parseFloat(coordsArray.get(i).toString());
		    }
        	bTexture = true;
        	initTexture();
        }else{
        	bTexture = false;
        	initCompleted = true;
        }
    }

	public boolean initCompleted() {
		return initCompleted;
	}

	public int[] getIndices() {
		return indices;
	}

	public boolean hasIndices() {
		return indiceBufferItems > 0 ? true : false;
	}
	
	public boolean hasTexture(){
		return bTexture;
	}

	public int getIndiceBufferItems() {
		return indiceBufferItems;
	}
	
	private void initTexture() {
		texture.setVisible(false);
		RootPanel.get().add(texture);
		texture.setUrl(GWT.getHostPageBaseURL() + textureURL);
	    
		texture.addLoadHandler(new LoadHandler() {
	    	@Override
			public void onLoad(LoadEvent event) {
	    		RootPanel.get().remove(texture);
				GWT.log("texture image loaded", null);
	    		initCompleted = true;
			}
	    });
	}

	public Image getTextureImage() {
		return texture;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}
		
}

