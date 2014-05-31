package fws.worldeditor;

import fws.utility.map.ColorRenderer;
import fws.utility.state.State;
import fws.world.*;
import fws.world.generation.*;
import org.lwjgl.input.Keyboard;

public class RainfallState extends State
{
	WorldGenerationMap map_;
	
	private ColorRenderer<WorldGenerationCell> renderer_;
	private ColorRainfall color_rainfall_;
	
	private NoiseAlgorithm rainfall_algo_noise_;
	private RainShadowAlgorithm rainfall_algo_shadow_;
	private SineAlgorithm rainfall_algo_sine_;
	private MultiplyAlgorithms rainfall_algo_product_;
	
	public RainfallState(WorldGenerationMap map, int cell_size)
	{
		map_ = map;
		
		int width = map_.getMap().getWidth();
		int height = map_.getMap().getHeight();
		
		// generation
		
		rainfall_algo_noise_ = new NoiseAlgorithm(3, 0.3f, 0.1f, 200);
		rainfall_algo_shadow_= new RainShadowAlgorithm(map_);
		rainfall_algo_sine_ = new SineAlgorithm(-20.0f, height / 3.0f, 1.0f, 0.2f, 1.0f);
		
		rainfall_algo_product_ = new MultiplyAlgorithms();
		rainfall_algo_product_.addAlgorithm(rainfall_algo_sine_);
		rainfall_algo_product_.addAlgorithm(rainfall_algo_shadow_);
		
		map_.setRainfallAlgo(rainfall_algo_product_);
		
		// rendering
		
		color_rainfall_ = new ColorRainfall();
		renderer_ = new ColorRenderer(map_.getMap(), cell_size, color_rainfall_);
	}
	
	@Override
	public String getName()
	{
		return "Rainfall";
	}
	
	@Override
	public void processKeyboard()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			map_.setRainfallAlgo(rainfall_algo_sine_);
			map_.generate();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_2))
		{
			map_.setRainfallAlgo(rainfall_algo_shadow_);
			map_.generate();
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_3))
		{
			map_.setRainfallAlgo(rainfall_algo_product_);
			map_.generate();
		}
	}
	
	@Override
	public void render()
	{
		renderer_.render();
	}
}