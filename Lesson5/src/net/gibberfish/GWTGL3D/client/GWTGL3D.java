package net.gibberfish.GWTGL3D.client;

import java.util.EmptyStackException;
import java.util.Stack;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwtgl.array.Float32Array;
import com.googlecode.gwtgl.array.Uint16Array;
import com.googlecode.gwtgl.binding.WebGLBuffer;
import com.googlecode.gwtgl.binding.WebGLProgram;
import com.googlecode.gwtgl.binding.WebGLRenderingContext;
import com.googlecode.gwtgl.binding.WebGLShader;
import com.googlecode.gwtgl.binding.WebGLTexture;
import com.googlecode.gwtgl.binding.WebGLUniformLocation;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTGL3D implements EntryPoint {
	

    private WebGLRenderingContext glContext;
    private WebGLProgram shaderProgram;
    private int vertexPositionAttribute;
    private int vertexColorAttribute;
    private int textureCoordAttribute;
    private WebGLBuffer vertexBuffer;
    private WebGLBuffer vertexColorBuffer;
    private WebGLBuffer vertexIndexBuffer;
    private WebGLBuffer vertexTextureCoordBuffer;
    private WebGLUniformLocation mvMatrixUniform;
    private WebGLUniformLocation pMatrixUniform;
    private WebGLUniformLocation samplerUniform;

    private GLMatrixMat4 mvMatrix;
    private GLMatrixMat4 pMatrix;
    private Mesh mesh;
    private Timer loopTimer, loadTimer;
    private long lastTime, timeNow;
    private Stack<GLMatrixMat4> mvMatrixStack;
    
	private WebGLTexture texture;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		initGL();
		initShaders();
		initMatrices();
		
		glContext.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
	    glContext.enable(WebGLRenderingContext.DEPTH_TEST);

	    lastTime = 0;

	    loopTimer = new Timer() {
			@Override
			public void run() {
				loop();
			}
		};
        initMesh();
	}
	
	private void initMatrices() {
		mvMatrix = new GLMatrixMat4("mvMatrix");
        pMatrix = new GLMatrixMat4("pMatrix");  
        
        mvMatrixStack = new Stack<GLMatrixMat4>(); 
	}

	private void loop() {
		drawScene();
		animate();
	}

	private void animate() {
		timeNow = System.currentTimeMillis();
        if (lastTime != 0) {
            long elapsed = timeNow - lastTime;
            mesh.rotate(elapsed);
        }
        lastTime = timeNow;		
	}

	private void initMesh() {
		mesh = new Mesh();	
		//mesh.initTriangle();
		//mesh.initPyramid();
		//mesh.initJsonModel("/pyramid.json");
		mesh.initJsonModel("/cube.json");
		
		loadTimer = new Timer() {
			@Override
			public void run() {
				initBuffers();
			}
		};
		
		loadTimer.scheduleRepeating(100);
	}

	private void initGL(){
		final Canvas webGLCanvas = Canvas.createIfSupported();
        webGLCanvas.setCoordinateSpaceHeight(500);
        webGLCanvas.setCoordinateSpaceWidth(500);
        glContext = (WebGLRenderingContext) webGLCanvas.getContext("experimental-webgl");
        if(glContext == null) {
                Window.alert("Sorry, Your Browser doesn't support WebGL!");
        }
        glContext.viewport(0, 0, 500, 500);
        
        RootPanel.get("gwtGL").add(webGLCanvas);
	}
	
	public void initShaders() {
        //WebGLShader fragmentShader = getShader(WebGLRenderingContext.FRAGMENT_SHADER, Shaders.INSTANCE.fragmentShader().getText());
        //WebGLShader vertexShader = getShader(WebGLRenderingContext.VERTEX_SHADER, Shaders.INSTANCE.vertexShader().getText());

        WebGLShader fragmentShader = getShader(WebGLRenderingContext.FRAGMENT_SHADER, Shaders.INSTANCE.fragmentTextureShader().getText());
        WebGLShader vertexShader = getShader(WebGLRenderingContext.VERTEX_SHADER, Shaders.INSTANCE.vertexTextureShader().getText());
        
        shaderProgram = glContext.createProgram();
        glContext.attachShader(shaderProgram, vertexShader);
        glContext.attachShader(shaderProgram, fragmentShader);
        glContext.linkProgram(shaderProgram);

        if (!glContext.getProgramParameterb(shaderProgram, WebGLRenderingContext.LINK_STATUS)) {
                throw new RuntimeException("Could not initialise shaders");
        }

        glContext.useProgram(shaderProgram);

        vertexPositionAttribute = glContext.getAttribLocation(shaderProgram, "aVertexPosition");
        glContext.enableVertexAttribArray(vertexPositionAttribute);

        //vertexColorAttribute = glContext.getAttribLocation(shaderProgram, "aVertexColor");
        //glContext.enableVertexAttribArray(vertexColorAttribute);
        
        textureCoordAttribute = glContext.getAttribLocation(shaderProgram, "aTextureCoord");
        glContext.enableVertexAttribArray(textureCoordAttribute);
        
        pMatrixUniform = glContext.getUniformLocation(shaderProgram, "uPMatrix");
        mvMatrixUniform = glContext.getUniformLocation(shaderProgram, "uMVMatrix");
        samplerUniform = glContext.getUniformLocation(shaderProgram, "uSampler");
	}
	
	private WebGLShader getShader(int type, String source) {
        WebGLShader shader = glContext.createShader(type);

        glContext.shaderSource(shader, source);
        glContext.compileShader(shader);

        if (!glContext.getShaderParameterb(shader, WebGLRenderingContext.COMPILE_STATUS)) {
                throw new RuntimeException(glContext.getShaderInfoLog(shader));
        }

        return shader;
	}
	
	private void initBuffers() {
		if (mesh.initCompleted()){
			loadTimer.cancel();
			
			if (mesh.hasTexture()){
				vertexTextureCoordBuffer = glContext.createBuffer();
		        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexTextureCoordBuffer);
		        glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array.create(mesh.getTextureCoords()), WebGLRenderingContext.STATIC_DRAW);
		        
				texture = glContext.createTexture();
				glContext.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
			    glContext.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1);
			    glContext.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA, WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, mesh.getTextureImage().getElement());
			    glContext.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER, WebGLRenderingContext.NEAREST);
			    glContext.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER, WebGLRenderingContext.NEAREST);
			    glContext.bindTexture(WebGLRenderingContext.TEXTURE_2D, null);
	        }else{
	        	vertexColorBuffer = glContext.createBuffer();
	 	        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexColorBuffer);
	 	        glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array.create(mesh.getColors()), WebGLRenderingContext.STATIC_DRAW);
	 	        
	        }

	        vertexBuffer = glContext.createBuffer();
	        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
	        glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array.create(mesh.getVertices()), WebGLRenderingContext.STATIC_DRAW);
	       
	        if (mesh.hasIndices()){
	        	vertexIndexBuffer = glContext.createBuffer();
	        	glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, vertexIndexBuffer);
	        	glContext.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, Uint16Array.create(mesh.getIndices()), WebGLRenderingContext.STATIC_DRAW);
	        }

	        loopTimer.scheduleRepeating(10);
		}
	}
	
	private void mvPushMatrix() {
		GLMatrixMat4 copy = new GLMatrixMat4("mvMatrix");
        copy.clone(mvMatrix);
        mvMatrixStack.push(copy);
    }

    private void mvPopMatrix() {
        if (mvMatrixStack.empty()) {
        	throw new EmptyStackException();
        }
        mvMatrix = mvMatrixStack.pop();
    }
    
    private void setMatrixUniforms(){
    	glContext.uniformMatrix4fv(pMatrixUniform, false, pMatrix.getMat4());
        glContext.uniformMatrix4fv(mvMatrixUniform, false, mvMatrix.getMat4());
    }
    
	private void drawScene() {
		glContext.viewport(0, 0, 500, 500);
		glContext.clear(WebGLRenderingContext.COLOR_BUFFER_BIT | WebGLRenderingContext.DEPTH_BUFFER_BIT);

		GLMatrix.mat4Perspective(45, 500, 500, 0.1, 100.0, pMatrix);
		GLMatrix.mat4Identity(mvMatrix);
		
		mvPushMatrix();
		
		GLMatrix.mat4Translate(mvMatrix, 0f, 0.0f, -7.0f);
		GLMatrix.mat4Rotate(mvMatrix, mesh.getRotationRad(), 1f, 1f, 1f);
        
        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
        glContext.vertexAttribPointer(vertexPositionAttribute, mesh.getVertexBufferSize(), WebGLRenderingContext.FLOAT, false, 0, 0);
        
        if(!mesh.hasTexture()){
	        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexColorBuffer);
	        glContext.vertexAttribPointer(vertexColorAttribute, mesh.getColorBufferSize(), WebGLRenderingContext.FLOAT, false, 0, 0);
        }else{
	        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexTextureCoordBuffer);
	        glContext.vertexAttribPointer(textureCoordAttribute, mesh.getTextureSize(), WebGLRenderingContext.FLOAT, false, 0, 0);
	
	        glContext.activeTexture(WebGLRenderingContext.TEXTURE0);
	        glContext.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
	        glContext.uniform1i(samplerUniform, 0);
        }
        
        if (mesh.hasIndices()){
        	glContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, vertexIndexBuffer);
	        setMatrixUniforms();
	        glContext.drawElements(WebGLRenderingContext.TRIANGLES, mesh.getIndiceBufferItems(), WebGLRenderingContext.UNSIGNED_SHORT, 0);
        }else{
        	setMatrixUniforms();
        	glContext.drawArrays(WebGLRenderingContext.TRIANGLES, 0, mesh.getVertexBufferItems());
        }
        mvPopMatrix();
	}
	
}
