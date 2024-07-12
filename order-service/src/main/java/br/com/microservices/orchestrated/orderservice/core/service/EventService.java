package br.com.microservices.orchestrated.orderservice.core.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.dto.EventFilters;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

	private final EventRepository repository;

	public void notifyEnding(Event event) {
		event.setOrderId(event.getOrderId());
		event.setCreatedAt(LocalDateTime.now());
		save(event);
		log.info("Order {} with saga notified! TransactionId: {}", event.getOrderId(), event.getTransactionId());
	}

	public Event save(Event event) {
		return repository.save(event);
	}

	public List<Event> findAll() {
		return repository.findAllByOrderByCreatedAtDesc();
	}

	public Event findByFilters(EventFilters filters) {
		validateEmptyFilters(filters);
		if (!filters.getOrderId().isEmpty()) {
			return findByOrderId(filters.getOrderId());
		} else {
			return findByTransactionId(filters.getTransactionId());
		}
	}

	private Event findByOrderId(String orderId) {
		return repository.findTop1ByOrderIdByIdOrderByCreatedAtDesc(orderId)
				.orElseThrow(() -> new ValidationException("Event not found by orderId."));
	}

	private Event findByTransactionId(String transactionId) {
		return repository.findTop1ByTranscationIdByIdOrderByCreatedAtDesc(transactionId)
				.orElseThrow(() -> new ValidationException("Event not found by transactionId."));
	}

	private void validateEmptyFilters(EventFilters filters) {
		if (filters.getOrderId().isEmpty() && filters.getTransactionId().isEmpty()) {
			throw new ValidationException("OrderId or TransactionId must be informed.");
		}
	}

}
