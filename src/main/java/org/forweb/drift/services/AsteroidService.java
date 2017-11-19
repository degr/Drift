package org.forweb.drift.services;

import org.forweb.drift.entity.drift.Asteroid;
import org.forweb.drift.utils.IncrementalId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsteroidService {

    public Asteroid create(double max, double min, IncrementalId ids) {
        return new Asteroid(Math.random() * max, Math.random() * min, ids.get());
    }

    public List<Asteroid> createMulty (int limit, double max, double min, IncrementalId ids) {
        if(limit < 1) {
            throw new RuntimeException("Invalid arguments exception");
        }
        List<Asteroid> out = new ArrayList<>();
        while(limit-- > 0) {
            out.add(this.create(max, min, ids));
        }
        return out;
    }
}
