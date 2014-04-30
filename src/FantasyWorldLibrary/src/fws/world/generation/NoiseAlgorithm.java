package fws.world.generation;

import fws.utility.SimplexNoise;
import java.util.Random;

public class NoiseAlgorithm
{
	private static final int MAX_RANDOM = 20000;
	
	private int octaves_;
	private float roughness_;
	private float scale_;
	
	private int seed_;
	private Random random_ = new Random();
	
	private int offset_x_;
	private int offset_y_;
	
	public NoiseAlgorithm(int octaves, float roughness, float scale)
	{
		octaves_ = octaves;
		roughness_ = roughness;
		scale_ = scale;
	}
	
	public NoiseAlgorithm(int octaves, float roughness, float scale, int seed)
	{
		this(octaves, roughness, scale);
		
		seed_= seed;
	}
	
	public void update()
	{
		seed_++;
		random_.setSeed(seed_);
		offset_x_ = random_.nextInt(MAX_RANDOM);
		offset_y_ = random_.nextInt(MAX_RANDOM);
	}
	
	public float generate(int x, int y)
	{
		float ax = x + offset_x_;
		float ay = y + offset_y_;
				
		float noise = (float)SimplexNoise.getOctavedNoise(ax, ay, octaves_, roughness_, scale_);
		
		return (noise + 1.0f) / 2.0f;
	}
}
