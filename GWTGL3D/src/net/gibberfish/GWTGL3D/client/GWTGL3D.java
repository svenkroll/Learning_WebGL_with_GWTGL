package net.gibberfish.GWTGL3D.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwtgl.array.Float32Array;
import com.googlecode.gwtgl.binding.WebGLBuffer;
import com.googlecode.gwtgl.binding.WebGLProgram;
import com.googlecode.gwtgl.binding.WebGLRenderingContext;
import com.googlecode.gwtgl.binding.WebGLShader;
import com.googlecode.gwtgl.binding.WebGLUniformLocation;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTGL3D implements EntryPoint {
	

    private WebGLRenderingContext glContext;
    private WebGLProgram shaderProgram;
    private int vertexPositionAttribute;
    private WebGLBuffer vertexBuffer;
    private WebGLUniformLocation mvMatrixUniform;
    private WebGLUniformLocation pMatrixUniform;
    private int vertexBufferSize;
    private int vertexBufferItems;
    private GLMatrixMat4 mvMatrix;
    private GLMatrixMat4 pMatrix;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Canvas webGLCanvas = Canvas.createIfSupported();
        webGLCanvas.setCoordinateSpaceHeight(500);
        webGLCanvas.setCoordinateSpaceWidth(500);
        glContext = (WebGLRenderingContext) webGLCanvas.getContext("experimental-webgl");
        if(glContext == null) {
                Window.alert("Sorry, Your Browser doesn't support WebGL!");
        }
        glContext.viewport(0, 0, 500, 500);
        
        RootPanel.get("gwtGL").add(webGLCanvas);
        start();
		
	}
		
	private void start() {
		
        initShaders();
       
        mvMatrix = new GLMatrixMat4("mvMatrix");
        mvMatrix.create();
        
        pMatrix = new GLMatrixMat4("pMatrix");
        pMatrix.create();
        
        initBuffers();

        glContext.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glContext.enable(WebGLRenderingContext.DEPTH_TEST);

        drawScene();
	}
	
	public void initShaders() {
        WebGLShader fragmentShader = getShader(WebGLRenderingContext.FRAGMENT_SHADER, Shaders.INSTANCE.fragmentShader().getText());
        WebGLShader vertexShader = getShader(WebGLRenderingContext.VERTEX_SHADER, Shaders.INSTANCE.vertexShader().getText());

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

        pMatrixUniform = glContext.getUniformLocation(shaderProgram, "uPMatrix");
        mvMatrixUniform = glContext.getUniformLocation(shaderProgram, "uMVMatrix");
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
        vertexBuffer = glContext.createBuffer();
        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
        float[] vertices = new float[]{
                         0.0f,  1.0f,  -5.0f, // first vertex
                        -1.0f, -1.0f,  -5.0f, // second vertex
                         1.0f, -1.0f,  -5.0f  // third vertex
        };
        
        glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array.create(vertices), WebGLRenderingContext.STATIC_DRAW);
        vertexBufferSize = 3;
        vertexBufferItems = 3;
	}
	
	private void drawScene() {
		
		glContext.viewport(0, 0, 500, 500);
		glContext.clear(WebGLRenderingContext.COLOR_BUFFER_BIT | WebGLRenderingContext.DEPTH_BUFFER_BIT);

		pMatrix.update(GLMatrix.mat4Perspective(45, 500, 500, 0.1, 100.0, pMatrix.getName()));

		mvMatrix.update(GLMatrix.mat4Identity(mvMatrix.getName()));

		mvMatrix.update(GLMatrix.mat4Translate(mvMatrix.getName(), -1.5f, 0.0f, -7.0f));
        
        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
        glContext.vertexAttribPointer(vertexPositionAttribute, vertexBufferSize, WebGLRenderingContext.FLOAT, false, 0, 0);
        
        glContext.uniformMatrix4fv(pMatrixUniform, false, pMatrix.getMat4());
        glContext.uniformMatrix4fv(mvMatrixUniform, false, mvMatrix.getMat4());
        
        glContext.drawArrays(WebGLRenderingContext.TRIANGLES, 0, vertexBufferItems);
	}
	
}
