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
import lombok.ToString;

@Entity
@Table(name = "LEAGUE_MEMBER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LeagueMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LEAGUE_MEMBER_ID")
	private Integer leagueMemberId;

	@Column(name = "TEAM_NAME", length = 500)
	private String tameName;

	@Column(name = "ABBREV", length = 5)
	private String abbrev;

}