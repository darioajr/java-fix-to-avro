# java-fix-to-avro
FIX to Avro converter built with Java 21, enabling the use of custom FIX schemas and facilitating the generation of both Avro data and schema definitions.

[![codecov](https://codecov.io/github/darioajr/java-fix-to-avro/branch/main/graph/badge.svg?style=flat-square)](https://app.codecov.io/github/darioajr/java-fix-to-avro) [![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fdarioajr%2Fjava-fix-to-avro.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fdarioajr%2Fjava-fix-to-avro?ref=badge_shield) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=darioajr_java-fix-to-avro&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=darioajr_java-fix-to-avro) [![Maven Central Version](https://img.shields.io/maven-central/v/io.github.darioajr.converter/fixtoavro)](https://central.sonatype.com/artifact/io.github.darioajr.converter/fixtoavro)




## Example
 ```java
    // Use default schema
    FixConverter fixConverter = new FixConverter();
    String newOrderSingle = "8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|"
        + "34=1|52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|";
    GenericRecord defaultRecord = fixConverter.convertToAvro(newOrderSingle,
        FixDefaultVersion.FIX_4_4);
    System.out.println(defaultRecord);

    Main main = new Main();
    // Use custom schema
    FixCustomVersion fixCustomVersion = new FixCustomVersion(
        FixDefaultVersion.FIX_4_4.getVersion(),    
        main.getSchemaPath("schemas/FIX44_custom.xml"));

    String newOrderSingleCustom = "8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|52"
        + "=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|";
    
    GenericRecord customRecord = fixConverter.convertToAvro(newOrderSingleCustom,
        fixCustomVersion);
    System.out.println(customRecord);

    FixMessageParser parser = new FixMessageParser();
    String fixMessage = "8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|52"
        .concat("=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|");

    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4"); // verify version
    fieldCriteria.put("35", Arrays.asList("D", "G")); // MsgType must be "D" or "G"
    fieldCriteria.put("54", Arrays.asList("1", "2")); // Side must be "1" or "2"

    Map<String, String> parsedFields = parser.parse(fixMessage, FixDefaultVersion.FIX_4_4);

    FixMessageValidator validator = new FixMessageValidator();
    validator.validateFields(parsedFields, FixDefaultVersion.FIX_4_4, fieldCriteria);

    System.out.println("Valid!");
  }
  ```
  
## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fdarioajr%2Fjava-fix-to-avro.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fdarioajr%2Fjava-fix-to-avro?ref=badge_large)
