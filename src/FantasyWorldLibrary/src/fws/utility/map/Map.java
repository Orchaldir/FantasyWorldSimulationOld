package fws.utility.map;

public interface Map<T extends Cell>
{
	int getNumberOfCells();
	T getCell(int index);
}
