package es.fantasymanager.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PARAMETER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Parameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PARAMETER_ID")
	private Integer parameterId;

	@Column(name = "CODE")
	private String code;

	@Column(name = "VALUE")
	private String value;

}
