package fws.worldeditor;

import fws.utility.map.*;
import fws.utility.state.State;
import fws.utility.Color;
import fws.world.*;
import fws.world.generation.NoiseAlgorithm;
import org.lwjgl.input.Keyboard;

public class ElevationState extends State
{
	private WorldGenerationMap map_;
	
	private ColorRenderer renderer_;
	
	// plate tectonics
	
	private PlateTectonicsMap tectonics_map_;
	private PlateType land_type_;
	private PlateType water_type_;
	
	private ColorRenderer<PlateTectonicsCell> tectonics_renderer_;
	private ColorPlateTectonics color_tectonics_;
	
	// elevation
	
	private ColorRenderer<WorldGenerationCell> elevation_renderer_;
	private ColorLandAndWater color_elevation_;
	
	private NoiseAlgorithm elevation_algo_noise_;
	
	public ElevationState(WorldGenerationMap map, int cell_size)
	{
		map_ = map;
		
		// plate tectonics
		
		int tectonics_cell_size = 50;
		int width = 12;
		int height = 6;
		
		land_type_ = new PlateType("Land", 0.75f, new Color(0.0f, 1.0f, 0.0f));
		water_type_ = new PlateType("Water", 0.25f, new Color(0.0f, 0.0f, 1.0f));
		
		tectonics_map_ = new PlateTectonicsMap(width, height, water_type_);
		
		tectonics_map_.getMap().getCell(4, 2).type_ = land_type_;
		
		color_tectonics_ = new ColorPlateTectonics();
		tectonics_renderer_ = new ColorRenderer(tectonics_map_.getMap(), tectonics_cell_size, color_tectonics_);
		
		// elevation
		
		elevation_algo_noise_ = new NoiseAlgorithm(3, 0.3f, 0.1f);
		map_.setElevationAlgo(elevation_algo_noise_);
		
		color_elevation_ = new ColorLandAndWater(map_);
		elevation_renderer_ = new ColorRenderer(map_.getMap(), cell_size, color_elevation_);
		
		renderer_ = tectonics_renderer_;
	}
	
	@Override
	public String getName()
	{
		return "Elevation";
	}
	
	@Override
	public void processKeyboard()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			renderer_ = tectonics_renderer_;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_2))
		{
			renderer_ = elevation_renderer_;
		}
	}
	
	@Override
	public void render()
	{
		renderer_.render();
	}
}
