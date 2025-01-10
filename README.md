# java-fix-to-avro
FIX to Avro converter built with Java 21, enabling the use of custom FIX schemas and facilitating the generation of both Avro data and schema definitions.

[![codecov](https://codecov.io/github/darioajr/java-fix-to-avro/branch/main/graph/badge.svg?style=flat-square)](https://app.codecov.io/github/darioajr/java-fix-to-avro) [![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fdarioajr%2Fjava-fix-to-avro.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fdarioajr%2Fjava-fix-to-avro?ref=badge_shield) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=darioajr_java-fix-to-avro&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=darioajr_java-fix-to-avro) [![Maven Central Version](https://img.shields.io/maven-central/v/io.github.darioajr.converter/fixtoavro)](https://central.sonatype.com/artifact/io.github.darioajr.converter/fixtoavro)




## Example
 ```java
    // Use default schema
    FixConverterImpl converterService = new FixConverterImpl();
    String newOrderSingle = """
      8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|
        """;
    GenericRecord defaultRecord = converterService.convertFixToAvro(newOrderSingle,
        FixVersion.FIX_4_4);
    System.out.println(defaultRecord);

    // Use custom schema
    FixVersion fixVersion = FixVersion.FIX_4_4;
    fixVersion.setCustomSchemaPath(Paths.get("src/main/resources/schemas/FIX44_custom.xml"));
    String newOrderSingleCustom = """
      8=FIX.4.4|9=123|35=XX|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=94|
        """;
    GenericRecord customRecord = converterService.convertFixToAvro(newOrderSingleCustom,
        fixVersion);
    System.out.println(customRecord);

    converterService.resetToDefaultSchema(FixVersion.FIX_4_4);

    FixMessageParser parser = new FixMessageParser();
    String fixMessage = """
      8=FIX.4.4|9=123|35=D|49=SenderCompID|56=TargetCompID|34=1|
      52=20231208-12:34:56|11=Order123|54=1|38=100|55=AAPL|44=50.00|10=242|
        """;

    // Validations criteria: tag=values (String or List<String>)
    Map<String, Object> fieldCriteria = new HashMap<>();
    fieldCriteria.put("8", "FIX.4.4"); // Verifica versão
    fieldCriteria.put("35", Arrays.asList("D", "G")); // MsgType must be "D" or "G"
    fieldCriteria.put("54", Arrays.asList("1", "2")); // Side must be "1" or "2"
  
    Map<String, String> parsedFields = parser.parse(fixMessage, FixVersion.FIX_4_4);

    //Validation
    FixMessageValidator validator = new FixMessageValidator();
    validator.validateFields(parsedFields, FixVersion.FIX_4_4, fieldCriteria);

    System.out.println("Valid!");
  }
  ```
  
## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fdarioajr%2Fjava-fix-to-avro.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fdarioajr%2Fjava-fix-to-avro?ref=badge_large)
