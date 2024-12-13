package io.github.darioajr.converter.core;

import io.github.darioajr.converter.models.FixVersion;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;

public interface FixConverter {
    GenericRecord convertFixToAvro(String rawMessage, FixVersion version) throws IOException;
    byte[] convertFixToAvroByteArray(String rawMessage, FixVersion version) throws IOException;
}
