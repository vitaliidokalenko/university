package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Timeframe;

public class TimeframeRepository {

	List<Timeframe> timeframes = new ArrayList<>();
	
	public void create(Timeframe timeframe) {
		timeframes.add(timeframe);
	}
	
	public void delete(Timeframe timeframe) {
		timeframes.remove(timeframe);
	}
	
	public List<Timeframe> getTimeframes(){
		return timeframes;
	}
}
