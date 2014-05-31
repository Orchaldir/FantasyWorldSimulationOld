package fws.world;

public class PlateType
{
	private String name_;
	private float elevation_;
	
	public PlateType(String name, float elevation)
	{
		name_ = name;
		setElevation(elevation);
	}

	public String getName()
	{
		return name_;
	}
	
	public float getElevation()
	{
		return elevation_;
	}

	public final void setElevation(float elevation)
	{
		elevation_ = Math.min(Math.max(elevation, 0.0f), 1.0f);
	}
}
