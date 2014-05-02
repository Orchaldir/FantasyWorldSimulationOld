package fws.world;

import fws.utility.map.Cell;

public class WorldGenerationCell extends Cell implements WorldData
{
	private float elevation_ = 0.5f;
	private float temperature_ = 0.5f;

	public WorldGenerationCell(int id)
	{
		super(id);
	}
	
	@Override
	public float getElevation()
	{
		return elevation_;
	}

	public void setElevation(float elevation)
	{
		elevation_ = Math.min(Math.max(elevation, 0.0f), 1.0f);
	}
	
	@Override
	public float getTemperature()
	{
		return temperature_;
	}

	public void setTemperature(float temperature)
	{
		temperature_ = Math.min(Math.max(temperature, 0.0f), 1.0f);
	}
}
