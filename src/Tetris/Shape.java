package Tetris;

import java.awt.Color;

public class Shape 
{
	public static final int MAX_SIZE = 5;
	
	private ShapeType type;
	private Orientation orientation;
	public boolean tilemap[][];
	public Color color;
	
	private enum Orientation
	{
		rotate0,
		rotate90,
		rotate180,
		rotate270;
	}
	
	public Shape(ShapeType shapetype)
	{
		type = shapetype;
		tilemap = new boolean[MAX_SIZE][MAX_SIZE];
		orientation = Orientation.rotate0;
		for(int i = 0; i < MAX_SIZE; i++)
		{
			for(int j = 0; j < MAX_SIZE; j++)
			{
				tilemap[i][j] = false;
			}
		}
		switch (type) 
		{
			case Square:
				color = Color.MAGENTA;
				tilemap[1][1] = true;
				tilemap[1][2] = true;
				tilemap[2][1] = true;
				tilemap[2][2] = true;
				break;
			case Long:
				color = Color.CYAN;
				tilemap[1][3] = true;
				tilemap[2][3] = true;
				tilemap[3][3] = true;
				tilemap[4][3] = true;
				break;
			case L:
				color = Color.GREEN;
				tilemap[1][2] = true;
				tilemap[2][2] = true;
				tilemap[3][2] = true;
				tilemap[3][3] = true;
				break;
			case ReverseL:
				color = Color.ORANGE;
				tilemap[1][2] = true;
				tilemap[2][2] = true;
				tilemap[3][2] = true;
				tilemap[3][1] = true;
				break;
			case Z:
				color = Color.PINK;
				tilemap[1][1] = true;
				tilemap[1][2] = true;
				tilemap[2][2] = true;
				tilemap[2][3] = true;
				break;
			case ReverseZ:
				color = Color.BLUE;
				tilemap[1][2] = true;
				tilemap[1][3] = true;
				tilemap[2][1] = true;
				tilemap[2][2] = true;
				break;
			case Tri:
				color = Color.YELLOW;
				tilemap[1][2] = true;
				tilemap[2][1] = true;
				tilemap[2][2] = true;
				tilemap[2][3] = true;
				break;
			default:
				break;
		}
			
	}
	
	public Shape rotateRight()
	{
		Shape toReturn = new Shape(type);
		for(int i = 0; i < MAX_SIZE; i++)
		{
			for(int j = 0; j < MAX_SIZE; j++)
			{
				toReturn.tilemap[i][j] = (true && tilemap[i][j]);
			}
		}
		switch (type) 
		{
			case Square: 	//Don't have to do anything
				break;
			case Long:
				if(orientation == Orientation.rotate0 || orientation == Orientation.rotate180)
				{
					
					toReturn.tilemap[1][3] = false;
					toReturn.tilemap[2][3] = false;
					toReturn.tilemap[3][3] = false;
					toReturn.tilemap[4][3] = false;
					toReturn.tilemap[2][0] = true;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[2][3] = true;
					toReturn.orientation = Orientation.rotate90;
				}
				else
				{
					toReturn.tilemap[2][0] = false;
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[2][3] = false;
					toReturn.tilemap[1][3] = true;
					toReturn.tilemap[2][3] = true;
					toReturn.tilemap[3][3] = true;
					toReturn.tilemap[4][3] = true;
					toReturn.orientation = Orientation.rotate0;
				}
				break;
			case L:
				if(orientation == Orientation.rotate0)
				{
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[3][2] = false;
					toReturn.tilemap[3][3] = false;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[2][3] = true;
					toReturn.tilemap[3][1] = true;
					toReturn.orientation = Orientation.rotate90;
				}
				else if(orientation == Orientation.rotate90)
				{
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[2][3] = false;
					toReturn.tilemap[3][1] = false;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[3][2] = true;
					toReturn.tilemap[1][1] = true;
					toReturn.orientation = Orientation.rotate180;
				}
				else if(orientation == Orientation.rotate180)
				{
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[3][2] = false;
					toReturn.tilemap[1][1] = false;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[2][3] = true;
					toReturn.tilemap[1][3] = true;
					toReturn.orientation = Orientation.rotate270;
				}
				else //270
				{
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[2][3] = false;
					toReturn.tilemap[1][3] = false;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[3][2] = true;
					toReturn.tilemap[3][3] = true;
					toReturn.orientation = Orientation.rotate0;
				}
				break;
			case ReverseL:
				if(orientation == Orientation.rotate0)
				{
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[3][2] = false;
					toReturn.tilemap[3][1] = false;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[2][3] = true;
					toReturn.tilemap[1][1] = true;
					toReturn.orientation = Orientation.rotate90;
				}
				else if(orientation == Orientation.rotate90)
				{
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[2][3] = false;
					toReturn.tilemap[1][1] = false;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[3][2] = true;
					toReturn.tilemap[1][3] = true;
					toReturn.orientation = Orientation.rotate180;
				}
				else if(orientation == Orientation.rotate180)
				{
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[3][2] = false;
					toReturn.tilemap[1][3] = false;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[2][3] = true;
					toReturn.tilemap[3][3] = true;
					toReturn.orientation = Orientation.rotate270;
				}
				else //270
				{
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[2][3] = false;
					toReturn.tilemap[3][3] = false;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[3][2] = true;
					toReturn.tilemap[3][1] = true;
					toReturn.orientation = Orientation.rotate0;
				}
				break;
			case Z:
				if(orientation == Orientation.rotate0 || orientation == Orientation.rotate180)
				{
					toReturn.tilemap[1][1] = false;
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[2][3] = false;
					
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[3][1] = true;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][2] = true;
					
					toReturn.orientation = Orientation.rotate90;
				}
				else
				{
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[3][1] = false;
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][2] = false;
					
					toReturn.tilemap[1][1] = true;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[2][3] = true;
					
					toReturn.orientation = Orientation.rotate0;
				}
				break;
			case ReverseZ:
				if(orientation == Orientation.rotate0 || orientation == Orientation.rotate180)
				{
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[1][3] = false;
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					
					toReturn.tilemap[1][1] = true;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[3][2] = true;
				
					
					toReturn.orientation = Orientation.rotate90;
				}
				else
				{
					toReturn.tilemap[1][1] = false;
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[3][2] = false;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[1][3] = true;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					
					toReturn.orientation = Orientation.rotate0;
				}
				break;
			case Tri:
				if(orientation == Orientation.rotate0)
				{
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[2][3] = false;				
					toReturn.tilemap[2][3] = true;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[3][2] = true;
					color = Color.YELLOW;		
					toReturn.orientation = Orientation.rotate90;
				}
				else if(orientation == Orientation.rotate90)
				{
					toReturn.tilemap[2][3] = false;
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[3][2] = false;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[2][3] = true;
					toReturn.tilemap[3][2] = true;
					toReturn.orientation = Orientation.rotate180;
				}
				else if(orientation == Orientation.rotate180)
				{
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[2][3] = false;
					toReturn.tilemap[3][2] = false;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[3][2] = true;
					toReturn.tilemap[2][1] = true;
					toReturn.orientation = Orientation.rotate270;
				}
				else //270
				{
					toReturn.tilemap[1][2] = false;
					toReturn.tilemap[2][2] = false;
					toReturn.tilemap[3][2] = false;
					toReturn.tilemap[2][1] = false;
					toReturn.tilemap[1][2] = true;
					toReturn.tilemap[2][1] = true;
					toReturn.tilemap[2][2] = true;
					toReturn.tilemap[2][3] = true;
					toReturn.orientation = Orientation.rotate0;
				}
				break;
			default:
				break;
		}
		return toReturn;
	}
}
