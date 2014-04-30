package fws.utility.map;

import static org.lwjgl.opengl.GL11.*;

public class ColorRenderer<T extends Cell>
{
	private Map<T> map_;
	
	private int cell_size_;
	
	private ColorSelector<T> selector_;
	
	public ColorRenderer(Map<T> map, int cell_size, ColorSelector<T> selector)
	{
		map_ = map;
		cell_size_ = cell_size;
		
		selector_ = selector;
	}
	
	public int getCellSize()
	{
		return cell_size_;
	}
	
	public void setSelector(ColorSelector<T> selector)
	{
		selector_ = selector;
	}
	
	public T getCell(int x, int y)
	{
		return map_.getCell(x / (float)cell_size_, y / (float)cell_size_);
	}
	
	public void render()
	{
		glScalef(cell_size_, cell_size_, cell_size_);
		
		for(int index = 0; index < map_.getNumberOfCells(); index++)
		{
			selector_.selectColor(map_.getCell(index));
			map_.renderCell(index);
		}
	}
}
