/*
 * Copyright 2021 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.graphql.schema.scalars

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingSerializeException
import io.jmix.graphql.schema.scalar.LocalDateTimeScalar
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeScalarTest extends Specification {

    private final LocalDateTimeScalar scalar = new LocalDateTimeScalar()
    private Coercing coercing

    @SuppressWarnings('unused')
    def setup() {
        coercing = scalar.getCoercing()
    }

    def "localDateTime scalar test"() {
        given:
        def stringDate = new StringValue("2021-01-01T23:59:59")
        def localDateTime = LocalDateTime.from(
                DateTimeFormatter
                        .ofPattern(LocalDateTimeScalar.LOCAL_DATE_TIME_FORMAT)
                        .parse(stringDate.getValue())
        )
        def parsedLiteral
        def parsedValue
        def serialized
        def nullParsedLiteral
        def nullParsedValue

        when:
        parsedLiteral = (LocalDateTime) coercing.parseLiteral(stringDate)
        parsedValue = (LocalDateTime) coercing.parseValue(stringDate)
        serialized = coercing.serialize(localDateTime)
        nullParsedLiteral = (LocalDateTime) coercing.parseLiteral(new StringValue(""))
        nullParsedValue = (LocalDateTime) coercing.parseValue(new StringValue(""))

        then:
        parsedLiteral.isEqual(localDateTime)
        parsedValue.isEqual(localDateTime)
        serialized == stringDate.getValue()
        nullParsedLiteral.isEqual(LocalDateTime.MIN)
        nullParsedValue.isEqual(LocalDateTime.MIN)
    }

    def "localDateTime scalar throws CoercingSerializeException"() {
        when:
        coercing.serialize("")

        then:
        def exception = thrown(CoercingSerializeException)
        exception.message == "Expected type 'LocalDateTime' but was 'String'."
    }

    def "localDateTime scalar throws CoercingParseLiteralException with parseLiteral"() {
        when:
        coercing.parseLiteral("")

        then:
        def exception = thrown(CoercingParseLiteralException)
        exception.message == "Expected type 'StringValue' but was 'String'."
    }

    def "localDateTime scalar throws CoercingParseLiteralException with parseValue"() {
        when:
        coercing.parseValue("")

        then:
        def exception = thrown(CoercingParseLiteralException)
        exception.message == "Expected type 'StringValue' but was 'String'."
    }
}