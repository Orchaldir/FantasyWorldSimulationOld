package fws.worldeditor;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import fws.utility.map.*;
import fws.utility.state.*;
import fws.world.*;

public class WorldEditor
{
	public static final int DISPLAY_HEIGHT = 480;
	public static final int DISPLAY_WIDTH = 640;
	
	private StateMgr state_mgr_;
	
	private WorldGenerationMap map_;

	public static void main(String[] args)
	{
		WorldEditor main = null;
		try
		{
			System.out.println("Keys:");
			System.out.println("esc   - Exit");
			main = new WorldEditor();
			main.create();
			main.run();
		} catch(Exception ex)
		{
			System.out.println("Exception:" + ex.toString());
		} finally
		{
			if(main != null)
			{
				main.destroy();
			}
		}
	}

	public WorldEditor()
	{
		// map
		
		int cell_size = 10;
		int width = 60;
		int height = 40;
		
		//map_ = new WorldGenerationMap(MapType.SQUARE_MAP, width, height);
		map_ = new WorldGenerationMap(MapType.HEX_MAP, width, height);
		
		// states
		
		state_mgr_ = new StateMgr();
		state_mgr_.add(new ElevationState(map_, cell_size));
		state_mgr_.add(new TemperatureState(map_, cell_size));
		state_mgr_.add(new RainfallState(map_, cell_size));
		state_mgr_.setActive("Elevation");
		
		// generate map
		
		map_.generate();
	}

	public void create() throws LWJGLException
	{
		//Display
		Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		Display.setFullscreen(false);
		Display.setTitle("Fantasy World Simulation - Map Editor");
		Display.create();

		//Keyboard
		Keyboard.create();

		//Mouse
		Mouse.setGrabbed(false);
		Mouse.create();

		//OpenGL
		initGL();
		resizeGL();
	}

	public void destroy()
	{
		//Methods already check if created before destroying.
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public void initGL()
	{
		//2D Initialization
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
	}

	public void processKeyboard()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_F1))
		{
			state_mgr_.setActive("Elevation");
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_F2))
		{
			state_mgr_.setActive("Temperature");
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_F3))
		{
			state_mgr_.setActive("Rainfall");
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			map_.getElevationAlgo().nextSeed();
			map_.generate();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_T))
		{
			map_.getTemperatureAlgo().nextSeed();
			map_.generate();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_R))
		{
			map_.getRainfallAlgo().nextSeed();
			map_.generate();
		}
		
		state_mgr_.processKeyboard();
	}

	public void processMouse()
	{
		state_mgr_.processMouse();
	}

	public void render()
	{
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
		
		state_mgr_.render();
	}

	public void resizeGL()
	{
		//2D Scene
		glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(0.0f, DISPLAY_WIDTH, 0.0f, DISPLAY_HEIGHT);
		glPushMatrix();

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glPushMatrix();
	}

	public void run()
	{
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			if(Display.isVisible())
			{
				processKeyboard();
				processMouse();
				update();
				render();
			} else
			{
				if(Display.isDirty())
				{
					render();
				}
				try
				{
					Thread.sleep(100);
				} catch(InterruptedException ex)
				{
				}
			}
			Display.update();
			Display.sync(60);
		}
	}

	public void update()
	{
		state_mgr_.update();
	}

}
