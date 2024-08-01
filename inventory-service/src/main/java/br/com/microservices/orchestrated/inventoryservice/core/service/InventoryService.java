package br.com.microservices.orchestrated.inventoryservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.inventoryservice.core.dto.Event;
import br.com.microservices.orchestrated.inventoryservice.core.producer.KafkaProducer;
import br.com.microservices.orchestrated.inventoryservice.core.repository.InventoryRepository;
import br.com.microservices.orchestrated.inventoryservice.core.repository.OrderInventoryRepository;
import br.com.microservices.orchestrated.inventoryservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryService {

	private static final String CURRENT_SOURCE = "INVENTORY_SERVICE";
	
	private final JsonUtil jsonUtil;
	private final KafkaProducer producer;
	private final InventoryRepository inventoryRepository;
	private final OrderInventoryRepository orderInventoryRepository;
	
	
	public void updateInventory(Event event) {
		
	}
	
}
