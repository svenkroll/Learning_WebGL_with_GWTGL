package net.gibberfish.GWTGL3D.client;

import java.util.EmptyStackException;
import java.util.Stack;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
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

    private GLMatrixMat4 mvMatrix;
    private GLMatrixMat4 pMatrix;
    private Mesh mesh;
    private Timer loopTimer;
    long lastTime, timeNow;
    private Stack<GLMatrixMat4> mvMatrixStack;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		initGL();
		initShaders();
		initMatrices();
        initMesh();
        initBuffers();

        glContext.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glContext.enable(WebGLRenderingContext.DEPTH_TEST);

        lastTime = 0;

        loopTimer = new Timer() {
			@Override
			public void run() {
				loop();
			}
		};
	
		loopTimer.scheduleRepeating(10);
		
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
		mesh.initPyramid();
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
        glContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array.create(mesh.getVertices()), WebGLRenderingContext.STATIC_DRAW);
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
    
	private void drawScene() {
		
		glContext.viewport(0, 0, 500, 500);
		glContext.clear(WebGLRenderingContext.COLOR_BUFFER_BIT | WebGLRenderingContext.DEPTH_BUFFER_BIT);

		GLMatrix.mat4Perspective(45, 500, 500, 0.1, 100.0, pMatrix);

		GLMatrix.mat4Identity(mvMatrix);
		
		mvPushMatrix();
		GLMatrix.mat4Translate(mvMatrix, -1.5f, 0.0f, -7.0f);
		GLMatrix.mat4Rotate(mvMatrix, mesh.getRotationRad(), 0f, 1f, 0f);
        
        glContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertexBuffer);
        glContext.vertexAttribPointer(vertexPositionAttribute, mesh.getVertexBufferSize(), WebGLRenderingContext.FLOAT, false, 0, 0);
        
        glContext.uniformMatrix4fv(pMatrixUniform, false, pMatrix.getMat4());
        glContext.uniformMatrix4fv(mvMatrixUniform, false, mvMatrix.getMat4());
        
        glContext.drawArrays(WebGLRenderingContext.TRIANGLES, 0, mesh.getVertexBufferItems());
        mvPopMatrix();
	}
	
}
