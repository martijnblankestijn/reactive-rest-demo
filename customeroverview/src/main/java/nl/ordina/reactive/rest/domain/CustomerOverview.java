package nl.ordina.reactive.rest.domain;

import nl.ordina.reactive.rest.serialization.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class CustomerOverview {
  public Customer customer;
  public Contract[] contracts;
  public Communication[] communications;
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  public LocalDateTime retrievalTimestamp;
}
