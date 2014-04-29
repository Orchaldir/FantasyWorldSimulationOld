package fws.world;

import fws.utility.map.Cell;
import fws.utility.map.ColorSelector;
import static org.lwjgl.opengl.GL11.glColor3f;

public class ColorLandAndWater<T extends Cell & WorldData> implements ColorSelector<T>
{
	private float sea_level_ = 0.5f;
	
	@Override
	public void selectColor(T cell)
	{
		float elevation = cell.getElevation();
		
		if(elevation >= sea_level_)
		{
			float value = (elevation - sea_level_) / (1.0f - sea_level_);
			glColor3f(value, 1.0f, value);
		}
		else
		{
			float value = elevation / sea_level_;
			glColor3f(0.0f, 0.0f, value);
		}
	}
}
