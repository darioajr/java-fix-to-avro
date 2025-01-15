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
 * Implementation of AvroUtils.
 * 
 */
public class AvroUtils {

  /**
   * Implementation of convertFixToAvroByteArray.
   * 
   */ 
  public static byte[] convertFixToAvroByteArray(String rawMessage, SchemaProvider schema)
      throws IOException {
    GenericRecord record = convertFixToAvro(rawMessage, schema);
    Schema avroSchema = AvroSchemaReader.readDefaultAvroSchema();
    return serializeGenericRecordToBytes(record, avroSchema);
  }

  /**
   * Implementation of convertFixToAvro.
   * 
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
   * Função para carregar o DataDictionary.
   * 
   */
  private static DataDictionary loadDataDictionary(String dictionaryPath)
      throws IOException, ConfigError {
    try (FileInputStream configFile = new FileInputStream(dictionaryPath)) {
      return new DataDictionary(configFile);
    }
  }

  /**
   * Função para serializar o GenericRecord em byte[].
   * 
   */
  @SuppressWarnings("unused")
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
