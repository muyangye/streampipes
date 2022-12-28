#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

"""
Classes of the StreamPipes data model that are commonly shared.
"""

from typing import List, Optional

from pydantic import BaseModel, Field, StrictBool, StrictInt, StrictStr

__all__ = [
    "BaseElement",
    "BasicModel",
    "EventSchema",
]


def _snake_to_camel_case(snake_case_string: str) -> str:
    """Converts a string in snake_case format to camelCase style."""

    tokens = snake_case_string.split("_")

    return tokens[0] + "".join(t.title() for t in tokens[1:])


class BasicModel(BaseModel):
    """Basic model class used for the whole Python StreamPipes data model."""

    class Config:
        """Configuration class for Pydantic.
        Defines alias generator to convert field names from camelCase (API) to snake_case (Python codebase).
        """

        alias_generator = _snake_to_camel_case


class BaseElement(BasicModel):
    """Structure of a basic element in the StreamPipes backend"""

    element_id: Optional[StrictStr]


class EventPropertyQualityRequirement(BaseElement):
    """
    Data model of an `EventPropertyQualityRequirement` in compliance to the StreamPipes Backend.
    """

    minimum_property_quality: Optional[BaseElement] = Field(alias="eventPropertyQualityDefinition")
    maximum_property_quality: Optional[BaseElement] = Field(alias="eventPropertyQualityDefinition")


class ValueSpecification(BaseElement):
    """
    Data model of an `ValueSpecification` in compliance to the StreamPipes Backend.
    """

    min_value: Optional[int]
    max_value: Optional[int]
    step: Optional[float]


class EventProperty(BaseElement):
    """
    Data model of an `EventProperty` in compliance to the StreamPipes Backend.
    """

    label: Optional[StrictStr]
    description: Optional[StrictStr]
    runtime_name: StrictStr
    required: StrictBool
    domain_properties: List[StrictStr]
    event_property_qualities: List[BaseElement] = Field(alias="eventPropertyQualities")
    requires_event_property_qualities: List[EventPropertyQualityRequirement]
    property_scope: Optional[StrictStr]
    index: StrictInt
    runtime_id: Optional[StrictStr]
    runtime_type: Optional[StrictStr]
    measurement_unit: Optional[StrictStr]
    value_specification: Optional[ValueSpecification]


class EventSchema(BaseElement):
    """
    Data model of an `EventSchema` in compliance to the StreamPipes Backend.
    """

    event_properties: List[EventProperty]


class ApplicationLink(BaseElement):
    """
    Data model of an `ApplicationLink` in compliance to the StreamPipes Backend.
    """

    application_name: Optional[StrictStr]
    application_description: Optional[StrictStr]
    application_url: Optional[StrictStr]
    application_icon_url: Optional[StrictStr]
    application_link_type: Optional[StrictStr]


class TopicDefinition(BaseElement):
    """
    Data model of a `TopicDefinition` in compliance to the StreamPipes Backend.
    """

    actual_topic_name: StrictStr


class TransportProtocol(BaseElement):
    """
    Data model of a `TransportProtocol` in compliance to the StreamPipes Backend.
    """

    broker_hostname: StrictStr
    topic_definition: TopicDefinition
    port: StrictInt


class TransportFormat(BaseElement):
    """
    Data model of a `TransportFormat` in compliance to the StreamPipes Backend.
    """

    rdf_type: Optional[List[Optional[StrictStr]]]


class EventGrounding(BaseElement):
    """
    Data model of an `EventGrounding` in compliance to the StreamPipes Backend.
    """

    transport_protocols: List[TransportProtocol]
    transport_formats: Optional[List[Optional[TransportFormat]]]


class MeasurementCapability(BaseElement):
    """
    Data model of a `MeasurementCapability` in compliance to the StreamPipes Backend.
    """

    capability: Optional[StrictStr]


class MeasurementObject(BaseElement):
    """
    Data model of a `MeasurementObject` in compliance to the StreamPipes Backend.
    """

    measures_object: Optional[StrictStr]