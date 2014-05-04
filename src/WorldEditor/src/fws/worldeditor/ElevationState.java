package fws.worldeditor;

import fws.utility.map.ColorRenderer;
import fws.utility.state.State;
import fws.world.*;
import fws.world.generation.NoiseAlgorithm;

public class ElevationState extends State
{
	WorldGenerationMap map_;
	
	private ColorRenderer<WorldGenerationCell> renderer_;
	private ColorLandAndWater color_land_water_;
	
	private NoiseAlgorithm elevation_algo_noise_;
	
	public ElevationState(WorldGenerationMap map, int cell_size)
	{
		map_ = map;
		
		// generation
		
		elevation_algo_noise_ = new NoiseAlgorithm(3, 0.3f, 0.1f);
		map_.setElevationAlgo(elevation_algo_noise_);
		
		// rendering
		
		color_land_water_ = new ColorLandAndWater(map_);
		renderer_ = new ColorRenderer(map_.getMap(), cell_size, color_land_water_);
	}
	
	@Override
	public String getName()
	{
		return "Elevation";
	}
	
	@Override
	public void render()
	{
		renderer_.render();
	}
}
