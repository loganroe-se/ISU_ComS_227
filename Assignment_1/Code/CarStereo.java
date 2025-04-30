package hw1;

/**
 * Creates a basic car stereo with the basic functions using various accessor and mutator methods.
 * @author Logan Roe
 */
public class CarStereo 
{
	/**
	 * How much the volume changes by per step.
	 */
	public static final double VOLUME_STEP = 0.16;
	
	/**
	 * Volume of the radio.
	 */
	private double volume;
	
	/**
	 * Tuner frequency of the radio.
	 */
	private double tuner;
	
	/**
	 * The minimum frequency.
	 */
	private double minFrequency;
	
	/**
	 * The maximum frequency;
	 */
	private double maxFrequency;
	
	/**
	 * Saves the preset value.
	 */
	private int presetStation;
	
	/**
	 * The number of stations which only needs to be set one time.
	 */
	private final int numStations;
	
	/**
	 * Constructs a car stereo with initial values of 0.5 for volume and the tuner frequency set to minimum.
	 * @param givenMinFrequency minimum frequency of the band width of this radio
	 * @param givenMaxFrequency maximum frequency of the band width of this radio
	 * @param givenNumStations number of stations for this radio
	 */
	public CarStereo(double givenMinFrequency, double givenMaxFrequency, int givenNumStations)
	{
		volume = 0.5;
		tuner = givenMinFrequency;
		numStations = givenNumStations;
		minFrequency = givenMinFrequency;
		maxFrequency = givenMaxFrequency;
	}
	
	/**
	 * Returns the volume.
	 * @return current volume
	 */
	public double getVolume()
	{
		return volume;
	}
	
	/**
	 * Increases the volume by the step value, without going above 1.0.
	 */
	public void louder()
	{
		volume += 0.16;
		volume = Math.min(volume, 1.0);
	}
	
	/**
	 * Decreases the volume by the step value, without going below 0.
	 */
	public void quieter()
	{
		volume -= 0.16;
		volume = Math.max(volume, 0);
	}
	
	/**
	 * Returns the tuner frequency that the radio is currently at.
	 * @return the current frequency
	 */
	public double getTuner()
	{
		return tuner;
	}
	
	/**
	 * Sets the tuner frequency to the givenFrequency. 
	 * If it exceeds the maximum, the tuner is set to the max. 
	 * if it goes below the minimum then the tuner is set to the minimum.
	 * @param givenFrequency what the frequency is set to
	 */
	public void setTuner(double givenFrequency)
	{
		tuner = givenFrequency;
		tuner = Math.max(tuner,  minFrequency);
		tuner = Math.min(tuner,  maxFrequency);
	}
	
	/**
	 * Positive/negative degrees of rotation of a knob changes the frequency based on amount turned.
	 * One full rotation is the full band width which is from minimum to max frequency, or vice versa.
	 * A fraction of a turn adjusts the tuner frequency by the same fraction of the entire band width.
	 * @param degrees how much to turn the knob
	 */
	public void turnDial(double degrees)
	{
		// Turns the degrees into a fraction and multiply by the full band width.
		tuner += ((degrees / 360) * (maxFrequency - minFrequency));
		tuner = Math.max(tuner,  minFrequency);
		tuner = Math.min(tuner,  maxFrequency);
	}
	
	/**
	 * Tuner is set to the broadcast frequency of the given station, if applicable.
	 * If the number is less than 0, the tuner is set to the frequency of station 0.
	 * If the number is greater than or equal to N (the number of stations) then the tuner is set to the broadcast frequency of station N -1.
	 * @param stationNumber the station number the frequency is to be set to
	 */
	public void setTunerFromStationNumber(int stationNumber)
	{
		// An interval is created dependent on the minimum and maximum frequencies and the number of stations.
		double interval = (maxFrequency - minFrequency) / numStations;
		// Tuner is being set to the frequency of the specified station number.
		tuner = stationNumber * interval + (interval / 2) + minFrequency;
		// Tuner is being set to the lowest station number's frequency if it is below said value.
		tuner = Math.max(tuner,  (interval / 2) + minFrequency);
		// TUner is being set to the highest station number's frequency if it is above said value.
		tuner = Math.min(tuner,  (numStations - 1) * interval + (interval / 2) + minFrequency);
	}
	
	/**
	 * Determines what station number is closest to the current tuner frequency.
	 * If the tuner is exactly between two stations then it is rounded to the higher station number.
	 * If the tuner is at maximum frequency then N (number of stations) - 1 is returned.
	 * @return current station number
	 */
	public int findStationNumber()
	{
		// An interval is created dependent on the minimum and maximum frequencies and the number of stations.
		double interval = (maxFrequency - minFrequency) / numStations;
		// Calculates the double station number which is then rounded and casted to an int.
		int stationNumber = (int) Math.round(((tuner - ((interval / 2) + minFrequency)) / interval));
		// Assuring that the station number does not go over the max number of stations - 1.
		stationNumber = Math.min(stationNumber,  numStations - 1);
		return stationNumber;
	}
	
	/**
	 * Tuner is set to the frequency of the next station below the current one.
	 * If the current station is 0 then it wraps back to N (number of stations) - 1 station.
	 */
	public void seekDown()
	{
		// An interval is created dependent on the minimum and maximum frequencies and the number of stations.
		double interval = (maxFrequency - minFrequency) / numStations;
		// If the station number is 0 then it is wrapped back to the max due to the modulus. The rest is used to calculate the frequency of the station.
		tuner = (((findStationNumber() + numStations - 1) % numStations) * interval) + ((interval / 2) + minFrequency);
	}
	
	/**
	 * Tuner is set to the frequency of the next station above the current one.
	 * If the current station is N (number of stations) - 1 then it warps back to zero.
	 */
	public void seekUp()
	{
		// An interval is created dependent on the minimum and maximum frequencies and the number of stations.
		double interval = (maxFrequency - minFrequency) / numStations;
		// If the station number is at the max then it is wrapped back to 0 due to the modulus. The rest is used to calculate the frequency of the station.
		tuner = (((findStationNumber() + 1) % numStations) * interval) + ((interval / 2) + minFrequency);
	}
	
	/**
	 * Keeps the current station number as a preset.
	 */
	public void savePreset()
	{
		presetStation = findStationNumber();
	}
	
	/**
	 * Sets the frequency to the preset station's frequency.
	 */
	public void goToPreset()
	{
		// An interval is created dependent on the minimum and maximum frequencies and the number of stations.
		double interval = (maxFrequency - minFrequency) / numStations;
		// Calculating the frequency of a given station.
		tuner = (presetStation * interval) + ((interval / 2) + minFrequency);
	}
}