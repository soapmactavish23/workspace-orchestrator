package br.com.microservices.orchestrated.orderservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EventService {

	private final EventRepository repository;
	
	public Event save(Event event) {
		return repository.save(event);
	}
	
}
