package fws.utility.map;

public class SquareMap<T extends Cell> implements Map<T>
{
	private final int width_;
	private final int height_;
	private T[] cells_;
	
	
	public SquareMap(int width, int height, T[] cells)
	{
		width_ = width;
		height_ = height;
		cells_ = cells;
	}
	
	// cells
	
	@Override
	public int getNumberOfCells()
	{
		return cells_.length;
	}

	@Override
	public T getCell(int index)
	{
		return cells_[index];
	}

}
