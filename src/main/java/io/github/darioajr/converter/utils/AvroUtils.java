/**
 * Copyright 2025 Dario Alves Junior. All Rights Reserved.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package io.github.darioajr.converter.utils;

import io.github.darioajr.converter.core.SchemaProvider;
import io.github.darioajr.converter.parser.AvroSchemaReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.field.BeginString;
import quickfix.field.BodyLength;
import quickfix.field.CheckSum;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.SenderCompID;
import quickfix.field.SendingTime;
import quickfix.field.TargetCompID;

/**
 * Utility class for Avro-related operations.
 * 
 */
public class AvroUtils {

  /**
   * Default constructor.
   * This constructor is intentionally empty. Nothing special is needed here.
   */
  public AvroUtils() {
    // Default constructor
  }
  
  /**
   * Converts a FIX message to an Avro byte array.
   *
   * @param rawMessage the raw FIX message as a string
   * @param schema the schema provider for the Avro schema
   * @return the serialized Avro byte array
   * @throws IOException if an I/O error occurs during conversion or serialization
   */ 
  public static byte[] convertFixToAvroByteArray(String rawMessage, SchemaProvider schema)
      throws IOException {
    GenericRecord record = convertFixToAvro(rawMessage, schema);
    Schema avroSchema = AvroSchemaReader.readDefaultAvroSchema();
    return serializeGenericRecordToBytes(record, avroSchema);
  }

  /**
   * Converts a FIX message to an Avro GenericRecord.
   *
   * @param rawMessage the raw FIX message as a string
   * @param schema the schema provider for the Avro schema
   * @return the converted Avro GenericRecord
   * @throws RuntimeException if an error occurs during conversion
   */
  public static GenericRecord convertFixToAvro(String rawMessage, SchemaProvider schema) {
    try {
      Message message = new Message();
      DataDictionary dataDictionary = loadDataDictionary(schema.getSchemaPath());
      rawMessage = rawMessage.replace("|", "\u0001"); // Substitui "|" por SOH (\u0001)
      message.fromString(rawMessage, dataDictionary, true);

      Schema avroSchema = AvroSchemaReader.readDefaultAvroSchema();

      GenericRecord record = new GenericData.Record(avroSchema);
      record.put("beginString", message.getHeader().getString(BeginString.FIELD));
      record.put("bodyLength", message.getHeader().getString(BodyLength.FIELD));
      record.put("msgType", message.getHeader().getString(MsgType.FIELD));
      record.put("senderCompID", message.getHeader().getString(SenderCompID.FIELD));
      record.put("targetCompID", message.getHeader().getString(TargetCompID.FIELD));
      record.put("msgSeqNum", message.getHeader().getString(MsgSeqNum.FIELD));
      record.put("sendingTime", message.getHeader().getString(SendingTime.FIELD));
      record.put("checkSum", message.getTrailer().getString(CheckSum.FIELD));

      Map<String, String> fields = FixUtils.getFieldsAsMap(message);
      record.put("fields", fields);
      return record;
    } catch (IOException | InvalidMessage | FieldNotFound | ConfigError e) {
      throw new RuntimeException("Erro ao converter mensagem FIX para Avro", e);
    }
  }

  /**
   * Loads the DataDictionary from the specified file path.
   *
   * @param dictionaryPath the path to the DataDictionary file
   * @return the loaded DataDictionary
   * @throws IOException if an I/O error occurs reading from the file
   * @throws ConfigError if there is an error in the configuration
   */
  private static DataDictionary loadDataDictionary(String dictionaryPath)
      throws IOException, ConfigError {
    try (FileInputStream configFile = new FileInputStream(dictionaryPath)) {
      return new DataDictionary(configFile);
    }
  }

  /**
   * Serializes a GenericRecord to a byte array.
   *
   * @param record the GenericRecord to serialize
   * @param schema the Avro schema for the record
   * @return the serialized byte array
   * @throws IOException if an I/O error occurs during serialization
   */
  private static byte[] serializeGenericRecordToBytes(GenericRecord record, Schema schema)
      throws IOException {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      DatumWriter<GenericRecord> datumWriter = new SpecificDatumWriter<>(schema);
      BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
      datumWriter.write(record, encoder);
      encoder.flush();
      return byteArrayOutputStream.toByteArray();
    }
  }
}
