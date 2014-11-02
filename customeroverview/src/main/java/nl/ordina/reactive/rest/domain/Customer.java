package nl.ordina.reactive.rest.domain;

import nl.ordina.reactive.rest.serialization.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class Customer {
  public String id;
  public String name;
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  public LocalDate birthDate;


  @Override public String toString() {
    return "Customer{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", birthDate=" + birthDate +
        '}';
  }
}
