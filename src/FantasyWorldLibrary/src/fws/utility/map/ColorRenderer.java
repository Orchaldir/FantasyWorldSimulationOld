package fws.utility.map;

public class ColorRenderer<T extends Cell>
{
	private Map<T> map_;
	
	private int cell_size_;
	private int border_;
	
	private ColorSelector<T> selector_;
	
	public ColorRenderer(Map<T> map, int cell_size, int border, ColorSelector<T> selector)
	{
		map_ = map;
		cell_size_ = cell_size;
		border_ = border;
		
		selector_ = selector;
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
		for(int index = 0; index < map_.getNumberOfCells(); index++)
		{
			selector_.selectColor(map_.getCell(index));
			map_.renderCell(index, cell_size_, border_);
		}
	}
}
