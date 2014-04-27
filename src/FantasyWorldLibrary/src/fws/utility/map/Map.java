package fws.utility.map;

public interface Map<T extends Cell>
{
	int getNumberOfCells();
	T getCell(int index);
	
	void renderCell(int index, int cell_size, int border);
}
