package fws.worldeditor;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import fws.utility.map.*;
import fws.world.*;

public class WorldEditor
{

	public static final int DISPLAY_HEIGHT = 480;
	public static final int DISPLAY_WIDTH = 640;
	public static final Logger LOGGER = Logger.getLogger(WorldEditor.class.getName());
	
	private Map<WorldGenerationCell> map_;
	private ColorRenderer<WorldGenerationCell> renderer_;
	private ColorSelector color_elevation_;
	private ColorSelector color_land_water_;
	private ColorSelector color_random_;
	
	private float elevation_delta_ = 0.1f;

	static
	{
		try
		{
			LOGGER.addHandler(new FileHandler("errors.log", true));
		} catch(IOException ex)
		{
			LOGGER.log(Level.WARNING, ex.toString(), ex);
		}
	}

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
			LOGGER.log(Level.SEVERE, ex.toString(), ex);
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
		int width = 20;
		int height = 10;
		int cell_size = 30;
		int border = 2;
		
		Random random = new Random();
		
		WorldGenerationCell[] cells = new WorldGenerationCell[width*height];
		
		for(int index = 0; index < cells.length; index++)
		{
			cells[index] = new WorldGenerationCell(index);
			cells[index].setElevation(random.nextFloat());
		}
		
		map_ = new SquareMap(width, height, cells);
		//map_ = new HexMap(width, height, cells);
		
		color_elevation_ = new ColorElevation();
		color_land_water_ = new ColorLandAndWater();
		color_random_ = new RandomColorSelector();
		
		renderer_ = new ColorRenderer(map_, cell_size, border, color_elevation_);
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
			renderer_.setSelector(color_elevation_);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_F2))
		{
			renderer_.setSelector(color_land_water_);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_F3))
		{
			renderer_.setSelector(color_random_);
		}
	}

	public void processMouse()
	{
		if(Mouse.isButtonDown(0))
		{
			WorldGenerationCell cell = renderer_.getCell(Mouse.getX(), Mouse.getY());
			
			if(cell != null)
			{
				cell.setElevation(cell.getElevation() + elevation_delta_);
			}
		}
		else if(Mouse.isButtonDown(1))
		{
			WorldGenerationCell cell = renderer_.getCell(Mouse.getX(), Mouse.getY());
			
			if(cell != null)
			{
				cell.setElevation(cell.getElevation() - elevation_delta_);
			}
		}
	}

	public void render()
	{
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
		
		renderer_.render();
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
		
	}

}
