package old;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

public class LeverView extends GLSurfaceView implements GLSurfaceView.Renderer {
	public static final int LEVER_MASS = 20;
	
	private Handler _handler;
	private float _leverLength=4f;
	private float _leverWidth = .5f;
	private float _leverThickness = .2f;
	private float _leverHeight = 1f;
	/** angle of lever &theta; */
	private float _rotation = 0f;
	private float _angularVelocity=0;
	
	private float halfLeverLength = _leverLength/2;
	/** Angle when lever touches floor */
	private float _angleRange;
	
	private List<Weight> _weights = new ArrayList<Weight>();
	
	private FloatBuffer fulcrumBuffer;
	private float []fulcrumCoords = {
			-.1f,0,0, .1f,0,0, 0,1,0
	};
	private FloatBuffer leverVB;
	private float []leverCoords = {
			//front face
			-halfLeverLength, 0f, 0,				//bottom-left
			halfLeverLength,	0f, 0,				//bottom-right
			halfLeverLength,  _leverThickness, 0,	//top-right
			-halfLeverLength, _leverThickness, 0,	//top-left
			//back face
			-halfLeverLength, 0f, -1*_leverWidth,				//bottom-left
			-halfLeverLength, _leverThickness, -1*_leverWidth,//top-left
			halfLeverLength,  _leverThickness, -1*_leverWidth,//top-right
			halfLeverLength,	0f, -1*_leverWidth				//bottom-right
	};
	private ShortBuffer leverIndexBuffer;
	private short []leverIndices = { 
			0,2,3,0,1,2,	//front
			3,6,5,3,2,6,	//top
			 	//left
				//right
				//back
			0,4,7,0,7,1		//bottom
			};
	private FloatBuffer floorVB;
	private float []floorCoords = {
			10f, 0f, 10f,
			10f, 0f, -10f,
			-10f, 0f, -10f,
			-10f, 0f, 10f
	};
	private FloatBuffer floorNB;
	private float []floorNormals = {
			0f, 1f, 10f,
			0f, 1f, -10f,
			0f, 1f, -10f,
			0f, 1f, 10f
	};
	private ShortBuffer floorIndexBuffer;
	private short []floorIndices = { 0, 1, 2, 0, 2, 3 };
	
	private long timestamp;
	
	public LeverView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setRenderer(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0f, 0f, 0f, 1.f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glDepthRangef(0f, 1f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK); 
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, new float[] { -1f, -1f, -1f }, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[] { 1f, 1f, 1f,1f }, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, new float[] { 1f, 1f, 1f,1f }, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[] { 0f,0f, 0f,1f }, 0);
		
		fulcrumBuffer = allocate(fulcrumCoords);
		leverVB = allocate(leverCoords);
		leverIndexBuffer = allocate(leverIndices);
		floorVB = allocate(floorCoords);
		floorIndexBuffer = allocate(floorIndices);
		floorNB = allocate(floorNormals);
		_angleRange =(float)Math.atan(_leverHeight/(halfLeverLength));
		
		_weights.add(new Weight(5, -1*halfLeverLength));
		_weights.add(new Weight(0, halfLeverLength));
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0,0,width,height);
	    gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    GLU.gluPerspective(gl, 45f, (float) width / height, 1, 20);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0f,4f, 10f, 0f, 0f, 0f, 0f, 1f, -1f);
		
		long current = System.currentTimeMillis();
		if(timestamp==0) timestamp=current;
		float interval = (current-timestamp)/1000f;
		
		float alpha = 0;
		for(Weight w : _weights) 
			alpha += w.calculateAngularAcceleration(_rotation);
		
		_angularVelocity += alpha*interval;
		_rotation += _angularVelocity*interval;
		_rotation = Math.max(-1*_angleRange, _rotation);
		_rotation = Math.min(_rotation, _angleRange);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		//draw floor
		gl.glColor4f(0f, 1f, 1f, 1f);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, new float[] { 1f, 1f, 1f, 1f }, 0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] { 0f, 1f, 1f, 1f }, 0);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floorVB);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, floorNB );
		gl.glDrawElements(GL10.GL_TRIANGLES, floorIndices.length, GL10.GL_UNSIGNED_SHORT, floorIndexBuffer);
		
		// draw fulcrum
		gl.glColor4f(1f, 0f, 0f, 1f);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] { 1f, 0f, 0f, 1f }, 0);
		gl.glPushMatrix();
		gl.glScalef(1f, _leverHeight, 1f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fulcrumBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, fulcrumCoords.length/3);
		gl.glPopMatrix();
		
		//draw lever
		gl.glColor4f(0.6f, .7f, .2f, 1f);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] { 0.6f, 0.7f, 0.2f, 1.0f  }, 0);
		gl.glTranslatef(0f, _leverHeight, 0f);
		gl.glRotatef((float)Math.toDegrees(_rotation), 0f, 0f, 1f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leverVB);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, leverVB);
		gl.glDrawElements(GL10.GL_TRIANGLES, leverIndices.length, GL10.GL_UNSIGNED_SHORT, leverIndexBuffer);
		
		for(Weight w : _weights) 
			w.draw(gl);
		
		timestamp = current;
	}
	
	public void setHandler(Handler h) {
		this._handler = h;
	}
	
	private void printStatus(Handler handler, String view, String text) {
		if(handler==null) return;
		Message msg = handler.obtainMessage();
		msg.setData(new Bundle());
		msg.getData().putString(view, text);
		handler.sendMessage(msg);
	}
	
	public static  FloatBuffer allocate(float []vertices) {
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4); 
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer buf = bb.asFloatBuffer();  
		buf.put(vertices);    
		buf.position(0);
		return buf;
	}
	
	public static ShortBuffer allocate(short []indices) {
		ByteBuffer bb = ByteBuffer.allocateDirect(indices.length * 2); 
		bb.order(ByteOrder.nativeOrder());
		ShortBuffer buf = bb.asShortBuffer();  
		buf.put(indices);    
		buf.position(0);
		return buf;
	}
}
