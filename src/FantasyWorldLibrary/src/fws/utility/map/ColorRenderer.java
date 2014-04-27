package fws.utility.map;

public class ColorRenderer<T extends Cell>
{
	private Map<T> map_;
	private int cell_size_;
	private int border_;
	
	public ColorRenderer(Map<T> map, int cell_size, int border)
	{
		map_ = map;
		cell_size_ = cell_size;
		border_ = border;
	}
	
	public void render()
	{
		for(int index = 0; index < map_.getNumberOfCells(); index++)
		{
			map_.renderCell(index, cell_size_, border_);
		}
	}
}