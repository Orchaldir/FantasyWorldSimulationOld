package fws.world;

import fws.utility.map.HexMap;
import fws.utility.map.Map;
import fws.world.generation.GenerationAlgorithm;

public class WorldGenerationMap
{
	private Map<WorldGenerationCell> map_;
	
	private GenerationAlgorithm elevation_algo_;
	private GenerationAlgorithm temperature_algo_;
	
	private float sea_level_ = 0.5f;
	
	public WorldGenerationMap(int width, int height)
	{
		WorldGenerationCell[] cells = new WorldGenerationCell[width*height];
		
		for(int index = 0; index < cells.length; index++)
		{
			cells[index] = new WorldGenerationCell(index);
		}
		
		//map_ = new SquareMap(width, height, cells);
		map_ = new HexMap(width, height, cells);
	}
	
	public Map getMap()
	{
		return map_;
	}
	
	// elevation
	
	public void setElevationAlgo(GenerationAlgorithm algo)
	{
		elevation_algo_ = algo;
	}
	
	public GenerationAlgorithm getElevationAlgo()
	{
		return elevation_algo_;
	}
	
	public void generateElevation()
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
	
	// temperature
	
	public void setTemperatureAlgo(GenerationAlgorithm algo)
	{
		temperature_algo_ = algo;
	}
	
	public GenerationAlgorithm geTemperatureAlgo()
	{
		return temperature_algo_;
	}
	
	public void generateTemperature()
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
	
	// sea level
	
	public float getSeaLevel()
	{
		return sea_level_;
	}
	
	public void setSeaLevel(float sea_level)
	{
		sea_level_ = sea_level;
	}
}
