package br.com.microservices.orchestrated.orderservice.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.microservices.orchestrated.orderservice.core.document.Event;

public interface EventRepository extends MongoRepository<Event, String> {

	List<Event> findAllByOrderByCreatedAtDesc();
	
	Optional<Event> findTop1ByOrderIdByIdOrderByCreatedAtDesc(String orderId);
	
	Optional<Event> findTop1ByTranscationIdByIdOrderByCreatedAtDesc(String transactionId);
	
}
