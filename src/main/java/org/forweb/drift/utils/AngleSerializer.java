package org.forweb.drift.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.forweb.geometry.misc.Angle;

import java.io.IOException;

public class AngleSerializer extends StdSerializer<Angle> {

    public AngleSerializer() {
        this(null);
    }

    public AngleSerializer(Class<Angle> t) {
        super(t);
    }

    @Override
    public void serialize(Angle value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeNumber(value.doubleValue());
    }
}
