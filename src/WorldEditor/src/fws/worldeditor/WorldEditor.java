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
import fws.utility.SimplexNoise;
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
	
	private Random random_ = new Random(42);
	
	private static final int ELEVATION_SEED = 20000;
	private float elevation_delta_ = 0.1f;
	private int elevation_octaves_ = 3;
	private float elevation_roughness_ = 0.3f;
	private float elevation_scale_ = 0.01f;

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
		int cell_size = 1;
		int width = DISPLAY_WIDTH / cell_size;
		int height = DISPLAY_HEIGHT / cell_size;
		
		WorldGenerationCell[] cells = new WorldGenerationCell[width*height];
		
		for(int index = 0; index < cells.length; index++)
		{
			cells[index] = new WorldGenerationCell(index);
		}
		
		map_ = new SquareMap(width, height, cells);
		//map_ = new HexMap(width, height, cells);
		
		color_elevation_ = new ColorElevation();
		color_land_water_ = new ColorLandAndWater();
		color_random_ = new RandomColorSelector();
		
		renderer_ = new ColorRenderer(map_, cell_size, color_land_water_);
		
		createElevation();
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
	
	public void createElevation()
	{
		int cell_size = renderer_.getCellSize();
		int offset_x = random_.nextInt(ELEVATION_SEED);
		int offset_y = random_.nextInt(ELEVATION_SEED);
		
		for(int x = 0; x < map_.getWidth(); x++)
		{
			for(int y = 0; y < map_.getHeight(); y++)
			{
				WorldGenerationCell cell = map_.getCell(x, y);
				
				// position independent of cell size
				float ax = x * cell_size + offset_x;
				float ay = y * cell_size + offset_y;
				
				float noise = (float)SimplexNoise.getOctavedNoise(ax, ay, 
						elevation_octaves_, elevation_roughness_, elevation_scale_);
				float evaluation = (noise + 1.0f) / 2.0f;
				
				cell.setElevation(evaluation);
			}
		}
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
		else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			createElevation();
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
