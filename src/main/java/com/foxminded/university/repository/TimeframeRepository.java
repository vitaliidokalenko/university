package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.foxminded.university.model.Timeframe;

public class TimeframeRepository {

	List<Timeframe> timeframes = new ArrayList<>();
	AtomicInteger id = new AtomicInteger(1);

	public void create(Timeframe timeframe) {
		timeframe.setId(id.getAndIncrement());
		timeframes.add(timeframe);
	}

	public void deleteById(int id) {
		timeframes.removeIf(t -> t.getId() == id);
	}

	public List<Timeframe> getTimeframes() {
		return timeframes;
	}
}
