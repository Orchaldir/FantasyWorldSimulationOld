package fws.utility.map;

import static org.lwjgl.opengl.GL11.*;

public class HexMap<T extends Cell> extends Map<T>
{
	public static final float CELL_WIDTH = 1.0f;
	public static final float HALF_CELL_WIDTH = CELL_WIDTH * 0.5f;
		
	public static final float CELL_HEIGHT = (float) (CELL_WIDTH * 2.0f / Math.sqrt(3.0f));
	public static final float QUARTER_CELL_HEIGHT = CELL_HEIGHT * 0.25f;
	public static final float THREE_QUARTER_CELL_HEIGHT = CELL_HEIGHT * 0.75f;
	
	public HexMap(int width, int height, T[] cells)
	{
		super(width, height, cells);
	}
	
	float getRowStart(int row)
	{
		return (row % 2) * HALF_CELL_WIDTH;
	}
	
	// cells
	
	@Override
	public T getCell(float x, float y)
	{
		int row = (int) (y * CELL_HEIGHT);
		int column = (int) (x * CELL_WIDTH - getRowStart(row));
		
		return getCell(column, row);
	}
	
	// rendering

	@Override
	void renderCell(int index)
	{
		int column = getColumn(index);
		int row = getRow(index);
		
		// x values
		
		float x0 = column * CELL_WIDTH + getRowStart(row);
		float x1 = x0 + HALF_CELL_WIDTH;
		float x2 = x0 + CELL_WIDTH;
		
		// y values
		
		float y0 = row * THREE_QUARTER_CELL_HEIGHT;
		float y1 = y0 + QUARTER_CELL_HEIGHT;
		float y2 = y0 + THREE_QUARTER_CELL_HEIGHT ;
		float y3 = y0 + CELL_HEIGHT;
		
		// draw the hexagon
		
		glBegin(GL_POLYGON);
		
		glVertex2f(x1, y0);
		glVertex2f(x2, y1);
		glVertex2f(x2, y2);
		glVertex2f(x1, y3);
		glVertex2f(x0, y2);
		glVertex2f(x0, y1);
		
		glEnd();
	}
}
