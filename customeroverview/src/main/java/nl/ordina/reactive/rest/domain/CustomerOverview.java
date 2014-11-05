package nl.ordina.reactive.rest.domain;

import nl.ordina.reactive.rest.serialization.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class CustomerOverview {
  public Customer customer;
  public Contract[] contracts;
  public Communication[] communications;
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  public LocalDateTime retrievalTimestamp;

  public CustomerOverview() {}
  public CustomerOverview(Customer customer) {
    this.customer = customer;
    this.retrievalTimestamp = now();
  }

  public CustomerOverview(CustomerOverview source, Contract[] contracts) {
    copy(source);
    this.contracts = contracts;
  }

  public CustomerOverview(CustomerOverview source, Communication[] communications) {
    copy(source);
    this.communications = communications;
  }

  private void copy(CustomerOverview source) {
    this.customer = source.customer;
    this.contracts = source.contracts;
    this.communications = source.communications;
    this.retrievalTimestamp = source.retrievalTimestamp;
  }


  public CustomerOverview add(Contract[] contracts) {
    return new CustomerOverview(this, contracts);
  }

  public CustomerOverview add(Communication[] communications) {
    return new CustomerOverview(this, communications);
  }

}
