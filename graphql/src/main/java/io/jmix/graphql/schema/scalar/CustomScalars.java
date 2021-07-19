package io.jmix.graphql.schema.scalar;

import graphql.schema.GraphQLScalarType;
import io.jmix.graphql.schema.scalar.coercing.BigDecimalCoercing;
import io.jmix.graphql.schema.scalar.coercing.FileRefCoercing;
import io.jmix.graphql.schema.scalar.coercing.LongCoercing;
import io.jmix.graphql.schema.scalar.coercing.UUIDCoercing;
import io.jmix.graphql.schema.scalar.coercing.VoidCoercing;

import java.text.SimpleDateFormat;

public class CustomScalars {

    public static final SimpleDateFormat SERIALIZATION_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final SimpleDateFormat SERIALIZATION_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SERIALIZATION_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static GraphQLScalarType GraphQLDate = new DateScalar();
    public static GraphQLScalarType GraphQLTime = new TimeScalar();
    public static GraphQLScalarType GraphQLDateTime = new DateTimeScalar();
    public static GraphQLScalarType GraphQLLocalDateTime = new LocalDateTimeScalar();
    public static GraphQLScalarType GraphQLLocalDate = new LocalDateScalar();
    public static GraphQLScalarType GraphQLLocalTime = new LocalTimeScalar();
    public static GraphQLScalarType GraphQLOffsetDateTime = new OffsetDateTimeScalar();
    public static GraphQLScalarType GraphQLOffsetTime = new OffsetTimeScalar();

    public static GraphQLScalarType GraphQLLong = GraphQLScalarType.newScalar()
            .name("Long")
            .coercing(new LongCoercing()).build();

    public static GraphQLScalarType GraphQLBigDecimal = GraphQLScalarType.newScalar()
            .name("BigDecimal")
            .coercing(new BigDecimalCoercing()).build();

    public static GraphQLScalarType GraphQLUUID = GraphQLScalarType.newScalar()
            .name("UUID")
            .coercing(new UUIDCoercing()).build();


    public static GraphQLScalarType GraphQLVoid = GraphQLScalarType.newScalar()
            .name("Void").coercing(new VoidCoercing()).build();

    public static GraphQLScalarType GraphQLFile = GraphQLScalarType.newScalar()
            .name("FileRef").coercing(new FileRefCoercing()).build();
}
