package br.com.microservices.orchestrated.paymentservice.core.model;

import java.time.LocalDateTime;

import br.com.microservices.orchestrated.paymentservice.core.enums.EPaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String orderId;

	@Column(nullable = false)
	private String transactionId;

	@Column(nullable = false)
	private boolean success;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@Column(nullable = false)
	private int totalItems;
	
	@Column(nullable = false)
	private double totalAmount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EPaymentStatus status;

	@PrePersist
	public void prePersist() {
		var now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
		status = EPaymentStatus.PENDING;
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

}
