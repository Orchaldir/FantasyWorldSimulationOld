package fws.world.generation;

import fws.utility.map.Cell;
import fws.world.WorldData;
import fws.world.WorldGenerationMap;

public class ModifiedByElevationAlgorithm<T extends Cell & WorldData> implements GenerationAlgorithm<T>
{
	private WorldGenerationMap map_;
	private float sea_level_;
	private float remainder_;
	
	private float value0_;
	private float value1_;
	private float delta_;
	
	public ModifiedByElevationAlgorithm(WorldGenerationMap map, float value0, float value1)
	{
		map_ = map;
		
		value0_ = value0;
		value1_ = value1;
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
		
		delta_ = value1_ - value0_;
	}

	@Override
	public float generate(float x, float y, T cell)
	{
		float t = Math.max((cell.getElevation() - sea_level_) / remainder_, 0.0f);
		
		return value0_ + delta_ * t;
	}

	@Override
	public boolean useGenerate()
	{
		return true;
	}
}
