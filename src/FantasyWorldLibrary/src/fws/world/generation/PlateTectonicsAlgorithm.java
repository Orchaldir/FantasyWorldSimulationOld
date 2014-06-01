package fws.world.generation;

import fws.utility.map.Cell;
import fws.utility.map.Map;
import fws.world.*;

public class PlateTectonicsAlgorithm<T extends Cell & WorldData> implements GenerationAlgorithm<T>
{
	private PlateTectonicsMap tectonics_map_;
	
	public PlateTectonicsAlgorithm(PlateTectonicsMap tectonics_map)
	{
		tectonics_map_ = tectonics_map;
	}
	
	@Override
	public void nextSeed()
	{
		
	}

	@Override
	public void update()
	{
		
	}

	@Override
	public float generate(float x, float y, T cell)
	{
		float tectonics_x = x / tectonics_map_.getCellSize();
		float tectonics_y = y / tectonics_map_.getCellSize();
		int cell_x = (int)tectonics_x;
		int cell_y = (int)tectonics_y;
		
		float rest_x = tectonics_x - (float)Math.floor(tectonics_x);
		float rest_y = tectonics_y - (float)Math.floor(tectonics_y);
		
		// get elevations
		
		float value00; // left & down
		float value10; // right & down
		float value01; // left & up
		float value11; // right & up
		float factor_x, factor_y;
		
		if(rest_x <= 0.5f && rest_y <= 0.5f)
		{
			// case left & down
			
			factor_x = rest_x + 0.5f;
			factor_y = rest_y + 0.5f;
			
			value00 = tectonics_map_.getElevation(cell_x-1, cell_y-1);
			value10 = tectonics_map_.getElevation(cell_x, cell_y-1);
			value01 = tectonics_map_.getElevation(cell_x-1, cell_y);
			value11 = tectonics_map_.getElevation(cell_x, cell_y);
		}
		else if(rest_x > 0.5f && rest_y <= 0.5f)
		{
			// case right & down
			
			factor_x = rest_x - 0.5f;
			factor_y = rest_y + 0.5f;
			
			value00 = tectonics_map_.getElevation(cell_x, cell_y-1);
			value10 = tectonics_map_.getElevation(cell_x+1, cell_y-1);
			value01 = tectonics_map_.getElevation(cell_x, cell_y);
			value11 = tectonics_map_.getElevation(cell_x+1, cell_y);
		}
		else if(rest_x <= 0.5f && rest_y > 0.5f)
		{
			// case left & up
			
			factor_x = rest_x + 0.5f;
			factor_y = rest_y - 0.5f;
			
			value00 = tectonics_map_.getElevation(cell_x-1, cell_y);
			value10 = tectonics_map_.getElevation(cell_x, cell_y);
			value01 = tectonics_map_.getElevation(cell_x-1, cell_y+1);
			value11 = tectonics_map_.getElevation(cell_x, cell_y+1);
		}
		else
		{
			// case right & up
			
			factor_x = rest_x - 0.5f;
			factor_y = rest_y - 0.5f;
			
			value00 = tectonics_map_.getElevation(cell_x, cell_y);
			value10 = tectonics_map_.getElevation(cell_x+1, cell_y);
			value01 = tectonics_map_.getElevation(cell_x, cell_y+1);
			value11 = tectonics_map_.getElevation(cell_x+1, cell_y+1);
		}
		
		// bilinear interpolation
		
		float value0 = value00 * (1.0f - factor_x) + value10 * factor_x;
		float value1 = value01 * (1.0f - factor_x) + value11 * factor_x;
		
		float value = value0 * (1.0f - factor_y) + value1 * factor_y;
		
		return value;
	}

}
