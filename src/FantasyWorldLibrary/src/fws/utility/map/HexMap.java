package fws.utility.map;

import static org.lwjgl.opengl.GL11.*;

public class HexMap<T extends Cell> extends Map<T>
{
	public HexMap(int width, int height, T[] cells)
	{
		super(width, height, cells);
	}

	@Override
	void renderCell(int index, int cell_width, int border)
	{
		int column = getColumn(index);
		int row = getRow(index);
		
		// hexagon height & width
		
		int cell_height = (int) (cell_width * 2.0f / Math.sqrt(3.0f));
		int quarter_height = cell_height / 4;
		int three_quarter_height = cell_height * 3 / 4;
		
		int half_width = cell_width / 2;
		
		// x values
		
		int x0 = column * cell_width;
		
		if(row % 2 == 1)
		{
			x0 += half_width;
		}
		
		int x1 = x0 + half_width;
		int x2 = x0 + cell_width - border;
		
		// y values
		
		int y0 = row * three_quarter_height;
		int y1 = y0 + quarter_height;
		int y2 = y0 + three_quarter_height - border;
		int y3 = y0 + cell_height - border;
		
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
