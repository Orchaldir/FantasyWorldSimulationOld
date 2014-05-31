package fws.world;

import fws.utility.map.*;

public class PlateTectonicsMap
{
	private SquareMap<PlateTectonicsCell> map_;
	
	public PlateTectonicsMap(int width, int height, PlateType type)
	{
		PlateTectonicsCell[] cells = new PlateTectonicsCell[width*height];
		
		for(int index = 0; index < cells.length; index++)
		{
			cells[index] = new PlateTectonicsCell(index, type);
		}
		
		map_ = new SquareMap(width, height, cells);
	}
	
	public SquareMap<PlateTectonicsCell> getMap()
	{
		return map_;
	}
}
