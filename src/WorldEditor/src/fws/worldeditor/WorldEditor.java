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
	
	private WorldGenerationMap map_;
	
	private ColorRenderer<WorldGenerationCell> renderer_;
	private ColorSelector color_elevation_;
	private ColorSelector color_land_water_;
	private ColorSelector color_random_;
	private ColorSelector color_temperature_;
	
	private float elevation_delta_ = 0.1f;
	
	private GenerationAlgorithm elevation_algo_noise_;
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
		
		// map
		
		map_ = new WorldGenerationMap(width, height);
		
		elevation_algo_noise_ = new NoiseAlgorithm(3, 0.3f, 0.1f);
		
		temperature_algo_linear_= new LinearGradientAlgorithm(20.0f, width, 1.0f, 0.0f);
		temperature_algo_noise_ = new NoiseAlgorithm(3, 0.3f, 0.1f, 100);
		temperature_algo_radial_ = new RadialGradientAlgorithm(hw, hh, hw, 0.0f, 1.0f);
		
		map_.setElevationAlgo(elevation_algo_noise_);
		map_.setTemperatureAlgo(temperature_algo_linear_);
		
		map_.generateElevation();
		map_.generateTemperature();
		
		// renderer
		
		color_elevation_ = new ColorElevation();
		color_land_water_ = new ColorLandAndWater(map_);
		color_random_ = new RandomColorSelector();
		color_temperature_ = new ColorTemperature();
		
		renderer_ = new ColorRenderer(map_.getMap(), cell_size, color_temperature_);
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
			renderer_.setSelector(color_temperature_);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_F4))
		{
			renderer_.setSelector(color_random_);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			map_.getElevationAlgo().nextSeed();
			map_.generateElevation();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_T))
		{
			map_.geTemperatureAlgo().nextSeed();
			map_.generateTemperature();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			map_.setTemperatureAlgo(temperature_algo_noise_);
			map_.generateTemperature();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_2))
		{
			map_.setTemperatureAlgo(temperature_algo_linear_);
			map_.generateTemperature();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_3))
		{
			map_.setTemperatureAlgo(temperature_algo_radial_);
			map_.generateTemperature();
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
