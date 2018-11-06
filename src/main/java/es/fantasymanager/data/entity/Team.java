package es.fantasymanager.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "players")
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer teamId;

	@Column(nullable = false, unique = true, length = 50)
	private String name;

	@Column(unique = true, length = 4)
	private String shortCode;

	@Column(unique = true, length = 50)
	private String longCode;

	@OneToMany(mappedBy = "team")
	private List<Player> players;

}
