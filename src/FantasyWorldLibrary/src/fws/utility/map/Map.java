package fws.utility.map;

public abstract class Map<T extends Cell>
{
	protected final int width_;
	protected final int height_;
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
	
	public int getIndex(int column, int row)
	{
		return column + row * width_;
	}
	
	// cells

	public int getNumberOfCells()
	{
		return cells_.length;
	}

	public T getCell(int index)
	{
		if(index < 0 || index >= cells_.length)
			return null;
		
		return cells_[index];
	}
	
	abstract T getCell(float x, float y);
	
	// rendering
	
	abstract void renderCell(int index, int cell_size, int border);
}
