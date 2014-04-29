package fws.utility.map;

import static org.lwjgl.opengl.GL11.*;

public class SquareMap<T extends Cell> extends Map<T>
{
	public SquareMap(int width, int height, T[] cells)
	{
		super(width, height, cells);
	}
	
	// cells
	
	@Override
	public T getCell(float x, float y)
	{
		return getCell(getIndex((int)x, (int)y));
	}
	
	// rendering

	@Override
	public void renderCell(int index, int cell_size, int border)
	{
		int column = getColumn(index);
		int row = getRow(index);
		
		//System.out.printf("Index=%d -> column=%d row%d\n", index, column, row);
		
		int start_x = column * cell_size;
		int start_y = row * cell_size;
		
		int end_x = start_x + cell_size - border;
		int end_y = start_y + cell_size - border;
		
		glBegin(GL_QUADS);
		
		glTexCoord2f(0.0f, 0.0f);
		glVertex2f(start_x, start_y);
		
		glTexCoord2f(1.0f, 0.0f);
		glVertex2f(end_x, start_y);
		
		glTexCoord2f(1.0f, 1.0f);
		glVertex2f(end_x, end_y);
		
		glTexCoord2f(0.0f, 1.0f);
		glVertex2f(start_x, end_y);
		
		glEnd();
	}

}
