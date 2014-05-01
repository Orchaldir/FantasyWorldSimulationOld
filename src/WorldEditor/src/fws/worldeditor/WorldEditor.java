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
import fws.world.generation.*;
import fws.world.*;

public class WorldEditor
{
	public static final int DISPLAY_HEIGHT = 480;
	public static final int DISPLAY_WIDTH = 640;
	
	private Map<WorldGenerationCell> map_;
	private ColorRenderer<WorldGenerationCell> renderer_;
	private ColorSelector color_elevation_;
	private ColorSelector color_land_water_;
	private ColorSelector color_random_;
	private ColorSelector color_temperature_;
	
	private float elevation_delta_ = 0.1f;
	
	private GenerationAlgorithm elevation_algo_;
	private GenerationAlgorithm temperature_algo_;
	private GenerationAlgorithm temperature_algo_linear_;
	private GenerationAlgorithm temperature_algo_noise_;
	private GenerationAlgorithm temperature_algo_radial_;

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
		int cell_size = 20;
		int width = 30;
		int height = 20;
		float hw = width / 2.0f;
		float hh = height / 2.0f;
		
		WorldGenerationCell[] cells = new WorldGenerationCell[width*height];
		
		for(int index = 0; index < cells.length; index++)
		{
			cells[index] = new WorldGenerationCell(index);
		}
		
		//map_ = new SquareMap(width, height, cells);
		map_ = new HexMap(width, height, cells);
		
		elevation_algo_ = new NoiseAlgorithm(3, 0.3f, 0.1f);
		
		temperature_algo_linear_= new LinearGradientAlgorithm(20.0f, width, 1.0f, 0.0f);
		temperature_algo_noise_ = new NoiseAlgorithm(3, 0.3f, 0.1f, 100);
		temperature_algo_radial_ = new RadialGradientAlgorithm(hw, hh, hw, 0.0f, 1.0f);
		temperature_algo_ = temperature_algo_linear_;
		
		color_elevation_ = new ColorElevation();
		color_land_water_ = new ColorLandAndWater();
		color_random_ = new RandomColorSelector();
		color_temperature_ = new ColorTemperature();
		
		renderer_ = new ColorRenderer(map_, cell_size, color_temperature_);
		
		createElevation();
		createTemperature();
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
		elevation_algo_.update();
		
		for(int x = 0; x < map_.getWidth(); x++)
		{
			for(int y = 0; y < map_.getHeight(); y++)
			{
				WorldGenerationCell cell = map_.getCell(x, y);
				
				cell.setElevation(elevation_algo_.generate(x, y));
			}
		}
	}
	
	public void createTemperature()
	{
		temperature_algo_.update();
		
		for(int x = 0; x < map_.getWidth(); x++)
		{
			for(int y = 0; y < map_.getHeight(); y++)
			{
				WorldGenerationCell cell = map_.getCell(x, y);
				
				cell.setTemperature(temperature_algo_.generate(x, y));
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
			renderer_.setSelector(color_temperature_);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_F4))
		{
			renderer_.setSelector(color_random_);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			elevation_algo_.nextSeed();
			createElevation();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_T))
		{
			temperature_algo_.nextSeed();
			createTemperature();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			temperature_algo_ = temperature_algo_noise_;
			createTemperature();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_2))
		{
			temperature_algo_ = temperature_algo_linear_;
			createTemperature();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_3))
		{
			temperature_algo_ = temperature_algo_radial_;
			createTemperature();
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
