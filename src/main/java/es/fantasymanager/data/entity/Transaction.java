package es.fantasymanager.data.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRANSACTION")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTION_ID")
	private Integer transactionId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "FANTASY_TEAM_ID")
	private FantasyTeam fantasyTeam;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "PLAYER_ADDED_ID")
	private Player playerAdded;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "PLAYER_DROPPED_ID")
	private Player playerDropped;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE")
	private Date date;

}
