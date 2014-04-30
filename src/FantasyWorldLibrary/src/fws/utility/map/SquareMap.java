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
	public void renderCell(int index)
	{
		int column = getColumn(index);
		int row = getRow(index);
		
		//System.out.printf("Index=%d -> column=%d row%d\n", index, column, row);
		
		float start_x = column;
		float start_y = row;
		
		float end_x = start_x + 1.0f;
		float end_y = start_y + 1.0f;
		
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
