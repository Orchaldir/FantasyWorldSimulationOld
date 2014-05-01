package fws.world.generation;

public interface GenerationAlgorithm
{
	void nextSeed();
	void update();
	float generate(float x, float y);
}
