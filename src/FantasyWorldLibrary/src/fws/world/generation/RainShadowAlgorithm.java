package fws.world.generation;

import fws.utility.map.Cell;
import fws.utility.map.Map;
import fws.world.WorldData;
import fws.world.WorldGenerationCell;
import fws.world.WorldGenerationMap;

public class RainShadowAlgorithm<T extends Cell & WorldData> implements GenerationAlgorithm<T>
{
	private WorldGenerationMap map_;
	private float sea_level_;
	private float remainder_;
	
	private float[][] rainfall_;
	
	private float min_rainfall_ = 0.2f;
	private float min_cost_ = 0.02f;
	private float normal_rainfall_ = 0.6f;
	private float normal_cost_ = 0.2f;
	private float recharge_ = 0.2f;
	private float water_;
	
	public RainShadowAlgorithm(WorldGenerationMap map)
	{
		map_ = map;
	}
	
	@Override
	public void nextSeed()
	{
		
	}

	@Override
	public void update()
	{
		sea_level_ = map_.getSeaLevel();
		remainder_ = 1.0f - sea_level_;
		
		Map<WorldGenerationCell> map = map_.getMap();
		
		int width = map.getWidth();
		int height = map.getHeight();
		
		rainfall_ = new float[width][height];
		
		// x axis
		
		for(int y = 0; y < height; y++)
		{
			// positive direction
			
			water_ = map.getCell(0, y).getElevation() > sea_level_ ? 0.0f : 1.0f;
			
			for(int x = 0; x < width; x++)
			{
				updateCell(x, y);
			}
			
			// negative direction
			
			water_ = map.getCell(width-1, y).getElevation() > sea_level_ ? 0.0f : 1.0f;
			
			for(int x = width-1; x >= 0; x--)
			{
				updateCell(x, y);
			}
		}
		
		// y axis
		
		for(int x = 0; x < width; x++)
		{
			// positive direction
			
			water_ = map.getCell(x, 0).getElevation() > sea_level_ ? 0.0f : 1.0f;
			
			for(int y = 0; y < height; y++)
			{
				updateCell(x, y);
			}
			
			// negative direction
			
			water_ = map.getCell(x, height-1).getElevation() > sea_level_ ? 0.0f : 1.0f;
			
			for(int y = height-1; y >= 0; y--)
			{
				updateCell(x, y);
			}
		}
	}
	
	public void updateCell(int x, int y)
	{
		WorldGenerationCell cell = map_.getMap().getCell(x, y);
		float elevation = cell.getElevation();
		float rainfall;

		if(elevation > sea_level_)
		{
			float t = (elevation - sea_level_) / remainder_;
			
			if(water_ > normal_cost_)
			{
				rainfall = normal_rainfall_ * water_ * (1.0f + t);
				water_ = Math.max(water_ - normal_cost_ * (1.0f + t), 0.0f);
			}
			else
			{
				rainfall = min_rainfall_ * water_ * (1.0f + t);
				water_ = Math.max(water_ - min_cost_ * (1.0f + t), 0.0f);
			}
		}
		else
		{
			water_ = Math.min(water_ + recharge_, 1.0f);
			rainfall = 0.5f;
		}
		
		rainfall_[x][y] = Math.max(rainfall, rainfall_[x][y]);
	}

	@Override
	public float generate(float x, float y, T cell)
	{
		int cx = map_.getMap().getColumn(cell.getId());
		int cy = map_.getMap().getRow(cell.getId());
		
		return rainfall_[cx][cy];
	}
}
