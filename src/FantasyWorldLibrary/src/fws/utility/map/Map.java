package fws.utility.map;

public abstract class Map<T extends Cell>
{
	private final int width_;
	private final int height_;
	private T[] cells_;
	
	public Map(int width, int height, T[] cells)
	{
		width_ = width;
		height_ = height;
		cells_ = cells;
	}
	
	// index
	
	public int getColumn(int index)
	{
		return index % width_;
	}
	
	public int getRow(int index)
	{
		return index / width_;
	}
	
	// cells

	public int getNumberOfCells()
	{
		return cells_.length;
	}

	public T getCell(int index)
	{
		return cells_[index];
	}
	
	abstract void renderCell(int index, int cell_size, int border);
}
